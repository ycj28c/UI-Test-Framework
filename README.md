UITest-Parent  [![Build Status](https://travis-ci.org/ycj28c/UI-Test-Framework.svg?branch=master)](https://travis-ci.org/ycj28c/UI-Test-Framework)
===============
* uitest-framework: the framework with JAVA, MAVEN, TestNG, WebDriver(Selenium2)
* uitest-demo: the demo shows how to use the uitest-framework

UITest-Framework
===============

WebDriver Automation test framework support multiple-OS and multiple-browser.

Setup
-----
1) Install the JAVA8
2) Use sourceTree or other GIT tools to download the uitest-framework code.
3) We use MAVEN to manage the project, please download and Install Apache MAVEN:
 
	http://maven.apache.org/download.cgi

How to use
----------
To implement this framework for your project, must add the uitest-framework jar as library.
At the your project, add dependency into your pom.xml

		<dependency>
			<groupId>io.ycj28c</groupId>
			<artifactId>uitest-parent</artifactId>
			<version>{latest version such as 1.0.0}-SNAPSHOT</version>
		</dependency>

Go into the uitest-framework directory, run the MAVEN command to install the uitest-framework into MAVEN local repository. 
  
	mvn package
	mvn clean install
	
Global Selenium wait time
-------------------------
This parameter allow us to set the wait time to get the element, we can use the 'ELEMENT_WAIT_TIME' when we write the pageObject, if using the ElementHelper class, the default waiting time is setting to 'ELEMENT_WAIT_TIME'.

To set the global wait time, add this into properties:

	ELEMENT_WAIT_TIME = 12 
	
if not defined, the default value is 12 seconds.

Screenshot Function
-------------------
The screenshot function will catch the testNG fails and the exception, store in "target/surefire-reports/screenshots", generate the html report in "target/surefire-reports/screenshots", compress the screenshot folder into "target/surefire-reports/screenShotReport.zip".

To open the screenshot report function, add this into properties:

	screenshot.status = on
 	
Screenshot Jenkins setting
--------------------------
To integrate the Screenshot function with Jenkins, can do the below setting in Jenkins Job:

+ In "Editable Email Notification", add "target\surefire-reports\screenShotReport.zip" in Attachments.
+ Add Parameters -> Archive the artifacts, add "target\surefire-reports\screenshots\*" in Files to archive.

Selenium Event Listener
-----------------------
The Selenium event listener allow us to monitor each operations in browser, such as before click, after element found etc. It help us to better locate the problem, also allow us to add more feature in future.

To open the Selenium event listener function, add this into properties:

	event.listener.switch = on 
	event.listener.log.switch = on

TestNG Listener & RetryAnalyzer
-------------------------------
The TestNG listener allow us to monitor the test status, can locate the problem on certain status such as onTestStart, onTestFailed etc. In uitest-framework, the TestNG listener class is "TestNGListener". TestNG listener is set to open by default.

The retry function allow us to retry the test when test fails, it will automatically setRetryAnalyzer "TestNGRetryAnalyzer" class for the test method, but if RetryAnalyzer is already existed, then will use the RetryAnalayzer you set. It dependent on the TestNG listener, to enable the retry, add below parameter in properties:
	
	# retry function on/off
	testNG.retry.switch = on
	# the max retry time
	testNG.retry.maxRetry = 2 
	# all/exception/default
	testNG.retry.retryType = exception
	
TestSuite configuration
-----------------------
Suggest to this this way, it is more flexible and powerful, add below in Surefire plugin:

	<suiteXmlFiles>
    	<suiteXmlFile>src/test/resources/testSuiteXml/${testSuite}</suiteXmlFile>
	</suiteXmlFiles>
	
${testSuite} is variable you can set in your properties, also can call by Maven such as "mvn -DtestSuite=all.xml".
inside the testSuite XML, we can define the running test and parallel running parameter. For example:

	<!DOCTYPE suite SYSTEM "http://beust.com/testng/testng-1.0.dtd" >
	<!-- testNG suite example: http://websystique.com/java/testing/testng-suites-example/ -->
	<suite name="All Suite">
		<test name="all">
			<packages>
				<package name="test.*" />
			</packages>
		</test>
	</suite>

it means we run all the tests in package test.*, for more information, please visit http://websystique.com/java/testing/testng-suites-example.

