package utils;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;

import java.io.File;
import java.net.URL;

public class EmailUtils {
    public static void sendMsg() throws Exception {
        // HtmlEmail mail = new HtmlEmail();
        ImageHtmlEmail mail = new ImageHtmlEmail();

        // load your HTML email template
        String htmlEmailTemplate = ".... <img src=\"http://www.apache.org/images/feather.gif\"> ....";

        // define you base URL to resolve relative resource locations
        URL url = new URL("http://www.apache.org");

        mail.setDataSourceResolver(new DataSourceUrlResolver(url));

        String hostname = PropertiesUtils.getString("mail.host");
        String password = PropertiesUtils.getString("mail.password");
        String username = PropertiesUtils.getString("mail.username");
        String[] toList = PropertiesUtils.getStringArray("mail.touser");
        String subject = PropertiesUtils.getString("mail.title");
        mail.setHostName(hostname); // 邮件服务器域名
        mail.setAuthentication(username, password); // 邮箱账户
        mail.setCharset("UTF-8"); // 邮件的字符集
        mail.setFrom(username); // 发件人地址
        for (String to : toList) {
            mail.addTo(to); // 收件人地址，可以设置多个
        }

        mail.setSubject(subject); // 邮件主题
        mail.setHtmlMsg(htmlEmailTemplate); // 邮件正文
        String rString = mail.send(); // 发送邮件
        System.out.println(rString);
    }

    public static void sendEmailsWithAttachments(String title, String context, String... filepath)
            throws EmailException {

        String hostname = PropertiesUtils.getString("mail.host");
        String password = PropertiesUtils.getString("mail.password");
        String username = PropertiesUtils.getString("mail.username");
        String[] toList = PropertiesUtils.getStringArray("mail.touser");

        // Create the email message
        HtmlEmail email = new HtmlEmail();
        email.setHostName(hostname); // 邮件服务器域名
        email.setAuthentication(username, password); // 邮箱账户
        email.setCharset("UTF-8"); // 邮件的字符集

        // mail.setSSLOnConnect(true); // 是否启用SSL
        // mail.setSslSmtpPort(sslSmtpPort); // 若启用，设置smtp协议的SSL端口号
        email.setSubject(title);
        email.setFrom(username); // 发件人地址
        email.setHtmlMsg(context);

        for (String to : toList) {
            email.addTo(to); // 收件人地址，可以设置多个
        }
        // add the attachment
        if(filepath != null){
            for (String fp : filepath) {
                EmailAttachment attachment = new EmailAttachment();
                attachment.setPath(fp);
                attachment.setDisposition(EmailAttachment.ATTACHMENT);
                attachment.setDescription("测试结果");
                File f = new File(fp);
                attachment.setName(f.getName());
                email.attach(attachment);
            }
        }
        // send the email
        email.send();
    }

    public static void main(String[] args) {
        try {
            //sendMsg();
            String path= "d:\\result.xlsx";
            sendEmailsWithAttachments("测试结果","请查收",path);
            sendEmailsWithAttachments("测试结果--无附件","请查收");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
