package cn.tf.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.tf.domain.User;
import cn.tf.service.UserService;

public class TestApp {
	
	
	public static void main(String[] args) {
		User user=new User();
		user.setUsername("李四");
		user.setPassword("aaa");
		
		String xmlpath="applicationContext.xml";
		
		ApplicationContext applicationContext=new ClassPathXmlApplicationContext(xmlpath);
		UserService userService=(UserService) applicationContext.getBean("userService");
		userService.addUser(user);
		
	}

}
