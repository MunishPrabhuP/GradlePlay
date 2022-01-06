import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.projectFeatures.buildReportTab
import jetbrains.buildServer.configs.kotlin.v2019_2.ui.add

version = "2020.1"

project {
    buildType(HealthCheck)
    buildType(E2ETests)
    buildType(APITests)
    buildType(LICRelease)
    buildType(LicensingUI)
    buildType(CustomTestRunner)

    features {
        add {
            buildReportTab {
                id = "PROJECT_EXT_2"
                title = "API Report"
                startPage = "api-tests/reports/tests/test/index.html"
            }
        }
        add {
            buildReportTab {
                id = "PROJECT_EXT_3"
                title = "E2E Report"
                startPage = "e2e-tests/reports/tests/test/index.html"
            }
        }
        add {
            buildReportTab {
                id = "PROJECT_EXT_4"
                title = "HealthCheck Report"
                startPage = "health-check/reports/tests/test/index.html"
            }
        }
    }
    sequential {
        buildType(HealthCheck)
        parallel {
            buildType(E2ETests)
            buildType(APITests)
        }
        buildType(LICRelease)
    }
}

object HealthCheck : BuildType({
    name = "Health Check"
    artifactRules = "library/build/reports/ => health-check/reports/"

    vcs {
        root(DslContext.settingsRoot)
        cleanCheckout = true
    }

    steps {
        gradle {
            name = "Execute Health Check(s)"
            tasks = "clean test --tests com.demo.e2e.HealthCheck"
            buildFile = "library/build.gradle"
        }
    }
})

object APITests : BuildType({
    name = "API Tests"
    artifactRules = "api-tests/build/reports/ => api-tests/reports/"

    vcs {
        root(DslContext.settingsRoot)
        cleanCheckout = true
    }
    dependencies {
        snapshot(HealthCheck) {
            onDependencyFailure = FailureAction.FAIL_TO_START
        }
    }
    steps {
        gradle {
            name = "Execute API Test(s)"
            tasks = "clean test --tests com.demo.e2e.SampleAPITests"
            buildFile = "api-tests/build.gradle"
        }
    }
})

object E2ETests : BuildType({
    name = "E2E Tests"
    artifactRules = "e2e-tests/build/reports/ => e2e-tests/reports/"

    vcs {
        root(DslContext.settingsRoot)
        cleanCheckout = true
    }
    dependencies {
        snapshot(HealthCheck) {
            onDependencyFailure = FailureAction.FAIL_TO_START
        }
    }
    steps {
        script {
            scriptContent = "echo %dep.LICRelease.env.TEST_PHASE%"
        }
        gradle {
            name = "Execute E2E Test(s)"
            tasks = "clean test --tests com.demo.e2e.SampleE2ETests"
            buildFile = "e2e-tests/build.gradle"
        }
    }
})

object LICRelease : BuildType({
    name = "Levitate LIC Release"

    params {
        select(
            name = "env.TEST_PHASE",
            value = "",
            label = "TEST_PHASE",
            description = "Required Test Phase",
            display = ParameterDisplay.PROMPT,
            options = listOf("Comprehensive", "Sanity"),
            readOnly = false,
            allowMultiple = false
        )
    }
    steps {
        script {
            scriptContent = "echo ##teamcity[setParameter name='env.TEST_PHASE' value='dummy']"
        }
    }
})

object LicensingUI : BuildType({
    name = "Licensing UI"

    steps {
        script {
            scriptContent = "npm i -g @testim/testim-cli"
        }
        script {
            scriptContent =
                "testim --label \"Licensing\" --branch \"feature/lic-usage\" --token \"b0Q13JwYtxAQ7EecdNMLbkW4YE61DcUYkpe1oAAQCTYjwwbWYA\" --project \"aeHu7B27U7VgxRvjagV2\" --grid \"Testim-Grid\""
        }
    }
})

object CustomTestRunner : BuildType({
    name = "Custom Test Runner"

    vcs {
        root(DslContext.settingsRoot)
        cleanCheckout = true
    }
    params {
        text(
            name = "BRANCH",
            value = "master",
            label = "BRANCH",
            description = "SCM/VCS Branch",
            display = ParameterDisplay.PROMPT,
            readOnly = false,
            allowEmpty = false
        )
        text(
            name = "RUN_ONLY",
            value = "",
            label = "RUN_ONLY",
            description = "To run Single Test : SampleE2ETests/com.demo.e2e.SampleE2ETests, To run all the Tests in a package - com.demo.e2e.*",
            display = ParameterDisplay.PROMPT,
            readOnly = false,
            allowEmpty = false
        )
        select(
            name = "TEST_TYPE",
            value = "",
            label = "TEST_TYPE",
            description = "Build File of the Test(s)",
            display = ParameterDisplay.PROMPT,
            readOnly = false,
            allowMultiple = false,
            options = listOf("api-tests", "e2e-tests", "library")
        )
    }
    steps {
        script {
            name = "Fetching all Active Branch(s) from VCS"
            scriptContent = "git fetch"
        }
        script {
            name = "Checkout to Branch"
            scriptContent = "git checkout %BRANCH%"
        }
        gradle {
            name = "Execute Test(s)"
            tasks = "clean test --tests %RUN_ONLY%"
            buildFile = "%TEST_TYPE%/build.gradle"
        }
    }
})
