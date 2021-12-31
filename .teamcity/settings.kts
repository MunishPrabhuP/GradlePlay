import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.projectFeatures.buildReportTab
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

version = "2021.2"

project {
    buildType(Comprehensive)
    buildType(Sanity)
    features {
        buildReportTab {
            id = "PROJECT_EXT_4"
            title = "Gradle Report"
            startPage = "reports/tests/test/index.html"
        }
    }
}

object Comprehensive : BuildType({
    name = "GradlePlay Comprehensive Test"
    artifactRules = "e2e-tests/build/reports => reports"

    params {
        text(
            name = "BRANCH",
            value = "",
            label = "BRANCH",
            description = "VCS Branch to build",
            display = ParameterDisplay.PROMPT,
            readOnly = false,
            allowEmpty = false
        )
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
//        checkbox(
//            "RERUN_FAILED_TEST",
//            "true",
//            "RERUN_FAILED_TEST",
//            "Enable this to rerun Failed TestCases",
//            ParameterDisplay.NORMAL,
//            readOnly = false
//        )
    }
    vcs {
        root(DslContext.settingsRoot)
        cleanCheckout = true
    }

    steps {
        gradle {
            name = "Execute Tests"
            buildFile = "e2e-tests/build.gradle"
            tasks = "clean test --tests com.demo.e2e.TestNGAnnotation"
        }
        script {
            name = "Say GoodBye"
            scriptContent = "echo 'Say GoodBye to Munish Prabhu'"
        }
    }
    triggers {
        vcs {
            branchFilter = "+:master"
        }
    }
})

object Sanity : BuildType({
    name = "GradlePlay Sanity Test"
    artifactRules = "e2e-tests/build/reports => reports"

    params {
        text(
            name = "BRANCH",
            value = "",
            label = "BRANCH",
            description = "VCS Branch to build",
            display = ParameterDisplay.PROMPT,
            readOnly = false,
            allowEmpty = false
        )
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
//        checkbox(
//            "RERUN_FAILED_TEST",
//            "true",
//            "RERUN_FAILED_TEST",
//            "Enable this to rerun Failed TestCases",
//            ParameterDisplay.NORMAL,
//            readOnly = false
//        )
    }
    vcs {
        root(DslContext.settingsRoot)
        cleanCheckout = true
    }

    steps {
        gradle {
            name = "Execute Tests"
            buildFile = "e2e-tests/build.gradle"
            tasks = "clean test --tests com.demo.e2e.TestNGAnnotation"
        }
        script {
            name = "Say GoodBye"
            scriptContent = "echo 'Say GoodBye to Munish Prabhu'"
        }
    }
    triggers {
        vcs {
            branchFilter = "+:master"
        }
    }
})
