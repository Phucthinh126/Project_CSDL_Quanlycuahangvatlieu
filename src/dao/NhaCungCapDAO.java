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
		String sql = "SELECT MANHACC, TENNHACUNGCAP, SDT, DIACHI FROM NHACUNGCAP ORDER BY TENNHACUNGCAP";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				NhaCungCapDTO ncc = new NhaCungCapDTO(rs.getString("MANHACC"), rs.getString("TENNHACUNGCAP"),
						rs.getString("SDT"), rs.getString("DIACHI"));
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
		String sql = "SELECT MANHACC, TENNHACUNGCAP, SDT, DIACHI FROM NHACUNGCAP WHERE MANHACC=?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maNhaCC);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new NhaCungCapDTO(rs.getString("MANHACC"), rs.getString("TENNHACUNGCAP"), rs.getString("SDT"),
						rs.getString("DIACHI"));
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
		String sql = "INSERT INTO NHACUNGCAP(MANHACC, TENNHACUNGCAP, SDT, DIACHI) VALUES (?,?,?,?)";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, ncc.getMaNhaCC());
			ps.setString(2, ncc.getTenNhaCungCap());
			ps.setString(3, ncc.getSdt());
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
		String sql = "UPDATE NHACUNGCAP SET TENNHACUNGCAP=?, SDT=?, DIACHI=? WHERE MANHACC=?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, ncc.getTenNhaCungCap());
			ps.setString(2, ncc.getSdt());
			ps.setString(3, ncc.getDiaChi());
			ps.setString(4, ncc.getMaNhaCC());
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
		String sql = "DELETE FROM NHACUNGCAP WHERE MANHACC=?";

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
		String sql = "SELECT MANHACC, TENNHACUNGCAP, SDT, DIACHI FROM NHACUNGCAP "
				+ "WHERE TENNHACUNGCAP LIKE ? ORDER BY TENNHACUNGCAP";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, "%" + tenNhaCC + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				NhaCungCapDTO ncc = new NhaCungCapDTO(rs.getString("MANHACC"), rs.getString("TENNHACUNGCAP"),
						rs.getString("SDT"), rs.getString("DIACHI"));
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
	public List<NhaCungCapDTO> searchByPhone(String sdt) {
		List<NhaCungCapDTO> list = new ArrayList<>();
		String sql = "SELECT MANHACC, TENNHACUNGCAP, SDT, DIACHI FROM NHACUNGCAP "
				+ "WHERE SDT LIKE ? ORDER BY TENNHACUNGCAP";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, "%" + sdt + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				NhaCungCapDTO ncc = new NhaCungCapDTO(rs.getString("MANHACC"), rs.getString("TENNHACUNGCAP"),
						rs.getString("SDT"), rs.getString("DIACHI"));
				list.add(ncc);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi searchByPhone NhaCungCap: " + e.getMessage());
		}
		return list;
	}

	/**
	 * sinh ma nha cung cap tu dong
	 */
	public String generateMaNhaCC() {
		String sql = "SELECT COUNT(*) as cnt FROM NHACUNGCAP";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int count = rs.getInt("cnt") + 1;
				return String.format("NCC%03d", count);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi sinh mã NhaCC: " + e.getMessage());
		}
		return "NCC001";
	}

}
