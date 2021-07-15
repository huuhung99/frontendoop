package nguyenhuuhung.b17dccn298.controller;

import nguyenhuuhung.b17dccn298.model.KhachHang;
import nguyenhuuhung.b17dccn298.model.response.ResponseBodyDto;
import nguyenhuuhung.b17dccn298.model.response.ResponseCodeEnum;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;

@Controller
public class KhachHangController {
    private String url="http://localhost:8080/khachhang";
    RestTemplate rest = new RestTemplate();
    @GetMapping
    public String home(Model model){
        ResponseBodyDto<List<KhachHang>> response = (ResponseBodyDto<List<KhachHang>>)rest.getForObject(url, ResponseBodyDto.class);
        if(response.getCode().equals(ResponseCodeEnum.R_200)){
            model.addAttribute("khachHangs",response.getItems());
            return "quanLyKhachHang";
        }
        return ResponseCodeEnum.R_404.name();
    }

    @GetMapping("/them")
    private String KhachHangForm(Model model) {
        model.addAttribute("kh", new KhachHang());
        return "khachHangForm";
    }

    @PostMapping("/them")
    public String addNew(@ModelAttribute("kh") KhachHang kh, Model model){
        ResponseBodyDto<KhachHang> response = rest.postForObject(url, kh, ResponseBodyDto.class);
        if(response.getCode().equals(ResponseCodeEnum.R_201)){
            model.addAttribute("message","Thêm khách hàng thành công");
            return "showMessage";
        }
        model.addAttribute("message","Thất bại:"+response.getMessage());
        return "showMessage";
    }

    @GetMapping("/sua/{id}")
    private String suaKhachHangForm(Model model, @PathVariable("id") Long id) {
        ResponseBodyDto<List<KhachHang>> response = (ResponseBodyDto<List<KhachHang>>)rest.getForObject(url+"/{id}", ResponseBodyDto.class, id);
        if(response.getCode().equals(ResponseCodeEnum.R_200)){
//            model.addAttribute("kh", response.getItem());
            LinkedHashMap tmp = (LinkedHashMap)response.getItems().get(0);
            KhachHang khachHang=new KhachHang();
            if(tmp.get("maKhachHang")!=null){
                khachHang.setMaKhachHang(Long.valueOf(String.valueOf(tmp.get("maKhachHang"))));
            }
            if(tmp.get("hoTen")!=null){
                khachHang.setHoTen(String.valueOf(tmp.get("hoTen")));
            }
            if(tmp.get("diaChi")!=null){
                khachHang.setDiaChi(String.valueOf(tmp.get("diaChi")));
            }
            if(tmp.get("nhomKhachHang")!=null){
                khachHang.setNhomKhachHang(String.valueOf(tmp.get("nhomKhachHang")));
            }
            model.addAttribute("kh", khachHang);
            return "khachHangForm";
        }
        model.addAttribute("message",response.getMessage());
        return "showMessage";
    }

    @PostMapping("/sua/{id}")
    public String sua(@ModelAttribute("kh") KhachHang kh, Model model,@PathVariable Long id){
        kh.setMaKhachHang(id);
        ResponseBodyDto<KhachHang> response = rest.postForObject(url, kh, ResponseBodyDto.class);
        if(response.getCode().equals(ResponseCodeEnum.R_201)){
            model.addAttribute("message","Sửa khách hàng thành công");
            return "showMessage";
        }
        model.addAttribute("message","Thất bại:"+response.getMessage());
        return "showMessage";
    }
    @GetMapping("/xoa/{id}")
    private String xoaKhachHang(@PathVariable("id") Long id,Model model) {
        rest.delete("http://localhost:8080/khachhang/{id}", id);
        model.addAttribute("message","Xóa thành công!");
        return "showMessage";
    }
    @GetMapping("/tim")
    private String timKhachHang(Model model, @RequestParam("keyword") String keyword) {
        if(keyword!=null&&!keyword.isBlank()){
            ResponseBodyDto<List<KhachHang>> response = rest.getForObject("http://localhost:8080/khachhang/search/{keyword}", ResponseBodyDto.class, keyword);
            if(response.getCode().equals(ResponseCodeEnum.R_200)){
                model.addAttribute("khachHangs",response.getItems());
                return "quanLyKhachHang";
            }
            else{
                model.addAttribute("message",response.getMessage());
                return "showMessage";
            }
        }
        return "redirect:/";
    }
}
