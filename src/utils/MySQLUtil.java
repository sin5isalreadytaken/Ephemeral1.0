package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLUtil {
	
	private Connection getConnection(){
		String url = "jdbc:mysql://localhost:3306/ephemeral";
		String username = "root";
		String password = "sin5";
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	private void releaseResources(Connection connection, PreparedStatement ps, ResultSet rs){
		if (null != rs){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (null != ps){
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (null != connection){
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * 数据库中插入初始数据
	 */
	public static void main(String[] arg0){
		MySQLUtil mySQLUtil = new MySQLUtil();
		Connection connection = null;
		PreparedStatement ps = null;
		connection = mySQLUtil.getConnection();
		String[] sqls = {
				"insert into joke(joke_content) values('再想想吧...')"
		};
		try {
			for (String sql : sqls){
				ps = connection.prepareStatement(sql);
				ps.execute();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("success");
	}
	
	//获取问答知识表中的所有记录
	public static List<Knowledge> findAllKnowledge(){
		List<Knowledge> knowledgeList = new ArrayList<Knowledge>();
		String sql = "select * from knowledge";
		MySQLUtil mySQLUtil = new MySQLUtil();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		connection = mySQLUtil.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()){
				Knowledge knowledge = new Knowledge();
				knowledge.setId(rs.getInt("id"));
				knowledge.setQuestion(rs.getString("question"));
				knowledge.setAnswer(rs.getString("answer"));
				knowledge.setCategory(rs.getInt("category"));
				knowledgeList.add(knowledge);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			mySQLUtil.releaseResources(connection, ps, rs);
		}
		return knowledgeList;
	}
	
	//获取上一次的聊天类别
	public static int getLastCategory(String openId){
		int chatCategory = -1;
		String sql = "select chat_category from chat_log where open_id=? order by id desc limit 0,1";
		
		MySQLUtil mySQLUtil = new MySQLUtil();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		connection = mySQLUtil.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, openId);
			rs = ps.executeQuery();
			if (rs.next()){
				chatCategory = rs.getInt("chat_category");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			mySQLUtil.releaseResources(connection, ps, rs);
		}
		return chatCategory;
	}
	
	//根据知识id随机获取一个答案
	public static String getKnowledgeSub(int knowledgeId){
		String knowledgeAnswer = "";
		String sql = "select answer from knowledge_sub where pid=? order by rand() limit 0,1";
		
		MySQLUtil mySQLUtil = new MySQLUtil();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		connection = mySQLUtil.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, knowledgeId);
			rs = ps.executeQuery();
			if (rs.next()){
				knowledgeAnswer = rs.getString("answer");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			mySQLUtil.releaseResources(connection, ps, rs);
		}
		return knowledgeAnswer;
	}
	
	//随机获取一条笑话
	public static String getJoke(){
		String jokeContent = "";
		String sql = "select joke_content from joke order by rand() limit 0,1";
		
		MySQLUtil mySQLUtil = new MySQLUtil();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		connection = mySQLUtil.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()){
				jokeContent = rs.getString("joke_content");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			mySQLUtil.releaseResources(connection, ps, rs);
		}
		return jokeContent;
	}
	
	//保存聊天记录
	public static void saveChatLog(String openId, String createTime, String reqMsg, String respMsg, int chatCategory){
		String sql = "insert into chat_log(open_id,create_time,req_msg,resp_msg,chat_category) values(?,?,?,?,?)";
		
		MySQLUtil mySQLUtil = new MySQLUtil();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		connection = mySQLUtil.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, openId);
			ps.setString(2, createTime);
			ps.setString(3, reqMsg);
			ps.setString(4, respMsg);
			ps.setInt(5, chatCategory);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			mySQLUtil.releaseResources(connection, ps, rs);
		}
	}
	
}
