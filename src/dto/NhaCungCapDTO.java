package dto;

public class NhaCungCapDTO {
	private String maNhaCC;
	private String tenNhaCungCap;
	private String sdt;
	private String diaChi;
	
	public NhaCungCapDTO() {
		
	}
	public NhaCungCapDTO(String maNhaCC, String tenNhaCungCap, String sdt, String diaChi) {
		super();
		this.maNhaCC = maNhaCC;
		this.tenNhaCungCap = tenNhaCungCap;
		this.sdt = sdt;
		this.diaChi = diaChi;
	}
	public String getMaNhaCC() {
		return maNhaCC;
	}
	public void setMaNhaCC(String maNhaCC) {
		this.maNhaCC = maNhaCC;
	}
	public String getTenNhaCungCap() {
		return tenNhaCungCap;
	}
	public void setTenNhaCungCap(String tenNhaCungCap) {
		this.tenNhaCungCap = tenNhaCungCap;
	}
	public String getSdt() {
		return sdt;
	}
	public void setSdt(String sdt) {
		this.sdt = sdt;
	}
	public String getDiaChi() {
		return diaChi;
	}
	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}
	@Override
	public String toString() {
		return "NhaCungCapDTO{maNhaCC='" + maNhaCC
	             + "', tenNhaCungCap='" + tenNhaCungCap
	             + "', sdt='" + sdt + "', diaChi='" + diaChi + "'}";

	}

}
