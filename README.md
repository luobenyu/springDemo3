# springDemo3
spring基础，包含事务管理、转账案例、spring整合hibernate，struts整合spring等
#事务管理
一组业务操作，要么全部成功，要么全部失败。

ACID：原子性、一致性、隔离型、持久性
隔离问题：脏读、不可重复读、虚读

隔离级别：读未提交、读已提交、可重复读、串行化

    jdbc事务操作：
    try{
    	//获得连接
    	conn=DriverManage...
    	//开启事务
    	conn.setAutoCommit(false);
	savepoint=conn.setSavepoint();
		
	conn.commit();
    
    }catch(){
    if(savepoint==null){
    	//回滚
    	conn.rollback();

	}else{
		conn.rollback(savepoint);
	conn.commit();
	}

    }finally{
    	//释放
    	conn.close();
    }


##Spring的事务
spring是基于aop进行事务管理的，导入jar包：spring-tx...

PlatformTransactionManager:平台事务管理器，spring的基本操作都必须izai事务管理器的平台上进行操作。
TransactionStatus:事务状态，用于记录事务状态，方便在事务管理器平台上，进行事务操作。             
TransactionDefinition:事务定义，事物详情说明，

##核心组件详解
###平台事务管理器
JDBC、Hibernate、Mybatis、JPA都具有自己的事务管理器。

导入jar包：spring-jdbc...  spring-orm...

事务管理器：
DataSourceTransactionManager ， JDBC事务管理器
HibernateTransactionManager ， hibernate 事务管理器

通过事务管理器去解析事务详情去获得事务状态，通过状态管理事务。

###事务状态
是否是新的事务、是否有保存点、设置了回滚、是否回滚、是否已经完成、刷新状态同步

###事务详情：
名称、是否只读、获得超时时间、隔离级别、传播行为

传播行为：

PROPAGATION_REQUIRED, required ： 默认
	支持当前事务，A如果已经在事务中，B将直接使用A中事务。
	如果不存在创建新的，A不在事务中，B将创建新的。

PROPAGATION_SUPPORTS supports ：
	支持当前事务，A如果已经在事务中，B将直接使用A中事务。
	使用非事务执行，A不在事务中，B将也不使用事务执行。

PROPAGATION_MANDATORY mandatory ：
	支持当前事务，A如果已经在事务中，B将直接使用A中事务。
	如果没有事务将抛出异常，A不在事务中，B将抛异常。

PROPAGATION_REQUIRES_NEW ，requires new ：
	创建新的。如果A没有，B将创建新的。
	挂起之前的。如果A有事务，B将挂起A的事务，创建新的。

PROPAGATION_NOT_SUPPORTED ， not supported：
	B将以非事务执行，如果A已经在事务中，将A事务挂起。

PROPAGATION_NEVER ，never
	B将以非事务执行，如果A已经在事务中，B将抛异常。

PROPAGATION_NESTED nested ：
	将采用嵌套事务执行。底层使用保存点Savepoint


采用配置的方式去设置“事务详情”，spring通过事务管理器去管理事务。


##案例：转账
###半自动事务
使用工厂Bean生成代理、
设置事务管理代码、设置事务管理器
、设置接口、设置目标、设置事务属性（事务详情）




配置文件：

    <!-- 事务管理器 -->
	<bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="DataSource"  ref="dataSource"></property>
	</bean>
	
	<!-- 代理对象,用于生产事务的代理对象 -->
	<bean id="accountServiceProxy"  class="org.springframework.transaction.interceptor.TransactionProxyFactoryBean">
		<property name="transactionManager"  ref="txManager"></property>
		<!-- 接口 -->
		<property name="proxyInterfaces" value="cn.tf.service.AccountService"></property>
		<!-- 目标类 -->
		<property name="target" ref="accountService"></property>
		<!-- 事务属性 -->
		<property name="transactionAttributes">
			<props>
				<prop  key="transfer">PROPAGATION_REQUIRED</prop>
			</props>
		</property>
	</bean>


###使用AOP

    <!-- 事务管理器 -->
	<bean  id="txManager"  class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource"  ref="dataSource"></property>
	</bean>
	
	<!-- 配置事务详情 -->
	<tx:advice id="txAdvice" transaction-manager="txManager">
		<tx:attributes>
			<!-- <tx:method> 确定事务详情配置
					name : 确定方法名称
						transfer 确定名称
						add* 	add开头
						*	任意
					propagation 传播行为
					isolation 隔离级别
					read-only="false" 是否只读
					rollback-for="" 指定异常回滚（-）
					no-rollback-for="" 指定异常提交（+）
				经典应用：开发中规定
					<tx:method name="add*"/>
					<tx:method name="update*"/>
					<tx:method name="delete*"/>
					<tx:method name="find*" read-only="true"/> -->
			<tx:method name="transfer" propagation="REQUIRED" isolation="DEFAULT" read-only="false"/>
		</tx:attributes>
	</tx:advice>
	
	<!-- aop，将通知应用 目标类 -->
	<aop:config>
		<aop:pointcut expression="execution(* cn.tf.service..*.*(..))" id="myPointcut"/>
		<aop:advisor advice-ref="txAdvice" pointcut-ref="myPointcut"/>
	</aop:config>
	



