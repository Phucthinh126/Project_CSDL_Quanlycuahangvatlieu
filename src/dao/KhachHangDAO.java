package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DBConnection;
import dto.KhachHangDTO;

public class KhachHangDAO {
	private Connection con = DBConnection.getConnection();
	
	// chuc nang cơ ban
	
	/*
	 * Lay dạnh sach tat ca khach hang
	 */
	
	   public List<KhachHangDTO> getAll() {
	        List<KhachHangDTO> list = new ArrayList<>();
	        String sql = "SELECT MAKHACHHANG, TENKHACHHANG, SDT, DIACHI FROM KHACHHANG ORDER BY TENKHACHHANG";

	        try (PreparedStatement ps = con.prepareStatement(sql)) {
	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {
	                KhachHangDTO kh = new KhachHangDTO(
	                        rs.getString("MAKHACHHANG"),
	                        rs.getString("TENKHACHHANG"),
	                        rs.getString("SDT"),
	                        rs.getString("DIACHI"));
	                list.add(kh);
	            }
	        } catch (SQLException e) {
	            System.out.println("Lỗi getAll KhachHang: " + e.getMessage());
	        }
	        return list;
	    }

	
	

}
