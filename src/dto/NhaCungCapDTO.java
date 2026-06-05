package dto;

public class NhaCungCapDTO {

	private String maNCC;
	private String tenNCC;
	private String soDienThoai;
	private String diaChi;

	public NhaCungCapDTO() {
	}

	public NhaCungCapDTO(String maNCC, String tenNCC, String soDienThoai, String diaChi) {
		super();
		this.maNCC = maNCC;
		this.tenNCC = tenNCC;
		this.soDienThoai = soDienThoai;
		this.diaChi = diaChi;
	}

	public String getMaNCC() {
		return maNCC;
	}

	public void setMaNCC(String maNCC) {
		this.maNCC = maNCC;
	}

	public String getTenNCC() {
		return tenNCC;
	}

	public void setTenNCC(String tenNCC) {
		this.tenNCC = tenNCC;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	@Override
	public String toString() {
		return "Nha Cung Cap: [ " + " MaNCC: " + maNCC + ", TenNCC: " + tenNCC + ", Sdt: " + soDienThoai + ", DiaChi: "
				+ diaChi + " ]";
	}
}
