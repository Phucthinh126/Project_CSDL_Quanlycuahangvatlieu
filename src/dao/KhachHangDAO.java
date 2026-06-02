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
	 * Lay danh sach tat ca khach hang trong bang khachHangDTO
	 */

	public List<KhachHangDTO> getAll() {
		List<KhachHangDTO> list = new ArrayList<>();
		String sql = "SELECT MAKHACHHANG, TENKHACHHANG, SDT, DIACHI FROM KHACHHANG ORDER BY TENKHACHHANG";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				KhachHangDTO kh = new KhachHangDTO(rs.getString("MAKHACHHANG"), rs.getString("TENKHACHHANG"),
						rs.getString("SDT"), rs.getString("DIACHI"));
				list.add(kh);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi getAll KhachHang: " + e.getMessage());
		}
		return list;
	}

	/*
	 * Lay thong tin khach hang qua ma khach hang
	 * 
	 */

	public KhachHangDTO getById(String maKhachHang) {
		String sql = "SELECT MAKHACHHANG, TENKHACHHANG, SDT, DIACHI FROM KHACHHANG WHERE MAKHACHHANG=?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maKhachHang);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new KhachHangDTO(rs.getString("MAKHACHHANG"), rs.getString("TENKHACHHANG"), rs.getString("SDT"),
						rs.getString("DIACHI"));
			}
		} catch (SQLException e) {
			System.out.println("Lỗi getById KhachHang: " + e.getMessage());
		}
		return null;
	}

	/*
	 * Them khach hang moi
	 * 
	 */
	public boolean insert(KhachHangDTO kh) {
		String sql = "INSERT INTO KHACHHANG(MAKHACHHANG, TENKHACHHANG, SDT, DIACHI) VALUES (?,?,?,?)";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, kh.getMaKhachHang());
			ps.setString(2, kh.getTenKhachHang());
			ps.setString(3, kh.getSdt());
			ps.setString(4, kh.getDiaChi());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Lỗi insert KhachHang: " + e.getMessage());
			return false;
		}
	}

	/**
	 * cap nhat thong tin khach hang
	 * 
	 */
	public boolean update(KhachHangDTO kh) {
		String sql = "UPDATE KHACHHANG SET TENKHACHHANG=?, SDT=?, DIACHI=? WHERE MAKHACHHANG=?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, kh.getTenKhachHang());
			ps.setString(2, kh.getSdt());
			ps.setString(3, kh.getDiaChi());
			ps.setString(4, kh.getMaKhachHang());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Lỗi update KhachHang: " + e.getMessage());
			return false;
		}
	}

	/**
	 * xoa khach hang
	 */
	public boolean delete(String maKhachHang) {
		String sql = "DELETE FROM KHACHHANG WHERE MAKHACHHANG=?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maKhachHang);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Lỗi delete KhachHang: " + e.getMessage());
			return false;
		}
	}

	/**
	 * tim kiem theo ten
	 * 
	 */
	public List<KhachHangDTO> searchByName(String tenKhachHang) {
		List<KhachHangDTO> list = new ArrayList<>();
		String sql = "SELECT MAKHACHHANG, TENKHACHHANG, SDT, DIACHI FROM KHACHHANG " + "WHERE TENKHACHHANG LIKE ? "
				+ "ORDER BY TENKHACHHANG";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, "%" + tenKhachHang + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				KhachHangDTO kh = new KhachHangDTO(rs.getString("MAKHACHHANG"), rs.getString("TENKHACHHANG"),
						rs.getString("SDT"), rs.getString("DIACHI"));
				list.add(kh);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi searchByName KhachHang: " + e.getMessage());
		}
		return list;
	}

	/**
	 * tim kiem khach hang theo so dien thoai
	 * 
	 */
	public KhachHangDTO findBySdt(String sdt) {
		if (sdt == null)
			return null;
		sdt = sdt.trim();

		System.out.println("Tìm kiếm khách hàng với SDT: '" + sdt + "'");

		String sql = "SELECT MAKHACHHANG, TENKHACHHANG, SDT, DIACHI FROM KHACHHANG WHERE TRIM(SDT)=?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, sdt);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				System.out.println("Tìm thấy khách hàng: " + rs.getString("TENKHACHHANG"));
				return new KhachHangDTO(rs.getString("MAKHACHHANG"), rs.getString("TENKHACHHANG"), rs.getString("SDT"),
						rs.getString("DIACHI"));
			} else {
				System.out.println("--> Không tìm thấy khách hàng nào khớp với SDT trên trong Database.");
			}
		} catch (SQLException e) {
			System.out.println("Lỗi findBySdt KhachHang: " + e.getMessage());
		}
		return null;
	}

	/**
	 * sinh ma kh tu dong
	 * 
	 */
	public String generateMaKhachHang() {
		String sql = "SELECT MAX(MAKHACHHANG) as max_ma FROM KHACHHANG";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String maxMa = rs.getString("max_ma");
				if (maxMa == null || maxMa.trim().isEmpty()) {
					return "KH001";
				}

				String sohientai = maxMa.substring(2).trim();
				int nextNumber = Integer.parseInt(sohientai) + 1;

				return String.format("KH%04d", nextNumber);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi sinh mã KhachHang: " + e.getMessage());
		} catch (NumberFormatException e) {
			System.out.println("Lỗi định dạng số khi sinh mã: " + e.getMessage());
		}
		return "KH000";
	}

}
