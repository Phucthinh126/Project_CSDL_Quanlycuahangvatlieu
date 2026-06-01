package dto;

import java.sql.Timestamp;

public class DonHangDTO {
	private String maDonHang;
	private String maKhachHang;
	private double tongTien;
	private Timestamp ngayDat;
	private Timestamp ngayGiao;
	public DonHangDTO(String maDonHang, String maKhachHang, double tongTien, Timestamp ngayDat, Timestamp ngayGiao) {
		super();
		this.maDonHang = maDonHang;
		this.maKhachHang = maKhachHang;
		this.tongTien = tongTien;
		this.ngayDat = ngayDat;
		this.ngayGiao = ngayGiao;
	}
	public String getMaDonHang() {
		return maDonHang;
	}
	public void setMaDonHang(String maDonHang) {
		this.maDonHang = maDonHang;
	}
	public String getMaKhachHang() {
		return maKhachHang;
	}
	public void setMaKhachHang(String maKhachHang) {
		this.maKhachHang = maKhachHang;
	}
	public double getTongTien() {
		return tongTien;
	}
	public void setTongTien(double tongTien) {
		this.tongTien = tongTien;
	}
	public Timestamp getNgayDat() {
		return ngayDat;
	}
	public void setNgayDat(Timestamp ngayDat) {
		this.ngayDat = ngayDat;
	}
	public Timestamp getNgayDao() {
		return ngayGiao;
	}
	public void setNgayDao(Timestamp ngayDao) {
		this.ngayGiao = ngayDao;
	}
	@Override
	public String toString() {
		  return "DonHangDTO{maDonHang='" + maDonHang
		             + "', maKhachHang='" + maKhachHang
		             + "', tongTien=" + tongTien
		             + ", ngayDat=" + ngayDat
		             + ", ngayGiao=" + ngayGiao + "}";

	}

}
