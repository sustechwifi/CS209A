package practice.lab.lab8;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Properties;


public class MailTest {
    public static void main(String[] args) throws MessagingException, IOException, GeneralSecurityException {
        // read properties
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(
                Paths.get("C:\\Users\\25874\\Desktop\\CS209A\\labs\\src\\main\\resources\\mail.properties")
        )) {
            props.load(in);
        }

        // read message info
        List<String> lines = Files.readAllLines(
                Paths.get("C:\\Users\\25874\\Desktop\\CS209A\\labs\\src\\main\\resources\\message.txt")
                , StandardCharsets.UTF_8);

        String from = lines.get(0);
        String to = lines.get(1);
        String subject = lines.get(2);

        StringBuilder builder = new StringBuilder();
        for (int i = 3; i < lines.size(); i++) {
            builder.append(lines.get(i));
            builder.append("\n");
        }

        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.put("mail.smtp.ssl.socketFactory", sf);


        // read password for your email account
//        System.out.println("Password: ");
//        Scanner scanner = new Scanner(System.in);
//        String password = scanner.next();

        Session mailSession = Session.getDefaultInstance(props);
        mailSession.setDebug(true);
        MimeMessage message = new MimeMessage(mailSession);
        System.out.println(from);
        System.out.println(to);
        System.out.println(subject);
        System.out.println(builder);
        // TODO 1: check the MimeMessage API to figure out how to set the sender, receiver, subject, and email body
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(builder.toString());

        // TODO 2: check the Session API to figure out how to connect to the mail server and send the message
        message.saveChanges();
        var ts = mailSession.getTransport();
        ts.connect(
                null,
                props.getProperty("mail.smtps.password"));










































































































































































































//        ts.connect(null, pro);

        ts.sendMessage(message, message.getAllRecipients());

        ts.close();
    }
}