package patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2019_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with id = 'Release'
accordingly, and delete the patch script.
*/
changeBuildType(RelativeId("Release")) {
    vcs {

        check(showDependenciesChanges == false) {
            "Unexpected option value: showDependenciesChanges = $showDependenciesChanges"
        }
        showDependenciesChanges = true
    }

    triggers {
        add {
            vcs {
                triggerRules = "+:.teamcity/**"
                branchFilter = ""
            }
        }
    }
}