###基于注解
添加注解：@Transactional，可以修饰在类或者方法上

在xml中配置事务管理器，并交由spring。

	<!-- 事务管理器 -->
	<bean  id="txManager"  class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource"  ref="dataSource"></property>
	</bean>
	<!-- 使用注解 -->
	<tx:annotation-driven  transaction-manager="txManager"/>

#web开发
导入jar包，spring-web...

在web.xml中配置：

	<!-- 通过 servletContext 初始化参数设置xml位置 -->
	<context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>classpath:applicationContext.xml</param-value>
	</context-param>

	<!-- spring 监听器 -->
	<listener>
		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener>

在servlet的post方法中：

    //spring 加容器存在 ServletContext中, sc.setAttribute(name, object)
		ServletContext sc = this.getServletContext();
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
		UserService userService = (UserService) ac.getBean("userService");


在applicationContext.xml中

    <bean id="userService"  class="cn.tf.service.UserService"></bean>

#SSH整合

jar包

配置文件：
applicationContext.xml

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans 
       					   http://www.springframework.org/schema/beans/spring-beans.xsd
       					   http://www.springframework.org/schema/context 
       					   http://www.springframework.org/schema/context/spring-context.xsd
       					   http://www.springframework.org/schema/aop 
       					   http://www.springframework.org/schema/aop/spring-aop.xsd
       					   http://www.springframework.org/schema/tx 
       					   http://www.springframework.org/schema/tx/spring-tx.xsd">

    </beans>

hibernate.cfg.xml
    
    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE hibernate-configuration PUBLIC
	"-//Hibernate/Hibernate Configuration DTD 3.0//EN"
	"http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
    <hibernate-configuration>
    <session-factory>
	<property name="hibernate.connection.driver_class">
		com.mysql.jdbc.Driver
	</property>
	<property name="hibernate.connection.url">
		jdbc:mysql://localhost:3306/dbone?useUnicode=true&amp;characterEncoding=UTF-8
	</property>
	<property name="hibernate.connection.username">zp</property>
	<property name="hibernate.connection.password">a</property>
	<property name="hibernate.dialect">
		org.hibernate.dialect.MySQL5Dialect
	</property>

	<!-- 3 sql -->
	<property name="hibernate.show_sql">true</property>
	<property name="hibernate.format_sql">true</property>

	<!-- 4 语句ddl -->
	<property name="hibernate.hbm2ddl.auto">update</property>

	<!-- 5 取消bean校验 -->
	<property name="javax.persistence.validation.mode">none</property>

	<!-- 6 绑定session -->
	<property name="hibernate.current_session_context_class">
		thread
	</property>


	<!-- 添加映射 -->
	<mapping resource="cn/tf/domain/User.hbm.xml" />


    </session-factory>
    </hibernate-configuration>


User.hbm.xml

    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE hibernate-mapping PUBLIC 
    "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
    "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
    <hibernate-mapping>
	<class name="cn.tf.domain.User" table="t_user">
		<id name="id">
			<generator class="native"></generator>
		</id>
		<property name="username"></property>
		<property name="password"></property>
	</class>
    </hibernate-mapping>

struts.xml

    <?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE struts PUBLIC
	"-//Apache Software Foundation//DTD Struts Configuration 2.3//EN"
	"http://struts.apache.org/dtds/struts-2.3.dtd">
    <struts>
	<!-- 开发模式 -->
    <constant name="struts.devMode" value="true" />
    <!-- struts标签模板 -->
    <constant name="struts.ui.theme" value="simple"></constant>

    <package name="default" namespace="/" extends="struts-default">

    </package>
    </struts>
    


web.xml

    <?xml version="1.0" encoding="UTF-8"?>
    <web-app version="3.0" 
	xmlns="http://java.sun.com/xml/ns/javaee" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xsi:schemaLocation="http://java.sun.com/xml/ns/javaee 
	http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd">
	<!-- 确定xml文件位置 -->
	<context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>classpath:applicationContext.xml</param-value>
	</context-param>
	<!-- spring监听器，加载xml文件 -->
	<listener>
		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener>

	<!-- struts 前端控制器 -->
	<filter>
        <filter-name>struts2</filter-name>
        <filter-class>org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter</filter-class>
    </filter>

    <filter-mapping>
        <filter-name>struts2</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
    </web-app>


##spring整合hibernate
有cfg.xml

