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

	/*
	 * Them chi tiet cho mot nha cung cap
	 */
	public boolean insert(ChiTietNhaCungCapDTO ctncc) {
		String sql = "INSERT INTO ChiTietNhaCungCap (MaNCC, MaVL, SoLuong, GiaNhap) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, ctncc.getMaNCC());
			ps.setString(2, ctncc.getMaVL());
			ps.setInt(3, ctncc.getSoLuong());
			ps.setBigDecimal(4, ctncc.getGiaNhap());

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Lỗi insert ChiTietNhaCungCap: " + e.getMessage());
			return false;
		}
	}

	/*
	 * Cap nhat lai so luong, gia
	 */
	public boolean update(ChiTietNhaCungCapDTO ctncc) {
		String sql = "UPDATE ChiTietNhaCungCap SET SoLuong = ?, GiaNhap = ? WHERE MaNCC = ? AND MaVL = ?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, ctncc.getSoLuong());
			ps.setBigDecimal(2, ctncc.getGiaNhap());
			ps.setString(3, ctncc.getMaNCC());
			ps.setString(4, ctncc.getMaVL());

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Lỗi update ChiTietNhaCungCap: " + e.getMessage());
			return false;
		}
	}

	/*
	 * Xoa thong tin cung cap mat hang
	 */
	public boolean delete(String maNCC, String maVL) {
		String sql = "DELETE FROM ChiTietNhaCungCap WHERE MaNCC = ? AND MaVL = ?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maNCC);
			ps.setString(2, maVL);

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Lỗi delete ChiTietNhaCungCap: " + e.getMessage());
			return false;
		}
	}

	/**
	 * lay danh sach tat ca cac lan nhap hang
	 */
	public List<ChiTietNhaCungCapDTO> getAll() {
		List<ChiTietNhaCungCapDTO> list = new ArrayList<>();
		String sql = "SELECT MaNCC, MaVL, SoLuong, GiaNhap FROM ChiTietNhaCungCap ORDER BY MaNCC";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				ChiTietNhaCungCapDTO ctncc = new ChiTietNhaCungCapDTO(rs.getString("MaNCC"), rs.getString("MaVL"),
						rs.getInt("SoLuong"), rs.getBigDecimal("GiaNhap"));
				list.add(ctncc);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi getAll Supply: " + e.getMessage());
		}
		return list;
	}

	/*
	 * Danh sach vat lieu duoc cung cap boi mot nha cung cap
	 */
	public List<ChiTietNhaCungCapDTO> getSupplyBySupplier(String maNhaCC) {
		List<ChiTietNhaCungCapDTO> list = new ArrayList<>();
		String sql = "SELECT MaNCC, MaVL, SoLuong, GiaNhap FROM ChiTietNhaCungCap WHERE MaNCC = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maNhaCC);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ChiTietNhaCungCapDTO ctncc = new ChiTietNhaCungCapDTO(rs.getString("MaNCC"), rs.getString("MaVL"),
						rs.getInt("SoLuong"), rs.getBigDecimal("GiaNhap"));
				list.add(ctncc);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi getSupplyBySupplier: " + e.getMessage());
		}
		return list;
	}

	/*
	 * Lay đanh sach nha cung cap co cung cap loai vat lieu cu the
	 */
	public List<ChiTietNhaCungCapDTO> getSupplyByMaterial(String maVatLieu) {
		List<ChiTietNhaCungCapDTO> list = new ArrayList<>();
		String sql = "SELECT MaNCC, MaVL, SoLuong, GiaNhap FROM ChiTietNhaCungCap WHERE MaVL = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maVatLieu);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ChiTietNhaCungCapDTO ctncc = new ChiTietNhaCungCapDTO(rs.getString("MaNCC"), rs.getString("MaVL"),
						rs.getInt("SoLuong"), rs.getBigDecimal("GiaNhap"));
				list.add(ctncc);
			}
		} catch (SQLException e) {
			System.out.println("Lỗi getSupplyByMaterial: " + e.getMessage());
		}
		return list;
	}

}
