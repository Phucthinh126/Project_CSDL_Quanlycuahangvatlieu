package dto;

public class VatLieuDTO {
	private String maVatLieu; 
	private int soLuong; 
	private String tenVatLieu; 
	private double giaBan;
	private double giaNhap;
	private String donVi;
	
	
	public VatLieuDTO(String maVatLieu, int soLuong, String tenVatLieu, double giaBan, double giaNhap, String donVi) {
		super();
		this.maVatLieu = maVatLieu;
		this.soLuong = soLuong;
		this.tenVatLieu = tenVatLieu;
		this.giaBan = giaBan;
		this.giaNhap = giaNhap;
		this.donVi = donVi;
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


	public String getTenVatLieu() {
		return tenVatLieu;
	}


	public void setTenVatLieu(String tenVatLieu) {
		this.tenVatLieu = tenVatLieu;
	}


	public double getGiaBan() {
		return giaBan;
	}


	public void setGiaBan(double giaBan) {
		this.giaBan = giaBan;
	}


	public double getGiaNhap() {
		return giaNhap;
	}


	public void setGiaNhap(double giaNhap) {
		this.giaNhap = giaNhap;
	}


	public String getDonVi() {
		return donVi;
	}


	public void setDonVi(String donVi) {
		this.donVi = donVi;
	}


	@Override
	public String toString() {
		 return "VatLieuDTO{maVatLieu='" + maVatLieu + "', soLuong=" + soLuong
	             + ", tenVatLieu='" + tenVatLieu + "', giaBan=" + giaBan
	             + ", giaNhap=" + giaNhap + ", donVi='" + donVi + "'}";

	}
	
	
	

}
