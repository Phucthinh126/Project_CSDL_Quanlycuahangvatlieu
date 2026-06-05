package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DBConnection;
import dto.VatLieuDTO;

public class VatLieuDAO {
	private Connection con = DBConnection.getConnection();

	/*
	 * Lay danh sach toan bo vat lieu
	 */
	public List<VatLieuDTO> getAll() {
		List<VatLieuDTO> list = new ArrayList<>();
		String sql = "SELECT MaVL, TenVL, DonViTinh, SoLuongTon, GiaBan " + "FROM VATLIEU " + "ORDER BY MaVL";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				VatLieuDTO vl = new VatLieuDTO(rs.getString("MaVL"), rs.getString("TenVL"), rs.getString("DonViTinh"),
						rs.getInt("SoLuongTon"), rs.getBigDecimal("GiaBan"));
				list.add(vl);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi getAll VatLieu: " + e.getMessage());
		}
		return list;
	}

	/**
	 * Lấy thông tin chi tiết một vật liệu theo mã
	 * 
	 */
	public VatLieuDTO getById(String maVatLieu) {

		String sql = "SELECT MaVL, TenVL, DonViTinh, SoLuongTon, GiaBan " + "FROM VATLIEU WHERE MaVL=?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maVatLieu);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new VatLieuDTO(rs.getString("MaVL"), rs.getString("TenVL"), rs.getString("DonViTinh"),
						rs.getInt("SoLuongTon"), rs.getBigDecimal("GiaBan"));
			}
		} catch (SQLException e) {
			System.out.println("Lỗi getById VatLieu: " + e.getMessage());
		}
		return null;
	}

	/**
	 * Thêm vật liệu mới vào kho
	 */
	public boolean insert(VatLieuDTO vl) {
		String sql = "INSERT INTO VatLieu (MaVL, TenVL, DonViTinh, SoLuongTon, GiaBan) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, vl.getMaVL());
			ps.setString(2, vl.getTenVL());
			ps.setString(3, vl.getDonViTinh());
			ps.setInt(4, vl.getSoLuongTon());
			ps.setBigDecimal(5, vl.getGiaBan());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Lỗi insert VatLieu: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Cập nhật thông tin vật liệu (giá bán, đơn vị, v.v.)
	 * 
	 */
	public boolean update(VatLieuDTO vl) {
		String sql = "UPDATE VatLieu SET TenVL = ?, DonViTinh = ?, SoLuongTon = ?, GiaBan = ? WHERE MaVL = ?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, vl.getTenVL());
			ps.setString(2, vl.getDonViTinh());
			ps.setInt(3, vl.getSoLuongTon());
			ps.setBigDecimal(4, vl.getGiaBan());
			ps.setString(5, vl.getMaVL());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Lỗi update VatLieu: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Xóa vật liệu khỏi kho
	 * 
	 */
	public boolean delete(String maVatLieu) {
		String sql = "DELETE FROM VatLieu WHERE MaVL = ?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maVatLieu);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Lỗi delete VatLieu: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Tìm kiếm vật liệu theo tên
	 * 
	 */
	public List<VatLieuDTO> searchByName(String tenVatLieu) {
		List<VatLieuDTO> list = new ArrayList<>();
		String sql = "SELECT MaVL, TenVL, DonViTinh, SoLuongTon, GiaBan FROM VatLieu WHERE TenVL LIKE ? ORDER BY TenVL";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, "%" + tenVatLieu + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				VatLieuDTO vl = new VatLieuDTO(rs.getString("MaVL"), rs.getString("TenVL"), rs.getString("DonViTinh"),
						rs.getInt("SoLuongTon"), rs.getBigDecimal("GiaBan"));
				list.add(vl);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi searchByName VatLieu: " + e.getMessage());
		}
		return list;
	}

	/**
	 * Lấy danh sách vật liệu có số lượng tồn kho > 0 (con hang)
	 * 
	 */
	public List<VatLieuDTO> getAvailableStock() {
		List<VatLieuDTO> list = new ArrayList<>();
		String sql = "SELECT MaVL, TenVL, DonViTinh, SoLuongTon, GiaBan FROM VatLieu WHERE SoLuongTon > 0 ORDER BY TenVL";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				VatLieuDTO vl = new VatLieuDTO(rs.getString("MaVL"), rs.getString("TenVL"), rs.getString("DonViTinh"),
						rs.getInt("SoLuongTon"), rs.getBigDecimal("GiaBan"));
				list.add(vl);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi getAvailableStock: " + e.getMessage());
		}
		return list;
	}

	/**
	 * sinh ma vat lieu tu dong
	 * 
	 */
	public String generateMaVatLieu() {
		String sql = "SELECT MAX(MaVL) as max_ma FROM VatLieu";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String maxMa = rs.getString("max_ma");

				if (maxMa == null || maxMa.trim().isEmpty()) {
					return "VL0001";
				}
				maxMa = maxMa.trim();

				String phanSo = maxMa.substring(2);

				int nextNumber = Integer.parseInt(phanSo) + 1;

				return String.format("VL%04d", nextNumber);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi sinh mã VatLieu: " + e.getMessage());
		} catch (NumberFormatException e) {
			System.out.println("Lỗi chuyển đổi số khi sinh mã: " + e.getMessage());
		}

		return "VL0001";
	}

	/**
	 * lay so luong ton kho cua vat lieu
	 */
	public int getStockQuantity(String maVatLieu) {
		String sql = "SELECT SoLuongTon FROM VatLieu WHERE MaVL = ?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maVatLieu);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt("SoLuongTon");
			}
		} catch (SQLException e) {
			System.out.println("Lỗi lấy số lượng: " + e.getMessage());
		}
		return -1;
	}
}
