package dto;

public class ChiTietDonHangDTO {
	private String maDonHang;
	private String maVatLieu;
	private int soLuong;
	public ChiTietDonHangDTO(String maDonHang, String maVatLieu, int soLuong) {
		super();
		this.maDonHang = maDonHang;
		this.maVatLieu = maVatLieu;
		this.soLuong = soLuong;
	}
	public String getMaDonHang() {
		return maDonHang;
	}
	public void setMaDonHang(String maDonHang) {
		this.maDonHang = maDonHang;
	}
	public String getMaVatLieu() {
		return maVatLieu;
	}
	public void setMaVatLieu(String maVatLieu) {
		this.maVatLieu = maVatLieu;
	}
	public int getSoLuong() {
		return soLuong;
	}
	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}
	@Override
	public String toString() {
		  return "ChiTietDonHangDTO{maDonHang='" + maDonHang
		             + "', maVatLieu='" + maVatLieu
		             + "', soLuong=" + soLuong + "}";

	}

}
