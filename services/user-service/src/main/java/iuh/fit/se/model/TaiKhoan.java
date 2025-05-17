package iuh.fit.se.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Deprecated
public class TaiKhoan {
    private String cauHoi;
    private String traLoi;
    private String tenDangNhap;
    private String matKhau;
}