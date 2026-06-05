package main;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import dao.ChiTietDonHangDAO;
import dao.DonHangDAO;
import dao.KhachHangDAO;
import dto.ChiTietDonHangDTO;
import dto.DonHangDTO;
import dto.KhachHangDTO;

public class TestKhachHang {
	public static void main(String[] args) {
		KhachHangDAO khDAO = new KhachHangDAO();
		DonHangDAO dhDao = new DonHangDAO();
		ChiTietDonHangDAO ctDhDao = new ChiTietDonHangDAO();

		// Lay tat ca danh sach khach hang
//		List<KhachHangDTO> listAll = khDAO.getAll();
//		for (KhachHangDTO e : listAll) {
//			System.out.println(e);
//		}

		// lay thong tin khach hang qua id
		System.out.println("thong tin khach hang");
		System.out.println(khDAO.getById("KH007"));

		// them khach hang moi
		KhachHangDTO khNew = new KhachHangDTO("KH201", "Nguyen Van Moi", "0999999997", "TP Ho Chi Minh");
		KhachHangDTO khNew1 = new KhachHangDTO("KH202", "Nguyen Van Moi Moi", "0919999997", "TP Ho Chi Minh");

		khDAO.insert(khNew);
		khDAO.insert(khNew1);
		System.out.println("\nLay thong tin khach hang moi them: ");
		System.out.println(khDAO.getById("KH201"));
		System.out.println(khDAO.getById("KH202"));

		System.out.println("\nCap nhat thong tin khach hang - them dia chi: ");
		KhachHangDTO khNew2 = new KhachHangDTO("KH201", "Nguyen Van Moi", "0999999997", "TP Ho Chi Minh, Thu Duc");
		khDAO.update(khNew2);
		System.out.println(khDAO.getById("KH201"));

		System.out.println("\nXoa Khach hang ma 'KH202' ");
		khDAO.delete("KH202");
		System.out.println("Thong tin khach hang co ma 'KH202': " + khDAO.getById("KH202"));

		System.out.println("\nTim kiem khach hang theo ten");
		List<KhachHangDTO> searchByName = khDAO.searchByName("Ton Thanh Long");
		for (KhachHangDTO e : searchByName) {
			System.out.println(e);
		}

		System.out.println("\nTim kiem khach hang theo sdt");
		khDAO.findBySdt("0999999997");

//		// Tao don hang
		DonHangDTO dh1 = new DonHangDTO("DH201", "KH201", LocalDateTime.now(), new BigDecimal(0));//
		// them chi tiet cho don hang
		ChiTietDonHangDTO ctDh1 = new ChiTietDonHangDTO("DH201", "VL0081", 5, new BigDecimal(0)); // gia ban la 0 tu
																									// dong cap nhat
		ChiTietDonHangDTO ctDh2 = new ChiTietDonHangDTO("DH201", "VL0055", 10, new BigDecimal(0));
		System.out.println("\nThem don hang moi va chi tiet don hang cho khach hang ma KH201");
//		dhDao.insert(dh1); // them don hang
//		ctDhDao.insert(ctDh1); // them chi tiet
//		ctDhDao.insert(ctDh2); // them chi tiet

		System.out.println("\nXem danh sach don hang cua khach hang co ma 'KH201'");
		List<DonHangDTO> listDh = dhDao.getOrdersByCustomer("KH201");
		for (DonHangDTO e : listDh) {
			System.out.println(e);
		}
		System.out.println("\nXem danh sach chi tiet don hang cua khach hang co ma don hang 'DH201'");
		List<ChiTietDonHangDTO> listCtDh = dhDao.getOrderDetails("DH201");
		for (ChiTietDonHangDTO e : listCtDh) {
			System.out.println(e);
		}

	}

}
