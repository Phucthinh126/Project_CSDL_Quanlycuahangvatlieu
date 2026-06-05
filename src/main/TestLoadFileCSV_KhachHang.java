package main;

import java.util.List;

import database.CSVLoader;

public class TestLoadFileCSV_KhachHang {
	public static void main(String[] args) {

		String filePath = "D:\\10. SQL\\Project_QuanLyCuaHangVatTu\\QuanLyCuuHangVatTu\\src\\csv\\KhachHangMoi.csv";
		List<String> logs = CSVLoader.loadKhachHangFromCSV(filePath);
		System.out.println("\n=== KẾT QUẢ LOG CHẠY ĐƯỢC ===");
		for (String log : logs) {
			System.out.println(log);
		}
	}
}