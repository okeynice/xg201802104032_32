package security;

import bysj.domain.User;
import com.alibaba.fastjson.JSONObject;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login.ctl")
public class LoginController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        //创建json对象message，以便向往前端响应信息
        JSONObject message =new JSONObject();
        try{
            User loggedUser = UserService.getInstance().findByPasswordAndUsername(password,username);
            if (loggedUser!=null){
                message.put("message","登陆成功");
                HttpSession session = req.getSession();
                //10分钟没有操作，则使session失效
                session.setMaxInactiveInterval(10 * 60);
                session.setAttribute("currentUser" , loggedUser);
                resp.getWriter().println(message);
                //此处应重定向到索引页（菜单页）
                return;
            }else {
                message.put("message","用户名或密码错误");
            }
        }catch (SQLException e){
            message.put("message","数据库操作异常");
            e.printStackTrace();
        }catch (Exception e){
            message.put("message","网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        resp.getWriter().println(message);
    }
}
