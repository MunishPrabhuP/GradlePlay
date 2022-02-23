GW=./gradlew

run-ui-tests:
	testim --token "b0Q13JwYtxAQ7EecdNMLbkW4YE61DcUYkpe1oAAQCTYjwwbWYA" --project "aeHu7B27U7VgxRvjagV2" --grid "Testim-Grid" --branch "$(BRANCH)" --base-url "$(BASE_URL)" --suite "LIC $(SUITE) Tests" --retries 1 --report-file test-report.xml

upload-ui-tests-report:
	python /usr/local/bin/upload-results.py aab90813-5657-4821-9e05-e5adcb9599b5 True test-report.xml

publish-test-results:
	$(GW) -p $(TEST_TYPE) jiraIdZephyrMapper \
	    -Dresults.testng.json="$(TEST_TYPE)/build/reports/tests/test/test-jira-mapping.json" \
	    -Dproject.name="Licensing" \
	    -Dproject.version="$(RELEASE_VERSION)" \
	    -Dtest.cycle.name="$(CYCLE)" \
	    -Dtest.cycle.folder.name="$(FOLDER)" \
	    -Dsoft.export=false

update-build-number:
	echo "##teamcity[buildNumber '$(RELEASE_VERSION)']"

update-teamcity_buildconf_name-env-variable:
	echo "##teamcity[setParameter name='env.TEAMCITY_BUILDCONF_NAME' value='$(BUILDCONF_NAME)']"
