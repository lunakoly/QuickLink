package org.lunakoly.quicklink.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.vcs.changes.ChangeListManager
import com.intellij.openapi.vfs.VfsUtilCore
import org.lunakoly.quicklink.repository.getRepositoryInfo
import org.lunakoly.quicklink.ui.showClickableListIfNeeded
import org.lunakoly.quicklink.ui.toast
import org.lunakoly.quicklink.urlbuilder.LineOffset
import org.lunakoly.quicklink.urlbuilder.Selection
import org.lunakoly.quicklink.urlbuilder.UrlBuilders
import org.lunakoly.quicklink.utils.PopupException
import org.lunakoly.quicklink.utils.catchingPopupExceptions
import org.lunakoly.quicklink.utils.toDomain
import java.awt.datatransfer.StringSelection

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

class ModifiedFileException : PopupException(
    "Locally modified files do not have a remote representation",
    "Modified File",
)

class CopyLineLinkAction : DumbAwareAction() {
    @Suppress("ThrowsCount")
    private fun generateLineLink(event: AnActionEvent) {
        val project = event.project
            ?: throw NoProjectException()
        val editor = event.getData(CommonDataKeys.EDITOR)
            ?: throw NoEditorException()
        val currentFile = event.dataContext.getData(CommonDataKeys.VIRTUAL_FILE)?.canonicalFile
            ?: throw NoActiveFileException()

        val selection = if (editor.selectionModel.hasSelection()) {
            val startLine = 1 + editor.selectionModel.selectionStartPosition!!.getLine()
            val endLine = 1 + editor.selectionModel.selectionEndPosition!!.getLine()
            val startColumn = 1 + editor.selectionModel.selectionStartPosition!!.getColumn()
            val endColumn = 1 + editor.selectionModel.selectionEndPosition!!.getColumn()
            val startOffset = LineOffset(startLine, startColumn)
            val endOffset = LineOffset(endLine, endColumn)
            Selection.MultilineSelection(startOffset, endOffset)
        } else {
            val lineOffset = LineOffset(1 + editor.document.getLineNumber(editor.caretModel.offset), 0)
            Selection.SingleLinkSelection(lineOffset)
        }

        val repositoryInfo = getRepositoryInfo(project, currentFile)

        val filePath = VfsUtilCore
            .findRelativePath(repositoryInfo.root, currentFile, VfsUtilCore.VFS_SEPARATOR_CHAR)
            ?: throw RelativePathException()

        val currentFileIsModified = ChangeListManager
            .getInstance(project)
            .getChange(currentFile) != null

        if (currentFileIsModified) {
            throw ModifiedFileException()
        }

        editor.showClickableListIfNeeded(
            repositoryInfo.remotesNames,
            title = "Select a Remote",
        ) {
            val remoteLink = repositoryInfo.remotes[it]
                ?: return@showClickableListIfNeeded

            val urlBuilder = UrlBuilders.fromDomain(remoteLink.toDomain())
            val url = urlBuilder.buildUrl(remoteLink, repositoryInfo, filePath, selection)

            CopyPasteManager.getInstance().setContents(StringSelection(url))
            project.toast("Line link copied: $url")
        }
    }

    override fun actionPerformed(event: AnActionEvent) {
        catchingPopupExceptions {
            generateLineLink(event)
        }
    }
}
