package bysj.basic.user;

import bysj.domain.User;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import service.UserService;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/user.ctl")
public class UserController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //读取参数id
        String id_str = request.getParameter("id");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学院对象，否则响应id指定的学院对象
            if (id_str == null && username == null) {
                responseUsers(response);
            } else if(id_str != null){
                int id = Integer.parseInt(id_str);
                responseUser(id, response);
            }else if(password == null){
                responseUserToTeacher(username,response);
            }else if(username !=null && password !=null && id_str ==null){
                responseLogin(username,password,response);
            }
        }catch (SQLException e){
            e.printStackTrace();
            message.put("message", "数据库操作异常");
            //响应message到前端
            response.getWriter().println(message);
        }catch(Exception e){
            message.put("message", "网络异常");
            //响应message到前端
            response.getWriter().println(message);
        }
    }


    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置请求字符编码为UTF-8
        request.setCharacterEncoding("UTF-8");
        String user_json = JSONUtil.getJSON(request);
        //将JSON字串解析为School对象
        User userToUpdate = JSON.parseObject(user_json, User.class);

        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表修改School对象对应的记录
        try {
            UserService.getInstance().update(userToUpdate);
            message.put("message", "修改成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
        }catch(Exception e){
            message.put("message", "网络异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);

        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();

        //到数据库表中删除对应的学院
        try {
            UserService.getInstance().delete(id);
            message.put("message", "删除成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
        }catch(Exception e){
            message.put("message", "网络异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置请求字符编码为UTF-8
        request.setCharacterEncoding("UTF-8");
        //根据request对象，获得代表参数的JSON字串
        String user_json = JSONUtil.getJSON(request);

        //将JSON字串解析为School对象
        User userToAdd = JSON.parseObject(user_json, User.class);
        //前台没有为id赋值，此处模拟自动生成id。如果Dao能真正完成数据库操作，删除下一行。

        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //在数据库表中增加School对象
        try {
            UserService.getInstance().userToAdd(userToAdd);
            message.put("message", "增加成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
        }catch(Exception e){
            message.put("message", "网络异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    private void responseUser(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找学院
        User user = UserService.getInstance().find(id);
        String user_json = JSON.toJSONString(user);

        //响应school_json到前端
        response.getWriter().println(user_json);
    }

    private void responseLogin(String username, String password, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找学院
        User userToUsernameAndPassword = UserService.getInstance().findByPasswordAndUsername(password,username);
        String user_json = JSON.toJSONString(userToUsernameAndPassword);

        //响应school_json到前端
        response.getWriter().println(user_json);
    }


    private void responseUserToTeacher(String username, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        User users = UserService.getInstance().findByUsername(username);
        //根据school的id查找所属的系
        String user_json = JSON.toJSONString(users, SerializerFeature.DisableCircularReferenceDetect);
        //响应

        //加入数据信息
        message.put("statusCode", "200");
        message.put("message", "查找成功");
        message.put("data", user_json);

        //响应message到前端
        response.getWriter().println(user_json);
    }

    //响应所有学院对象
    private void responseUsers(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有学院
        Collection<User> users = UserService.getInstance().findAll();
        String user_json = JSON.toJSONString(users, SerializerFeature.DisableCircularReferenceDetect);

        //响应schools_json到前端
        response.getWriter().println(user_json);
    }
}
