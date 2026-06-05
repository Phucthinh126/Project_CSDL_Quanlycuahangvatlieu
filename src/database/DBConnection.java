package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static final String SERVER = "LAPTOP-2O3RKTB4";
	private static final String PORT = "1433";
	private static final String DATABASE = "QuanLyVatLieu";
	private static final String USER = "sa";
	private static final String PASSWORD = "123456789";

	private static final String URL = "jdbc:sqlserver://" + SERVER + ":" + PORT + ";" + "databaseName=" + DATABASE + ";"
			+ "encrypt=false;" + "trustServerCertificate=true;";

	private static Connection connection = null;

	static {
		try {
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError("Không thể kết nối DB: " + e.getMessage());
		}
	}

	public static Connection getConnection() {
		return connection; // Không throw exception nữa
	}

	// test ket noi
	public static void main(String[] args) throws SQLException {
		try (Connection conn = getConnection()) {
			if (conn != null) {
				System.out.println("Ket noi thanh cong");
				System.out.println("Database: " + connection.getCatalog());
			}
		} catch (SQLException e) {
			System.out.println("Loi ket noi: " + e.getMessage());
		}
	}

}
