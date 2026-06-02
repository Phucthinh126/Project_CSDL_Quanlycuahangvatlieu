package main;

import java.util.List;

import dao.DonHangDAO;
import dao.KhachHangDAO;
import dto.ChiTietDonHangDTO;
import dto.DonHangDTO;

public class TestVatLieu {
	public static void main(String[] args) {
		KhachHangDAO khDAO = new KhachHangDAO();
		DonHangDAO dhDAO = new DonHangDAO();

		// Lay tat ca danh sach khach hang
//		List<KhachHangDTO> listAll = khDAO.getAll();
//		for (KhachHangDTO e : listAll) {
//			System.out.println(e);
//		}

		// lay thong tin khach hang qua id
//		System.out.println("thong tin khach hang");
//		System.out.println(khDAO.getById("KH007"));

		//

//------------------------------Test chuc nang cua DonHang---------------------
		// lay thong tin tat ca don hang
//		List<DonHangDTO> listAllDH = dhDAO.getAll();
//		for (DonHangDTO e : listAllDH) {
//			System.out.println(e);
//		}

		// Danh sach chi tiet don hang thong qua ma don hang
		List<ChiTietDonHangDTO> getOrderDetails = dhDAO.getOrderDetails("DH150");
		for (ChiTietDonHangDTO e : getOrderDetails) {
			System.out.println(e);
		}

		// Doanh thu trong mot khoan thoi gian
		System.out
				.println("Doanh thu tu thang 4-5 nam 2026: " + dhDAO.getRevenueByDateRange("2026-04-01", "2026-04-01"));
		// Danh sach don hang cua khach hang
		System.out.println("Danh sach don hang cua khach hang");
		List<DonHangDTO> getOrdersByCustomer = dhDAO.getOrdersByCustomer("KH007");
		for (DonHangDTO e : getOrdersByCustomer) {
			System.out.println(e);
		}

	}

}
