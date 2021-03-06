package report.custom;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.util.Date;
import lombok.Data;
import test.DriverFactory;
import utils.JavaWrappers;


@Data
public class FeatureReporting extends ReportsSession {


  private String logsFolder = "";
  private PrintStream masterPrintHtml;
  private int totalPassTestCase = 0;
  private int totalSkipTestCase = 0;
  private int totalFailTestCase = 0;
  private long mSuiteStartTime =0;
  private String featureFilePath = "";
  private String featureName = "";
  private String featureReportFolder = "";
  private String featureReportFilePath ="";


  public synchronized void createFeatureFileHeader(String featureName) {
    try {
      totalFailTestCase = 0;
      totalPassTestCase = 0;
      totalSkipTestCase = 0;
      this.featureName = featureName;
      featureReportFolder = featurePath + File.separator + featureName;
      JavaWrappers.createDir(featurePath, featureName).renameTo(new File(featurePath, featureName));
      JavaWrappers.createDir(featureReportFolder, "Feature").renameTo(new File(featureReportFolder, "TestCases"));
      String testReport = featureReportFolder + File.separator + "TestCases";
      String screenShot = testReport + File.separator + "ScreenShot";
      logsFolder = testReport + File.separator + "Logs";
			if (!(new File(screenShot).exists())) {
				JavaWrappers.createDir(testReport, "ScreenShot")
						.renameTo(new File(testReport, "ScreenShot"));
			}
      if (!(new File(logsFolder).exists())) {
        JavaWrappers.createDir(testReport, "Logs").renameTo(new File(testReport, "Logs"));
      }
      featureReportFilePath = "Feature" + File.separator + featureName + File.separator + featureName + ".html";
      openSuiteFile();
      createSuiteHtml();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    System.out.println(featureReportFolder);
  }


  private void openSuiteFile() {
    try {
      OutputStream masterHtmlFile = new FileOutputStream(
          new File(featureReportFolder + "/" + featureName + ".html"), true);
      featureFilePath = featureReportFolder + File.separator + featureName + ".html";
      masterPrintHtml = new PrintStream(masterHtmlFile);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * Call method to add the test case into the file
   *
   * @param strPassFail
   * @param scenarioName
   * @param partialResultFileLink
   */
  public void addTestToFeatureFile(String deviceAndVersion,String tagName,String moduleName,String scenarioName,String strPassFail,
      String partialResultFileLink, String totalExecutionTime) {

    masterPrintHtml.println("<tr>");
    masterPrintHtml.println("<td width='15%' bgcolor='' valign='top' align='center' ><font color='black' face='VERDANA' size='2'>"+deviceAndVersion+"</font></td>");
    masterPrintHtml.println("<td width='5%' bgcolor='' valign='top' align='center' ><font color='black' face='VERDANA' size='2'>"+tagName+"</font></td>");
    masterPrintHtml.println("<td width='10%' bgcolor='' valign='top' align='center' ><font color='black' face='VERDANA' size='2'>"+moduleName+"</font></td>");
    masterPrintHtml.println("<td width='55%' bgcolor='' valign='top' align='justify' ><font color='black' face='VERDANA' size='2'>"+scenarioName+"</font></td>");

    String value = strPassFail.toUpperCase();
    String imageLink = "";
		if (value.equals("PASS")) {
			imageLink = "<a href=\"" + "./TestCases/" + partialResultFileLink + "\">" + value + "</a>";
		} else {
			imageLink = "<a href=\"" + "./TestCases/" + partialResultFileLink + "\"><font color='#FF7373'>"+value+"</font></a>";
		}

    if (strPassFail.toUpperCase().equalsIgnoreCase("PASS")) {
      this.totalPassTestCase = this.totalPassTestCase + 1;
      masterPrintHtml.println("<td width='5%' bgcolor='' valign='middle' align='center'><b><font color='black' face='VERDANA' size='2'>"+imageLink+"</font></b></td>");
    } else if (strPassFail.toUpperCase().equalsIgnoreCase("FAIL")) {
      this.totalFailTestCase = this.totalFailTestCase + 1;
      masterPrintHtml.println("<td width='5%' bgcolor='' valign='middle' align='center'><b><font color='red' face='VERDANA' size='2'>"+imageLink+"</font></b></td>");
    } else if (strPassFail.toUpperCase().equalsIgnoreCase("SKIP")) {
      this.totalSkipTestCase = this.totalSkipTestCase + 1;
      masterPrintHtml.println("<td width='5%' bgcolor='' valign='middle' align='center'><b><font color='blue' face='VERDANA' size='2'>"+imageLink+"</font></b></td>");
    }
    masterPrintHtml.println("<td width='10%' bgcolor='' valign='middle' align='center' ><font color='black' face='VERDANA' size='2'>"+totalExecutionTime+"</font></td>");
    SummaryReport.addTestToSummaryReport(deviceAndVersion,tagName,moduleName,scenarioName,strPassFail,partialResultFileLink,totalExecutionTime);
  }


  /**
   * Create a header functionality for master file html
   *
   * @author Ankit
   */
  public String createAPIFeatureFileHeader( String name) {
    try {
      totalFailTestCase = 0;
      totalPassTestCase = 0;
      totalSkipTestCase = 0;
      featureName = name;
      JavaWrappers.createDir(featurePath, featureName)
          .renameTo(new File(featurePath, featureName));
      featureReportFolder = featurePath + File.separator + featureName;
      JavaWrappers.createDir(featureReportFolder, "Feature")
          .renameTo(new File(featureReportFolder, "Feature"));
      featureReportFilePath = "Feature" + File.separator + featureName + File.separator + featureName + ".html";
      openSuiteFile();
      createAPIFeatureHtml();
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return featureReportFolder;
  }


  public void createAPIFeatureHtml() {
    // creating html table
    masterPrintHtml.println("<html>");
    masterPrintHtml.println("<title>API Test Script-Report</title>");
    masterPrintHtml.println("<head></head>");
    masterPrintHtml.println("<body>");
    masterPrintHtml.println("<script type=\"text/javascript\">" +

        "function toggle_visibility(id,id1) {" +
        "var e = document.getElementById(id);" +

        "if(e.style.display == 'block') {" +
        "document.getElementById(id1).innerHTML = \"+\";" +
        "e.style.display = 'none';" +

        "}" +
        "else {" +
        "document.getElementById(id1).innerHTML = \"-\";" +
        "e.style.display = 'block';" +

        "}" +
        "}" +
        "</script>");

    // mentioning Suite name and Application on top of the table
    masterPrintHtml.println("<h2 align='center'>");
    masterPrintHtml.println("<span style='color:grey'>" + featureName + "</span>");
    masterPrintHtml.println("<BR>");
    masterPrintHtml.println("<BR>");
    //creating table for TestCases and their results
//		masterPrintHtml.println("<table border='0' width='100%' height='45'>");
    masterPrintHtml
        .println("<table class='tg' border='2' style='border-collapse:collapse;width:100%;'>");
    masterPrintHtml.println("<tbody>");

    //Setting the header for the table
//		masterPrintHtml.println("<tr bgcolor='#b3b3ff'>");
    masterPrintHtml.println("<tr bgcolor='#92DAEA'>");
    masterPrintHtml
        .println("<b><font color='Black' face='VERDANA' size='4'>TestCaseName</font></b>");
    masterPrintHtml.println("</td>");
    masterPrintHtml.println("<td width='auto' valign='middle' align='center'>");
    masterPrintHtml.println("<b><font color='Black' face='VERDANA' size='4'>Status</font></b>");
    masterPrintHtml.println("</td>");
    masterPrintHtml.println("</td>");
//		masterPrintHtml.println("<td width='auto' valign='middle' align='center'>");
//		masterPrintHtml.println("<b><font color='Black' face='VERDANA' size='4'>Blocked percentage</font></b>");
//		masterPrintHtml.println("</td>");
    masterPrintHtml.println("<td width='auto' valign='middle' align='center'>");
    masterPrintHtml.println("<b><font color='Black' face='VERDANA' size='4'>TimeTaken</font></b>");
    masterPrintHtml.println("</td>");
    masterPrintHtml.println("</tr>");
  }


  private void createSuiteHtml() {
    // creating html table
    masterPrintHtml.println("<html>");
    masterPrintHtml.println("<title>Automation Test Script-Report</title>");
    masterPrintHtml.println("<head></head>");
    masterPrintHtml.println("<body>");
    String driverType = DriverFactory.environment.get("driverType");
    String user = System.getProperty("user.name");
    String machineName = "";
    try{
      machineName = InetAddress.getLocalHost().getHostName();
    } catch(Exception ex){
      //Do nothing;
    }
    masterPrintHtml.println("<HTML><BODY><TABLE BORDER=0 CELLPADDING=3 CELLSPACING=1 WIDTH=100% BGCOLOR='#92DAEA'>");
    masterPrintHtml.println("<TR><TD WIDTH=90% ALIGN=CENTER BGCOLOR=WHITE><FONT FACE=VERDANA COLOR='BLACK' SIZE=3><B>" + DriverFactory.environment.get("orgName") + "</B></FONT></TD></TR>");
    masterPrintHtml.println("<TR><TD ALIGN=CENTER BGCOLOR='#92DAEA'><FONT FACE=VERDANA COLOR=Black SIZE=3><B>Module [" + featureName + "]</B><FONT></TD></TR></TABLE>");
    masterPrintHtml.println("<TABLE CELLPADDING=3 WIDTH=100%><TR height=30><TD WIDTH=100% ALIGN=CENTER BGCOLOR=WHITE><FONT FACE=VERDANA COLOR=//0073C5 SIZE=2><B>&nbsp; Automation: " + new Date() + " on Machine: " + machineName + " by user: " + user + "</B></FONT></TD></TR><TR HEIGHT=5></TR>");
    if(driverType.equalsIgnoreCase("android") || driverType.equalsIgnoreCase("ios")) {
      masterPrintHtml.println(
          "<TR height=30><TD WIDTH=100% ALIGN=CENTER BGCOLOR=WHITE><FONT FACE=VERDANA COLOR=//0073C5 SIZE=2><B>&nbsp; AppVersion :" + DriverFactory.environment.get("appVersion")+"-"+DriverFactory.environment.get("appBuildVersion") +"</B></FONT></TD></TR>");
      masterPrintHtml.println("<BR>");
    }
    masterPrintHtml.println("</TABLE>");
    masterPrintHtml
        .println("<table class='tg' border='2' style='border-collapse:collapse;width:100%;'>");
    masterPrintHtml.println("<tbody>");

    //Setting the header for the table
    masterPrintHtml.println("<tr bgcolor='#c6a9ff'>");
    masterPrintHtml.println("<td width='auto' valign='middle' align='center'>");
    if(driverType.equalsIgnoreCase("android") || driverType.equalsIgnoreCase("ios")) {
      masterPrintHtml
          .println("<b><font color='Black' face='VERDANA' size='3'>Device/Version</font></b>");
    }
    else if(DriverFactory.environment.get("projectType").equalsIgnoreCase("web")){
      masterPrintHtml
          .println("<b><font color='Black' face='VERDANA' size='3'>Browser/Version</font></b>");
    }
    else if(DriverFactory.environment.get("projectType").equalsIgnoreCase("Backend")){
      masterPrintHtml
          .println("<b><font color='Black' face='VERDANA' size='3'>Thread Type</font></b>");
    }
    masterPrintHtml.println("<td width='auto' valign='middle' align='center'>");
    masterPrintHtml
        .println("<b><font color='Black' face='VERDANA' size='3'>Priority</font></b>");
    masterPrintHtml.println("<td width='auto' valign='middle' align='center'>");
    masterPrintHtml
        .println("<b><font color='Black' face='VERDANA' size='3'>Module</font></b>");
    masterPrintHtml.println("<td width='auto' valign='middle' align='center'>");
    masterPrintHtml
        .println("<b><font color='Black' face='VERDANA' size='3'>TestCase</font></b>");
    masterPrintHtml.println("</td>");
    masterPrintHtml.println("<td width='auto' valign='middle' align='center'>");
    masterPrintHtml.println("<b><font color='Black' face='VERDANA' size='3'>Result</font></b>");
    masterPrintHtml.println("</td>");
    masterPrintHtml.println("<td width='auto' valign='middle' align='center'>");
    masterPrintHtml.println("<b><font color='Black' face='VERDANA' size='3'>Test Duration</font></b>");
    masterPrintHtml.println("</td>");
    masterPrintHtml.println("</tr>");
    mSuiteStartTime = System.currentTimeMillis();
  }

  /**
   * create footer part of the master file html
   *
   */
  private void completeSuiteFileFooter(MailReport finalReport) {
    openSuiteFile();
    long mEndTime = System.currentTimeMillis();
    int testCaseTotal = this.totalPassTestCase + this.totalFailTestCase;
    System.out.println(testCaseTotal);
    masterPrintHtml.println("</tbody>");
    masterPrintHtml.println("</table>");
    masterPrintHtml.println("<br>");

    masterPrintHtml
        .println("<table class='tg' border='2' style='border-collapse:collapse;width:100%;'>");
    masterPrintHtml.println("<tbody>");
    masterPrintHtml.println("<tr style='background-color:#92DAEA;text-align:center'>");
    masterPrintHtml.println(
        "<th class='tg-031e' colspan='6'><b><font color='Black' face='VERDANA' size='3'>TestScenario Execution Details</b><br></th>");
    masterPrintHtml.println("</tr>");
    masterPrintHtml.println("<tr style='background-color:#D7E0E1;'>");
    masterPrintHtml.println(
        "<td class='tg-9hbo' style='text-align:center'><b><font color='Black' face='VERDANA' size='2'>Total Test Cases</b></td>");
    masterPrintHtml.println(
        "<td class='tg-9hbo' style='text-align:center'><b><font color='Black' face='VERDANA' size='2'>Passed</b></td>");
    masterPrintHtml.println(
        "<td class='tg-9hbo' style='text-align:center'><b><font color='Black' face='VERDANA' size='2'>Failed</b></td>");
    masterPrintHtml.println(
        "<td class='tg-9hbo' style='text-align:center'><b><font color='Black' face='VERDANA' size='2'>Skipped</b></td>");
    masterPrintHtml.println(
        "<td class='tg-9hbo' style='text-align:center'><b><font color='Black' face='VERDANA' size='2'>Start Time</b></td>");
    masterPrintHtml.println(
        "<td class='tg-9hbo' style='text-align:center'><b><font color='Black' face='VERDANA' size='2'>End Time</b></td>");
    masterPrintHtml.println(
        "<td class='tg-9hbo' style='text-align:center'><b><font color='Black' face='VERDANA' size='2'>Total Execution Time</b></td>");
    masterPrintHtml.println("</td>");
    masterPrintHtml.println("<tr>");
    masterPrintHtml.println(
        "<td class='tg-yw4l' style='text-align:center'><font color='Black' face='VERDANA' size='2'>"
            + testCaseTotal + "</td>");
    masterPrintHtml.println(
        "<td class='tg-yw4l' style='text-align:center'><font color='Black' face='VERDANA' size='2'>"
            + this.totalPassTestCase + "</td>");
    masterPrintHtml.println(
        "<td class='tg-baqh' style='text-align:center'><font color='Black' face='VERDANA' size='2'>"
            + this.totalFailTestCase + "</td>");
    masterPrintHtml.println(
        "<td class='tg-baqh' style='text-align:center'><font color='Black' face='VERDANA' size='2'>"
            + this.totalSkipTestCase + "</td>");
    masterPrintHtml.println(
        "<td class='tg-yw4l' style='text-align:center'><font color='Black' face='VERDANA' size='2'>"
            + mSuiteStartTime + "</td>");
    masterPrintHtml.println(
        "<td class='tg-yw4l' style='text-align:center'><font color='Black' face='VERDANA' size='2'>"
            + JavaWrappers.getCurrentTime("HH:m:ss") + "</td>");
    String totalTimeTaken = JavaWrappers.getTime(mSuiteStartTime, mEndTime);
    masterPrintHtml.println(
        "<td class='tg-yw4l' style='text-align:center'><font color='Black' face='VERDANA' size='2'>"
            + totalTimeTaken + "</td>");
    masterPrintHtml.println("</tr>");

    masterPrintHtml.println("</tbody>");
    masterPrintHtml.println("</table>");
    masterPrintHtml.println("</body>");
    masterPrintHtml.println("</html>");
    masterPrintHtml.close();
    finalReport
        .addSuiteFileInto_FinalReport(this.totalPassTestCase, this.totalFailTestCase, this.totalSkipTestCase,featureName,
            featureReportFilePath, totalTimeTaken);
  }


}
