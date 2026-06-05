package dto;

import java.math.BigDecimal;

public class ChiTietNhaCungCapDTO {
	private String maNCC;
	private String maVL;
	private int soLuong;
	private BigDecimal giaNhap;

	public ChiTietNhaCungCapDTO() {
		super();
	}

	public ChiTietNhaCungCapDTO(String maNCC, String maVL, int soLuong, BigDecimal giaNhap) {
		super();
		this.maNCC = maNCC;
		this.maVL = maVL;
		this.soLuong = soLuong;
		this.giaNhap = giaNhap;
	}

	public String getMaNCC() {
		return maNCC;
	}

	public void setMaNCC(String maNCC) {
		this.maNCC = maNCC;
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

	public BigDecimal getGiaNhap() {
		return giaNhap;
	}

	public void setGiaNhap(BigDecimal giaNhap) {
		this.giaNhap = giaNhap;
	}

	@Override
	public String toString() {
		return "Chi Tiet Nha Cung Cap: [ " + "MaNCC: " + maNCC + ", MaVL: " + maVL + ", SoLuong: " + soLuong
				+ ", GiaNhap: " + " ]";
	}

}
