package nguyenhuuhung.b17dccn298.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseBodyDto<E> {
    private ResponseCodeEnum code;
    private String message;

    @JsonProperty("total_items")
    private long totalItems;
    private int page;
    private int size;
    private E item;
    private List<E> items;

    public ResponseBodyDto(E item,ResponseCodeEnum code,String message){
        this.code=code;
        this.message=message;
        this.item=item;
    }

    public ResponseBodyDto(List<E> items,ResponseCodeEnum code,String message,int totalItems){
        this.code=code;
        this.message=message;
        this.items=items;
        this.totalItems =totalItems ;
    }

    public ResponseBodyDto(ResponseCodeEnum code,String message){
        this.code=code;
        this.message=message;
    }


    public ResponseBodyDto(ResultPage<E> page, ResponseCodeEnum code, String message) {
        this.code = code;
        this.message = message;
        this.totalItems = page.getTotalItems();
        this.items = page.getPageList();
    }
}
