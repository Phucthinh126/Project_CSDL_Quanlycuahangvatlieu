package dto;

import java.math.BigDecimal;

public class ChiTietDonHangDTO {
	private String maDH;
	private String maVL;
	private int soLuong;
	private BigDecimal donGia;

	public ChiTietDonHangDTO() {
		super();
	}

	public ChiTietDonHangDTO(String maDH, String maVL, int soLuong, BigDecimal donGia) {
		super();
		this.maDH = maDH;
		this.maVL = maVL;
		this.soLuong = soLuong;
		this.donGia = donGia;
	}

	public String getMaDH() {
		return maDH;
	}

	public void setMaDH(String maDH) {
		this.maDH = maDH;
	}

	public String getMaVL() {
		return maVL;
	}

	public void setMaVL(String maVL) {
		this.maVL = maVL;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}

	public BigDecimal getDonGia() {
		return donGia;
	}

	public void setDonGia(BigDecimal donGia) {
		this.donGia = donGia;
	}

	@Override
	public String toString() {
		return "Chi Tiet Don Hang: [ " + "MaDH: " + maDH + ", MaVL: " + maVL + ", SoLuong: " + soLuong + ", DonGia: "
				+ donGia + " ]";
	}
}
