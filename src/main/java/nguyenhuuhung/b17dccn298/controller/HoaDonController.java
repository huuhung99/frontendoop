package nguyenhuuhung.b17dccn298.controller;

import nguyenhuuhung.b17dccn298.model.DienThoai;
import nguyenhuuhung.b17dccn298.model.HoaDon;
import nguyenhuuhung.b17dccn298.model.Item;
import nguyenhuuhung.b17dccn298.model.KhachHang;
import nguyenhuuhung.b17dccn298.model.response.ResponseBodyDto;
import nguyenhuuhung.b17dccn298.model.response.ResponseCodeEnum;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/hoadon")
public class HoaDonController {
    private String url="http://localhost:8080/hoadon";
    RestTemplate rest= new RestTemplate();
    public HoaDon hoaDon=new HoaDon();
    public List<Item> items=new ArrayList<>();
    public List<DienThoai> dienThoais=new ArrayList<>();
    public List<HoaDon> hoaDons=new ArrayList<>();
    @GetMapping
    public String getAll(Model model){
        hoaDons.clear();
        ResponseBodyDto<List<HoaDon>> response = (ResponseBodyDto<List<HoaDon>>)rest.getForObject(url, ResponseBodyDto.class);
        initHoaDon(response);
        if(response.getCode().equals(ResponseCodeEnum.R_200)){
            model.addAttribute("hoaDons",response.getItems());
            return "quanlyHoaDon";
        }
        return ResponseCodeEnum.R_404.name();
    }

