package cn.tf.dao;

public interface AccountDao {
	//收款
	public void in(String inUser,String money);
	//汇款
	public void out(String outUser,String money);
	

}
