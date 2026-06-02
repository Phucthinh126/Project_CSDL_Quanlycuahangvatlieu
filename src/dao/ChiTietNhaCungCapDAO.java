package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DBConnection;
import dto.ChiTietNhaCungCapDTO;

public class ChiTietNhaCungCapDAO {
	private Connection con = DBConnection.getConnection();

	/**
	 * lay danh sach tat ca cac lan nhap hang
	 */
	public List<ChiTietNhaCungCapDTO> getAll() {
		List<ChiTietNhaCungCapDTO> list = new ArrayList<>();
		String sql = "SELECT MANHACC, MAVATLIEU, SOLUONG, NGAYCUNGCAP FROM CHITIET_CUNGCAP "
				+ "ORDER BY NGAYCUNGCAP DESC";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				/*
				 * rs.getString("MANHACC"), rs.getString("MAVATLIEU"), rs.getInt("SOLUONG"),
				 * rs.getTimestamp("NGAYCUNGCAP"
				 */
				ChiTietNhaCungCapDTO ctcc = new ChiTietNhaCungCapDTO(rs.getString("MANHACC"), rs.getString("MAVATLIEU"),
						rs.getInt("SOLUONG"), rs.getTimestamp("NGAYCUNGCAP"));
				list.add(ctcc);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi getAll Supply: " + e.getMessage());
		}
		return list;
	}

	/**
	 * Lấy danh sách nhập hàng của một nhà cung cấp
	 * 
	 * @param maNhaCC - Mã nhà cung cấp
	 * @return List<ChiTietCungCapDTO> - Danh sách nhập hàng của nhà cung cấp đó
	 */
	public List<ChiTietNhaCungCapDTO> getSupplyBySupplier(String maNhaCC) {
		List<ChiTietNhaCungCapDTO> list = new ArrayList<>();
		String sql = "SELECT MANHACC, MAVATLIEU, SOLUONG, NGAYCUNGCAP FROM CHITIET_CUNGCAP "
				+ "WHERE MANHACC=? ORDER BY NGAYCUNGCAP DESC";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maNhaCC);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ChiTietNhaCungCapDTO ctcc = new ChiTietNhaCungCapDTO(rs.getString("MANHACC"), rs.getString("MAVATLIEU"),
						rs.getInt("SOLUONG"), rs.getTimestamp("NGAYCUNGCAP"));
				list.add(ctcc);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi getSupplyBySupplier: " + e.getMessage());
		}
		return list;
	}

	/**
	 * Lấy danh sách nhập hàng của một vật liệu
	 * 
	 * @param maVatLieu - Mã vật liệu
	 * @return List<ChiTietCungCapDTO> - Danh sách nhập hàng của vật liệu đó
	 */
	public List<ChiTietNhaCungCapDTO> getSupplyByMaterial(String maVatLieu) {
		List<ChiTietNhaCungCapDTO> list = new ArrayList<>();
		String sql = "SELECT MANHACC, MAVATLIEU, SOLUONG, NGAYCUNGCAP FROM CHITIET_CUNGCAP "
				+ "WHERE MAVATLIEU=? ORDER BY NGAYCUNGCAP DESC";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maVatLieu);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ChiTietNhaCungCapDTO ctcc = new ChiTietNhaCungCapDTO(rs.getString("MANHACC"), rs.getString("MAVATLIEU"),
						rs.getInt("SOLUONG"), rs.getTimestamp("NGAYCUNGCAP"));
				list.add(ctcc);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi getSupplyByMaterial: " + e.getMessage());
		}
		return list;
	}

	/**
	 * Lấy danh sách nhập hàng trong một khoảng thời gian
	 * 
	 * @param ngayBatDau  - Ngày bắt đầu
	 * @param ngayKetThuc - Ngày kết thúc
	 * @return List<ChiTietCungCapDTO> - Danh sách nhập hàng
	 */
	public List<ChiTietNhaCungCapDTO> getSupplyByDateRange(String ngayBatDau, String ngayKetThuc) {
		List<ChiTietNhaCungCapDTO> list = new ArrayList<>();
		String sql = "SELECT MANHACC, MAVATLIEU, SOLUONG, NGAYCUNGCAP FROM CHITIET_CUNGCAP "
				+ "WHERE NGAYCUNGCAP >= CAST(? AS DATE) " + "  AND NGAYCUNGCAP < CAST(? AS DATE) + 1 "
				+ "ORDER BY NGAYCUNGCAP DESC";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, ngayBatDau);
			ps.setString(2, ngayKetThuc);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ChiTietNhaCungCapDTO ctcc = new ChiTietNhaCungCapDTO(rs.getString("MANHACC"), rs.getString("MAVATLIEU"),
						rs.getInt("SOLUONG"), rs.getTimestamp("NGAYCUNGCAP"));
				list.add(ctcc);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi getSupplyByDateRange: " + e.getMessage());
		}
		return list;
	}
}
