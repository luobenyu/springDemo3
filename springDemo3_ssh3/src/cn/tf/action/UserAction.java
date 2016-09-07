package cn.tf.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.struts2.convention.annotation.Result;

import cn.tf.domain.User;
import cn.tf.service.UserService;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/")  
@ParentPackage("struts-default")  

public class UserAction extends ActionSupport  implements ModelDriven<User>{

	private User user=new User();
	@Autowired
	private UserService userService;
	
	@Override
	public User getModel() {
		return user;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@Action(value="userAction_add" ,results=@Result(name="add",location="/success.jsp"))
	public String add(){
		this.userService.addUser(user);
		return "add";
	}
	
}
