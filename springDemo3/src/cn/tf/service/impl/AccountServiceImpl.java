package cn.tf.service.impl;

import cn.tf.dao.AccountDao;
import cn.tf.service.AccountService;

public class AccountServiceImpl implements AccountService{

	private AccountDao accountDao;
	
	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}


	@Override
	public void transfer(String outUser, String inUser, String money) {
		accountDao.out(outUser, money);
		accountDao.in(inUser, money);
		
	}

}
