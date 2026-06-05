package main;

import dao.VatLieuDAO;

public class TestLoadFileCSV_VatLieu {
	public static void main(String[] args) {
		VatLieuDAO vl = new VatLieuDAO();
		// tai vat lieu vao database
		String filePath = "D:\\10. SQL\\Project_QuanLyCuaHangVatTu\\QuanLyCuuHangVatTu\\src\\csv\\vatlieumoi.csv";

		// load csv vao database
//		List<String> logs = CSVLoader.loadVatLieuFromCSV(filePath);
//		System.out.println("\n=== KẾT QUẢ LOG CHẠY ĐƯỢC ===");
//		for (String log : logs) {
//			System.out.println(log);
//		}

	}

}
