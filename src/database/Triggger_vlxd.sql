USE QuanLyVatLieu;
GO

-- Trigger 1: auto trừ tồn kho khi xuất bán
CREATE TRIGGER trg_XuatBan_CapNhatKho
ON ChiTietDonHang
AFTER INSERT
AS
BEGIN
    UPDATE VatLieu
    SET SoLuongTon = SoLuongTon - i.SoLuong
    FROM VatLieu v
    JOIN inserted i ON v.MaVL = i.MaVL;
END;
GO

-- Trigger 2: auto cộng tồn kho khi nhập hàng từ Nhà Cung Cấp
CREATE TRIGGER trg_NhapHang_CapNhatKho
ON ChiTietNhaCungCap
AFTER INSERT
AS
BEGIN
    UPDATE VatLieu
    SET SoLuongTon = SoLuongTon + i.SoLuong
    FROM VatLieu v
    JOIN inserted i ON v.MaVL = i.MaVL;
END;
GO