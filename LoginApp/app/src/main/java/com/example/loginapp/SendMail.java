package com.example.loginapp;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {

    final String smtpHost = "smtp.163.com";
    final String username = "shawphonechan@163.com";
    final String password = "XOHYHSLXQOSZDQNW";
    final boolean debug = true;


    public void sendmsg(String to,int rnd)throws Exception{

        SendMail sender = new SendMail();
        Session session = sender.createTLSSession();
        Message message = createTextMessage(session, username, to, "验证邮件", "Your Vefification Code:"+Integer.toString(rnd));
        Transport.send(message);
    }

    Session createTLSSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", this.smtpHost); // SMTP主机名
        props.put("mail.smtp.port", "25"); // 主机端口号
        props.put("mail.smtp.auth", "true"); // 是否需要用户认证
        props.put("mail.smtp.starttls.enable", "true"); // 启用TLS加密
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SendMail.this.username, SendMail.this.password);
            }
        });
        session.setDebug(this.debug); // 显示调试信息
        return session;
    }

    static Message createTextMessage(Session session, String from, String to, String subject, String body)
            throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject, "UTF-8");
        message.setText(body, "UTF-8");
        return message;
    }

}
