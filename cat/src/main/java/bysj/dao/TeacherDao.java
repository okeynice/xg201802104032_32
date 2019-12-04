package bysj.dao;

import bysj.domain.*;
import service.UserService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public final class TeacherDao {
	private static TeacherDao teacherDao = new TeacherDao();

	private TeacherDao() {
	}

	public static TeacherDao getInstance() {
		return teacherDao;
	}

	public Collection<Teacher> findAll() throws SQLException {
		Set<Teacher> teachers = new HashSet<Teacher>();
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		//执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
		ResultSet resultSet = statement.executeQuery("select * from teacher");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()) {
			Department department = DepartmentDao.getInstance().find(resultSet.getInt("department_id"));
			ProfTitle profTitle = ProfTitleDao.getInstance().find(resultSet.getInt("title_id"));
			Degree degree = DegreeDao.getInstance().find(resultSet.getInt("degree_id"));
			//创建Degree对象，根据遍历结果中的id,description,no,remarks值
			Teacher teacher = new Teacher(resultSet.getString("no"), resultSet.getInt("id"), resultSet.getString("name"), profTitle, degree, department);
			//向degrees集合中添加Degree对象
			teachers.add(teacher);
		}
		return teachers;
	}

	public boolean add(Teacher teacher) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Boolean affected = null;
		try {
			//获取数据库连接对象
			connection = JdbcHelper.getConn();
			connection.setAutoCommit(false);
			//添加预编译语句
			preparedStatement = connection.prepareStatement(
					"INSERT INTO teacher (name,title_id, degree_id, department_id,no) VALUES (?,?,?,?,?)");
			preparedStatement.setString(1, teacher.getName());
			preparedStatement.setInt(2, teacher.getTitle().getId());
			preparedStatement.setInt(3, teacher.getDegree().getId());
			preparedStatement.setInt(4, teacher.getDepartment().getId());
			preparedStatement.setString(5, teacher.getNo());

			//执行预编译对象的executeUpdate()方法，获得删除行数
			int affectedRowNum = preparedStatement.executeUpdate();
			System.out.println("添加了" + affectedRowNum + "条记录");
			//执行预编译语句，用其返回值、影响的行为数为赋值affectedRowNum
			PreparedStatement getNewTeacherId = connection.prepareStatement("select id from teacher where no=?");
			//将teacher的no字段赋值给查询条件
			getNewTeacherId.setString(1, teacher.getNo());
			ResultSet resultSet = getNewTeacherId.executeQuery();
			connection.commit();
			if (resultSet.next()) {
				Teacher userTeacher = TeacherDao.getInstance().find(resultSet.getInt("id"));
				User userToadd = new User(teacher.getNo(),teacher.getNo()	, userTeacher);
				UserService.getInstance().userToAdd(userToadd);
			}

			affected = affectedRowNum > 0;
			return affected;
		}catch (SQLException e){
			if(connection != null){
				e.printStackTrace();
				connection.rollback();
			}
		}finally {
			if(connection != null){
				connection.setAutoCommit(true);
			}
			JdbcHelper.close(preparedStatement,connection);
		}
		return affected;
	}


	public boolean delete(Integer id) throws SQLException{
		//获得连接对象
		Connection connection = null;;
		PreparedStatement pstmt = null;
		int affectedRowNum=0;
		try{
			connection = JdbcHelper.getConn();
			connection.setAutoCommit(false);
			pstmt= connection.prepareStatement("SELECT * FROM user where teacher_id = ?");
			pstmt.setInt(1,id);
			ResultSet resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				UserService.getInstance().delete(resultSet.getInt("id"));
			}
			PreparedStatement pstmt1 = connection.prepareStatement("DELETE FROM teacher WHERE ID =?");
			pstmt1.setInt(1,id);
			affectedRowNum = pstmt1.executeUpdate();
			connection.commit();
			return affectedRowNum > 0;
		}catch (SQLException e){
			e.printStackTrace();
			if(connection != null){
				connection.rollback();
			}
		}finally {
			if(connection != null){
				connection.setAutoCommit(true);
			}
			JdbcHelper.close(pstmt,connection);
		}
		return affectedRowNum>0;
	}
	public Teacher find(Integer id) throws SQLException {
		Set<Teacher> teachers = new HashSet<Teacher>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		//执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
		ResultSet resultSet = statement.executeQuery("select * from teacher");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			Degree degree = DegreeDao.getInstance().find(resultSet.getInt("degree_id"));
			Department department = DepartmentDao.getInstance().find(resultSet.getInt("department_id"));
			ProfTitle profTitle = ProfTitleDao.getInstance().find(resultSet.getInt("title_id"));
			//创建Degree对象，根据遍历结果中的id,description,no,remarks值
			Teacher teacher = new Teacher(resultSet.getString("no"),resultSet.getInt("id"),resultSet.getString("name"),profTitle,degree,department);
			//向degrees集合中添加Degree对象
			teachers.add(teacher);
		}
		//关闭资源
		JdbcHelper.close(resultSet,statement,connection);
		Department department1 = null;
		Teacher desiredTeacher = null;
		for (Teacher teacher : teachers) {
			if(id.equals(teacher.getId())){
				desiredTeacher =  teacher;
				break;
			}
		}
		return desiredTeacher;
	}

	public Teacher findToNo(String no) throws SQLException {
		Set<Teacher> teachers = new HashSet<Teacher>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		//执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
		ResultSet resultSet = statement.executeQuery("select * from teacher");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			Degree degree = DegreeDao.getInstance().find(resultSet.getInt("degree_id"));
			Department department = DepartmentDao.getInstance().find(resultSet.getInt("department_id"));
			ProfTitle profTitle = ProfTitleDao.getInstance().find(resultSet.getInt("title_id"));
			//创建Degree对象，根据遍历结果中的id,description,no,remarks值
			Teacher teacher = new Teacher(resultSet.getString("no"),resultSet.getInt("id"),resultSet.getString("name"),profTitle,degree,department);
			//向degrees集合中添加Degree对象
			teachers.add(teacher);
		}
		//关闭资源
		JdbcHelper.close(resultSet,statement,connection);
		Teacher desiredTeacher = null;
		for (Teacher teacher : teachers) {
			if(no.equals(teacher.getNo())){
				desiredTeacher =  teacher;
				break;
			}
		}
		return desiredTeacher;
	}

	public boolean update(Teacher teacher) throws SQLException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句，“？”作为占位符
		String addSchool_sql = "update teacher set name=? where id=?";
		//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
		PreparedStatement pstmt = connection.prepareStatement(addSchool_sql);
		//为预编译的语句参数赋值
		pstmt.setString(1,teacher.getName());
		System.out.println();
		pstmt.setInt(2,teacher.getId());
		//执行预编译对象的executeUpdate()方法，获取增加记录的行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("修改了 "+affectedRowNum+" 条");
		return affectedRowNum > 0;
	}
}
