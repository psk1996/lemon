完成用户登陆功能
用户请求登录页面，并在页面上输入用户名及密码
点击登录按钮后，服务端进行处理，并响应登陆结果

实现步骤：
1：在webapps/myweb/中添加三个页面：
login。html：登录页面
该页面中的form表单指定的路径：action = “login”
并且表单中包含两个输入框：用户名和密码

login_success.hrml：登录成功提示页面
显示一行字：登陆成功，欢迎回来

login_fail.html:登录失败提示页面
显示一行字：登录失败，用户名或密码不正确

2:在com.webserver.servelet包中添加用于处理登录业务的类：LoginServlet

3：修改ClientHandler，在第二个环节处理请求中再判断是否为注册请求后添加一个分支，
判断是否为请求登录。若是请求登录。则实例化LoginServlet并调用其service方法处理登录。
否则实行后续操作判断是否为请求一个静态资源


LoginServlet的实现：
1：首先通过request获取用户在登录页面上输入的用户名及密码

2：通过RandomAccessFile读取user.dat文件，对比每个注册用户，当用户输入的用户名与该文件中某个注册用户信息一致时，响应用户登录成功页面。
若用户名一致但密码不一致或者该用户名在user.dat文件中不存在时都响应用户登录失败页面










