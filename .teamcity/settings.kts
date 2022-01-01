import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
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
    buildType(HealthCheck)
    buildType(E2ETests)
    buildType(APITests)
    buildType(CustomTestRunner)

    sequential {
        buildType(HealthCheck)
        parallel {
            buildType(E2ETests)
            buildType(APITests)
        }
        buildType(CustomTestRunner)
    }
}

object CustomTestRunner : BuildType({
    name = "Custom Test Runner"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        script {
            scriptContent = "echo 'Happy New Year'"
        }
    }

    triggers {
        vcs {

        }
    }
})

object APITests : BuildType({
    name = "API Tests"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        gradle {
            name = "Execute API Tests"
            tasks = "clean test --tests com.demo.e2e.SampleAPITests"
            buildFile = "api-tests/build.gradle"
        }
    }
})

object E2ETests : BuildType({
    name = "E2E Tests"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        gradle {
            name = "Execute E2E Tests"
            tasks = "clean test --tests com.demo.e2e.SampleE2ETests"
            buildFile = "e2e-tests/build.gradle"
        }
    }
})

object HealthCheck : BuildType({
    name = "Health Check"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        gradle {
            name = "Execute Health Check(s)"
            tasks = "clean test --tests com.demo.e2e.HealthCheck"
            buildFile = "build.gradle"
        }
    }
})


