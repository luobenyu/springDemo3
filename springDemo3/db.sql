CREATE TABLE t_account(
	id  INT PRIMARY KEY AUTO_INCREMENT,
	username VARCHAR(50),
	money NUMERIC(10,2)
)

INSERT INTO t_account(username,money) VALUES('张三','1980.2');
INSERT INTO t_account(username,money) VALUES('李四','13678.27');

SELECT * FROM t_account;