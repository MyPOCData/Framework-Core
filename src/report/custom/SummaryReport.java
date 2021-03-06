package report.custom;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.util.Date;
import test.DriverFactory;

public class SummaryReport extends ReportsSession{

  public static PrintStream printStream;
  private static int passTestCase = 0;
  private static int skipTestCase = 0;
  private static int failTestCase = 0;
  private static int total =0;

  public void initializeSummaryReport() throws FileNotFoundException {
    OutputStream outputStream = new FileOutputStream(new File(reportFolder + "/"+"Summary.html"), true);
    printStream = new PrintStream(outputStream);
   header();
  }

  private void header(){
    printStream.println("<html>");
    printStream.println("<title>Automation Summary Report</title>");
    printStream.println("<head></head>");
    printStream.println("<body>");
    String driverType = DriverFactory.environment.get("driverType");
    String user = System.getProperty("user.name");
    String machineName = "";
    try{
      machineName = InetAddress.getLocalHost().getHostName();
    } catch(Exception ex){
      //Do nothing;
    }
    printStream.println("<HTML><BODY><TABLE BORDER=0 CELLPADDING=3 CELLSPACING=1 WIDTH=100% BGCOLOR='#92DAEA'>");
    printStream.println("<TR><TD WIDTH=90% ALIGN=CENTER BGCOLOR=WHITE><FONT FACE=VERDANA COLOR='BLACK' SIZE=3><B>" + DriverFactory.environment.get("orgName") + "</B></FONT></TD></TR></TABLE>");
//    printStream.println("<TR><TD ALIGN=CENTER BGCOLOR='#92DAEA'><FONT FACE=VERDANA COLOR=Black SIZE=3><B>Module [" + featureName + "]</B><FONT></TD></TR></TABLE>");
    printStream.println("<TABLE CELLPADDING=3 WIDTH=100%><TR height=30><TD WIDTH=100% ALIGN=CENTER BGCOLOR=WHITE><FONT FACE=VERDANA COLOR=//0073C5 SIZE=2><B>&nbsp; Automation: " + new Date() + " on Machine: " + machineName + " by user: " + user + "</B></FONT></TD></TR><TR HEIGHT=5></TR>");
    if(driverType.equalsIgnoreCase("android") || driverType.equalsIgnoreCase("ios")) {
      printStream.println(
          "<TR height=30><TD WIDTH=100% ALIGN=CENTER BGCOLOR=WHITE><FONT FACE=VERDANA COLOR=//0073C5 SIZE=2><B>&nbsp; AppVersion :" + DriverFactory.environment.get("appVersion")+"-"+DriverFactory.environment.get("appBuildVersion") +"</B></FONT></TD></TR>");
      printStream.println("<BR>");
    }
    printStream.println("</TABLE>");

    printStream
        .println("<table class='tg' border='2' style='border-collapse:collapse;width:100%;'>");
    printStream.println("<tbody>");
    printStream.println("<tr bgcolor='#c6a9ff'>");
    printStream.println("<td width='auto' valign='middle' align='center'>");
    if(driverType.equalsIgnoreCase("android") || driverType.equalsIgnoreCase("ios")) {
      printStream
          .println("<b><font color='Black' face='VERDANA' size='3'>Device/Version</font></b>");
    }
    else if(DriverFactory.environment.get("projectType").equalsIgnoreCase("web")){
      printStream
          .println("<b><font color='Black' face='VERDANA' size='3'>Browser/Version</font></b>");
    }
    else if(DriverFactory.environment.get("projectType").equalsIgnoreCase("Backend")){
      printStream
          .println("<b><font color='Black' face='VERDANA' size='3'>Thread Type</font></b>");
    }
    printStream.println("<td width='auto' valign='middle' align='center'>");
    printStream
        .println("<b><font color='Black' face='VERDANA' size='3'>Priority</font></b>");
    printStream.println("<td width='auto' valign='middle' align='center'>");
    printStream
        .println("<b><font color='Black' face='VERDANA' size='3'>Module</font></b>");
    printStream.println("<td width='auto' valign='middle' align='center'>");
    printStream
        .println("<b><font color='Black' face='VERDANA' size='3'>TestCase</font></b>");
    printStream.println("</td>");
    printStream.println("<td width='auto' valign='middle' align='center'>");
    printStream.println("<b><font color='Black' face='VERDANA' size='3'>Result</font></b>");
    printStream.println("</td>");
    printStream.println("<td width='auto' valign='middle' align='center'>");
    printStream.println("<b><font color='Black' face='VERDANA' size='3'>Test Duration</font></b>");
    printStream.println("</td>");
    printStream.println("</tr>");
  }

  /**
   * Call method to add the test case into the file
   */
  public static void addTestToSummaryReport(String deviceAndVersion,String tagName,String moduleName,String scenarioName,String strPassFail,
      String partialResultFileLink, String totalExecutionTime) {
    String moduleReportLink = "<a style=\"text-decoration: none;\" href=./"+DriverFactory.getFeatureReport(moduleName).getFeatureReportFilePath() +">"+moduleName+"</a>";
    printStream.println("<tr>");
    printStream.println("<td width='15%' bgcolor='' valign='top' align='center' ><font color='black' face='VERDANA' size='2'>"+deviceAndVersion+"</font></td>");
    printStream.println("<td width='5%' bgcolor='' valign='top' align='center' ><font color='black' face='VERDANA' size='2'>"+tagName+"</font></td>");
    printStream.println("<td width='10%' bgcolor='' valign='top' align='center' ><font color='black' face='VERDANA' size='2'>"+moduleReportLink+"</font></td>");
    printStream.println("<td width='55%' bgcolor='' valign='top' align='justify' ><font color='black' face='VERDANA' size='2'>"+scenarioName+"</font></td>");

    String value = strPassFail.toUpperCase();
    String imageLink = "";
    if (value.equals("PASS")) {
      imageLink = "<a href=\"./Feature/"+ moduleName+"/TestCases/" + partialResultFileLink + "\">" + value + "</a>";
    } else {
      imageLink = "<a href=\"./Feature/"+ moduleName+"/TestCases/" + partialResultFileLink + "\"><font color='#FF7373'>"+value+"</font></a>";
    }
    if (strPassFail.toUpperCase().equalsIgnoreCase("PASS")) {
      passTestCase = passTestCase + 1;
      printStream.println("<td width='5%' bgcolor='' valign='middle' align='center'><b><font color='black' face='VERDANA' size='2'>"+imageLink+"</font></b></td>");
    } else if (strPassFail.toUpperCase().equalsIgnoreCase("FAIL")) {
      failTestCase = failTestCase + 1;
      printStream.println("<td width='5%' bgcolor='' valign='middle' align='center'><b><font color='red' face='VERDANA' size='2'>"+imageLink+"</font></b></td>");
    } else if (strPassFail.toUpperCase().equalsIgnoreCase("SKIP")) {
      skipTestCase = skipTestCase + 1;
      printStream.println("<td width='5%' bgcolor='' valign='middle' align='center'><b><font color='blue' face='VERDANA' size='2'>"+imageLink+"</font></b></td>");
    }
    printStream.println("<td width='10%' bgcolor='' valign='middle' align='center' ><font color='black' face='VERDANA' size='2'>"+totalExecutionTime+"</font></td>");
  }




}
