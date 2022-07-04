package patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.GradleBuildStep
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
        gradle {
            name = "Execute Visual Tests"
            executionMode = BuildStep.ExecutionMode.ALWAYS
            tasks = "clean test --tests com.demo.e2e.SampleVisualTests"
            buildFile = "visual/build.gradle"
            dockerImage = "selenium/standalone-chrome:3.141.59"
            dockerRunParameters = "--name visual-container -d -p 4445:4444"
        }
    }
    steps {
        insert(0) {
            script {
                name = "Starting Container"
                scriptContent = "docker run -d -p 4445:4444 -v /dev/shm:/dev/shm selenium/standalone-chrome"
            }
        }
        update<GradleBuildStep>(1) {
            clearConditions()
            dockerImage = ""
            dockerRunParameters = "--name visual-container -d -p 4445:4444 -v /dev/shm:/dev/shm"
        }
    }
}
