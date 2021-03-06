package hello;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@AllArgsConstructor
@Transactional
public class OrderController {

    private OrderRepository repository;

    @CrossOrigin
    @GetMapping("/basket/{userId}")
    public FullBasket getBasket(@PathVariable long userId) {
        List<Order> orders = repository.findAllByUserId(userId);
        long fullPrice = countFullPrice(orders);

        return FullBasket.builder().basket(orders).fullPrice(fullPrice).build();
    }

    @CrossOrigin
    @PostMapping("/basket/{userId}")
    public FullBasket addToBasket(@PathVariable long userId, @RequestBody Order order) {
        order.setUserId(userId);
        Order totalOrder = repository
                .findByProductIdAndUserId(order.getProductId(), userId)
                .map(foundOrder -> {
                            foundOrder.setQuantity(foundOrder.getQuantity() + order.getQuantity());
                            return foundOrder;
                        }
                ).orElse(order);

        repository.save(totalOrder);

        return getBasket(userId);
    }

    @CrossOrigin
    @PostMapping("/basket/{userId}/delete")
    public FullBasket deleteFromBasket(@PathVariable long userId, @RequestBody Order order) {
        Order foundOrder = repository
                .findByProductIdAndUserId(order.getProductId(), userId)
                .orElseThrow(() -> new RuntimeException("Can't find order with productId " + order.getProductId() + " and userId = " + userId));

        if ((foundOrder.getQuantity() > order.getQuantity())) {
            foundOrder.setQuantity(foundOrder.getQuantity() - order.getQuantity());
        } else {
            repository.delete(foundOrder);
        }

        return getBasket(userId);
    }

    private long countFullPrice(List<Order> orders) {
        long fullPrice = 0L;
        for (Order o : orders) {
            fullPrice += o.getPrice() * o.getQuantity();
        }
        return fullPrice;
    }
}
