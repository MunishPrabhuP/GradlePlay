import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.projectFeatures.buildReportTab
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

version = "2021.2"

project {
    buildType(Cac)
    features {
        buildReportTab {
            id = "PROJECT_EXT_4"
            title = "Gradle Report"
            startPage = "reports/tests/test/index.html"
        }
    }
}

object Cac : BuildType({
    name = "GradlePlay CAC Test"
    artifactRules = "e2e-tests/build/reports => reports"

    params {
        text(
            "TEST_FILE",
            "",
            "TEST_FILE",
            "File/Package to test",
            ParameterDisplay.NORMAL,
            readOnly = false,
            allowEmpty = true
        )
        checkbox(
            "RERUN_FAILED_TEST",
            "",
            "TEST_FILE",
            "File/Package to test",
            ParameterDisplay.NORMAL,
            readOnly = false, "true", "false"
        )
        param("TEST_FILE", "")
        param("RERUN_TEST", "true")
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
