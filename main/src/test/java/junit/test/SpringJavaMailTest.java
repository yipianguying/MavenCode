package junit.test;

import javax.mail.internet.MimeMessage;

import com.crowd.funding.utils.DesUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;



public class SpringJavaMailTest {
	public static void main(String[] args) throws Exception {
		// 使用JAVA程序发送邮件
		ApplicationContext application = new ClassPathXmlApplicationContext("spring/spring-*.xml");

		// 邮件发送器，由Spring框架提供的
		JavaMailSenderImpl javaMailSender = (JavaMailSenderImpl) application.getBean("sendMail");

		javaMailSender.setDefaultEncoding("UTF-8");

		MimeMessage mail = javaMailSender.createMimeMessage();// 一封邮件
		MimeMessageHelper helper = new MimeMessageHelper(mail);
		helper.setSubject("告白书"); //邮件标题

		StringBuilder content = new StringBuilder();

		String param = "ILY";
		param = DesUtils.encrypt(param, "abcdefghijklmnopquvwxyz");

		content.append("<a href='http://www.atcrowdfunding.com/test/act.do?p=" + param + "'>激活链接</a>");

		helper.setText(content.toString(), true);

		helper.setFrom("admin@atguigu.com");
		helper.setTo("test@atguigu.com");

		javaMailSender.send(mail);
	}

}
