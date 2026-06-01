package main;

import java.util.List;

import dao.KhachHangDAO;
import dto.KhachHangDTO;

public class MainTest {
	public static void main(String[] args) {
		KhachHangDAO khDAO = new KhachHangDAO();
		
		// Lay tat ca danh sach khach hang
		List<KhachHangDTO> listAll = khDAO.getAll();
//		for(KhachHangDTO e: listAll) {
//		    System.out.println(e);  
//		}
//		
		
		// lay thong tin khach hang qua id
		System.out.println(khDAO.getById("KH007"));
		
	}

}