How to run multiple threads
---------------------------
We can run multiple threads through the SureFire plug-in, there are two ways to set up:

+ use "parallel" and "threadCount" parameters

	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-surefire-plugin</artifactId>
	<version>${maven.surefire.version}</version>
		<configuration>
			<parallel>classes</parallel>
			<threadCount>${threads}</threadCount>
		</configuration>
		....
Then you can run maven command such as: "mvn -Dthreads=6 test" to run test with 6 threads at classes level

+ set up at testSuiteXML
Strongly suggest to use this way, if you want to want selenium grid, must configure in testSuiteXML.
first add the suiteXmlFile configuration under SureFile plug-in, for example:

	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-surefire-plugin</artifactId>
	<version>${maven.surefire.version}</version>
	<configuration>
		<suiteXmlFiles>
			<suiteXmlFile>src/test/resources/testSuiteXml/all.xml</suiteXmlFile>
		</suiteXmlFiles>
	</configuration>
	....
	
Secondly set the xml, in this case, is all.xml:

	<!DOCTYPE suite SYSTEM "http://beust.com/testng/testng-1.0.dtd" >
	<!-- testNG suite example: http://websystique.com/java/testing/testng-suites-example/ -->
	<suite name="function test">
		<test name="Home Page Function Test" parallel="methods" thread-count="3">
			<groups>
			<run>
				<include name="ui-functionaltest" />
			</run>
		</groups>
		  <classes>
            <class name="test.ui.HomePageTest" />
        </classes>
		</test>
	</suite>

It means the test suite will run one test, which is running the "HomePageTest" class methods and those tests should be in "ui-functionaltest" group.
"parallel="methods" thread-count="3" " means parallel running in methods level, run 3 threads in parallel.

Selenium Grid
-------------
First make sure the selenium grid server and node is already running in servers, how to set up, please check "How to use selenium Grid" docs at confluence or google it.
Secondly add the parameter in properties and mark it open:

	selenium.grid.switch = on 

Most important, is to set up the testSuiteXml, add "browser","nodeUrl" for each test, for example:

	<!DOCTYPE suite SYSTEM "http://beust.com/testng/testng-1.0.dtd" >
	<!-- testNG suite example: http://websystique.com/java/testing/testng-suites-example/ -->
	<suite name="All Suite" parallel="tests" thread-count="6">
		<test name="rest">
			<classes>
	        	<class name="test.restful.HealthTest" />
	      </classes>
		</test>
		<test name="logout">
			<parameter name="browser" value="firefox" />
	      <parameter name="nodeUrl" value="http://10.10.10.1:5555"/>
			<classes>
	        	<class name="test.ui.LogoutTest" />
	      </classes>
		</test>
		<test name="login">
			<parameter name="browser" value="chrome" />
	      <parameter name="nodeUrl" value="http://10.10.10.1:5556"/>
			<classes>
	        	<class name="test.ui.LoginTest" />
	      </classes>
		</test>
		<test name="pagetest1">
	      <parameter name="nodeUrl" value="http://10.10.10.2:5556"/>
			<classes>
	        	<class name="test.ui.PageTest1" />
	      </classes>
		</test>
		<test name="pagetest2">
			<parameter name="browser" value="chrome" />
			<classes>
	        	<class name="test.ui.pagetest2"/>
	      </classes>
		</test>
	</suite>>

"parallel="tests" thread-count="6"" means run in tests level, run total 6 threads at the same time.
"<parameter name="browser" value="chrome" />" means run chrome browser in remote node.
"<parameter name="nodeUrl" value="http://10.10.10.1:5556"/>" means connect to the http://10.10.10.1 remote node.

Tips: if didn't specify the "browser" parameter, will run browser as -Dbrowser parameter, if no -Dbrowser parameter, default run firefox.
Tips: if didn't specify the "nodeUrl" parameter, will run in local machine.

Integration with Slack
---------------------
Now our uitest-framework can integration with slack, support test error log, test steps, fail screenshot.
To start this feature, we need to add parameters into your env-*.properties, below is a example:

