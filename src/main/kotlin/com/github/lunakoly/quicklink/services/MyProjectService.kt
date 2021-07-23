package com.github.lunakoly.quicklink.services

import com.github.lunakoly.quicklink.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {
    init {
        println(MyBundle.message("projectService", project.name))
    }
}
