package main;

import java.math.BigDecimal;
import java.util.List;

import dao.ChiTietNhaCungCapDAO;
import dao.NhaCungCapDAO;
import dao.VatLieuDAO;
import dto.ChiTietNhaCungCapDTO;
import dto.NhaCungCapDTO;
import dto.VatLieuDTO;

public class TestVatLieu {
	public static void main(String[] args) {
		VatLieuDAO vlDao = new VatLieuDAO();
		NhaCungCapDAO ncc = new NhaCungCapDAO();
		ChiTietNhaCungCapDAO ctncc = new ChiTietNhaCungCapDAO();

		// Lay toan bo vat lieu
		List<VatLieuDTO> listVl = vlDao.getAll();
		for (VatLieuDTO e : listVl) {
//			System.out.println(e);
		}
		// Lay toan bo nha cung cap
		List<NhaCungCapDTO> listNcc = ncc.getAll();
		for (NhaCungCapDTO e : listNcc) {
//			System.out.println(e);
		}

		// Lay toan bo chi tiet lan nhap hang
		List<ChiTietNhaCungCapDTO> listCT = ctncc.getAll();
		for (ChiTietNhaCungCapDTO e : listCT) {
//			System.out.println(e);
		}

		// tao vat lieu moi
		VatLieuDTO vlMoi = new VatLieuDTO("VL0081", "Tấm panel cách nhiệt EPS (1.2m x 2.4m x 5cm)", "Tấm", 15,
				new BigDecimal(320000));
		// tao nha cung cap moi
		NhaCungCapDTO nccMoi = new NhaCungCapDTO("NCC036", "Công ty TNHH Vật Liệu Xanh Việt", "02837123456",
				"789 Đường số 8, KCN Tân Tạo, Quận Bình Tân, TP HCM");

		// Tao chi tiet nha cung cap - so luong nhap hang vat lieu
		ChiTietNhaCungCapDTO ctNccMoi = new ChiTietNhaCungCapDTO("NCC036", "VL0081", 1000, new BigDecimal(255000));

//
//		vlDao.insert(vlMoi); // them moi vat lieu
//		ncc.insert(nccMoi); // them moi nha cc
//		ctncc.insert(ctNccMoi); // cap nhat nhap hang cho vlMoi

		System.out.println("Xem thong tin vat lieu moi: voi ma VL0081");
		System.out.println(vlDao.getById("VL0081"));
		System.out.println("\nXem thong tin nha cung cap moi voi ma: NCC036");
		System.out.println(ncc.getById("NCC036"));
		System.out.println("\nXem thong tin so luong mat hang trong vat lieu da nhap hang cho vlMoi");
		System.out.println(vlDao.getById("VL0081").getSoLuongTon());

	}

}
