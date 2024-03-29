package patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.exec
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.schedule
import jetbrains.buildServer.configs.kotlin.v2019_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with id = 'E2ETests'
accordingly, and delete the patch script.
*/
changeBuildType(RelativeId("E2ETests")) {
    expectSteps {
        exec {
            name = "Updating TEAMCITY_BUILDCONF_NAME Environment Variable"
            executionMode = BuildStep.ExecutionMode.ALWAYS
            path = "make"
            arguments = "update-teamcity_buildconf_name-env-variable BUILDCONF_NAME='Levitate LIC E2E %RUN_MODE% Tests'"
        }
        exec {
            name = "Updating Build Number"
            executionMode = BuildStep.ExecutionMode.ALWAYS

            conditions {
                matches("VERSION", """^[0-9]{2}\.[0-9]{1,2}\.[0-9]{1,2}""")
            }
            path = "make"
            arguments = "update-build-number RELEASE_VERSION=%VERSION%"
        }
        gradle {
            name = "Execute E2E Test(s)"
            tasks = "clean test -Drun.group=%RUN_MODE%"
            buildFile = "e2e-tests/build.gradle"
        }
    }
    steps {
        insert(3) {
            script {
                scriptContent = """
                    echo ${'$'}pwd
                    echo %teamcity.build.checkoutDir%
                    ls "%teamcity.build.checkoutDir%/api-tests"
                """.trimIndent()
            }
        }
    }

    triggers {
        add {
            schedule {
                schedulingPolicy = daily {
                    hour = 23
                    minute = 30
                    timezone = "Etc/UTC"
                }
                branchFilter = ""
                triggerBuild = always()
            }
        }
    }

    failureConditions {

        check(executionTimeoutMin == 0) {
            "Unexpected option value: executionTimeoutMin = $executionTimeoutMin"
        }
        executionTimeoutMin = 180
    }
}
