import jetbrains.buildServer.configs.kotlin.v2019_2.BuildStep
import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2019_2.DslContext
import jetbrains.buildServer.configs.kotlin.v2019_2.FailureAction
import jetbrains.buildServer.configs.kotlin.v2019_2.ParameterDisplay
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.project
import jetbrains.buildServer.configs.kotlin.v2019_2.projectFeatures.buildReportTab
import jetbrains.buildServer.configs.kotlin.v2019_2.sequential
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.schedule
import jetbrains.buildServer.configs.kotlin.v2019_2.ui.add
import jetbrains.buildServer.configs.kotlin.v2019_2.version

version = "2020.1"

project {
    buildType(HealthCheck)
    buildType(E2ETests)
    buildType(APITests)
    buildType(Release)
    buildType(UITests)
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
//            buildType(UITests)
        }
        buildType(Release)
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
    params {
        select(
            name = "RUN_MODE",
            value = "%RELEASE_RUN_MODE%",
            label = "RUN MODE",
            description = "Test(s) Run Mode",
            display = ParameterDisplay.PROMPT,
            options = listOf("Comprehensive", "Sanity"),
            readOnly = false,
            allowMultiple = false
        )
        text(
            name = "ZEPHYR_VERSION",
            value = "%RELEASE_VERSION%",
            label = "ZEPHYR TEST VERSION",
            description = "Product Release Version (Ex.) 22.1.0",
            display = ParameterDisplay.PROMPT,
            readOnly = false,
            allowEmpty = true
        )
        text(
            name = "ZEPHYR_CYCLE",
            value = "%RELEASE_CYCLE%",
            label = "ZEPHYR TEST CYCLE",
            description = "Test Execution Cycle",
            display = ParameterDisplay.PROMPT,
            readOnly = false,
            allowEmpty = true
        )
        text(
            name = "ZEPHYR_FOLDER",
            value = "%RELEASE_API_FOLDER%",
            label = "ZEPHYR TEST FOLDER",
            description = "Test(s) Execution Folder",
            display = ParameterDisplay.PROMPT,
            readOnly = false,
            allowEmpty = true
        )
    }
    dependencies {
        snapshot(HealthCheck) {
            onDependencyFailure = FailureAction.FAIL_TO_START
        }
    }
    steps {
        gradle {
            name = "Execute API Test(s)"
            tasks = "clean test -Drun.group=%RUN_MODE%"
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
    params {
        select(
            name = "RUN_MODE",
            value = "%RELEASE_RUN_MODE%",
            label = "RUN MODE",
            description = "Test(s) Run Mode",
            display = ParameterDisplay.PROMPT,
            options = listOf("Comprehensive", "Sanity"),
            readOnly = false,
            allowMultiple = false
        )
        text(
            name = "ZEPHYR_VERSION",
            value = "%RELEASE_VERSION%",
            label = "ZEPHYR TEST VERSION",
            description = "Product Release Version (Ex.) 22.1.0",
            display = ParameterDisplay.PROMPT,
            readOnly = false,
            allowEmpty = true
        )
        text(
            name = "ZEPHYR_CYCLE",
            value = "%RELEASE_CYCLE%",
            label = "ZEPHYR TEST CYCLE",
            description = "Test Execution Cycle",
            display = ParameterDisplay.PROMPT,
            readOnly = false,
            allowEmpty = true
        )
        text(
            name = "ZEPHYR_FOLDER",
            value = "%RELEASE_E2E_FOLDER%",
            label = "ZEPHYR TEST FOLDER",
            description = "Test(s) Execution Folder",
            display = ParameterDisplay.PROMPT,
            readOnly = false,
            allowEmpty = true
        )
    }
    dependencies {
        snapshot(HealthCheck) {
            onDependencyFailure = FailureAction.FAIL_TO_START
        }
    }
    steps {
        gradle {
            name = "Execute E2E Test(s)"
            tasks = "clean test -Drun.group=%RUN_MODE%"
            buildFile = "e2e-tests/build.gradle"
        }
        script {
            name = "Say Hello"
            executionMode = BuildStep.ExecutionMode.ALWAYS

            conditions {
                doesNotEqual("RELEASE_VERSION", "")
            }
            scriptContent = "echo 'Say Hello'"
        }
    }
})