########### Slack ###########

	slack.status = on
	slack.files.upload.api = https://slack.com/api/files.upload
	slack.token = xxxxxx-xxxxxxx-xxxxxxx-xxxxxxx-xxxxx
	slack.webhook.url = https://hooks.slack.com/services/xxxxxxx/xxxxxxx/xxxxxx
	slack.display.name = Demo Bot
	slack.send.channel = DemoChannel
	slack.send.user = DemoUser

#############################

Description:

+ slack.status: on/off, means open or close the slack me
+ slack.files.upload.api: the attachment upload slack api, default should be "https://slack.com/api/files.upload"
+ slack.token: need token to upload the attachment(screenshot) into the slack server. For token introduction, check "https://api.slack.com/web"
+ slack.webhook.url: the webhook allow us to send text or attachment into slack channel/user. For incoming Webhooks introduction, check "https://api.slack.com/incoming-webhooks"
+ slack.display.name: the bot name display
+ slack.send.channel: the channel that framework will send test result
+ slack.send.user: the user that framework will send test result

can also set the pom.xml to allow it send or not send to slack, add below configuration
		
		<plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            ...
            <property>
            			<name>slack</name> 
            			<value>${slack}</value>
      			</property>
      </plugin>
		....
		<profile>
            <id>default</id>
            <activation>
                <activeByDefault>true</activeByDefault>
                ...
            </activation>
            <properties>
                ...
                <slack>on</slack>
            </properties>
        </profile>

Then you can run MAVEN command with "-Dslack=off" to disable sending message to slack, this is very useful when integrate with jenkins.

Test Description Support
------------------------
To enable the screenshot report and slack display the test steps, must add "description" to each test cases, such as:

	@Test(groups = {"ui-smoketest", "ui-functionaltest" },
			description = " /**\n"
					+ "  * check the valid corporate account\n"
					+ "  * \n"
					+ "  * 1. goto login page\n"
					+ "  * 2. login your account\n"
					+ "  * 3. check the home page URL match with expect URL\n"
					+ "  * 4. check the home page Title match with expect Title\n"
					+ "  */")
	public void testValidAccount()	{
		....
	}

Then we can easily see the test steps in the output. If you need the tool to transfer the javaDoc to description, can contact Ralph Yang ^^

Multiple DataSource Support
--------------------------
We support the multiple datasource configuration, to import multiple datasource, write your env*.properties like this:

	product=tiger,rabbit
	
	tiger.driverClassName=oracle.jdbc.driver.OracleDriver
	tiger.url=jdbc:oracle:thin:@10.10.10.10:1521:tiger
	tiger.username=tiger
	tiger.password=tiger
	
	rabbit.driverClassName=oracle.jdbc.driver.OracleDriver
	rabbit.url=jdbc:oracle:thin:@20.20.20.20:1521:rabbit
	rabbit.username=rabbit
	rabbit.password=rabbit

To get all the datasource in your code, use functions in DataSourceUtil class. Here are the description for methods included:

+ DataSourceUtil.lookupActiveDS(): get the current active datasource
+ DataSourceUtil.lookup(String dataSource): look up the specific datasource
+ DataSourceUtil.active(String name): active the specific datasource in the hashtable as active datasoure
+ DataSourceUtil.toHash(): get the hash table for all the datasource
+ DataSourceUtil.bind(String name, HikariConfig param): bind the datasource with the configuration
+ DataSourceUtil.rebind(String name, HikariConfig param): rebind the datasource with the configuration
+ DataSourceUtil.unbind(): unbind the specific datasource from datasource hashtable
+ DataSourceUtil.getConnectionPoolsInfo(): get the datasource connection pool information

Multiple Login/Logout URL Support
--------------------------
Sometimes we may need to login/logout with different url(such as single sign on and regular login), now framework are able to config multiple login/logout:
	
	# single sign on: 1 # login2: 2
	login_mode = 1
	
	# Login/out
	loginUrl = https://xxx.xxx.xxx/app/login/login1.jsp
	logoutUrl  = https://xxx.xxx.xxx/app/login_serv1?logout
	
	loginUrl2 = https://xxx.xxx.xxx/app/login/login2.jsp
	logoutUrl2  = https://xxx.xxx.xxx/app/login_serv2?logout

Change the login_mode to select the login options you want.
To add more login/logout, just add "loginUrl" plus login_mode number, such as loginUrl3, loginUrl4.	
	
	
	
	
