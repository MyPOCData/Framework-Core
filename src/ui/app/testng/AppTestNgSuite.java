package ui.app.testng;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import report.allure.Reporting;
import test.DriverFactory;
import test.TestNgSuite;
import ui.app.android.AndroidUtility;
import ui.driverUtils.AppiumServer;
import utils.SendReportInMail;

public class AppTestNgSuite extends TestNgSuite {

	private static Logger log = LoggerFactory.getLogger(AppTestNgSuite.class);

//  private String finalReportFile = "";

	/**
	 * Below are the setup done in Before Suite
	 *
	 * @param itx 1. get the configuration file and read all values from
	 *            configurations file and save into environment variable 2.
	 *            Initialize the report folder structure
	 */
	@Override
	@BeforeSuite(alwaysRun = true)
	public void initializeSuite(ITestContext itx) throws IOException {
		setupEnvironmentAndConfig();
		initializeLocatorsFile();
		if (DriverFactory.environment.get("driverType").equalsIgnoreCase("android")
				|| DriverFactory.environment.get("driverType").equalsIgnoreCase("ios")) {
			reserveDevices(itx);
		}
		setupReport();
		
		// Clean Allure Report data and folders
		Reporting.cleanAllureResults();
	}

	@Override
	@AfterSuite(alwaysRun = true)
	public void tearDownSuite() {
		
		// Generate Allure Report
		try {
			Reporting.genrateAllureReport();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
				
		mailReport.createConsolidateReport();
		// send mail code
		boolean isMailSend = Boolean.parseBoolean(DriverFactory.environment.get("emailNotification"));
		if (isMailSend) {
			SendReportInMail.emailReport("Android_Automation", DriverFactory.environment.get("subject"));
		}
		if (DriverFactory.environment.get("driverType").equalsIgnoreCase("android")
				|| DriverFactory.environment.get("driverType").equalsIgnoreCase("ios")) {
			new AppiumServer().killAllAppiumServer();
		}
	}

	private void getTotalDeviceConnected() {
		if (DriverFactory.environment.get("driverType").equalsIgnoreCase("android")) {
			AndroidUtility ads = new AndroidUtility();
			DriverFactory.connectedDevices = ads.getConnectedDevices();
		} else {
			// for iOS
		}
	}

	public void reserveDevices(ITestContext itx) {
		getTotalDeviceConnected();
		try {
			for (int i = 0; i < DriverFactory.connectedDevices.size(); i++) {
				String deviceID = DriverFactory.connectedDevices.get(i);
				if (new AndroidUtility().networkSetUp(DriverFactory.environment.get("networkType"), deviceID)) {
					String deviceName = new AndroidUtility().getDeviceName(deviceID);
					String deviceOsVersion = new AndroidUtility().getDeviceAndroidVersion(deviceID);
					DriverFactory.deviceDetails.put(deviceID + "_" + "deviceName", deviceName);
					DriverFactory.deviceDetails.put(deviceID + "_" + "deviceOsVersion", deviceOsVersion);
					DriverFactory.availableDevices.add(deviceID);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("Total connected devices are: " + DriverFactory.connectedDevices.size());
		log.info("Total available devices are: " + DriverFactory.availableDevices.size());
		System.setProperty("TestNg_threadCount", "" + DriverFactory.availableDevices.size());
		if (DriverFactory.availableDevices.size() == 0) {
			System.err.println("There is no device available to run the suite");
			System.exit(0);
		}
		itx.getCurrentXmlTest().getSuite().setDataProviderThreadCount(DriverFactory.availableDevices.size());
		itx.getCurrentXmlTest().getSuite().setPreserveOrder(false);
	}
}
