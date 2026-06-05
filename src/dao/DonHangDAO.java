package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import database.DBConnection;
import dto.ChiTietDonHangDTO;
import dto.DonHangDTO;

public class DonHangDAO {
	private Connection con = DBConnection.getConnection();

	/**
	 * Thêm một đơn hàng mới vào cơ sở dữ liệu
	 */
	public boolean insert(DonHangDTO dh) {
		String sql = "INSERT INTO DonHang (MaDH, MaKH, NgayLap, TongTien) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, dh.getMaDH());
			ps.setString(2, dh.getMaKH());

			// Nếu NgayLap trong DTO bị null, ghi nhận thời gian hiện tại của hệ thống
			if (dh.getNgayLap() != null) {
				ps.setObject(3, dh.getNgayLap());
			} else {
				ps.setObject(3, LocalDateTime.now());
			}

			ps.setBigDecimal(4, dh.getTongTien());

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Lỗi insert DonHang: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Cập nhật thông tin đơn hàng (Thay đổi khách hàng hoặc tổng tiền)
	 */
	public boolean update(DonHangDTO dh) {
		String sql = "UPDATE DonHang SET MaKH = ?, NgayLap = ?, TongTien = ? WHERE MaDH = ?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, dh.getMaKH());
			ps.setObject(2, dh.getNgayLap());
			ps.setBigDecimal(3, dh.getTongTien());
			ps.setString(4, dh.getMaDH());

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Lỗi update DonHang: " + e.getMessage());
			return false;
		}
	}

	/*
	 * Lay toan bo danh sach don hang
	 */
	public List<DonHangDTO> getAll() {
		List<DonHangDTO> list = new ArrayList<>();
		String sql = "SELECT MaDH, MaKH, NgayLap, TongTien FROM DonHang ORDER BY NgayLap DESC";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				LocalDateTime ngayLap = rs.getObject("NgayLap", LocalDateTime.class);

				DonHangDTO dh = new DonHangDTO(rs.getString("MaDH"), rs.getString("MaKH"), ngayLap,
						rs.getBigDecimal("TongTien"));
				list.add(dh);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi getAll DonHang: " + e.getMessage());
		}
		return list;
	}

	/*
	 * Danh sach chi tiet 1 don hang
	 */

	public List<ChiTietDonHangDTO> getOrderDetails(String maDonHang) {
		List<ChiTietDonHangDTO> list = new ArrayList<>();
		String sql = "SELECT MaDH, MaVL, SoLuong, DonGia FROM ChiTietDonHang WHERE MaDH = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maDonHang);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ChiTietDonHangDTO ctdh = new ChiTietDonHangDTO(rs.getString("MaDH"), rs.getString("MaVL"),
							rs.getInt("SoLuong"), rs.getBigDecimal("DonGia"));
					list.add(ctdh);
				}
			}
		} catch (SQLException e) {
			System.out.println("Lỗi getOrderDetails: " + e.getMessage());
		}
		return list;
	}

	/*
	 * Lay danh sach don hang cua mot khach hang
	 * 
	 */
	public List<DonHangDTO> getOrdersByCustomer(String maKhachHang) {
		List<DonHangDTO> list = new ArrayList<>();
		String sql = "SELECT MaDH, MaKH, NgayLap, TongTien FROM DonHang WHERE MaKH = ? ORDER BY NgayLap DESC";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maKhachHang);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				LocalDateTime ngayLap = rs.getObject("NgayLap", LocalDateTime.class);

				DonHangDTO dh = new DonHangDTO(rs.getString("MaDH"), rs.getString("MaKH"), ngayLap,
						rs.getBigDecimal("TongTien"));
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
		String sql = "SELECT MAX(MaDH) as max_ma FROM DonHang";

		try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				String maxMa = rs.getString("max_ma");
				if (maxMa == null || maxMa.trim().isEmpty()) {
					return "DH0001";
				}
				maxMa = maxMa.trim();
				String phanSo = maxMa.substring(2); // Cắt bỏ chữ "DH"
				int nextNumber = Integer.parseInt(phanSo) + 1;
				return String.format("DH%04d", nextNumber);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi sinh mã DonHang: " + e.getMessage());
		} catch (NumberFormatException e) {
			System.out.println("Lỗi định dạng số khi sinh mã đơn hàng: " + e.getMessage());
		}
		return "DH0000";
	}

}
