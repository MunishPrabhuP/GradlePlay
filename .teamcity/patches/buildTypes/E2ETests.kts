package patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with id = 'E2ETests'
accordingly, and delete the patch script.
*/
changeBuildType(RelativeId("E2ETests")) {
    expectSteps {
        gradle {
            name = "Execute E2E Test(s)"
            tasks = "clean test -Drun.group=%RUN_MODE%"
            buildFile = "e2e-tests/build.gradle"
        }
    }
    steps {
        insert(1) {
            script {
                name = "Say Hello"
                executionMode = BuildStep.ExecutionMode.ALWAYS

                conditions {
                    doesNotEqual("RELEASE_VERSION", "22.0.1")
                }
                scriptContent = "echo 'Say Hello'"
            }
        }
    }
}
