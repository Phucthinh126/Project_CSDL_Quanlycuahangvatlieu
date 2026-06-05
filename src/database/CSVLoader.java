package database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dao.ChiTietDonHangDAO;
import dao.ChiTietNhaCungCapDAO;
import dao.DonHangDAO;
import dao.KhachHangDAO;
import dao.NhaCungCapDAO;
import dao.VatLieuDAO;
import dto.ChiTietDonHangDTO;
import dto.ChiTietNhaCungCapDTO;
import dto.DonHangDTO;
import dto.KhachHangDTO;
import dto.NhaCungCapDTO;
import dto.VatLieuDTO;

public class CSVLoader {
	// Load vat lieu
	public static List<String> loadVatLieuFromCSV(String filePath) {
		List<String> logs = new ArrayList<>();
		VatLieuDAO dao = new VatLieuDAO();
		int successCount = 0;
		int errorCount = 0;
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(Files.newInputStream(Paths.get(filePath)), StandardCharsets.UTF_8))) {
			String headerLine = reader.readLine();
			if (headerLine == null) {
				logs.add("Lỗi: File CSV rỗng");
				return logs;
			}
			String[] headers = parseCSVLine(headerLine);
			logs.add("File header: " + String.join(", ", headers));
			String line;
			int lineNumber = 2;
			while ((line = reader.readLine()) != null) {
				try {
					String[] values = parseCSVLine(line);
					// Kiểm tra số cột của dòng dữ liệu so với header
					if (values.length != headers.length) {
						logs.add("Dòng " + lineNumber + ": Số cột không khớp");
						errorCount++;
						lineNumber++;
						continue;
					}
					// b1: Đọc và trim dữ liệu
					String maVL = values[0].replace("\"", "").trim();
					String tenVL = values[1].replace("\"", "").trim();
					String donVi = values[2].replace("\"", "").trim(); // Cột 3: DonViTinh ("Cây")
					int soLuong = Integer.parseInt(values[3].replace("\"", "").trim()); // Cột 4: SoLuongTon (1500)
					BigDecimal giaBan = new BigDecimal(values[4].replace("\"", "").trim()); // Cột 5: GiaBan (285000)
					// b2: Sinh mã tự động nếu file CSV bỏ trống mã vật liệu
					if (maVL.isEmpty()) {
						maVL = dao.generateMaVatLieu(); // Hoặc hàm sinh mã vật liệu của bạn
						logs.add(" --> Dòng " + lineNumber + ": Sinh mã tự động -> " + maVL);
					}
					// b3: Kiểm tra tính hợp lệ dữ liệu căn bản
					if (tenVL.isEmpty()) {
						logs.add("Dòng " + lineNumber + ": Tên vật liệu không được để trống");
						errorCount++;
						lineNumber++;
						continue;
					}
					if (soLuong < 0 || giaBan.compareTo(BigDecimal.ZERO) < 0) {
						logs.add("Dòng " + lineNumber + ": Số lượng hoặc giá bán không được âm");
						errorCount++;
						lineNumber++;
						continue;
					}
					// Khởi tạo DTO
					VatLieuDTO vl = new VatLieuDTO(maVL, tenVL, donVi, soLuong, giaBan);
					// b4: Kiểm tra trùng mã, ghi đè
					boolean isExisted = (dao.getById(maVL) != null);
					if (isExisted) {
						// Tình huống trùng mã -> Chạy lệnh UPDATE đè dữ liệu cũ
						if (dao.update(vl)) {
							logs.add("Dòng " + lineNumber + ": Mã '" + maVL
									+ "' bị trùng -> Đã ghi đè thông tin mới thành công");
							successCount++;
						} else {
							logs.add("Dòng " + lineNumber + ": Ghi đè thông tin cho mã '" + maVL + "' thất bại");
							errorCount++;
						}
					} else {
						// Tình huống mã mới -> Chạy lệnh INSERT thêm mới
						if (dao.insert(vl)) {
							logs.add("Dòng " + lineNumber + ": Thêm mới vật liệu '" + tenVL + "' thành công");
							successCount++;
						} else {
							logs.add("Dòng " + lineNumber + ": Thêm mới '" + tenVL + "' thất bại");
							errorCount++;
						}
					}
				} catch (NumberFormatException e) {
					logs.add("Dòng " + lineNumber + ": Lỗi định dạng số (Sai kiểu số lượng/giá tiền) - "
							+ e.getMessage());
					errorCount++;
				} catch (Exception e) {
					logs.add("Dòng " + lineNumber + ": Lỗi hệ thống - " + e.getMessage());
					errorCount++;
				}
				lineNumber++;
			}
			logs.add("\nKẾT QUẢ ĐỌC FILE: " + successCount + " dòng xử lý thành công, " + errorCount + " dòng bị lỗi");
		} catch (IOException e) {
			logs.add("Lỗi IO thiết bị khi đọc file: " + e.getMessage());
		}
		return logs;
	}

	public static List<String> loadKhachHangFromCSV(String filePath) {
		List<String> logs = new ArrayList<>();
		KhachHangDAO dao = new KhachHangDAO();
		int successCount = 0;
		int errorCount = 0;

		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(Files.newInputStream(Paths.get(filePath)), StandardCharsets.UTF_8))) {
			String headerLine = reader.readLine();
			if (headerLine == null) {
				logs.add("Lỗi: File CSV rỗng");
				return logs;
			}
			String[] headers = parseCSVLine(headerLine);
			String line;
			int lineNumber = 2;

			java.util.Map<String, KhachHangDTO> existedSdtMap = new java.util.HashMap<>();
			List<KhachHangDTO> allKhachHang = dao.getAll();

			if (allKhachHang != null) {
				for (KhachHangDTO kh : allKhachHang) {
					if (kh != null && kh.getSoDienThoai() != null && !kh.getSoDienThoai().trim().isEmpty()) {
						String sdtChuan = kh.getSoDienThoai().trim();
						// Nếu lỡ dưới DB bị trùng SĐT, chỉ giữ lại người đầu tiên trong Map để kiểm tra
						if (!existedSdtMap.containsKey(sdtChuan)) {
							existedSdtMap.put(sdtChuan, kh);
						}
					}
				}
			}

			// 2. TỐI ƯU SINH MÃ: Lấy mã gốc lớn nhất để tự tăng trong Java (Tránh lỗi dồn
			// toa mã)
			String maxMaHienTai = dao.generateMaKhachHang();
			int suffixNumber = 1;
			if (maxMaHienTai != null) {
				String chiLaySo = maxMaHienTai.replaceAll("[^0-9]", "");
				if (!chiLaySo.isEmpty()) {
					suffixNumber = Integer.parseInt(chiLaySo);
				}
			}

			while ((line = reader.readLine()) != null) {
				try {
					String[] values = parseCSVLine(line);
					if (values.length != headers.length) {
						logs.add("Dòng " + lineNumber + ": Số cột không khớp");
						errorCount++;
						lineNumber++;
						continue;
					}

					String maKH = values[0].replace("\"", "").trim();
					String tenKH = values[1].replace("\"", "").trim();
					String sdt = values[2].replace("\"", "").trim();
					String diaChi = values[3].replace("\"", "").trim();

					if (tenKH.isEmpty() || sdt.isEmpty()) {
						logs.add("Dòng " + lineNumber + ": Tên KH và SĐT không được để trống");
						errorCount++;
						lineNumber++;
						continue;
					}

					KhachHangDTO existedKh = existedSdtMap.get(sdt);

					if (existedKh != null) {
						// TÌNH HUỐNG 1: ĐÃ TỒN TẠI SĐT -> CẬP NHẬT (Giữ nguyên mã cũ dưới DB)
						KhachHangDTO khUpdate = new KhachHangDTO(existedKh.getMaKH(), tenKH, sdt, diaChi);
						if (dao.update(khUpdate)) {
							logs.add("Dòng " + lineNumber + ": Số điện thoại '" + sdt
									+ "' đã tồn tại -> Đã ghi đè thông tin mới cho mã " + existedKh.getMaKH());
							successCount++;
						} else {
							logs.add("Dòng " + lineNumber + ": Ghi đè thông tin khách hàng thất bại");
							errorCount++;
						}
					} else {

						if (maKH.isEmpty()) {
							maKH = String.format("KH%03d", suffixNumber);
							suffixNumber++; // Tăng số thứ tự để sẵn sàng cho khách tiếp theo
							logs.add("Dòng " + lineNumber + ": Sinh mã tự động cho khách hàng mới -> " + maKH);
						}

						KhachHangDTO khInsert = new KhachHangDTO(maKH, tenKH, sdt, diaChi);
						if (dao.insert(khInsert)) {
							logs.add("Dòng " + lineNumber + ": Thêm mới khách hàng '" + tenKH + "' thành công với mã "
									+ maKH);
							successCount++;
							existedSdtMap.put(sdt, khInsert);
						} else {
							logs.add("Dòng " + lineNumber + ": Thêm mới khách hàng thất bại");
							errorCount++;
						}
					}
				} catch (Exception e) {
					logs.add("Dòng " + lineNumber + ": Lỗi - " + e.getMessage());
					errorCount++;
				}
				lineNumber++;
			}
			logs.add("\nKẾT QUẢ KHÁCH HÀNG: " + successCount + " thành công, " + errorCount + " lỗi");
		} catch (IOException e) {
			logs.add("Lỗi đọc file: " + e.getMessage());
		}
		return logs;
	}

	// Load CSV cho nha cung cap
	public static List<String> loadNhaCungCapFromCSV(String filePath) {
		List<String> logs = new ArrayList<>();
		NhaCungCapDAO dao = new NhaCungCapDAO();
		int successCount = 0;
		int errorCount = 0;

		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(Files.newInputStream(Paths.get(filePath)), StandardCharsets.UTF_8))) {

			String headerLine = reader.readLine();
			if (headerLine == null)
				return logs;
			String[] headers = parseCSVLine(headerLine);
			String line;
			int lineNumber = 2;

			while ((line = reader.readLine()) != null) {
				try {
					String[] values = parseCSVLine(line);
					if (values.length != headers.length) {
						logs.add("Dòng " + lineNumber + ": Số cột không khớp");
						errorCount++;
						lineNumber++;
						continue;
					}

					String maNCC = values[0].replace("\"", "").trim();
					String tenNCC = values[1].replace("\"", "").trim();
					String sdt = values[2].replace("\"", "").trim();
					String diaChi = values[3].replace("\"", "").trim();

					if (maNCC.isEmpty()) {
						maNCC = dao.generateMaNhaCC();
						logs.add(" --> Dòng " + lineNumber + ": Sinh mã tự động -> " + maNCC);
					}

					NhaCungCapDTO ncc = new NhaCungCapDTO(maNCC, tenNCC, sdt, diaChi);

					if (dao.getById(maNCC) != null) {
						if (dao.update(ncc)) {
							logs.add("Dòng " + lineNumber + ": Trùng mã '" + maNCC
									+ "' -> Đã cập nhật ghi đè dữ liệu nhà cung cấp");
							successCount++;
						} else {
							logs.add("Dòng " + lineNumber + ": Cập nhật nhà cung cấp thất bại");
							errorCount++;
						}
					} else {
						if (dao.insert(ncc)) {
							logs.add("Dòng " + lineNumber + ": Thêm mới nhà cung cấp '" + tenNCC + "' thành công");
							successCount++;
						} else {
							logs.add("Dòng " + lineNumber + ": Thêm mới nhà cung cấp thất bại");
							errorCount++;
						}
					}
				} catch (Exception e) {
					logs.add("Dòng " + lineNumber + ": Lỗi - " + e.getMessage());
					errorCount++;
				}
				lineNumber++;
			}
			logs.add("\nKẾT QUẢ NHÀ CUNG CẤP: " + successCount + " thành công, " + errorCount + " lỗi");
		} catch (IOException e) {
			logs.add("Lỗi đọc file: " + e.getMessage());
		}
		return logs;
	}

	// Load csv chi tiet nha cung cap
	public static List<String> loadChiTietNhaCungCapFromCSV(String filePath) {
		List<String> logs = new ArrayList<>();
		ChiTietNhaCungCapDAO dao = new ChiTietNhaCungCapDAO();
		int successCount = 0;
		int errorCount = 0;

		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(Files.newInputStream(Paths.get(filePath)), StandardCharsets.UTF_8))) {

			String headerLine = reader.readLine();
			if (headerLine == null)
				return logs;
			String[] headers = parseCSVLine(headerLine);
			String line;
			int lineNumber = 2;

			while ((line = reader.readLine()) != null) {
				try {
					String[] values = parseCSVLine(line);
					if (values.length != headers.length) {
						logs.add("Dòng " + lineNumber + ": Số cột không khớp");
						errorCount++;
						lineNumber++;
						continue;
					}

					String maNCC = values[0].replace("\"", "").trim();
					String maVL = values[1].replace("\"", "").trim();
					int soLuong = Integer.parseInt(values[2].trim());
					BigDecimal giaNhap = new BigDecimal(values[3].trim());

					if (maNCC.isEmpty() || maVL.isEmpty() || soLuong < 0) {
						logs.add("Dòng " + lineNumber + ": Dữ liệu mã hoặc số lượng không hợp lệ");
						errorCount++;
						lineNumber++;
						continue;
					}

					ChiTietNhaCungCapDTO ctncc = new ChiTietNhaCungCapDTO(maNCC, maVL, soLuong, giaNhap);

					// Kiểm tra xem Nhà cung cấp này đã từng cung cấp Vật liệu này chưa
					boolean isExisted = dao.getSupplyBySupplier(maNCC).stream()
							.anyMatch(ct -> ct.getMaVL().equals(maVL));

					if (isExisted) {
						if (dao.update(ctncc)) {
							logs.add("Dòng " + lineNumber + ": Cặp mã (" + maNCC + ", " + maVL
									+ ") đã tồn tại -> Đã ghi đè số lượng và giá nhập");
							successCount++;
						} else {
							errorCount++;
						}
					} else {
						if (dao.insert(ctncc)) {
							logs.add("Dòng " + lineNumber + ": Thêm chi tiết nhập hàng từ " + maNCC + " thành công");
							successCount++;
						} else {
							errorCount++;
						}
					}
				} catch (Exception e) {
					logs.add("Dòng " + lineNumber + ": Lỗi - " + e.getMessage());
					errorCount++;
				}
				lineNumber++;
			}
			logs.add("\nKẾT QUẢ CHI TIẾT NHÀ CUNG CẤP: " + successCount + " thành công, " + errorCount + " lỗi");
		} catch (IOException e) {
			logs.add("Lỗi đọc file: " + e.getMessage());
		}
		return logs;
	}

	// Load csv cho don hang
	public static List<String> loadDonHangFromCSV(String filePath) {
		List<String> logs = new ArrayList<>();
		DonHangDAO dao = new DonHangDAO();
		int successCount = 0;
		int errorCount = 0;

		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(Files.newInputStream(Paths.get(filePath)), StandardCharsets.UTF_8))) {
			String headerLine = reader.readLine();
			if (headerLine == null)
				return logs;
			String[] headers = parseCSVLine(headerLine);
			String line;
			int lineNumber = 2;

			// 1. TỐI ƯU HIỆU NĂNG: Chuyển sang ArrayList để có thể add thêm mã mới vừa chèn
			// vào
			List<String> existedMaDHs = new ArrayList<>(dao.getAll().stream().map(DonHangDTO::getMaDH).toList());

			// 2. SỬA LỖI CHÍ MẠNG: Lấy mã lớn nhất hiện tại làm gốc để tự tăng tịnh tiến
			// trong Java
			String maxMaHienTai = dao.generateMaDonHang(); // Trả về mã kế tiếp từ DB (Ví dụ: "DH0202")
			int suffixNumber = 1;
			if (maxMaHienTai != null && !maxMaHienTai.trim().isEmpty()) {
				String chiLaySo = maxMaHienTai.replaceAll("[^0-9]", "");
				if (!chiLaySo.isEmpty()) {
					try {
						// Vì generateMaDonHang() đã tự +1 rồi, nên suffixNumber ở đây chính là số của
						// mã mới
						suffixNumber = Integer.parseInt(chiLaySo);
					} catch (NumberFormatException e) {
						suffixNumber = 1;
					}
				}
			}

			while ((line = reader.readLine()) != null) {
				try {
					String[] values = parseCSVLine(line);
					if (values.length != headers.length) {
						logs.add("Dòng " + lineNumber + ": Số cột không khớp (Yêu cầu " + headers.length + " cột)");
						errorCount++;
						lineNumber++;
						continue;
					}

					String maDH = values[0].replace("\"", "").trim();
					String maKH = values[1].replace("\"", "").trim();
					String ngayLapStr = values[2].replace("\"", "").trim();

					if (ngayLapStr.contains(" ") && !ngayLapStr.contains("T")) {
						ngayLapStr = ngayLapStr.replace(" ", "T");
					}
					if (ngayLapStr.length() == 10) {
						ngayLapStr += "T00:00:00";
					}
					LocalDateTime ngayLap = LocalDateTime.parse(ngayLapStr);
					BigDecimal tongTien = new BigDecimal(values[3].replace("\"", "").trim());

					if (maDH.isEmpty()) {
						maDH = String.format("DH%03d", suffixNumber);
						suffixNumber++; // Tăng số thứ tự ngay lập tức cho dòng tiếp theo
						logs.add("Dòng " + lineNumber + ": Sinh mã tự động -> " + maDH);
					}

					DonHangDTO dh = new DonHangDTO(maDH, maKH, ngayLap, tongTien);

					// Kiểm tra nhanh trong danh sách bộ nhớ tạm
					boolean isExisted = existedMaDHs.contains(maDH);

					if (isExisted) {
						if (dao.update(dh)) {
							logs.add("Dòng " + lineNumber + ": Mã đơn hàng '" + maDH
									+ "' bị trùng -> Đã cập nhật lại thông tin");
							successCount++;
						} else {
							logs.add("Dòng " + lineNumber + ": Cập nhật đơn hàng '" + maDH + "' thất bại");
							errorCount++;
						}
					} else {
						if (dao.insert(dh)) {
							logs.add("Dòng " + lineNumber + ": Tạo đơn hàng '" + maDH + "' thành công");
							successCount++;

							existedMaDHs.add(maDH);
						} else {
							logs.add("Dòng " + lineNumber + ": Thêm đơn hàng '" + maDH
									+ "' thất bại (Lỗi ràng buộc khóa ngoại MaKH)");
							errorCount++;
						}
					}
				} catch (java.time.format.DateTimeParseException e) {
					logs.add("Dòng " + lineNumber + ": Lỗi định dạng ngày tháng (Yêu cầu: YYYY-MM-DDTHH:MM:SS) - "
							+ e.getMessage());
					errorCount++;
				} catch (NumberFormatException e) {
					logs.add("Dòng " + lineNumber + ": Lỗi định dạng số tiền - " + e.getMessage());
					errorCount++;
				} catch (Exception e) {
					logs.add("Dòng " + lineNumber + ": Lỗi hệ thống - " + e.getMessage());
					errorCount++;
				}
				lineNumber++;
			}
			logs.add("\nKẾT QUẢ ĐƠN HÀNG: " + successCount + " thành công, " + errorCount + " lỗi");
		} catch (IOException e) {
			logs.add("Lỗi đọc file: " + e.getMessage());
		}
		return logs;
	}

	// Load csv cho chi tiet don hang
	public static List<String> loadChiTietDonHangFromCSV(String filePath) {
		List<String> logs = new ArrayList<>();
		ChiTietDonHangDAO dao = new ChiTietDonHangDAO();
		int successCount = 0;
		int errorCount = 0;

		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(Files.newInputStream(Paths.get(filePath)), StandardCharsets.UTF_8))) {

			String headerLine = reader.readLine();
			if (headerLine == null)
				return logs;
			String[] headers = parseCSVLine(headerLine);
			String line;
			int lineNumber = 2;

			while ((line = reader.readLine()) != null) {
				try {
					String[] values = parseCSVLine(line);
					if (values.length != headers.length) {
						logs.add("Dòng " + lineNumber + ": Số cột không khớp");
						errorCount++;
						lineNumber++;
						continue;
					}

					String maDH = values[0].replace("\"", "").trim();
					String maVL = values[1].replace("\"", "").trim();
					int soLuong = Integer.parseInt(values[2].trim());
					BigDecimal donGia = new BigDecimal(values[3].replace("\"", "").trim());

					if (maDH.isEmpty() || maVL.isEmpty() || soLuong <= 0) {
						logs.add("Dòng " + lineNumber + ": Dữ liệu hàng hóa không hợp lệ");
						errorCount++;
						lineNumber++;
						continue;
					}

					ChiTietDonHangDTO ctdh = new ChiTietDonHangDTO(maDH, maVL, soLuong, donGia);

					// Kiểm tra xem sản phẩm này đã được thêm vào đơn hàng này chưa
					boolean isExisted = dao.checkExisted(maDH, maVL);

					if (isExisted) {
						if (dao.update(ctdh)) {
							logs.add("Dòng " + lineNumber + ": Mặt hàng '" + maVL + "' đã có trong đơn '" + maDH
									+ "' -> Đã ghi đè số lượng mới");
							successCount++;
						} else {
							errorCount++;
						}
					} else {
						if (dao.insert(ctdh)) {
							logs.add("Dòng " + lineNumber + ": Thêm mặt hàng '" + maVL + "' vào đơn '" + maDH
									+ "' thành công");
							successCount++;
						} else {
							errorCount++;
						}
					}
				} catch (Exception e) {
					logs.add("Dòng " + lineNumber + ": Lỗi - " + e.getMessage());
					errorCount++;
				}
				lineNumber++;
			}
			logs.add("\nKẾT QUẢ CHI TIẾT ĐƠN HÀNG: " + successCount + " thành công, " + errorCount + " lỗi");
		} catch (IOException e) {
			logs.add("Lỗi đọc file: " + e.getMessage());
		}
		return logs;
	}

	// xu ly tach parse
	private static String[] parseCSVLine(String line) {
		List<String> values = new ArrayList<>();
		StringBuilder current = new StringBuilder();
		boolean insideQuotes = false;

		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);

			if (c == '"') {
				insideQuotes = !insideQuotes;
			} else if (c == ',' && !insideQuotes) {
				values.add(current.toString());
				current = new StringBuilder();
			} else {
				current.append(c);
			}
		}
		values.add(current.toString());

		return values.toArray(new String[0]);
	}

}
