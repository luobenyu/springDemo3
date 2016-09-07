package cn.tf.dao.impl;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import cn.tf.dao.AccountDao;

public class AccountDaoImpl extends  JdbcDaoSupport implements AccountDao{

	@Override
	public void in(String inUser, String money) {
		this.getJdbcTemplate().update("update t_account set money=money+? where username=?",money,inUser);
		
	}

	@Override
	public void out(String outUser, String money) {
		this.getJdbcTemplate().update("update t_account set money=money-? where username=?",money,outUser);
		
	}
	
	

}
