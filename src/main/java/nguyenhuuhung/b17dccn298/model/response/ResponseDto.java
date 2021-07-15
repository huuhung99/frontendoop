package nguyenhuuhung.b17dccn298.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
public class ResponseDto<E> {

    private String code;

    private String message;

    @JsonProperty("total_items")
    private long totalItems;

    @JsonProperty("size")
    private int size;

    @JsonProperty("page")
    private int page;

    /**
     * Reset API: Response data of 1 item
     */
    @JsonProperty("item")
    private E item;

    @JsonProperty("items")
    private List<E> items;

}


