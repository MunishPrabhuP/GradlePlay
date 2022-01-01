import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle

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
        buildReportTab {
            id = "PROJECT_EXT_1"
            title = "Gradle Report"
            startPage = "reports/tests/test/index.html"
        }
    }
}

object CustomTestRunner : BuildType({
    name = "Custom Test Runner"
    artifactRules = "e2e-tests/build/reports => reports"

    params {
        text(
            "RUN_ONLY",
            value = "",
            label = "RUN_ONLY",
            description = "To run Single Test : TestNGAnnotation/com.demo.e2e.TestNGAnnotation, To run all the Tests in a package - com.demo.e2e.*",
            display = ParameterDisplay.PROMPT,
            readOnly = false,
            allowEmpty = false
        )
        select(
            name = "TEST_TYPE",
            value = "",
            label = "TEST_TYPE",
            description = "Type of Test(s)",
            display = ParameterDisplay.PROMPT,
            readOnly = false,
            allowMultiple = false,
            options = listOf("E2E-TESTS", "API-TESTS")
        )
    }
    vcs {
        root(DslContext.settingsRoot)
        cleanCheckout = true
    }

    steps {
        gradle {
            tasks = "clean test --tests com.demo.e2e.SampleE2ETests"
            name = "Execute Tests"
            buildFile = "e2e-tests/build.gradle"
        }
    }
})
