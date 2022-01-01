import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

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
    dependencies {
        snapshot(HealthCheck) {
            onDependencyFailure = FailureAction.CANCEL
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

    vcs {
        root(DslContext.settingsRoot)
    }
    dependencies {
        snapshot(HealthCheck) {
            onDependencyFailure = FailureAction.CANCEL
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


