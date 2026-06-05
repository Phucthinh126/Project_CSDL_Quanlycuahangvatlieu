package dto;

import java.math.BigDecimal;

public class VatLieuDTO {
	private String maVL;
	private String tenVL;
	private String donViTinh;
	private int soLuongTon;
	private BigDecimal giaBan;

	public VatLieuDTO() {
		super();
	}

	public VatLieuDTO(String maVL, String tenVL, String donViTinh, int soLuongTon, BigDecimal giaBan) {
		super();
		this.maVL = maVL;
		this.tenVL = tenVL;
		this.donViTinh = donViTinh;
		this.soLuongTon = soLuongTon;
		this.giaBan = giaBan;
	}

	public String getMaVL() {
		return maVL;
	}

	public void setMaVL(String maVL) {
		this.maVL = maVL;
	}

	public String getTenVL() {
		return tenVL;
	}

	public void setTenVL(String tenVL) {
		this.tenVL = tenVL;
	}

	public String getDonViTinh() {
		return donViTinh;
	}

	public void setDonViTinh(String donViTinh) {
		this.donViTinh = donViTinh;
	}

	public int getSoLuongTon() {
		return soLuongTon;
	}

	public void setSoLuongTon(int soLuongTon) {
		this.soLuongTon = soLuongTon;
	}

	public BigDecimal getGiaBan() {
		return giaBan;
	}

	public void setGiaBan(BigDecimal giaBan) {
		this.giaBan = giaBan;
	}

	@Override
	public String toString() {
		return "Vat Lieu: [ " + "MaVL: " + maVL + ", TenVL: " + tenVL + ", DonViTinh: " + donViTinh + ", SoLuongTon: "
				+ soLuongTon + ", GiaBan: " + giaBan + " ]";
	}

}
