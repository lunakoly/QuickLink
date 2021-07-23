package com.github.lunakoly.quicklink.actions

import com.github.lunakoly.quicklink.repository.getRepositoryInfo
import com.github.lunakoly.quicklink.urlbuilder.UrlBuilderFactory
import com.github.lunakoly.quicklink.utils.PopupException
import com.github.lunakoly.quicklink.utils.catchingPopupExceptions
import com.github.lunakoly.quicklink.utils.removeDirectoryStepUp
import com.github.lunakoly.quicklink.utils.ui.showClickableListIfNeeded
import com.github.lunakoly.quicklink.utils.ui.toast
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.vcs.changes.ChangeListManager
import com.intellij.openapi.vfs.VfsUtilCore
import java.awt.datatransfer.StringSelection

class NoProjectException : PopupException(
    "Please, open a project",
    "No Open Project",
)

class NoProjectFileException : PopupException(
    "Could not access the project file",
    "No Project File",
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
    "Locally modifier files do not have a remote representation",
    "Modified File",
)

class CopyLineLinkAction : AnAction() {
    @Suppress("ThrowsCount")
    private fun generateLineLink(event: AnActionEvent) {
        val project = event.project
            ?: throw NoProjectException()
        val projectFile = project.projectFile
            ?: throw NoProjectFileException()
        val editor = event.getData(CommonDataKeys.EDITOR)
            ?: throw NoEditorException()
        val currentFile = event.dataContext.getData(CommonDataKeys.VIRTUAL_FILE)
            ?: throw NoActiveFileException()

        val filePath = VfsUtilCore
            .findRelativePath(projectFile, currentFile, VfsUtilCore.VFS_SEPARATOR_CHAR)
            ?.removeDirectoryStepUp()
            ?: throw RelativePathException()

        val lineNumber = 1 + editor.document.getLineNumber(editor.caretModel.offset)
        val repositoryInfo = getRepositoryInfo(project)

        val currentFileIsModified = ChangeListManager
            .getInstance(project)
            .getChange(currentFile) != null

        if (currentFileIsModified) {
            throw ModifiedFileException()
        }

        editor.showClickableListIfNeeded(repositoryInfo.remotesNames) {
            val remoteLink = repositoryInfo.remotes[it]
                ?: return@showClickableListIfNeeded

            val urlBuilder = UrlBuilderFactory.guessByLink(remoteLink)
            val url = urlBuilder.buildUrl(remoteLink, repositoryInfo, filePath, lineNumber)

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
