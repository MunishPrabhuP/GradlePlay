import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.CustomChart
import jetbrains.buildServer.configs.kotlin.v2019_2.CustomChart.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.projectCustomChart

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2021.2"

project {

    buildType(CustomTestRunner)

    features {
        projectCustomChart {
            id = "PROJECT_EXT_4"
            title = "New chart title"
            seriesTitle = "Serie"
            format = CustomChart.Format.TEXT
            series = listOf(
                Serie(title = "Build Duration (all stages)", key = SeriesKey.BUILD_DURATION, sourceBuildTypeId = "AppDGradlePlay_CustomTestRunner")
            )
        }
    }
}

object CustomTestRunner : BuildType({
    name = "Custom Test Runner"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        gradle {
            tasks = "clean test --tests com.demo.e2e.SampleE2ETests"
            buildFile = "e2e-tests/build.gradle"
        }
    }
})
