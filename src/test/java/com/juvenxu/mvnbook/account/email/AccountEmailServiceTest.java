package com.juvenxu.mvnbook.account.email;

import static org.junit.Assert.assertEquals;

import javax.mail.Message;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import com.juvenxu.mvnbook.account.email.AccountEmailService;

public class AccountEmailServiceTest {
	private GreenMail greenMail;
	@Before
	public void startMailServer() throws Exception{
		greenMail = new GreenMail(ServerSetup.SMTP);
		greenMail.setUser("test@juvenxu.com", "123456");
		greenMail.start();
	}
	@Test
	public void testSendMail() throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("account-email.xml");
		AccountEmailService accountEmailService = (AccountEmailService) context.getBean("accountEmailService");
		String subject="Test Subject";
		String htmlText = "<h3>Test</h3>";
		accountEmailService.sendMail("test@juvenxu.com", subject, htmlText);
		greenMail.waitForIncomingEmail(2000,1);
		Message[] msgs = greenMail.getReceivedMessages();
		assertEquals(1, msgs.length);
		assertEquals(subject, msgs[0].getSubject());
		assertEquals(htmlText, GreenMailUtil.getBody(msgs[0]).trim());
	}
	@After
	public void stopMailServer() {
		greenMail.stop();
	}
}
