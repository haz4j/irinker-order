package hello;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findAllByUserId(long userId);

    Optional<Order> findByProductIdAndUserId(long productId, long userId);
}
