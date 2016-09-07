package cn.tf.service.impl;

import cn.tf.dao.UserDao;
import cn.tf.domain.User;
import cn.tf.service.UserService;

public class UserServiceImpl implements UserService{
	private UserDao userDao;
	
	
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}



	@Override
	public void addUser(User user) {
		this.userDao.save(user);
		
	}

}
