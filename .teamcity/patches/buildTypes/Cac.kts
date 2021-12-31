package patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.GradleBuildStep
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with id = 'Cac'
accordingly, and delete the patch script.
*/
changeBuildType(RelativeId("Cac")) {
    params {
        remove {
            checkbox("RERUN_FAILED_TEST", "true", label = "RERUN_FAILED_TEST", description = "Enable this to rerun Failed TestCases")
        }
        add {
            select("TEST_TYPE", "",
                    options = listOf("E2E-TESTS", "API-TESTS"))
        }
    }

    expectSteps {
        gradle {
            name = "Execute Tests"
            tasks = "clean test --tests com.demo.e2e.TestNGAnnotation"
            buildFile = "e2e-tests/build.gradle"
        }
        script {
            name = "Say GoodBye"
            scriptContent = "echo 'Say GoodBye to Munish Prabhu'"
        }
    }
    steps {
        update<GradleBuildStep>(0) {
            clearConditions()

            conditions {
                equals("BRANCH", "master")
            }
        }
    }
}
