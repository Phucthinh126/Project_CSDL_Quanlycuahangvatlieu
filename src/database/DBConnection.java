package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static final String SERVER  ="LAPTOP-2O3RKTB4";  // dia chi may chu sql
	private static final String PORT = "1433"; // cong mac dinh sql
	private static final String DATABASE = "QUANLYCUAHANGVATTU"; // ten database
	private static final String USER = "sa"; // ten tai khoan dang nhap
	private static final String PASSWORD = "123456789"; // mat khau
	
	// chuoi ket noi dau vao
	private static final String URL = "jdbc:sqlserver://" + SERVER + ":" + PORT + ";" +
	        "databaseName=" + DATABASE + ";" +
	        "encrypt=false;" +
	        "trustServerCertificate=true;";
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
	
	// test ket noi
	public static void main(String[] args) throws SQLException {
		try(Connection conn = getConnection()){
			if (conn!= null) {
				System.out.println("Ket noi thanh cong");
			}
		}catch (SQLException e) {
			System.out.println("Loi ket noi: " + e.getMessage());
		}
	}
	

}
