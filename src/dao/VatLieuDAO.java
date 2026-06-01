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
		String sql = "SELECT MAVATLIEU, SOLUONG, TENVATLIEU, GIABAN, GIANHAP, DONVI " + "FROM VATLIEU "
				+ "ORDER BY TENVATLIEU";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				VatLieuDTO vl = new VatLieuDTO(rs.getString("MAVATLIEU"), rs.getInt("SOLUONG"),
						rs.getString("TENVATLIEU"), rs.getDouble("GIABAN"), rs.getDouble("GIANHAP"),
						rs.getString("DONVI"));
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
		String sql = "SELECT MAVATLIEU, SOLUONG, TENVATLIEU, GIABAN, GIANHAP, DONVI "
				+ "FROM VATLIEU WHERE MAVATLIEU = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maVatLieu);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new VatLieuDTO(rs.getString("MAVATLIEU"), rs.getInt("SOLUONG"), rs.getString("TENVATLIEU"),
						rs.getDouble("GIABAN"), rs.getDouble("GIANHAP"), rs.getString("DONVI"));
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
		String sql = "INSERT INTO VATLIEU(MAVATLIEU, SOLUONG, TENVATLIEU, GIABAN, GIANHAP, DONVI) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, vl.getMaVatLieu());
			ps.setInt(2, vl.getSoLuong());
			ps.setString(3, vl.getTenVatLieu());
			ps.setDouble(4, vl.getGiaBan());
			ps.setDouble(5, vl.getGiaNhap());
			ps.setString(6, vl.getDonVi());
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
		String sql = "UPDATE VATLIEU SET SOLUONG=?, TENVATLIEU=?, GIABAN=?, GIANHAP=?, DONVI=? " + "WHERE MAVATLIEU=?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, vl.getSoLuong());
			ps.setString(2, vl.getTenVatLieu());
			ps.setDouble(3, vl.getGiaBan());
			ps.setDouble(4, vl.getGiaNhap());
			ps.setString(5, vl.getDonVi());
			ps.setString(6, vl.getMaVatLieu());
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
		String sql = "DELETE FROM VATLIEU WHERE MAVATLIEU=?";

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
		String sql = "SELECT MAVATLIEU, SOLUONG, TENVATLIEU, GIABAN, GIANHAP, DONVI " + "FROM VATLIEU "
				+ "WHERE TENVATLIEU LIKE ? " + "ORDER BY TENVATLIEU";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, "%" + tenVatLieu + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				VatLieuDTO vl = new VatLieuDTO(rs.getString("MAVATLIEU"), rs.getInt("SOLUONG"),
						rs.getString("TENVATLIEU"), rs.getDouble("GIABAN"), rs.getDouble("GIANHAP"),
						rs.getString("DONVI"));
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
		String sql = "SELECT MAVATLIEU, SOLUONG, TENVATLIEU, GIABAN, GIANHAP, DONVI " + "FROM VATLIEU "
				+ "WHERE SOLUONG > 0 " + "ORDER BY TENVATLIEU";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				VatLieuDTO vl = new VatLieuDTO(rs.getString("MAVATLIEU"), rs.getInt("SOLUONG"),
						rs.getString("TENVATLIEU"), rs.getDouble("GIABAN"), rs.getDouble("GIANHAP"),
						rs.getString("DONVI"));
				list.add(vl);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi getAvailableStock: " + e.getMessage());
		}
		return list;
	}

	/**
	 * Trừ số lượng vật liệu khi bán hàng
	 */
	public boolean deductStock(String maVatLieu, int soLuongBan) {
		// Kiểm tra xem số lượng có đủ không
		String checkSql = "SELECT SOLUONG FROM VATLIEU WHERE MAVATLIEU=?";
		try (PreparedStatement checkPs = con.prepareStatement(checkSql)) {
			checkPs.setString(1, maVatLieu);
			ResultSet rs = checkPs.executeQuery();
			if (rs.next()) {
				int soLuongHienTai = rs.getInt("SOLUONG");
				if (soLuongHienTai < soLuongBan) {
					System.out.println("Không đủ hàng! Tồn kho: " + soLuongHienTai + ", Bán: " + soLuongBan);
					return false;
				}
			}
		} catch (SQLException e) {
			System.out.println("Lỗi kiểm tra tồn kho: " + e.getMessage());
			return false;
		}

		// Trừ kho
		String sql = "UPDATE VATLIEU SET SOLUONG = SOLUONG - ? WHERE MAVATLIEU=?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, soLuongBan);
			ps.setString(2, maVatLieu);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Lỗi trừ kho: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Cộng số lượng vật liệu khi nhập thêm từ nhà cung cấp
	 * 
	 * 
	 */
	public boolean addStock(String maVatLieu, int soLuongNhap) {
		String sql = "UPDATE VATLIEU SET SOLUONG = SOLUONG + ? WHERE MAVATLIEU=?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, soLuongNhap);
			ps.setString(2, maVatLieu);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Lỗi cộng kho: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Sinh mã vật liệu tự động (VL001, VL002, ...)
	 * 
	 */
	public String generateMaVatLieu() {
		String sql = "SELECT COUNT(*) as cnt FROM VATLIEU";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int count = rs.getInt("cnt") + 1;
				return String.format("VL%03d", count);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi sinh mã: " + e.getMessage());
		}
		return "VL001";
	}

	/**
	 * Lấy số lượng tồn kho của một vật liệu
	 * 
	 */
	public int getStockQuantity(String maVatLieu) {
		String sql = "SELECT SOLUONG FROM VATLIEU WHERE MAVATLIEU=?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maVatLieu);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt("SOLUONG");
			}
		} catch (SQLException e) {
			System.out.println("Lỗi lấy số lượng: " + e.getMessage());
		}
		return -1;
	}
}
