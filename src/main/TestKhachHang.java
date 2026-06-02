package main;

import java.util.List;

import dao.KhachHangDAO;
import dto.KhachHangDTO;

public class TestKhachHang {
	public static void main(String[] args) {
		KhachHangDAO khDAO = new KhachHangDAO();

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
		System.out.println("Thong tin khach hang: " + khDAO.getById("KH202"));

		System.out.println("\nTim kiem khach hang theo ten");
		List<KhachHangDTO> searchByName = khDAO.searchByName("Ton Thanh Long");
		for (KhachHangDTO e : searchByName) {
			System.out.println(e);
		}

		System.out.println("\nTim kiem khach hang theo sdt");
		khDAO.findBySdt("0999999997");

	}

}
