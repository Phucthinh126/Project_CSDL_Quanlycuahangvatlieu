package main;

import java.util.List;

import dao.KhachHangDAO;
import dto.KhachHangDTO;

public class MainTest {
	public static void main(String[] args) {
		KhachHangDAO khDAO = new KhachHangDAO();
		List<KhachHangDTO> listAll = khDAO.getAll();
		for(KhachHangDTO e: listAll) {
		    System.out.println(e);  
		}
		
		
	}

}
