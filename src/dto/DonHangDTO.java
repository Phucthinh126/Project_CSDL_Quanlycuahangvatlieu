package dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DonHangDTO {
	private String maDH;
	private String maKH;
	private LocalDateTime ngayLap;
	private BigDecimal tongTien;

	public DonHangDTO() {
		super();
	}

	public DonHangDTO(String maDH, String maKH, LocalDateTime ngayLap, BigDecimal tongTien) {
		super();
		this.maDH = maDH;
		this.maKH = maKH;
		this.ngayLap = ngayLap;
		this.tongTien = tongTien;
	}

	public String getMaDH() {
		return maDH;
	}

	public void setMaDH(String maDH) {
		this.maDH = maDH;
	}

	public String getMaKH() {
		return maKH;
	}

	public void setMaKH(String maKH) {
		this.maKH = maKH;
	}

	public LocalDateTime getNgayLap() {
		return ngayLap;
	}

	public void setNgayLap(LocalDateTime ngayLap) {
		this.ngayLap = ngayLap;
	}

	public BigDecimal getTongTien() {
		return tongTien;
	}

	public void setTongTien(BigDecimal tongTien) {
		this.tongTien = tongTien;
	}

	@Override
	public String toString() {
		return "Dong Hang: [ " + "MaDH: " + maDH + ", MaKH: " + maKH + ", NgayLap: " + ngayLap + ", TongTien: "
				+ tongTien + " ]";
	}

}
