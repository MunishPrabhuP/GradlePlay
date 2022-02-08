package patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.ScriptBuildStep
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with id = 'E2ETests'
accordingly, and delete the patch script.
*/
changeBuildType(RelativeId("E2ETests")) {
    params {
        add {
            param("RELEASE_CYCLE", "")
        }
        add {
            param("RELEASE_E2E_FOLDER", "")
        }
        add {
            param("RELEASE_VERSION", "")
        }
        add {
            param("RELEASE_RUN_MODE", "")
        }
    }

    expectSteps {
        gradle {
            name = "Execute E2E Test(s)"
            tasks = "clean test -Drun.group=%RUN_MODE%"
            buildFile = "e2e-tests/build.gradle"
        }
        script {
            name = "Say Hello"
            executionMode = BuildStep.ExecutionMode.ALWAYS

            conditions {
                startsWith("ZEPHYR_VERSION", "%")
            }
            scriptContent = "echo 'Say Hello'"
        }
    }
    steps {
        update<ScriptBuildStep>(1) {
            clearConditions()

            conditions {
                matches("ZEPHYR_VERSION", "^[0-9]{2}")
            }
        }
    }
}
