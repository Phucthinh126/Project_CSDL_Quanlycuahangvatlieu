package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DBConnection;
import dto.NhaCungCapDTO;

public class NhaCungCapDAO {
	private Connection con = DBConnection.getConnection();

	/*
	 * Lay danh sach nha cung cap
	 */
	public List<NhaCungCapDTO> getAll() {
		List<NhaCungCapDTO> list = new ArrayList<>();
		String sql = "SELECT MaNCC, TenNCC, SoDienThoai, DiaChi FROM NhaCungCap ORDER BY TenNCC";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				NhaCungCapDTO ncc = new NhaCungCapDTO(rs.getString("MaNCC"), rs.getString("TenNCC"),
						rs.getString("SoDienThoai"), rs.getString("DiaChi"));
				list.add(ncc);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi getAll NhaCungCap: " + e.getMessage());
		}
		return list;
	}

	/**
	 * Lay thong tin nha cung cap theo ma
	 */

	public NhaCungCapDTO getById(String maNhaCC) {
		String sql = "SELECT MaNCC, TenNCC, SoDienThoai, DiaChi FROM NhaCungCap WHERE MaNCC = ?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maNhaCC);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new NhaCungCapDTO(rs.getString("MaNCC"), rs.getString("TenNCC"), rs.getString("SoDienThoai"),
						rs.getString("DiaChi"));
			}
		} catch (SQLException e) {
			System.out.println("Lỗi getById NhaCungCap: " + e.getMessage());
		}
		return null;
	}

	/*
	 * Them nha cung cap moi
	 */

	public boolean insert(NhaCungCapDTO ncc) {
		String sql = "INSERT INTO NhaCungCap (MaNCC, TenNCC, SoDienThoai, DiaChi) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, ncc.getMaNCC());
			ps.setString(2, ncc.getTenNCC());
			ps.setString(3, ncc.getSoDienThoai());
			ps.setString(4, ncc.getDiaChi());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Lỗi insert NhaCungCap: " + e.getMessage());
			return false;
		}
	}

	/**
	 * cap nhat thong tin nha cung cap
	 */
	public boolean update(NhaCungCapDTO ncc) {
		String sql = "UPDATE NhaCungCap SET TenNCC = ?, SoDienThoai = ?, DiaChi = ? WHERE MaNCC = ?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, ncc.getTenNCC());
			ps.setString(2, ncc.getSoDienThoai());
			ps.setString(3, ncc.getDiaChi());
			ps.setString(4, ncc.getMaNCC());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Lỗi update NhaCungCap: " + e.getMessage());
			return false;
		}
	}

	/**
	 * xoa nha cung cap
	 */
	public boolean delete(String maNhaCC) {
		String sql = "DELETE FROM NhaCungCap WHERE MaNCC = ?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maNhaCC);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Lỗi delete NhaCungCap: " + e.getMessage());
			return false;
		}
	}

	/**
	 * tim kiem nha cung cap theo ten
	 */
	public List<NhaCungCapDTO> searchByName(String tenNhaCC) {
		List<NhaCungCapDTO> list = new ArrayList<>();
		String sql = "SELECT MaNCC, TenNCC, SoDienThoai, DiaChi FROM NhaCungCap WHERE TenNCC LIKE ? ORDER BY TenNCC";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, "%" + tenNhaCC + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				NhaCungCapDTO ncc = new NhaCungCapDTO(rs.getString("MaNCC"), rs.getString("TenNCC"),
						rs.getString("SoDienThoai"), rs.getString("DiaChi"));
				list.add(ncc);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi searchByName NhaCungCap: " + e.getMessage());
		}
		return list;
	}

	/**
	 * tim kiem nha cung cap theo so dien thoai
	 */
	public NhaCungCapDTO searchByPhone(String sdt) {
		NhaCungCapDTO n = null;
		String sql = "SELECT MaNCC, TenNCC, SoDienThoai, DiaChi FROM NhaCungCap WHERE SoDienThoai LIKE ? ORDER BY TenNCC";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, "%" + sdt + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				NhaCungCapDTO ncc = new NhaCungCapDTO(rs.getString("MaNCC"), rs.getString("TenNCC"),
						rs.getString("SoDienThoai"), rs.getString("DiaChi"));
				n = ncc;
			}
		} catch (SQLException e) {
			System.out.println("Lỗi searchByPhone NhaCungCap: " + e.getMessage());
		}
		return n;
	}

	/**
	 * sinh ma nha cung cap tu dong
	 */
	public String generateMaNhaCC() {
		String sql = "SELECT MAX(MaNCC) as max_ma FROM NhaCungCap";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int count = rs.getInt("cnt") + 1;
				return String.format("NCC%03d", count);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi sinh mã NhaCC: " + e.getMessage());
		}
		return "NCC000";
	}

}
