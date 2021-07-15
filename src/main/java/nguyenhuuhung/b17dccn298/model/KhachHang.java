package nguyenhuuhung.b17dccn298.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhachHang{
    private Long maKhachHang;
    private String hoTen;
    private String diaChi;
    private String nhomKhachHang;
}
