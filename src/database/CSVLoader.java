package database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import dao.VatLieuDAO;
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

					// Kiểm tra số cột
					if (values.length != headers.length) {
						logs.add("Dòng " + lineNumber + ": Số cột không khớp");
						errorCount++;
						lineNumber++;
						continue;
					}

					// Parse dữ liệu
					String maVL = values[0].trim();
					String tenVL = values[1].trim();
					int soLuong = Integer.parseInt(values[2].trim());
					double giaBan = Double.parseDouble(values[3].trim());
					double giaNhap = Double.parseDouble(values[4].trim());
					String donVi = values[5].trim();

					// Sinh mã tự động nếu mã trống
					if (maVL.isEmpty()) {
						maVL = dao.generateMaVatLieu();
						logs.add(" -->Dòng " + lineNumber + ": Sinh mã tự động -> " + maVL);
					}

					// Kiểm tra dữ liệu hợp lệ
					if (tenVL.isEmpty()) {
						logs.add("Dòng " + lineNumber + ": Tên vật liệu không được để trống");
						errorCount++;
						lineNumber++;
						continue;
					}

					if (soLuong < 0 || giaBan < 0 || giaNhap < 0) {
						logs.add("Dòng " + lineNumber + ": Số lượng hoặc giá không được âm");
						errorCount++;
						lineNumber++;
						continue;
					}

					// Insert vào database
					VatLieuDTO vl = new VatLieuDTO(maVL, soLuong, tenVL, giaBan, giaNhap, donVi);
					if (dao.insert(vl)) {
						logs.add("Dòng " + lineNumber + ": Thêm " + tenVL + " thành công");
						successCount++;
					} else {
						logs.add("Dòng " + lineNumber + ": Thêm " + tenVL + " thất bại (có thể trùng mã)");
						errorCount++;
					}

				} catch (NumberFormatException e) {
					logs.add("Dòng " + lineNumber + ": Lỗi format dữ liệu - " + e.getMessage());
					errorCount++;
				} catch (Exception e) {
					logs.add("Dòng " + lineNumber + ": Lỗi - " + e.getMessage());
					errorCount++;
				}
				lineNumber++;
			}

			logs.add("\nKẾT QUẢ: " + successCount + " thành công, " + errorCount + " lỗi");

		} catch (IOException e) {
			logs.add("Lỗi đọc file: " + e.getMessage());
		}

		return logs;
	}

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
