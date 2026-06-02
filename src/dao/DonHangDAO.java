package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DBConnection;
import dto.ChiTietDonHangDTO;
import dto.DonHangDTO;

public class DonHangDAO {
	private Connection con = DBConnection.getConnection();

	/*
	 * Lay toan bo danh sach don hang
	 */
	public List<DonHangDTO> getAll() {
		List<DonHangDTO> list = new ArrayList<>();
		String sql = "SELECT MADONHANG, MAKHACHHANG, TONGTIEN, NGAYDAT, NGAYGIAO FROM DONHANG ORDER BY NGAYDAT DESC";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				DonHangDTO dh = new DonHangDTO(rs.getString("MADONHANG"), rs.getString("MAKHACHHANG"),
						rs.getDouble("TONGTIEN"), rs.getTimestamp("NGAYDAT"), rs.getTimestamp("NGAYGIAO"));
				list.add(dh);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi getAll DonHang: " + e.getMessage());
		}
		return list;
	}

	/*
	 * Danh sach chi tiet don hang
	 */

	public List<ChiTietDonHangDTO> getOrderDetails(String maDonHang) {
		List<ChiTietDonHangDTO> list = new ArrayList<>();
		String sql = "SELECT MADONHANG, MAVATLIEU, SOLUONG FROM CHITIETDONHANG WHERE MADONHANG=?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maDonHang);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ChiTietDonHangDTO ctdh = new ChiTietDonHangDTO(rs.getString("MADONHANG"), rs.getString("MAVATLIEU"),
						rs.getInt("SOLUONG"));
				list.add(ctdh);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi getOrderDetails: " + e.getMessage());
		}
		return list;
	}

	/*
	 * Tinh doanh thu mot khoang thoi gian
	 */
	public double getRevenueByDateRange(String ngayBatDau, String ngayKetThuc) {
		String sql = "SELECT SUM(TONGTIEN) as TONGTHU FROM DONHANG " + "WHERE NGAYDAT >= CAST(? AS DATE) "
				+ "  AND NGAYDAT < DATEADD(day, 1, CAST(? AS DATE))";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, ngayBatDau);
			ps.setString(2, ngayKetThuc);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getDouble("TONGTHU");
			}
		} catch (SQLException e) {
			System.out.println("Lỗi getRevenueByDateRange: " + e.getMessage());
		}
		return 0;
	}

	/*
	 * Lay danh sach don hang cua mot khach hang
	 * 
	 */
	public List<DonHangDTO> getOrdersByCustomer(String maKhachHang) {
		List<DonHangDTO> list = new ArrayList<>();
		String sql = "SELECT MADONHANG, MAKHACHHANG, TONGTIEN, NGAYDAT, NGAYGIAO FROM DONHANG "
				+ "WHERE MAKHACHHANG=? ORDER BY MAKHACHHANG DESC";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maKhachHang);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				DonHangDTO dh = new DonHangDTO(rs.getString("MADONHANG"), rs.getString("MAKHACHHANG"),
						rs.getDouble("TONGTIEN"), rs.getTimestamp("NGAYDAT"), rs.getTimestamp("NGAYGIAO"));
				list.add(dh);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi getOrdersByCustomer: " + e.getMessage());
		}
		return list;
	}

	/*
	 * Tao ma don hang tu dong
	 */
	public String generateMaDonHang() {
		String sql = "SELECT COUNT(*) as cnt FROM DONHANG";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int count = rs.getInt("cnt") + 1;
				return String.format("DH%03d", count);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi sinh mã DonHang: " + e.getMessage());
		}
		return "DH001";
	}

}
