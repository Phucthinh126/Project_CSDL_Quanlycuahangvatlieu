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
					String maVL = values[0].trim();
					String tenVL = values[1].trim();
					int soLuong = Integer.parseInt(values[2].trim());
					BigDecimal giaBan = new BigDecimal(values[3].trim());
					String donVi = values[4].trim();

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

	// Load khach hang
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

			while ((line = reader.readLine()) != null) {
				try {
					String[] values = parseCSVLine(line);
					if (values.length != headers.length) {
						logs.add("Dòng " + lineNumber + ": Số cột không khớp");
						errorCount++;
						lineNumber++;
						continue;
					}

					// Đọc dữ liệu và dọn sạch dấu ngoặc kép do Excel bọc ngoài
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

					KhachHangDTO kh = new KhachHangDTO(maKH, tenKH, sdt, diaChi);

					// Kiểm tra xem khách hàng này đã tồn tại qua SĐT chưa
					KhachHangDTO existedKh = dao.findBySdt(sdt);
					if (existedKh != null) {
						kh.setMaKH(existedKh.getMaKH()); // Giữ nguyên mã cũ của họ dưới DB
						if (dao.update(kh)) {
							logs.add("Dòng " + lineNumber + ": Số điện thoại '" + sdt
									+ "' đã tồn tại -> Đã ghi đè thông tin mới");
							successCount++;
						} else {
							logs.add("Dòng " + lineNumber + ": Ghi đè thông tin khách hàng thất bại");
							errorCount++;
						}
					} else {
						if (dao.insert(kh)) {
							logs.add("Dòng " + lineNumber + ": Thêm mới khách hàng '" + tenKH + "' thành công");
							successCount++;
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
					String maKH = values[1].replace("\"", "").trim();
					LocalDateTime ngayLap = LocalDateTime.parse(values[2].replace("\"", "").trim());
					BigDecimal tongTien = new BigDecimal(values[3].replace("\"", "").trim());

					if (maDH.isEmpty()) {
						maDH = dao.generateMaDonHang();
					}

					DonHangDTO dh = new DonHangDTO(maDH, maKH, ngayLap, tongTien);

					// Tìm kiếm xem mã đơn hàng đã tồn tại trong danh sách chưa
					boolean isExisted = dao.getAll().stream().anyMatch(d -> d.getMaDH().equals(dh.getMaDH()));

					if (isExisted) {
						if (dao.update(dh)) {
							logs.add("Dòng " + lineNumber + ": Mã đơn hàng '" + maDH
									+ "' bị trùng -> Đã cập nhật lại thông tin");
							successCount++;
						} else {
							errorCount++;
						}
					} else {
						if (dao.insert(dh)) {
							logs.add("Dòng " + lineNumber + ": Tạo đơn hàng '" + maDH + "' thành công");
							successCount++;
						} else {
							errorCount++;
						}
					}
				} catch (Exception e) {
					logs.add("Dòng " + lineNumber + ": Lỗi định dạng dữ liệu - " + e.getMessage());
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
