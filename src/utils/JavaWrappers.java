package utils;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JavaWrappers {

  static Logger log = LoggerFactory.getLogger(JavaWrappers.class);

  public static String getFormatedText(String value, String... params) {
    value = MessageFormat.format(value, (Object[]) params);
    return value;
  }

  public static void getMachineTimeZone() {
    Calendar now = Calendar.getInstance();

    //get current TimeZone using getTimeZone method of Calendar class
    TimeZone timeZone = now.getTimeZone();

    //display current TimeZone using getDisplayName() method of TimeZone class
    System.out.println("Current TimeZone is : " + timeZone.getDisplayName());
  }

  public static String getMinus_N_HourInCurrentTimePST(String DATE_FORMAT, int minusHour) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    ZoneId timeZone = ZoneId.of("America/New_York");
    LocalDateTime datetime = LocalDateTime.now(timeZone);
    String currentTime = datetime.format(formatter);
    datetime = datetime.minusHours(minusHour);
    String aftersubtraction = datetime.format(formatter);
    return currentTime + "~" + aftersubtraction;
  }


  public static String getCurrentDate() {
    DateFormat dateFormat = new SimpleDateFormat("MMM-D-YYYY");
    Date date = new Date();
    return dateFormat.format(date);
  }

  public static String getTimeOfAnyZone(String format, String zone) {
    Date date = new Date();
    DateFormat df = new SimpleDateFormat(format);

    // Use Madrid's time zone to format the date in
    df.setTimeZone(TimeZone.getTimeZone(zone));

    System.out.println("Date and time in " + zone + "  " + df.format(date));
    return df.format(date) + " " + zone;
  }

  /**
   * Return time in the format of (DD-MM-YYYY-HH-MM-ss)
   *
   * @return Return date_time in String
   * @author Ankit
   */
  public static String getCurrentDateAndTime() {

    DateFormat df = new SimpleDateFormat("dd-MMM-YYYY-HH-mm-ss");
    Date dateobj = new Date();
    //	       System.out.println("Current Date and time is: "+df.format(dateobj));
    return df.format(dateobj);
  }


  /**
   * Return the date in the given format
   *
   * @return Current date in the given format
   * @author Ankit
   */
  public static String getCurrentDate(String format) {

    DateFormat dateFormat = new SimpleDateFormat(format);
    Date date = new Date();
    return dateFormat.format(date);
  }


  public static String getCurrentDateInPST(String format) {
    TimeZone zone = TimeZone.getTimeZone("America/Los_Angeles");
    DateFormat dateFormat = new SimpleDateFormat(format);
    dateFormat.setTimeZone(zone);
    Date date = new Date();
    return dateFormat.format(date);
  }


  /**
   * Get yesterday date as in given format
   *
   * @param format
   * @return
   */
  public static String getYesterdayDateString(String format) {
    DateFormat dateFormat = new SimpleDateFormat(format);
    return dateFormat.format(yesterday());
  }

  private static Date yesterday() {
    final Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, -1);
    return cal.getTime();
  }


  public static String getTomorrowDateString(String format) {
    DateFormat dateFormat = new SimpleDateFormat(format);
    final Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, +1);
    return dateFormat.format(cal.getTime());
  }

  public static String getTomorrowDateInPST(String format) {
    TimeZone zone = TimeZone.getTimeZone("America/Los_Angeles");
    DateFormat dateFormat = new SimpleDateFormat(format);
    dateFormat.setTimeZone(zone);
    final Calendar cal = Calendar.getInstance(zone);
    cal.add(Calendar.DATE, +1);
    return dateFormat.format(cal.getTime());
  }

  /**
   * Return the date in the given format like(HH:mmm:ss)
   * @return Current date in the given format
   */
  public static String getCurrentTime(String format) {

    DateFormat dateFormat = new SimpleDateFormat(format);
    Date date = new Date();
    return dateFormat.format(date);
  }

  /**
   * Return time in (HH:MM:ss) where
   * @param startTime in mili second
   * @param endTime   in mili second
   * @return
   */
  public static String getTime(long startTime, long endTime) {
    long diff = endTime - startTime;
    long diffSeconds = diff / 1000 % 60;
    long diffMinutes = diff / (60 * 1000) % 60;
    long diffHours = diff / (60 * 60 * 1000) % 24;
    // long diffDays = diff / (24 * 60 * 60 * 1000);
    return (diffHours + ":" + diffMinutes + ":" + diffSeconds);
  }


  /**
   * Return time in second
   *
   * @param time      : Time string
   * @param splitTime , time format either, ":" ,  ",", "/"
   * @return
   */
  public static int getTimeInSeconds(String time, String splitTime) {
    String[] h1 = time.split(":");
    int temp;
    if (h1.length == 2) {
      int hour = Integer.parseInt(h1[0]);
      int minute = Integer.parseInt(h1[1]);
      temp = (60 * minute) + (3600 * hour);
    } else {
      int hour = Integer.parseInt(h1[0]);
      int minute = Integer.parseInt(h1[1]);
      int second = Integer.parseInt(h1[2]);
      temp = second + (60 * minute) + (3600 * hour);
    }
    System.out.println("Time: " + time + " in seconds: " + temp);
    return temp;
  }


  /**
   * Subtract all the values from given string except number and return
   * @param value
   * @return number from the given string
   */
  public static String getNumericValue(String value) {

    if (value.length() != 0) {
      return value.replaceAll("[^\\d]", "");
    } else {
      return "";
    }
  }

  public static String getAlphabetsOnly(String value) {

    if (value.length() != 0) {
      return value.replaceAll("[^A-Za-z]", "");
    } else {
      return "";
    }
  }

  /**
   * Return value in Indian Currency format, like (10,123 or 1,000)
   *
   * @param number
   * @return
   * <p>Suppose you entered A string which have number: 1456089
   * then method will return 14,56,089 as in Indian Currency format
   */
  public static String convertNumberIntoFormat(long number) {
    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
    formatter.setMaximumFractionDigits(0);
    DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) formatter)
        .getDecimalFormatSymbols();
    decimalFormatSymbols.setCurrencySymbol("");
    ((DecimalFormat) formatter).setDecimalFormatSymbols(decimalFormatSymbols);
    String moneyString = formatter.format(number).trim();
    moneyString = moneyString.replaceAll("\\u00A0", "");
    return moneyString;
  }


  /**
   * Will create directory in given path with folder name
   *
   * @param pathTofolder where to create folder
   * @param folderName   Name of the folder
   * @return file of the folder
   * @throws IOException
   */
  public synchronized static File createDir(String pathTofolder, String folderName)
      throws IOException {
    final File sysDir = new File(pathTofolder);
    File newTempDir;
    final int maxAttempts = 9;
    int attemptCount = 0;
    do {
      attemptCount++;
      if (attemptCount > maxAttempts) {
        throw new IOException("The highly improbable has occurred! Failed to "
            + "create a unique temporary directory after "
            + maxAttempts + " attempts.");
      }
      String dirName = folderName + "_" + getCurrentDateAndTime();
      newTempDir = new File(sysDir, dirName);
    } while (newTempDir.exists());

    if (newTempDir.mkdirs()) {
      return newTempDir;
    } else {
      throw new IOException("Failed to create temp dir named " + newTempDir.getAbsolutePath());
    }
  }


  /**
   * Will create directory in given path with folder name
   *
   * @param pathTofolder where to create folder
   * @param folderName   Name of the folder
   * @return file of the folder
   * @throws IOException
   */
  public synchronized static File createDirIfNotExist(String pathTofolder, String folderName)
      throws IOException {
    final File sysDir = new File(pathTofolder);
    File newTempDir = new File(sysDir, folderName);
    if (!newTempDir.exists()) {
      final int maxAttempts = 9;
      int attemptCount = 0;
      do {
        attemptCount++;
        if (attemptCount > maxAttempts) {
          throw new IOException("The highly improbable has occurred! Failed to "
              + "create a unique temporary directory after "
              + maxAttempts + " attempts.");
        }
        String dirName = folderName + "_" + getCurrentDateAndTime();
        newTempDir = new File(sysDir, dirName);
      } while (newTempDir.exists());

      if (newTempDir.mkdirs()) {
        return newTempDir;
      } else {
        throw new IOException("Failed to create temp dir named " + newTempDir.getAbsolutePath());
      }
    } else {
      return newTempDir;
    }
  }
  /**
   * Will delete the folder if Exist
   *
   * @param pathFolder
   */
  public static void deleteExistingFolder(File pathFolder) {
    if (pathFolder.exists()) {
      try {
        FileUtils.deleteDirectory(pathFolder);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * This method will return the number in between low and high, the low or high Example: if you
   * pass (5, 0), it may return 0,1,2,3,4,5
   *
   * @param high
   * @param low
   * @return
   */
  public static int getRandoNumber(int high, int low) {
    Random rn = new Random();
    int num = rn.nextInt((high - low) + 1) + low;
    return num;
  }


  public static String getRandomString(int length) {
    return RandomStringUtils.randomAlphabetic(length);
  }


  /**
   * This method will return the available port number in the passed port range
   *
   * @param high
   * @return available port number
   * @parm low
   */
  public int getAvailablePort(int high, int low) {

    int portNumber = getRandoNumber(high, low);
    try {
      new ServerSocket(portNumber).close();
//      System.out.println("port is available " + portNumber);
    } catch (Exception e) {
      System.err.println("port " + portNumber + " is not available");
      try {
        ServerSocket s = new ServerSocket(0);
        short port = (short) (s.getLocalPort());
        portNumber = Math.abs(port);
        s.close();
        System.out.println("Switching to port " + portNumber);
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
    return portNumber;
  }


  public static boolean isHindi(String text) {
    for (char charac : text.toCharArray()) {
      if (Character.UnicodeBlock.of(charac) == Character.UnicodeBlock.DEVANAGARI) {
        System.out.println("true");
        return true;
      }
    }
    return false;
  }

  public static boolean isEnglish(String name) {
    boolean found = name.matches("[a-zA-Z0-9]+");
    System.out.println(found);
    return found;
  }

//	public static void closeAllBrowsers() {
//
//		String platform = System.getProperty("os.name");
//		if (platform.startsWith("Windows")) {
//			String command = "taskkill /F /IM chrome* /T";
//			runABatFile(command);
//			command = "taskkill /F /IM ie* /T";
//			runABatFile(command);
//			command = "taskkill /F /IM firefox* /T";
//			runABatFile(command);
//		}
//	}
//	
//	public static void closeIEBrowsers() {
//
//		String platform = System.getProperty("os.name");
//		if (platform.startsWith("Windows")) {
//			String command = "taskkill /F /IM ie* /T";
//			runABatFile(command);
//		}
//	}	

//	public synchronized static boolean isFileDownloadedOnMachine(TestSuites ts, String filetype) throws Exception {
//		boolean flag = false;
//		if(isRemoteMachine()) {
//			String home = System.getProperty("user.home");
//			String downloadPath = home+File.separator+"Downloads";
//			File dir = new File(downloadPath);
//			File[] dirContents = dir.listFiles();
//
//			for (int i = 0; i <5; i++) {
//				if (getDetetedFile(ts,dirContents, filetype)) {
//					flag= true;
//					break;
//				}
//				else {
//					Thread.sleep(3000);
//					dir = new File(downloadPath);
//					dirContents = dir.listFiles();
//					if(getDetetedFile(ts,dirContents, filetype)) {
//						flag = true;
//						break;
//					}
//				}
//			}
//		}
//		else {
//			flag = true;
//		}
//		return flag;
////		return false;
//	}

//	public static String findFileOnMachine(String name) {
////		String name= "dashboard_david";
//		String path ="";
//		String home = System.getProperty("user.home");
//		String downloadPath = home+File.separator+"Downloads";
//		File file = new File(downloadPath);
//		File[] list = file.listFiles();
//		if(list!=null)
//			for (File fil : list)
//			{
//				if (fil.getName().contains(name))
//				{
//					path = fil.getAbsolutePath();
////					System.out.println(fil.getAbsolutePath());
//				}
//			}
//		return path;
//	}

//	public static boolean findAndDeleteFileOnMachine(TestSuites ts,File[] dirContents, String filetype) throws Exception {
//		for (int i = 0; i < dirContents.length; i++) {
//			if (dirContents[i].getName().contains(filetype)) {
//				try {
//				// File has been found, it can now be deleted:
//				System.out.println("Deleting Downloaded file: "+dirContents[i].getName());
//				dirContents[i].delete();
//				ts.getTestReporting().addTestSteps(ts,"<b>Deleted File </b>", dirContents[i].getName(), "PASS", false);
//				}catch(Exception e) {
//					e.printStackTrace();
//					ts.getTestReporting().addTestSteps(ts,"<b>Deleted File </b>", dirContents[i].getName(), "FAIL", false);	
//				}
//				return true;
//			}
//		}
//		return false;
//	}

//	public synchronized static void deleteDownloadedFilesOnMachine(String filetype) throws UnknownHostException {
//		if(isRemoteMachine()) {
//			String home = System.getProperty("user.home");
//			String downloadPath = home+File.separator+"Downloads";
//			File dir = new File(downloadPath);
//			File[] dirContents = dir.listFiles();
//
//			for (int i = 0; i < dirContents.length; i++) {
//				if (dirContents[i].getName().contains(filetype)) {
//					// File has been found, it can now be deleted:
//					System.out.println("Deleting file: "+dirContents[i].getName());
//					dirContents[i].delete();
//				}
//			}
//		}
//	}

//	public static boolean isRemoteMachine(){
//		try {
////			HashMap<String, String> mailMap = (HashMap<String, String>) new PropertiseFileUtility().getConfig(System.getProperty("user.dir")+File.separator+"src", "MailConfig.properties");
//			String machineIP = InetAddress.getLocalHost().getHostAddress();
//			System.out.println(machineIP);
//			if (!getprivateIP(CommonDataMaps.mailConfig).contains(machineIP)){
//				System.out.println("It's not a Jenkin Machine");
//				return false;
//			}else{
//				return true;
//			}
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	}

//	public static void setMailConfig() {
//		try {
//			CommonDataMaps.mailConfig = (HashMap<String, String>) new PropertiseFileUtility().getConfig(System.getProperty("user.dir")+File.separator+"src", "MailConfig.properties");
//			String machineIP = InetAddress.getLocalHost().getHostAddress();
//			System.out.println(machineIP);
//			if (!getprivateIP(CommonDataMaps.mailConfig).contains(machineIP)){
//				System.out.println("It's not a Jenkin Machine");
//			}else{
//				CommonDataMaps.mailConfig.put("JenkinPrivateMachineIP", machineIP);
//			}
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//			System.exit(0);
//		}
//	}


  public static void deleteDirectory(File dir) {
    try {
      recursiveDelete(dir);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void recursiveDelete(File file) {
    //to end the recursive loop
    if (!file.exists()) {
      return;
    }

    //if directory, go inside and call recursively
    if (file.isDirectory()) {
      for (File f : file.listFiles()) {
        //call recursively
        recursiveDelete(f);
      }
    }
    //call delete to delete files and empty directory
    file.delete();
    System.out.println("Deleted file/folder: " + file.getAbsolutePath());
  }

  public static String toCamelCase(String s){
    String[] parts = s.split(" ");
    String camelCaseString = "";
    for (String part : parts){
      camelCaseString = camelCaseString + toProperCase(part) + " ";
    }
    return camelCaseString.trim();
  }

  private static String toProperCase(String s) {
    return s.substring(0, 1).toUpperCase() +
        s.substring(1).toLowerCase();
  }

  public static void sleep(long seconds)  {
    try {
      Thread.sleep(seconds * 1000);
    }catch (Exception e){
      e.printStackTrace();
//      log.info("Threw a Exception in BaseUtil::wait, full stack trace follows:", e);
    }
  }


}
