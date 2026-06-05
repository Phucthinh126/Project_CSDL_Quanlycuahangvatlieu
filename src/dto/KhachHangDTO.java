package dto;

public class KhachHangDTO {
	private String maKH;
	private String tenKH;
	private String soDienThoai;
	private String diaChi;

	public KhachHangDTO() {

	}

	public KhachHangDTO(String maKH, String tenKH, String soDienThoai, String diaChi) {
		super();
		this.maKH = maKH;
		this.tenKH = tenKH;
		this.soDienThoai = soDienThoai;
		this.diaChi = diaChi;
	}

	public String getMaKH() {
		return maKH;
	}

	public void setMaKH(String maKH) {
		this.maKH = maKH;
	}

	public String getTenKH() {
		return tenKH;
	}

	public void setTenKH(String tenKH) {
		this.tenKH = tenKH;
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
		return "Khach Hang: [ " + "MaKH: " + maKH + ", TenKH: " + tenKH + ", Sdt: " + soDienThoai + ", DiaChi: "
				+ diaChi + " ]";
	}

}
