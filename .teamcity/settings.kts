import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2019_2.Project
import jetbrains.buildServer.configs.kotlin.v2019_2.projectFeatures.buildReportTab
import jetbrains.buildServer.configs.kotlin.v2019_2.ui.*

version = "2021.2"

project {
    buildType(HealthCheck)
    buildType(E2ETests)
    buildType(APITests)
    buildType(CustomTestRunner)

    features {
        add {
            buildReportTab {
                id = "PROJECT_EXT_2"
                title = "API Report"
                startPage = "reports/tests/test/index.html"
            }
        }
        add {
            buildReportTab {
                id = "PROJECT_EXT_3"
                title = "E2E Report"
                startPage = "reports/tests/test/index.html"
            }
        }
        add {
            buildReportTab {
                id = "PROJECT_EXT_4"
                title = "HealthCheck Report"
                startPage = "reports/tests/test/index.html"
            }
        }
    }
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
    artifactRules = "api-tests/build/reports/ => reports/"

    vcs {
        root(DslContext.settingsRoot)
    }
    dependencies {
        snapshot(HealthCheck) {
            onDependencyFailure = FailureAction.FAIL_TO_START
        }
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
    artifactRules = "e2e-tests/build/reports/ => reports/"

    vcs {
        root(DslContext.settingsRoot)
    }
    dependencies {
        snapshot(HealthCheck) {
            onDependencyFailure = FailureAction.FAIL_TO_START
        }
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
    artifactRules = "library/build/reports/ => reports/"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        gradle {
            name = "Execute Health Check(s)"
            tasks = "clean test --tests com.demo.e2e.HealthCheck"
            buildFile = "library/build.gradle"
        }
    }
})

