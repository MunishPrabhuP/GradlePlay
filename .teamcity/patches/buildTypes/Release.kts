package patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with id = 'Release'
accordingly, and delete the patch script.
*/
changeBuildType(RelativeId("Release")) {
    dependencies {
        expect(RelativeId("E2ETests")) {
            snapshot {
            }
        }
        update(RelativeId("E2ETests")) {
            snapshot {
                reuseBuilds = ReuseBuilds.NO
            }
        }

        expect(RelativeId("APITests")) {
            snapshot {
            }
        }
        update(RelativeId("APITests")) {
            snapshot {
                reuseBuilds = ReuseBuilds.NO
            }
        }

    }
}
