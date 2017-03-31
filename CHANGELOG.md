CHANGELOG
=========

> more update is coming soon

2.1.9 (2017-03-31)
------------------
- add the browser download folder support

2.1.8 (2017-02-28)
------------------
- update the selenium version and related driver support to 3.1.0, fix deprecate
- groovy 2.4.4 - > 2.4.8, HikariCP 2.3.9 -> 2.6.0, jackson 2.6.0 -> 2.8.6, ch.qos.logback 1.1.2 -> 1.2.1
- org.slf4j 1.7.12 -> 1.7.23, spring 4.1.7.RELEASE -> 5.0.0.M4, testng 6.9.6 -> 6.10
- git-commit-id-plugin 2.1.13 -> 2.2.2, maven-compiler-plugin 3.3 -> 3.6.1
- maven-resources-plugin 2.7 -> 3.0.2, maven.surefire 2.1.8.1 -> 2.19.1

2.1.7 (2017-01-09)
------------------
- fix the afterMethod browser hasQuit check
- disable the extensions for chrome driver
- fix the support for Linux, please set Linux32 and Linux64 in browser.properties
- fix the issue when the env parameter missing at afterSuite when generate report

2.1.6 (2016-07-31)
------------------
- open and close browser for each test method of abstract selenium test
- add the login mode parameter, able to login/logout with selected url
- stable fix for Selenium @BeforeClass, increase the robust
- more print information for debug

2.1.3 (2016-06-30)
------------------
- add ImageContrastUtil able to compare image by pixel
- modify the ScreenShotUitl for more common method

2.1.2 (2016-05-17)
------------------
- provide the multiple data source support
- fix the slack screenshot upload issue, now can do upload as token user to slack
- fix the slack properties null exception
- fix the JSONException due to the slack API update
- add the AbstractRestfulTest, split the Selenium UI test with restful test

2.0.1 (2015-12-01)
------------------
- add the slack support (support test fail log, test step, test screenshot, Bot name)
- add the slack channel and user target support, not support list yet
- add the Time Stamp for screenshot report
- add the description for screenshot report / slack
- restructure the screenshot report function
- change the pause unit from seconds to milliseconds
- add the scroll to element method for WebElements

1.5.0 (2015-11-01)
------------------
- add the demo to framework, show how to use the framework please go over the demo folder for detail.
- split the framework to uitest-parent, uitest-framework, uitest-demo
- now the uitest-framework support the Selenium Grid (must use testSuiteXML way, read README.md for detail)
- add parameter "selenium.grid.switch" properties
- add the testSuiteXml configuration way in demo
- add Ipad, Iphone, Andriod support for remote test (not test)

1.4.1 (2015-10-01)
------------------
- add pause methods for page object
- now print the method name when method start
- add detail help message for DB configuration error
- add the check is page refresh method in page super class
- add default value for browser if didn't get from properties

1.4.0 (2015-09-01)
------------------
- add the testNG listener
- add the testNG retry function
- add the propertiesUtil
- add the testNG parameter
- change the screenshot image output name "testNG.retry.switch", "testNG.retry.maxRetry" and "testNG.retry.retryType"
- add the mouseOver method in WebElements library

1.3.1 (2015-08-21)
------------------
- add the event listener functions, allow detail information
- add the parameter "event.listener.switch" and "event.listener.log.switch" in properties
- fix some comment typo
- fix the screenshot contains star problem
- fix the image output month not correct problem

1.3.0 (2015-08-15)
------------------
- add screenshot function
- add screenshot report
- add the screenshot parameter "screenshot.status" in properties

1.2.0 (2015-07-30)
------------------
- remove the unstable IE setting in SeleniumConfig
- update the selenium version from 2.40.0 to 2.45.0
- update the testNG version from 2.8.8 to 2.9.6
- update the spring version from 4.1.1.RELEASE to 4.1.7.RELEASE
- update the slf4j version from 1.7.7 to 1.7.12
- update the HikariCP version from 2.1.0 to 2.3.9
- update the groovy version from 2.3.7 to 2.3.9
- update the jackson version from 2.4.3 to 2.6.0
- update the git-commit-id-plugin version from 2.1.7 to 2.1.13
- update the maven-compiler-plugin version from 3.1 to 3.3
- update the maven-jar-plugin version from 2.4 to 2.6
- update the maven-resources-plugin version from 2.6 to 2.7
- update the maven.surefire version from 2.17 to 2.18.1

1.1.0 (2015-07-09)
------------------
- remove DB properties, use single properties
- add global time out parameter
- remove unused code
- modify the testNG annotation setting, optimize the test running speed
- remove the 10 seconds buffer time in WebElements class

1.0.0 (2015-06-26)
------------------
- initial project
- support Chrome, Firefox, IE(test in 9,11)
- has ElementHelper library, has more choice to operate web elements
- add support for oracle
- add the default MaxPoolSize in configuration
- add htmlUnit-driver support
- new methods for WebElements