USE QuanLyVatLieu;
GO

DELETE FROM ChiTietDonHang;
DELETE FROM ChiTietNhaCungCap;
DELETE FROM DonHang;
DELETE FROM VatLieu;
DELETE FROM NhaCungCap;
DELETE FROM KhachHang;
GO

-- Thêm dữ liệu Khách Hàng
INSERT INTO KhachHang (MaKH, TenKH, SoDienThoai, DiaChi) VALUES
('KH01', N'Công ty Xây dựng An Bình', '0901234567', N'123 Quốc lộ 1K, Dĩ An'),
('KH02', N'Nguyễn Văn Toàn', '0912345678', N'45 Võ Văn Ngân, Thủ Đức'),
('KH03', N'Trần Thị Mai', '0923456789', N'88 Tô Ký, Quận 12');

-- Thêm dữ liệu Nhà Cung Cấp
INSERT INTO NhaCungCap (MaNCC, TenNCC, SoDienThoai, DiaChi) VALUES
('NCC01', N'Nhà máy Xi măng Hà Tiên', '0283123456', N'KCN Sóng Thần'),
('NCC02', N'Đại lý Thép Pomina', '0283654321', N'KCN Đồng An');

-- Thêm dữ liệu Vật Liệu (Tồn kho ban đầu)
INSERT INTO VatLieu (MaVL, TenVL, DonViTinh, SoLuongTon, GiaBan) VALUES
('VL01', N'Xi măng Hà Tiên Đa Dụng', N'Bao', 500, 85000),
('VL02', N'Thép cuộn D10 Pomina', N'Kg', 2000, 16500),
('VL03', N'Cát xây tô', N'Khối', 150, 250000);

-- Thêm Đơn Hàng mẫu
INSERT INTO DonHang (MaDH, MaKH, NgayLap, TongTien) VALUES
('DH001', 'KH01', GETDATE(), 0);

--Test trigger
-- Test Trigger 1: Bán 50 bao Xi măng (VL01) -> Kho sẽ bị trừ 50, còn 450
INSERT INTO ChiTietDonHang (MaDH, MaVL, SoLuong, DonGia) VALUES
('DH001', 'VL01', 50, 85000);

-- Test Trigger 2: Nhập thêm 200 bao Xi măng (VL01) từ NCC -> Kho sẽ cộng 200, thành 650
INSERT INTO ChiTietNhaCungCap (MaNCC, MaVL, SoLuong, GiaNhap) VALUES
('NCC01', 'VL01', 200, 75000);
GO