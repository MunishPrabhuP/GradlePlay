import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

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
}

object CustomTestRunner : BuildType({
    name = "Custom Test Runner"

    artifactRules = """
        e2e-tests/build/reports => reports
        api-tests/build/reports => reports
    """.trimIndent()

    vcs {
        root(DslContext.settingsRoot)

        cleanCheckout = true
    }

    steps {
        gradle {
            tasks = "clean test --tests com.demo.e2e.SampleE2ETests"
            buildFile = "e2e-tests/build.gradle"
        }
    }

    triggers {
        vcs {
            branchFilter = ""
            perCheckinTriggering = true
            enableQueueOptimization = false

            enforceCleanCheckout = true
        }
    }
})
