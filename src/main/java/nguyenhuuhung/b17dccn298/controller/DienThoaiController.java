package nguyenhuuhung.b17dccn298.controller;

import nguyenhuuhung.b17dccn298.model.DienThoai;
import nguyenhuuhung.b17dccn298.model.DienThoai;
import nguyenhuuhung.b17dccn298.model.response.ResponseBodyDto;
import nguyenhuuhung.b17dccn298.model.response.ResponseCodeEnum;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;

@Controller
@RequestMapping("/dienthoai")
public class DienThoaiController {
    private String url="http://localhost:8080/dienthoai";
    RestTemplate rest = new RestTemplate();
    @GetMapping
    public String home(Model model){
        ResponseBodyDto<List<DienThoai>> response = (ResponseBodyDto<List<DienThoai>>)rest.getForObject(url, ResponseBodyDto.class);
        if(response.getCode().equals(ResponseCodeEnum.R_200)){
            model.addAttribute("dienThoais",response.getItems());
            return "quanLyDienThoai";
        }
        return ResponseCodeEnum.R_404.name();
    }

    @GetMapping("/them")
    private String DienThoaiForm(Model model) {
        model.addAttribute("dt", new DienThoai());
        return "dienThoaiForm";
    }

    @PostMapping("/them")
    public String addNew(@ModelAttribute("dt") DienThoai kh, Model model){
        ResponseBodyDto<DienThoai> response = rest.postForObject(url, kh, ResponseBodyDto.class);
        if(response.getCode().equals(ResponseCodeEnum.R_201)){
            model.addAttribute("message","Thêm điện thoại thành công");
            return "showMessage";
        }
        model.addAttribute("message","Thất bại:"+response.getMessage());
        return "showMessage";
    }

    @GetMapping("/sua/{id}")
    private String suaDienThoaiForm(Model model, @PathVariable("id") Long id) {
        ResponseBodyDto<List<DienThoai>> response = (ResponseBodyDto<List<DienThoai>>)rest.getForObject(url+"/{id}", ResponseBodyDto.class, id);
        if(response.getCode().equals(ResponseCodeEnum.R_200)){
            LinkedHashMap tmp = (LinkedHashMap)response.getItems().get(0);
            DienThoai dienThoai=new DienThoai();
            if(tmp.get("maDienThoai")!=null){
                dienThoai.setMaDienThoai(Long.valueOf(String.valueOf(tmp.get("maDienThoai"))));
            }
            if(tmp.get("tenDienThoai")!=null){
                dienThoai.setTenDienThoai(String.valueOf(tmp.get("tenDienThoai")));
            }
            if(tmp.get("hangSanXuat")!=null){
                dienThoai.setHangSanXuat(String.valueOf(tmp.get("hangSanXuat")));
            }
            if(tmp.get("model")!=null){
                dienThoai.setModel(String.valueOf(tmp.get("model")));
            }
            if(tmp.get("donGia")!=null){
                dienThoai.setDonGia(Float.valueOf(String.valueOf(tmp.get("donGia"))));
            }
            model.addAttribute("dt", dienThoai);
            return "DienThoaiForm";
        }
        model.addAttribute("message",response.getMessage());
        return "showMessage";
    }

    @PostMapping("/sua/{id}")
    public String sua(@ModelAttribute("kh") DienThoai kh, Model model,@PathVariable Long id){
        kh.setMaDienThoai(id);
        ResponseBodyDto<DienThoai> response = rest.postForObject(url, kh, ResponseBodyDto.class);
        if(response.getCode().equals(ResponseCodeEnum.R_201)){
            model.addAttribute("message","Sửa điện thoại thành công");
            return "showMessage";
        }
        model.addAttribute("message","Thất bại:"+response.getMessage());
        return "showMessage";
    }
    @GetMapping("/xoa/{id}")
    private String xoaDienThoai(@PathVariable("id") Long id,Model model) {
        rest.delete("http://localhost:8080/dienthoai/{id}", id);
        model.addAttribute("message","Xóa thành công!");
        return "showMessage";
    }
    @GetMapping("/tim")
    private String timDienThoai(Model model, @RequestParam("keyword") String keyword) {
        if(keyword!=null&&!keyword.isBlank()){
            ResponseBodyDto<List<DienThoai>> response = rest.getForObject(url+"/search/{keyword}", ResponseBodyDto.class, keyword);
            if(response.getCode().equals(ResponseCodeEnum.R_200)){
                model.addAttribute("dienThoais",response.getItems());
                return "quanLyDienThoai";
            }
            else{
                model.addAttribute("message",response.getMessage());
                return "showMessage";
            }
        }
        return "redirect:/dienthoai";
    }
}
