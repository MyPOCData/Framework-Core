package ui.app.android;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import java.net.URL;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.DriverFactory;
import ui.driverUtils.Drivers;
import utils.JavaWrappers;

public class Androiddriver extends Drivers {

  private static Logger log = LoggerFactory.getLogger(Androiddriver.class);

  public synchronized WebDriver createAndroidDriver(String deviceID, boolean noReset, boolean permission) throws Exception {
	  WebDriver driver = null;
	  new AndroidUtility().clearLogcat(deviceID);
	  new AndroidUtility().pressAndroidHomeKey(deviceID);
	  try {
		  desiredCapabilities = DesiredCapabilities.android();
		  String appiumServiceUrl = Drivers.getAppiumServerService(Integer.parseInt(DriverFactory.getTestDetails("port").trim())).getUrl().toString();
		  log.info("Appium service url for driver is " + appiumServiceUrl);
		  System.out.println("Appium service url for driver is " + appiumServiceUrl);
		  int sysPort = new JavaWrappers().getAvailablePort(5999, 5000);
		  desiredCapabilities.setCapability(MobileCapabilityType.NO_RESET, noReset);
		  desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, (Integer.parseInt(DriverFactory.environment.get("appiumNewCommandTimeOut"))*60));
		  desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, DriverFactory.getTestDetails("deviceName"));
		  desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
		  desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, DriverFactory.getTestDetails("deviceOsVersion"));
		  desiredCapabilities.setCapability(MobileCapabilityType.UDID, deviceID);
		  desiredCapabilities.setCapability(MobileCapabilityType.TAKES_SCREENSHOT, true);
		  desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
		  desiredCapabilities.setCapability(MobileCapabilityType.APPLICATION_NAME, DriverFactory.environment.get("appName"));
//    	desiredCapabilities.setCapability(MobileCapabilityType.APP, DriverFactory.environment.get("appPath"));
//	  	capabilities.setCapability("skipDeviceInitialization", true);
//	  	capabilities.setCapability("skipServerInstallation", true);
//	  	capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
//	  	capabilities.setCapability("clearDeviceLogsOnStart", true);
//    	desiredCapabilities.setCapability("appium:printPageSourceOnFindFailure", true);
		  desiredCapabilities.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS,permission);
		  desiredCapabilities.setCapability(AndroidMobileCapabilityType.BROWSER_NAME,"");
		  desiredCapabilities.setCapability(AndroidMobileCapabilityType.AUTO_LAUNCH,true);
		  desiredCapabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, DriverFactory.environment.get("appPackage"));
		  desiredCapabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, DriverFactory.environment.get("appActivity"));
		  desiredCapabilities.setCapability(AndroidMobileCapabilityType.SYSTEM_PORT, sysPort);
		  desiredCapabilities.setCapability(AndroidMobileCapabilityType.NO_SIGN, true);    
		  desiredCapabilities.setCapability(MobileCapabilityType.APP, System.getProperty("user.dir") + DriverFactory.environment.get("appLocation"));
		  try {
			  driver = new AndroidDriver<MobileElement>(new URL(appiumServiceUrl), desiredCapabilities);	
			  log.info("Android driver stated on device: "+deviceID);
			  waitForAppToBeInForeground(driver,DriverFactory.environment.get("appPackage"));
			  Drivers.setWebDriver(driver);
			  generateScreenshots();
			  JavaWrappers.sleep(1);
		  } catch (Exception e) {
			  System.err.println("Unable to initiate Android Driver on: " + DriverFactory.deviceDetails.get(deviceID + "_deviceName"));
			  e.printStackTrace();
			  throw new Exception("Unable to create Android driver "+e.getMessage());
		  }
    } catch (Exception e) {
    		e.printStackTrace();
    		DriverFactory.offlineDevices.add(deviceID);
    		throw new Exception("No Device connected " + deviceID);
    }
    return driver;
  }
}
