package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import report.custom.ReportsSession;
import test.DriverFactory;

public class SendReportInMail {

  private static Logger log = LoggerFactory.getLogger(SendReportInMail.class);


  public static void emailReport(String reportType, String subject){
    String username = DriverFactory.environment.get("sendEmailUserName");
    String password = DriverFactory.environment.get("sendEmailPassword");
    String to = DriverFactory.environment.get("to");
    String cc = DriverFactory.environment.get("cc");
//    String bcc = DriverFactory.environment.get("bcc");

    if(username.isEmpty() || password.isEmpty() || to == null)
      return;

//    if(DriverFactory.environment.get("sendEmailOnlyOnFailed").equalsIgnoreCase("false") || DriverFactory.environment.get("automationPassPercentage").equalsIgnoreCase("100")){
//      return;
//    }
    String from = username;

    Session session = Session.getDefaultInstance(getEmailProperties(), new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    });
    log.info("sending mail from: "+username);
    log.info("to: "+to);
    try{
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(from,reportType));
      message.setSubject(subject);
      message.setText("PFA");
      if(!to.trim().equals("")){
        for (int i = 0; i < to.split(";").length; i++)
        {
          if(!to.split(";")[i].trim().equals("")){
            message.addRecipient(RecipientType.TO, new InternetAddress(to.split(";")[i]));
          }
        }
      }
      if(cc != null && !cc.trim().equals("")){
        for (int i = 0; i < cc.split(";").length; i++)
        {
          if(!cc.split(";")[i].trim().equals("")){
            message.addRecipient(RecipientType.CC, new InternetAddress(cc.split(";")[i]));
          }
        }
      }
      
//      if(bcc != null && !bcc.trim().equals("")){
//        for (int i = 0; i < bcc.split(";").length; i++)
//        {
//          if(!bcc.split(";")[i].trim().equals("")){
//            message.addRecipient(RecipientType.BCC, new InternetAddress(bcc.split(";")[i]));
//          }
//        }
//      }
      
      String body = createMailBody(); 
      
      MimeBodyPart messageBodyPart = new MimeBodyPart();
      messageBodyPart.setContent(body, "text/html");
//      messageBodyPart.setText(body,"UTF-8","html");
      Multipart multipart = new MimeMultipart();
      multipart.addBodyPart(messageBodyPart);
      message.setContent(multipart);
//      System.out.println(session.getTransport().getURLName().getProtocol());
//      javax.mail.Transport transport = session.getTransport("smtp");
      Transport.send(message);
      System.out.println("Sent message successfully....");
    }catch (MessagingException | IOException mex) {
      mex.printStackTrace();
    }
  }


  private static String createMailBody() throws IOException {
	String reportPath = ReportsSession.reportFolder + "/Summary.html";
	String allureReportPath = System.getProperty("user.dir")+ "/Allure-Report/index.html";
    BufferedReader br = new BufferedReader(new FileReader(reportPath));
    String line;
    String mailBody ="Hi Team,<style type='text/css'>"+
        ".tg  {border-collapse:collapse;border-spacing:0;border-color:#999;}"+
        ".tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px"+ "5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#999;color:#444;background-color:#F7FDFA;}"+
        ".tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:10px"+ "5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#999;color:#fff;background-color:#92DAEA;}"+
        ".tg .tg-9hbo{font-weight:bold;vertical-align:top}"+
        ".tg .tg-yw4l{vertical-align:top}"+
        "</style>";
    mailBody +="<br><br><br>";
    while((line = br.readLine())!=null){
      mailBody += line;
    }
    br.close();
    mailBody += "</table>";
    mailBody += "<br>";
    mailBody += "<h4><b>Click on the link to see the detailed report : </b>";
    //mailBody +="<span style='color:blue'><a href='"+DriverFactory.environment.get("jenkinReportPath")+"'><b>report</b></a></span></h4>";
    mailBody +="<span style='color:blue'><a href='"+reportPath +"'><b>Report</b></a></span></h4>";
    
    mailBody += "<br>";
    mailBody += "<h4><b>Click on the link to see the Allure Report : </b>";
    //mailBody +="<span style='color:blue'><a href='"+DriverFactory.environment.get("jenkinReportPath")+"'><b>report</b></a></span></h4>";
    mailBody +="<span style='color:blue'><a href='"+allureReportPath +"'><b>Allure-Report</b></a></span></h4>";

//    mailBody += "<br>";
    if(!DriverFactory.environment.get("testCaseSheetUrl").isEmpty()){
      mailBody += "<h4><b>Click on the link to see the test cases sheet : </b>";
      //mailBody +="<span style='color:blue'><a href='"+DriverFactory.environment.get("testCaseSheetUrl")+"'><b>sheet</b></a></span></h4>";
    }

    mailBody += "<h4><b><span style='color:red'>Note:</b>";
    mailBody += "<span style='color:black'>contact DevOps if report's link is not accessible</span></h4>";
    mailBody += "<br>";
//   mailBody += "<span style='color:black'>firefox > about:config > privacy.file_unique_origin;false</span></h4>";	
    mailBody += "<br><br><br>";
    mailBody += "Thanks,";
    mailBody += "<br>";
    mailBody += "QaTeam";
    log.info("Email body created");
    return mailBody;

  }


  private static Properties getEmailProperties(){
    Properties properties = System.getProperties();
    properties.put("mail.smtp.host", "smtp.gmail.com");	
    properties.put("mail.smtp.socketFactory.port", "465");
    properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.transport.protocol", "smtp");
    properties.put("mail.smtp.auth", "true");
    //properties.put("mail.debug", "true");
    properties.put("mail.smtp.port", "465");
    properties.put("mail.smtp.socketFactory.fallback", "false");
    properties.put("mail.smtp.ssl.enable", "true");
    return properties;
  }



}