object Release : BuildType({
    name = "Product Release"

    params {
        select(
            name = "reverse.dep.*.RELEASE_RUN_MODE",
            value = "Comprehensive",
            label = "RUN MODE",
            description = "Test(s) Run Mode",
            display = ParameterDisplay.PROMPT,
            options = listOf("Comprehensive", "Sanity"),
            readOnly = false,
            allowMultiple = false
        )
        text(
            name = "reverse.dep.*.RELEASE_VERSION",
            value = "",
            label = "ZEPHYR TEST VERSION",
            description = "Product Release Version (Ex.) 22.1.0",
            display = ParameterDisplay.PROMPT,
            readOnly = false,
            allowEmpty = true
        )
        text(
            name = "reverse.dep.*.RELEASE_CYCLE",
            value = "",
            label = "ZEPHYR TEST CYCLE",
            description = "Test Execution Cycle",
            display = ParameterDisplay.PROMPT,
            readOnly = false,
            allowEmpty = true
        )
        text(
            name = "reverse.dep.*.RELEASE_E2E_FOLDER",
            value = "",
            label = "ZEPHYR E2E TEST FOLDER",
            description = "E2E Test(s) Execution Folder",
            display = ParameterDisplay.PROMPT,
            readOnly = false,
            allowEmpty = true
        )
        text(
            name = "reverse.dep.*.RELEASE_API_FOLDER",
            value = "",
            label = "ZEPHYR API TEST FOLDER",
            description = "API Test(s) Execution Folder",
            display = ParameterDisplay.PROMPT,
            readOnly = false,
            allowEmpty = true
        )
    }
    steps {
        script {
            scriptContent = """echo "Executing %reverse.dep.*.RELEASE_RUN_MODE% suite""""
        }
    }
})

object UITests : BuildType({
    name = "UI Tests"
    artifactRules = "ui-test-report.xml => ui-test-report.xml"

    params {
        text(
            name = "TestIM_BRANCH",
            value = "feature/lic-usage",
            label = "TestIM BRANCH",
            description = "Feature Branch in TestIM",
            display = ParameterDisplay.NORMAL,
            readOnly = false,
            allowEmpty = false
        )
    }
    steps {
        script {
            clearConditions()
            scriptContent =
                """
                set -x
                mkdir -p "%system.teamcity.build.workingDir%/.npm-packages"
                prefix=%system.teamcity.build.workingDir%/.npm-packages
                NPM_PACKAGES="%system.teamcity.build.workingDir%/.npm-packages"
                export PATH="${'$'}PATH:${'$'}NPM_PACKAGES/bin"
                export NODE_PATH="${'$'}NODE_PATH:${'$'}NPM_PACKAGES/lib/node_modules"
                npm config set prefix %system.teamcity.build.workingDir%/.npm-packages
                npm install -g @testim/testim-cli
                set +x
                %system.teamcity.build.workingDir%/.npm-packages/bin/testim \
                  --token "b0Q13JwYtxAQ7EecdNMLbkW4YE61DcUYkpe1oAAQCTYjwwbWYA" \
                  --project "aeHu7B27U7VgxRvjagV2" \
                  --label "Licensing" \
                  --branch %TestIM_BRANCH% \
                  --grid "Testim-Grid" \
                  --reporters teamcity,console
            """.trimIndent()
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
            label = "RUN ONLY",
            description = "To run Single Test : SampleE2ETests/com.demo.e2e.SampleE2ETests, To run all the Tests in a package - com.demo.e2e.*",
            display = ParameterDisplay.PROMPT,
            readOnly = false,
            allowEmpty = false
        )
        select(
            name = "TEST_TYPE",
            value = "",
            label = "TEST TYPE",
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
