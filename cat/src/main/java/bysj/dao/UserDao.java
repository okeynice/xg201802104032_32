package bysj.dao;

import bysj.domain.Teacher;
import bysj.domain.User;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public final class UserDao {
    private static UserDao userDao = new UserDao();
    private UserDao(){}
    public static UserDao getInstance(){
        return userDao;
    }

    public Collection<User> findAll() throws SQLException{
        Set<User> users = new HashSet<User>();
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        //执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
        ResultSet resultSet = statement.executeQuery("select * from user");
        //若结果集仍然有下一条记录，则执行循环体
        while (resultSet.next()){
            Teacher teacher = TeacherDao.getInstance().find(resultSet.getInt("Teacher"));
            //创建Degree对象，根据遍历结果中的id,description,no,remarks值
            User user = new User(resultSet.getInt("id"),resultSet.getString("username"),resultSet.getString("password"),teacher);
            //向degrees集合中添加Degree对象
            users.add(user);
        }
        return users;
    }
    public User find(Integer id) throws SQLException {
        Set<User> users = new HashSet<User>();
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        //执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
        ResultSet resultSet = statement.executeQuery("select * from user");
        //若结果集仍然有下一条记录，则执行循环体
        while (resultSet.next()){
            Teacher teacher = TeacherDao.getInstance().find(resultSet.getInt("Teacher"));
            User user = new User(resultSet.getInt("id"),resultSet.getString("username"),resultSet.getString("password"),teacher);
            //向degrees集合中添加Degree对象
            users.add(user);
        }
        //关闭资源
        JdbcHelper.close(resultSet,statement,connection);
        User desiredUser = null;
        for (User user : users) {
            if(id.equals(user.getId())){
                desiredUser =  user;
                break;
            }
        }
        return desiredUser;
    }

    public boolean userToAdd(User user) throws SQLException {
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String add = "INSERT INTO user(username,password,Teacher) VALUES" + " (?,?,?)";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(add);
        //为预编译的语句参数赋值
        pstmt.setString(1,user.getUsername());
        pstmt.setString(2,user.getPassword());
        pstmt.setInt(3,user.getTeacher().getId());
        //执行预编译对象的executeUpdate()方法，获取增加记录的行数
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("增加了 "+affectedRowNum+" 条");
        return affectedRowNum > 0;
    }

    public boolean delete(User user) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String delete = "DELETE FROM user WHERE ID =?";
        PreparedStatement pstmt = connection.prepareStatement(delete);
        pstmt.setInt(1,user.getId());
        int delete1 = pstmt.executeUpdate();
        return delete1>0;
    }

    public User findByUsernameAndPassword(String password,String username) throws SQLException {
        Set<User> users = new HashSet<User>();
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        //执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
        ResultSet resultSet = statement.executeQuery("select * from user");
        //若结果集仍然有下一条记录，则执行循环体
        while (resultSet.next()){
            Teacher teacher = TeacherDao.getInstance().find(resultSet.getInt("Teacher"));
            User user = new User(resultSet.getInt("id"),resultSet.getString("username"),resultSet.getString("password"),teacher);
            //向degrees集合中添加Degree对象
            users.add(user);
        }
        //关闭资源
        JdbcHelper.close(resultSet,statement,connection);
        User user = null;
        User desiredUser = userDao.findByUsername(username);
        if(desiredUser.getPassword().equals(password)){
            user = desiredUser;
        }
        return user;
    }


    /**public User findByUsernameAndPassword(String password,String username) throws SQLException {
     Set<User> users = new HashSet<User>();
     //获得连接对象
     Connection connection = JdbcHelper.getConn();
     PreparedStatement pstmt = connection.createStatement();
     //执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
     ResultSet resultSet = statement.executeQuery("select * from user where username=? and password=?");
     pstmt.setString(1,user.getUsername());
     pstmt.setString(2,user.getPassword());
     //若结果集仍然有下一条记录，则执行循环体
     while (resultSet.next()){
     Teacher teacher = TeacherDao.getInstance().find(resultSet.getInt("teacher_id"));
     User user = new User(resultSet.getInt("id"),resultSet.getString("username"),resultSet.getString("password"),teacher);
     //向degrees集合中添加Degree对象
     users.add(user);
     }
     //关闭资源
     JdbcHelper.close(resultSet,statement,connection);
     User user = null;
     User desiredUser = userDao.findByUsername(username);
     if(desiredUser.getPassword().equals(password)){
     user = desiredUser;
     }
     return user;
     }**/

    public User findByUsername(String username) throws SQLException {
        Set<User> users = new HashSet<User>();
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        //执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
        ResultSet resultSet = statement.executeQuery("select * from user");
        //若结果集仍然有下一条记录，则执行循环体
        while (resultSet.next()){
            Teacher teacher = TeacherDao.getInstance().find(resultSet.getInt("Teacher"));
            User user = new User(resultSet.getInt("id"),resultSet.getString("username"),resultSet.getString("password"),teacher);
            //向degrees集合中添加Degree对象
            users.add(user);
        }
        //关闭资源
        JdbcHelper.close(resultSet,statement,connection);
        User desiredUser = null;
        for (User user : users) {
            if(username.equals(user.getUsername())){
                desiredUser =  user;
                break;
            }
        }
        return desiredUser;
    }


    public boolean update(User user) throws SQLException {
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String password = "update user set password=? where id=?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(password);
        //为预编译的语句参数赋值
        pstmt.setString(1,user.getPassword());
        System.out.println();
        pstmt.setInt(2,user.getId());
        //执行预编译对象的executeUpdate()方法，获取增加记录的行数
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("修改了 "+affectedRowNum+" 条");
        return affectedRowNum > 0;
    }
}
