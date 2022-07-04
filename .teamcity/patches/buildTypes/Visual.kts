package patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.GradleBuildStep
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.ScriptBuildStep
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with id = 'Visual'
accordingly, and delete the patch script.
*/
changeBuildType(RelativeId("Visual")) {
    expectSteps {
        script {
            name = "Starting Container"
            executionMode = BuildStep.ExecutionMode.ALWAYS
            scriptContent = "docker run -d -p 4445:4444 -v /dev/shm:/dev/shm selenium/standalone-chrome:3.141.59"
        }
        gradle {
            name = "Execute Visual Tests"
            executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
            tasks = "clean test --tests com.demo.e2e.SampleVisualTests"
            buildFile = "visual/build.gradle"
        }
    }
    steps {
        update<ScriptBuildStep>(0) {
            enabled = false
            clearConditions()
        }
        update<GradleBuildStep>(1) {
            enabled = false
            clearConditions()
        }
        insert(2) {
            gradle {
                tasks = "clean test --tests com.demo.e2e.SampleVisualTests"
                buildFile = "visual/build.gradle"
                dockerImage = "selenium/standalone-chrome:latest"
                dockerRunParameters = "-d -p 4445:4445 -p 4446:4446 --name visual_automation"
            }
        }
    }
}
