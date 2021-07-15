package nguyenhuuhung.b17dccn298.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item{
    private Long id;
    private Float giaBan;
    private Integer soLuong;
    private DienThoai dienThoai;
    private HoaDon hoaDon;
}
