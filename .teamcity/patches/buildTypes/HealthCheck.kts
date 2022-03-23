package patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with id = 'HealthCheck'
accordingly, and delete the patch script.
*/
changeBuildType(RelativeId("HealthCheck")) {
    expectSteps {
        gradle {
            name = "Execute Health Check(s)"
            tasks = "clean test --tests com.demo.e2e.HealthCheck"
            buildFile = "library/build.gradle"
        }
    }
    steps {
        insert(1) {
            script {
                enabled = false
                scriptContent = """
                    set ENVIRONMENT=Alameda
                    echo %%ENVIRONMENT%%
                """.trimIndent()
            }
        }
    }
}
