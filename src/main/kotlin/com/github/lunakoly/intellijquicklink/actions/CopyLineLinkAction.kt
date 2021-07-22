package com.github.lunakoly.intellijquicklink.actions

import com.github.lunakoly.intellijquicklink.repository.getRepositoryInfo
import com.github.lunakoly.intellijquicklink.urlbuilder.UrlBuilderFactory
import com.github.lunakoly.intellijquicklink.utils.PopupException
import com.github.lunakoly.intellijquicklink.utils.catchingPopupExceptions
import com.github.lunakoly.intellijquicklink.utils.removeDirectoryStepUp
import com.github.lunakoly.intellijquicklink.utils.ui.showClickableListIfNeeded
import com.github.lunakoly.intellijquicklink.utils.ui.toast
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.vfs.VfsUtilCore
import java.awt.datatransfer.StringSelection

class NoProjectException : PopupException(
    "You first need to open a project",
    "No Project Opened",
)

class NoProjectFileException : PopupException(
    "The project file must be present",
    "No Project File Selected",
)

class NoEditorException : PopupException(
    "Something wrong happened to the editor",
    "Can't Get the Editor Instance",
)

class NoActiveFileException : PopupException(
    "You need to open some file first",
    "No File Selected",
)

class RelativePathException : PopupException(
    "Some error while calculating the relative file path",
    "Couldn't Calculate the Relative Path",
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

        editor.showClickableListIfNeeded(repositoryInfo.remotesNames) {
            val remoteLink = repositoryInfo.remotes[it]
                ?: return@showClickableListIfNeeded

            val urlBuilder = UrlBuilderFactory.guessByLink(remoteLink)
            val url = urlBuilder.buildUrl(remoteLink, repositoryInfo.branch, filePath, lineNumber)

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
