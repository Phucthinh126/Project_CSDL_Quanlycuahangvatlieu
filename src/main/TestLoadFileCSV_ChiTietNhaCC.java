package main;

import java.util.List;

import database.CSVLoader;

public class TestLoadFileCSV_ChiTietNhaCC {
	public static void main(String[] args) {
		String filePath = "D:\\10. SQL\\Project_QuanLyCuaHangVatTu\\QuanLyCuuHangVatTu\\src\\csv\\ChiTietCungCapMoi.csv";
		List<String> logs = CSVLoader.loadChiTietNhaCungCapFromCSV(filePath);
//		System.out.println("\n=== KẾT QUẢ LOG CHẠY ĐƯỢC ===");
//		for (String log : logs) {
//			System.out.println(log);
//		}
	}

}
