package org.lunakoly.quicklink.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.changes.ChangeListManager
import com.intellij.openapi.vfs.VfsUtilCore
import git4idea.repo.GitRepository
import org.lunakoly.quicklink.repository.*
import org.lunakoly.quicklink.ui.showClickableListIfNeeded
import org.lunakoly.quicklink.ui.toast
import org.lunakoly.quicklink.ui.warn
import org.lunakoly.quicklink.urlbuilder.LineOffset
import org.lunakoly.quicklink.urlbuilder.Selection
import org.lunakoly.quicklink.urlbuilder.UrlBuilders
import org.lunakoly.quicklink.utils.PopupException
import org.lunakoly.quicklink.utils.catchingPopupExceptions
import org.lunakoly.quicklink.utils.runInBackground
import org.lunakoly.quicklink.utils.toDomain
import java.awt.datatransfer.StringSelection

class CopyLineLinkAction : DumbAwareAction() {
    override fun actionPerformed(event: AnActionEvent) {
        catchingPopupExceptions {
            generateLineLink(event, ::getReasonablePublishedRepositoryInfo)
        }
    }
}

class CopyCurrentCommitLineLinkAction : DumbAwareAction() {
    override fun actionPerformed(event: AnActionEvent) {
        catchingPopupExceptions {
            generateLineLink(event) { repo, _, _ ->
                getCurrentCommitRepositoryInfo(repo)
            }
        }
    }
}

class CopyLatestDefaultLineLinkAction : DumbAwareAction() {
    override fun actionPerformed(event: AnActionEvent) {
        catchingPopupExceptions {
            generateLineLink(event, ::getLatestDefaultRepositoryInfo)
        }
    }
}

class NoProjectException : PopupException(
    "Please, open a project",
    "No Open Project",
)

class NoEditorException : PopupException(
    "Could not access the editor",
    "No Editor Instance",
)

class NoActiveFileException : PopupException(
    "Please, open a file",
    "No Open File",
)

class RelativePathException : PopupException(
    "Could not calculate the relative file path",
    "No Relative Path",
)

private inline fun generateLineLink(
    event: AnActionEvent,
    crossinline getInfo: (GitRepository, Project, String) -> RepositoryInfo,
) {
    val project = event.project
        ?: throw NoProjectException()
    val editor = event.getData(CommonDataKeys.EDITOR)
        ?: throw NoEditorException()
    val currentFile = event.dataContext.getData(CommonDataKeys.VIRTUAL_FILE)?.canonicalFile
        ?: throw NoActiveFileException()

    val currentFileIsModified = ChangeListManager
        .getInstance(project)
        .getChange(currentFile) != null

    if (currentFileIsModified) {
        project.warn("This is a locally modified file, the line number may be inaccurate")
    }

    val selection = if (editor.selectionModel.hasSelection()) {
        val startLine = 1 + editor.document.getLineNumber(editor.selectionModel.selectionStart)
        val endLine = 1 + editor.document.getLineNumber(editor.selectionModel.selectionEnd)
        val startColumn = 1 + editor.selectionModel.selectionStartPosition!!.getColumn()
        val endColumn = 1 + editor.selectionModel.selectionEndPosition!!.getColumn()
        val startOffset = LineOffset(startLine, startColumn)
        val endOffset = LineOffset(endLine, endColumn)
        Selection.MultilineSelection(startOffset, endOffset)
    } else {
        val lineOffset = LineOffset(1 + editor.document.getLineNumber(editor.caretModel.offset), 0)
        Selection.SingleLinkSelection(lineOffset)
    }

    val repository = getRepositoryFor(currentFile, project)
    val remotesMap = repository.getRemotesMap()

    editor.showClickableListIfNeeded(things = remotesMap.keys.toList(), title = "Select a Remote") { remote ->
        project.runInBackground("Generating the line link") {
            val repositoryInfo = getInfo(repository, project, remote)

            val filePath = VfsUtilCore
                .findRelativePath(repositoryInfo.root, currentFile, VfsUtilCore.VFS_SEPARATOR_CHAR)
                ?: throw RelativePathException()

            val remoteLink = remotesMap[remote]
                ?: return@runInBackground

            val urlBuilder = UrlBuilders.fromDomain(remoteLink.toDomain())
            val url = urlBuilder.buildUrl(remoteLink, repositoryInfo, filePath, selection)

            CopyPasteManager.getInstance().setContents(StringSelection(url))
            project.toast("Line link copied: $url")
        }
    }
}