    private void initHoaDon(ResponseBodyDto<List<HoaDon>> response) {
        if (response.getCode().equals(ResponseCodeEnum.R_200)) {
            List<List<HoaDon>> items = response.getItems();
            for(int i=0;i<items.size();i++){
                LinkedHashMap tmp=(LinkedHashMap)items.get(i);
                HoaDon hoaDon = new HoaDon();
                if (tmp.get("maHoaDon") != null) {
                    hoaDon.setMaHoaDon(Long.valueOf(String.valueOf(tmp.get("maHoaDon"))));
                }
                if (tmp.get("thoiGianTaoHoaDon") != null) {
                    try {
                        Date date=new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(tmp.get("thoiGianTaoHoaDon")));
                        hoaDon.setThoiGianTaoHoaDon(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (tmp.get("khachHang") != null) {
                    LinkedHashMap tmp1 = (LinkedHashMap) tmp.get("khachHang");
                    KhachHang khachHang = new KhachHang();
                    if (tmp1.get("maKhachHang") != null) {
                        khachHang.setMaKhachHang(Long.valueOf(String.valueOf(tmp1.get("maKhachHang"))));
                    }
                    if (tmp1.get("hoTen") != null) {
                        khachHang.setHoTen(String.valueOf(tmp1.get("hoTen")));
                    }
                    if (tmp1.get("diaChi") != null) {
                        khachHang.setDiaChi(String.valueOf(tmp1.get("diaChi")));
                    }
                    if (tmp1.get("nhomKhachHang") != null) {
                        khachHang.setNhomKhachHang(String.valueOf(tmp1.get("nhomKhachHang")));
                    }
                    hoaDon.setKhachHang(khachHang);
                }
                if (tmp.get("item") != null) {
                    List<Item> items1=new ArrayList<>();
                    List<LinkedHashMap> itemResponse=(List<LinkedHashMap>)tmp.get("item");
                    for(int j=0;j<itemResponse.size();j++){
                        Item item=new Item();
                        LinkedHashMap linkedHashMap = itemResponse.get(j);
                        if(linkedHashMap.get("id")!=null){
                            item.setId(Long.valueOf(String.valueOf(linkedHashMap.get("id"))));
                        }
                        if(linkedHashMap.get("giaBan")!=null){
                            item.setGiaBan(Float.valueOf(String.valueOf(linkedHashMap.get("giaBan"))));
                        }
                        if(linkedHashMap.get("soLuong")!=null){
                            item.setSoLuong(Integer.valueOf(String.valueOf(linkedHashMap.get("soLuong"))));
                        }
                        if(linkedHashMap.get("dienThoai")!=null){
                            DienThoai dienThoai=new DienThoai();
                            LinkedHashMap dienThoaiTmp=(LinkedHashMap)linkedHashMap.get("dienThoai");
                            if(dienThoaiTmp.get("id")!=null){
                                dienThoai.setMaDienThoai(Long.valueOf(String.valueOf(tmp.get("id"))));
                            }
                            if(dienThoaiTmp.get("tenDienThoai")!=null){
                                dienThoai.setTenDienThoai(String.valueOf(dienThoaiTmp.get("tenDienThoai")));
                            }
                            if(dienThoaiTmp.get("hangSanXuat")!=null){
                                dienThoai.setHangSanXuat(String.valueOf(dienThoaiTmp.get("hangSanXuat")));
                            }
                            if(dienThoaiTmp.get("model")!=null){
                                dienThoai.setModel(String.valueOf(dienThoaiTmp.get("model")));
                            }
                            if(dienThoaiTmp.get("donGia")!=null){
                                dienThoai.setDonGia(Float.valueOf(String.valueOf(dienThoaiTmp.get("donGia"))));
                            }
                            item.setDienThoai(dienThoai);
                        }
                        items1.add(item);
                    }
                    hoaDon.setItem(items1);
                }
                this.hoaDons.add(hoaDon);
            }

        }
    }

    @GetMapping("/byname")
    public String getAllByName(Model model){
        List<HoaDon> tmp=new ArrayList<>(hoaDons);
        Collections.sort(tmp, new Comparator<HoaDon>() {
            @Override
            public int compare(HoaDon a, HoaDon b) {
                return a.getKhachHang().getHoTen().compareTo(b.getKhachHang().getHoTen());
            }
        });
            model.addAttribute("hoaDons",tmp);
            return "quanlyHoaDon";
    }
    @GetMapping("/byitem")
    public String getAllByItem(Model model){
        List<HoaDon> tmp=new ArrayList<>(hoaDons);
        Collections.sort(tmp, new Comparator<HoaDon>() {
            @Override
            public int compare(HoaDon a, HoaDon b) {
                int soDienThoaiA=0;
                List<Item> item = a.getItem();
                for (int i=0;i<item.size();i++){
                    soDienThoaiA+=item.get(i).getSoLuong();
                }
                int soDienThoaiB=0;
                List<Item> itemb = b.getItem();
                for (int i=0;i<itemb.size();i++){
                    soDienThoaiB+=itemb.get(i).getSoLuong();
                }
                return soDienThoaiA<=soDienThoaiB?1:0;
            }
        });
            model.addAttribute("hoaDons",tmp);
            return "quanlyHoaDon";
    }
    @GetMapping("/{id}")
    public String taoHoaDon(Model model, @PathVariable Long id){
        hoaDon=new HoaDon();
        items.clear();
        dienThoais.clear();
        init(id);
        model.addAttribute("hd",hoaDon);
        model.addAttribute("dienThoais",this.dienThoais);
        model.addAttribute("dt",new DienThoai());
        return "taoHoaDon";
    }
    @PostMapping("/{id}")
    public String themItem(DienThoai dienThoai,Model model, @PathVariable Long id){
        for(int i=0;i<dienThoais.size();i++){
            if(dienThoais.get(i).getMaDienThoai().equals(dienThoai.getMaDienThoai())){
                dienThoai.setTenDienThoai(dienThoais.get(i).getTenDienThoai());
                dienThoai.setModel(dienThoais.get(i).getModel());
                dienThoai.setHangSanXuat(dienThoais.get(i).getHangSanXuat());
                dienThoai.setDonGia(dienThoais.get(i).getDonGia());
                break;
            }
        }
        this.addItem(dienThoai);
        model.addAttribute("hd",hoaDon);
        model.addAttribute("dienThoais",this.dienThoais);
        model.addAttribute("dt",new DienThoai());
        return "taoHoaDon";
    }

    private void addItem(DienThoai dienThoai) {
        boolean check=true;
        Item item=null;
        for(int i=0;i<items.size();i++){
            if(dienThoai.getMaDienThoai().equals(items.get(i).getId())){
                items.get(i).setSoLuong(items.get(i).getSoLuong()+1);
                check=false;
                break;
            }
        }
        if(check==true){
            item=new Item();
            item.setDienThoai(dienThoai);
            item.setId(dienThoai.getMaDienThoai());
            item.setGiaBan(dienThoai.getDonGia());
            item.setSoLuong(1);
            items.add(item);
        }
        hoaDon.setItem(items);
    }

    public void init(Long khachHangId) {
        ResponseBodyDto<List<KhachHang>> response = (ResponseBodyDto<List<KhachHang>>) rest.getForObject("http://localhost:8080/khachhang/" + khachHangId, ResponseBodyDto.class);
        if (response.getCode().equals(ResponseCodeEnum.R_200)) {
            LinkedHashMap tmp = (LinkedHashMap) response.getItems().get(0);
            KhachHang khachHang = new KhachHang();
            if (tmp.get("maKhachHang") != null) {
                khachHang.setMaKhachHang(Long.valueOf(String.valueOf(tmp.get("maKhachHang"))));
            }
            if (tmp.get("hoTen") != null) {
                khachHang.setHoTen(String.valueOf(tmp.get("hoTen")));
            }
            if (tmp.get("diaChi") != null) {
                khachHang.setDiaChi(String.valueOf(tmp.get("diaChi")));
            }
            if (tmp.get("nhomKhachHang") != null) {
                khachHang.setNhomKhachHang(String.valueOf(tmp.get("nhomKhachHang")));
            }
            hoaDon.setKhachHang(khachHang);
        }
        ResponseBodyDto<List<DienThoai>> responseDt = (ResponseBodyDto<List<DienThoai>>)rest.getForObject("http://localhost:8080/dienthoai", ResponseBodyDto.class);
        if(responseDt.getCode().equals(ResponseCodeEnum.R_200)){
            for(int i=0;i<responseDt.getItems().size();i++){
                LinkedHashMap tmp=(LinkedHashMap) responseDt.getItems().get(i);
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
                this.dienThoais.add(dienThoai);
            }
        }
    }
    @PostMapping("/them")
    public String addNew(@ModelAttribute("hd") HoaDon hd, Model model){
        hoaDon.setThoiGianTaoHoaDon(Date.from(Instant.now()));
        if(hoaDon.getItem().isEmpty()){
            model.addAttribute("message","Thất bại: hóa đơn không có điện thoại nào");
            return "showMessage";
        }
        ResponseBodyDto<HoaDon> response = rest.postForObject(url, hoaDon, ResponseBodyDto.class);
        if(response.getCode().equals(ResponseCodeEnum.R_201)){
            model.addAttribute("message","Thêm hóa đơn thành công");
            return "showMessage";
        }
        model.addAttribute("message","Thất bại:"+response.getMessage());
        return "showMessage";
    }
    @GetMapping("/thongke/{id}")
    public String thongke(Model model,@PathVariable Long id){
        hoaDons.clear();
        ResponseBodyDto<List<HoaDon>> responseHoaDon = (ResponseBodyDto<List<HoaDon>>)rest.getForObject(url, ResponseBodyDto.class);
        initHoaDon(responseHoaDon);
        KhachHang khachHang=new KhachHang();
        ResponseBodyDto<List<KhachHang>> response = (ResponseBodyDto<List<KhachHang>>)rest.getForObject("http://localhost:8080/khachhang/"+"/{id}", ResponseBodyDto.class, id);
        if(response.getCode().equals(ResponseCodeEnum.R_200)){
            LinkedHashMap tmp = (LinkedHashMap)response.getItems().get(0);
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
        }
        List<HoaDon> tmp=new ArrayList<>(this.hoaDons);
        tmp=tmp.stream().filter(a->a.getKhachHang().getMaKhachHang().equals(khachHang.getMaKhachHang()))
                .collect(Collectors.toList());
        model.addAttribute("hoaDons",tmp);
        Float tongTien=0f;
        Integer soLuong=0;
        for(int i=0;i<tmp.size();i++){
            for(int j=0;j<tmp.get(i).getItem().size();j++){
                tongTien+=tmp.get(i).getItem().get(j).getGiaBan()*tmp.get(i).getItem().get(j).getSoLuong();
                soLuong+=tmp.get(i).getItem().get(j).getSoLuong();
            }
        }
        model.addAttribute("tongTien",tongTien);
        model.addAttribute("soLuong",soLuong);
        return "thongke";
    }
//    @GetMapping("/tim")
//    private String timKhachHang(Model model, @RequestParam("keyword") String keyword) {
//        if(keyword!=null&&!keyword.isBlank()){
//            ResponseBodyDto<List<HoaDon>> response = rest.getForObject("http://localhost:8080/hoadon/search/{keyword}", ResponseBodyDto.class, keyword);
//            if(response.getCode().equals(ResponseCodeEnum.R_200)){
//                model.addAttribute("hoadons",response.getItems());
//                return "quanlyHoaDon";
//            }
//            else{
//                model.addAttribute("message",response.getMessage());
//                return "showMessage";
//            }
//        }
//        return "redirect:/hoadon";
//    }
}
