import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2019_2.DslContext
import jetbrains.buildServer.configs.kotlin.v2019_2.FailureAction
import jetbrains.buildServer.configs.kotlin.v2019_2.ParameterDisplay
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.project
import jetbrains.buildServer.configs.kotlin.v2019_2.projectFeatures.buildReportTab
import jetbrains.buildServer.configs.kotlin.v2019_2.sequential
import jetbrains.buildServer.configs.kotlin.v2019_2.ui.add
import jetbrains.buildServer.configs.kotlin.v2019_2.version

version = "2020.1"

project {
    buildType(HealthCheck)
    buildType(E2ETests)
    buildType(APITests)
    buildType(Release)
    buildType(ReleaseCycleSetup)

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
        buildType(ReleaseCycleSetup)
        parallel {
            buildType(E2ETests)
            buildType(APITests)
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
            label = "RELEASE VERSION",
            description = "Product Release Version (Ex.) 22.1.0",
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

object ReleaseCycleSetup : BuildType({
    name = "Release Cycle Setup"

    params {
        text(
            name = "VERSION",
            value = "%RELEASE_VERSION%",
            label = "RELEASE VERSION",
            description = "Product Release Version (Ex.) 22.1.0",
            display = ParameterDisplay.PROMPT,
            readOnly = false,
            allowEmpty = true
        )
    }
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
            name = "Execute Health Check(s)"
            conditions {
                matches("%VERSION%", "^[0-9]{2}\\.[0-9]{1,2}\\.[0-9]{1,2}")
            }
            tasks = "jar"
            buildFile = "library/build.gradle"
        }
        script {
            name = "Say Hello"
            conditions {
                matches("%VERSION%", "^[0-9]{2}\\.[0-9]{1,2}\\.[0-9]{1,2}")
            }
            scriptContent = "java -jar ./jars/ReleaseCycleSetup.jar %VERSION%"
        }
    }
})
