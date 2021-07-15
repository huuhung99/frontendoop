package nguyenhuuhung.b17dccn298.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoaDon{
    private Long maHoaDon;
    private Date thoiGianTaoHoaDon;
    private KhachHang khachHang;
    private List<Item> item;
}
