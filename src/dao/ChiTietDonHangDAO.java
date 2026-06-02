package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DBConnection;
import dto.ChiTietDonHangDTO;

public class ChiTietDonHangDAO {
	private Connection con = DBConnection.getConnection();

	/**
	 * them chi tiet don hang
	 */
	public boolean insertOrderDetail(ChiTietDonHangDTO ctdh) {
		String sql = "INSERT INTO CHITIETDONHANG(MADONHANG, MAVATLIEU, SOLUONG) VALUES (?,?,?)";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, ctdh.getMaDonHang());
			ps.setString(2, ctdh.getMaVatLieu());
			ps.setInt(3, ctdh.getSoLuong());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Lỗi insert OrderDetail: " + e.getMessage());
			return false;
		}
	}

	/**
	 * lay danh sach chi tiet don hang cua mot don hang
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

	/**
	 * cap nhat cua mot chi tiet don hang
	 */
	public boolean updateOrderDetail(ChiTietDonHangDTO ctdh) {
		String sql = "UPDATE CHITIETDONHANG SET SOLUONG=? WHERE MADONHANG=? AND MAVATLIEU=?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, ctdh.getSoLuong());
			ps.setString(2, ctdh.getMaDonHang());
			ps.setString(3, ctdh.getMaVatLieu());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Lỗi updateOrderDetail: " + e.getMessage());
			return false;
		}
	}

	/**
	 * xoa chi tiet don hang
	 */
	public boolean deleteOrderDetail(String maDonHang, String maVatLieu) {
		String sql = "DELETE FROM CHITIETDONHANG WHERE MADONHANG=? AND MAVATLIEU=?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maDonHang);
			ps.setString(2, maVatLieu);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Lỗi deleteOrderDetail: " + e.getMessage());
			return false;
		}
	}

}
