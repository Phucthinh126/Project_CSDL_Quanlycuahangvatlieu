package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DBConnection;
import dto.ChiTietDonHangDTO;

public class ChiTietDonHangDAO {
	private Connection con = DBConnection.getConnection();

	/**
	 * them chi tiet don hang
	 */
	public boolean insert(ChiTietDonHangDTO ctdh) {
		String sql = "INSERT INTO ChiTietDonHang (MaDH, MaVL, SoLuong, DonGia) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, ctdh.getMaDH());
			ps.setString(2, ctdh.getMaVL());
			ps.setInt(3, ctdh.getSoLuong());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Lỗi insert OrderDetail: " + e.getMessage());
			return false;
		}
	}

	/**
	 * cap nhat cua mot chi tiet don hang
	 */
	public boolean update(ChiTietDonHangDTO ctdh) {
		String sql = "UPDATE ChiTietDonHang SET SoLuong = ? WHERE MaDH = ? AND MaVL = ?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, ctdh.getSoLuong());
			ps.setString(2, ctdh.getMaDH());
			ps.setString(3, ctdh.getMaVL());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Lỗi updateOrderDetail: " + e.getMessage());
			return false;
		}
	}

	/**
	 * xoa chi tiet don hang
	 */
	public boolean delete(String maDonHang, String maVatLieu) {
		String sql = "DELETE FROM ChiTietDonHang WHERE MaDH = ? AND MaVL = ?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maDonHang);
			ps.setString(2, maVatLieu);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Lỗi deleteOrderDetail: " + e.getMessage());
			return false;
		}
	}

	/*
	 * Kiem tra mot mat hang da ton tai trong hon hang hay chua
	 */
	public boolean checkExisted(String maDonHang, String maVatLieu) {
		// Chỉ SELECT 1 để kiểm tra sự tồn tại, cực kỳ nhẹ và nhanh
		String sql = "SELECT 1 FROM ChiTietDonHang WHERE MaDH = ? AND MaVL = ?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maDonHang);
			ps.setString(2, maVatLieu);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next(); // Nếu có dữ liệu trả về true, ngược lại false
			}
		} catch (SQLException e) {
			System.out.println("Lỗi checkExisted: " + e.getMessage());
			return false;
		}
	}

}
