package kr.co.nanwe.cmmn.util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Class Name 		: MailUtil
 * @Description 	: 메일발송관련 클래스
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Component("mailUtil")
public class MailUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MailUtil.class);

	@Resource(name = "mailProp")
	private Properties mailProp;
	
	public boolean sendMail(String toMail, String title, String content) {
		
		//메일 서버
		String mailServer = mailProp.getProperty("mail.server");
		String userName = mailProp.getProperty("mail.username");
		String password = mailProp.getProperty("mail.password");
		String host = mailProp.getProperty("mail.host");
		String port = mailProp.getProperty("mail.port");
		String writer = mailProp.getProperty("mail.writer");
		
		title = "["+writer+"]" + title;
		
		boolean result = false;

		// SMTP 서버 정보를 설정한다. 
		Properties prop = new Properties();
		
		prop.put("mail.smtp.host", host);
		prop.put("mail.smtp.port", port);
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
		
		prop.put("mail.debug", "true");
		
		if(StringUtil.isEqual(mailServer, "gmail")) {
			prop.put("mail.transport.protocol", "smtp");
		} else if(StringUtil.isEqual(mailServer, "naver")) {
			prop.put("mail.smtp.ssl.trust", "*");
			prop.put("mail.smtp.ssl.checkserveridentity", "true");
			prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		} else if(StringUtil.isEqual(mailServer, "daum")) {
			prop.put("mail.smtp.ssl.trust", "*");
			prop.put("mail.smtp.ssl.checkserveridentity", "true");
			prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		} else {
			prop.put("mail.smtp.ssl.enable", "true");
			prop.put("mail.smtp.ssl.trust", host);
		}
		
		Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		});
		
		try {
						
			MimeMessage message = new MimeMessage(session); 
			message.setFrom(new InternetAddress(userName, writer, "UTF-8"));
			
			//수신자메일주소
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(toMail));
			
			// Subject
			message.setSubject(MimeUtility.encodeText(title, "UTF-8", "B"));
			
			// CONTENT
			message.setContent(content, "text/html; charset=UTF-8");
			
			// send the message
			Transport.send(message);
			
			result = true;
			
		} catch (MessagingException | UnsupportedEncodingException e) {
			LOGGER.debug(e.getMessage());
			result = false;
		}
		
		return result;
	}
}