使用Hibernate模板
    
    <bean  id="userService"  class="cn.tf.service.impl.UserServiceImpl">
		<property name="userDao" ref="userDao"></property>
	</bean>
	<bean id="userDao"  class="cn.tf.dao.impl.UserDaoImpl">
		<property name="hibernateTemplate"  ref="hibernateTemplate"></property>
	</bean>
	
	<bean id="hibernateTemplate"  class="org.springframework.orm.hibernate3.HibernateTemplate">
		<property name="sessionFactory"  ref="sessionFactory"></property>
	</bean>
	
	session工厂 ,特殊的bean生成SessionFactory加载配置文件
	<bean id="sessionFactory"  class="org.springframework.orm.hibernate3.LocalSessionFactoryBean">
		<property name="configLocations"  value="classpath:hibernate.cfg.xml"></property>
	</bean>

	事务管理
	<bean id="txManager"  class="org.springframework.orm.hibernate3.HibernateTransactionManager">
			<property name="sessionFactory"  ref="sessionFactory"></property>
	</bean>
	
	 事务详情 
	<tx:advice  id="txAdvice"  transaction-manager="txManager">
		<tx:attributes>
			<tx:method name="add*"/>
			<tx:method name="update*"/>
			<tx:method name="delete*"/>
			<tx:method name="find*"  read-only="true"/>
		</tx:attributes>
	</tx:advice>
	
	<aop:config>
		<aop:advisor advice-ref="txAdvice" pointcut="execution(* cn.tf.service..*.*(..))"/>
	</aop:config>



无cfg.xml
    
    <!-- 1 service层 -->
	<bean id="userService" class="cn.tf.service.impl.UserServiceImpl">
		<property name="userDao" ref="userDao"></property>
	</bean>
	<!-- 2 dao 层 -->
	<bean id="userDao" class="cn.tf.dao.impl.UserDaoImpl">
		<property name="sessionFactory" ref="sessionFactory"></property>
	</bean>
	
	<!-- 3.1 加载properties配置文件 -->
	<context:property-placeholder location="classpath:jdbcInfo.properties"/>
	<!-- 3.2 数据源（c3p0）-->
	<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
		<property name="driverClass" value="${jdbc.driverClass}"></property>
		<property name="jdbcUrl" value="${jdbc.jdbcUrl}"></property>
		<property name="user" value="${jdbc.user}"></property>
		<property name="password" value="${jdbc.password}"></property>
	</bean>
	
	<!-- 4 session工厂 
		* LocalSessionFactoryBean 特殊bean生成 SessionFactory
	-->
	<bean id="sessionFactory" class="org.springframework.orm.hibernate3.LocalSessionFactoryBean">

		<!-- 4.1 配置数据源 -->
		<property name="dataSource" ref="dataSource"></property>
		<!-- 4.2 hibernate 特有设置 -->
		<property name="hibernateProperties">
			<props>
				<prop key="hibernate.dialect">org.hibernate.dialect.MySQL5Dialect</prop>
				<prop key="hibernate.show_sql">true</prop>
				<prop key="hibernate.format_sql">true</prop>
				<prop key="hibernate.hbm2ddl.auto">update</prop>
				<prop key="javax.persistence.validation.mode">none</prop>
			</props>
		</property>
		<property name="mappingDirectoryLocations" value="classpath:cn/tf/*"></property>
	</bean>
	
	<!-- 5 事务管理 -->
	<bean id="transactionManager" class="org.springframework.orm.hibernate3.HibernateTransactionManager">
		<property name="sessionFactory" ref="sessionFactory"></property>
	</bean>
	
	<!-- 5.2 事务详情 -->
	<tx:advice id="txAdvice" transaction-manager="transactionManager">
		<tx:attributes>
			<tx:method name="add*"/>
			<tx:method name="update*"/>
			<tx:method name="delete*"/>
			<tx:method name="find*" read-only="true"/>
		</tx:attributes>
	</tx:advice>
	
	<!-- 5.3 aop 事务通知 应用 切入点 -->
	<aop:config>
		<aop:advisor advice-ref="txAdvice" pointcut="execution(* cn.tf.service..*.*(..))"/>
	</aop:config>



##struts整合spring
在applicationContext.xml中配置：


	<bean id="userAction"  class="cn.tf.action.UserAction" scope="prototype">
		<property name="userService" ref="userService"></property>
	</bean>

在struts.xml中

    <package name="default" namespace="/" extends="struts-default">
		<action name="userAction_*" class="userAction" method="{1}">
			<result name="add">/success.jsp</result>
		</action>	
    </package>


##注解开发
配置扫描注解

	<bean id="hibernateTemplate"  class="org.springframework.orm.hibernate3.HibernateTemplate">
		<property name="sessionFactory"  ref="sessionFactory"></property>
	</bean>

	<context:component-scan base-package="cn.tf"></context:component-scan>
	

配置事务管理器

    <!-- 5 事务管理 -->
	<bean id="transactionManager" class="org.springframework.orm.hibernate3.HibernateTransactionManager">
		<property name="sessionFactory" ref="sessionFactory"></property>
	</bean>
	<!-- 5.2 注释事务 -->
	<tx:annotation-driven transaction-manager="transactionManager"/>


struts的配置中：

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
