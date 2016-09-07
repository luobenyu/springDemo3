package cn.tf.service.impl;

import org.springframework.transaction.annotation.Transactional;

import cn.tf.dao.AccountDao;
import cn.tf.service.AccountService;
@Transactional
public class AccountServiceImpl implements AccountService{

	private AccountDao accountDao;
	
	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}


	@Override
	@Transactional()
	public void transfer(String outUser, String inUser, String money) {
		accountDao.out(outUser, money);
		
		//int i=1/0;
		accountDao.in(inUser, money);
		
	}

}
