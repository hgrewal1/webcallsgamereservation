/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cegepgim;

/**
 *
 * @author zzaier
 */



import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {
   public Void email(String receiver,String pass,String user) {
      // Recipient's email ID needs to be mentioned.
      String to = receiver;

      // Sender's email ID needs to be mentioned
      String from = "harvindergrewal56@gmail.com";
      final String username = "gamereservation";//change accordingly
      final String password = "hgrewal1";//change accordingly

      // Assuming you are sending email through relay.jangosmtp.net
      String host = "smtp.gmail.com";

      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.host", host);
      props.put("mail.smtp.port", "25");

      // Get the Session object.
      Session session = Session.getInstance(props,
         new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(username, password);
       }
         });

      try {
       // Create a default MimeMessage object.
       Message message = new MimeMessage(session);
    
       // Set From: header field of the header.
       message.setFrom(new InternetAddress(from));
    
       // Set To: header field of the header.
       message.setRecipients(Message.RecipientType.TO,
               InternetAddress.parse(to));
    
       // Set Subject: header field
       message.setSubject("Forgot Password");
    
       // Now set the actual message
       message.setText("Hello, User "+user+" \n"+
               "Your password for Game reservation Account is:-\n"+pass+"\n"+
                "Thanku");

       // Send message
       Transport.send(message);

       System.out.println("Sent message successfully....");

      } catch (MessagingException e) {
         throw new RuntimeException(e);
      }
  
   return null;}
}

