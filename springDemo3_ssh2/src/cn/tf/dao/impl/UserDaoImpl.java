package cn.tf.dao.impl;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import cn.tf.dao.UserDao;
import cn.tf.domain.User;

public class UserDaoImpl extends HibernateDaoSupport implements UserDao{
	
/*	private HibernateTemplate hibernateTemplate;
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	@Override
	public void save(User user) {
		hibernateTemplate.save(user);
	}
	*/
	
	@Override
	public void save(User user) {
		this.getHibernateTemplate().save(user);
	}
}
