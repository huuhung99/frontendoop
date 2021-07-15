package nguyenhuuhung.b17dccn298.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DienThoai{
    private Long maDienThoai;
    private String tenDienThoai;
    private String hangSanXuat;
    private String model;
    private Float donGia;
    private Item item;
}
