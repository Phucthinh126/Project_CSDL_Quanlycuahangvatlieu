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
	 * Lay dạnh sach tat ca khach hang trong bang khachHangDTO
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
	 * Cập nhật thông tin khách hàng
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
	 * Xóa khách hàng
	 * 
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
	 * Tìm kiếm khách hàng theo tên
	 * 
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
	 * Kiểm tra khách hàng có tồn tại hay không theo SDT Dùng khi nhân viên muốn
	 * biết khách hàng này đã từng mua hàng chưa
	 * 
	 */
	public KhachHangDTO findBySdt(String sdt) {
		String sql = "SELECT MAKHACHHANG, TENKHACHHANG, SDT, DIACHI FROM KHACHHANG WHERE SDT=?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, sdt);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new KhachHangDTO(rs.getString("MAKHACHHANG"), rs.getString("TENKHACHHANG"), rs.getString("SDT"),
						rs.getString("DIACHI"));
			}
		} catch (SQLException e) {
			System.out.println("Lỗi findBySdt KhachHang: " + e.getMessage());
		}
		return null;
	}

	/**
	 * Sinh mã khách hàng tự động (KH001, KH002, ...)
	 * 
	 */
	public String generateMaKhachHang() {
		String sql = "SELECT COUNT(*) as cnt FROM KHACHHANG";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int count = rs.getInt("cnt") + 1;
				return String.format("KH%03d", count);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi sinh mã KhachHang: " + e.getMessage());
		}
		return "KH001";
	}

}
