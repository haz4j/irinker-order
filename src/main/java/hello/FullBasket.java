package hello;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FullBasket {

    private List<Order> basket;

    private long fullPrice;
}
