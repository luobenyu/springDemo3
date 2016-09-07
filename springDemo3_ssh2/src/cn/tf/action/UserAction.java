package cn.tf.action;

import cn.tf.domain.User;
import cn.tf.service.UserService;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class UserAction extends ActionSupport  implements ModelDriven<User>{

	private User user=new User();
	private UserService userService;
	
	@Override
	public User getModel() {
		return user;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public String add(){
		this.userService.addUser(user);
		return "add";
	}
	

}
