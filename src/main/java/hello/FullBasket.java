package hello;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FullBasket {

    private List<Order> basket;

    @JsonProperty("full_price")
    private long fullPrice;
}
