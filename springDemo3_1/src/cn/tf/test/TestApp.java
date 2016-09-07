package cn.tf.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.tf.service.AccountService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class TestApp {
	
	@Autowired
	@Qualifier("accountServiceProxy")
	private AccountService accountService;
	
	
	@Test
	public void test1(){
		accountService.transfer("张三", "李四", "100");
	}

}
