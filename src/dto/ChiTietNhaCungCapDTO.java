package dto;

import java.sql.Timestamp;

public class ChiTietNhaCungCapDTO {
	private String maNhaCC;
	private String maVatLieu;
	private String soLuong;
	private Timestamp ngayCungCap;
	
	public ChiTietNhaCungCapDTO() {
		
	}
	public ChiTietNhaCungCapDTO(String maNhaCC, String maVatLieu, String soLuong, Timestamp ngayCungCap) {
		super();
		this.maNhaCC = maNhaCC;
		this.maVatLieu = maVatLieu;
		this.soLuong = soLuong;
		this.ngayCungCap = ngayCungCap;
	}
	public String getMaNhaCC() {
		return maNhaCC;
	}
	public void setMaNhaCC(String maNhaCC) {
		this.maNhaCC = maNhaCC;
	}
	public String getmaVatLieu() {
		return maVatLieu;
	}
	public void setmaVatLieu(String maVatLieu) {
		this.maVatLieu = maVatLieu;
	}
	public String getSoLuong() {
		return soLuong;
	}
	public void setSoLuong(String soLuong) {
		this.soLuong = soLuong;
	}
	public Timestamp getNgayCungCap() {
		return ngayCungCap;
	}
	public void setNgayCungCap(Timestamp ngayCungCap) {
		this.ngayCungCap = ngayCungCap;
	}
	
	@Override
	public String toString() {
		return "ChiTietCungCapDTO{maNhaCC='" + maNhaCC
	             + "', maVatLieu='" + maVatLieu
	             + "', soLuong=" + soLuong
	             + ", ngayCungCap=" + ngayCungCap + "}";

	}

}
