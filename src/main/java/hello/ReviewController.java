package hello;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@AllArgsConstructor
@Transactional
public class ReviewController {

    private ReviewRepository repository;
    public static String waiting_for_approval = "Your review is waiting for moderator approval";
    public static String approved = "Your review has been approved";
    public static String deleted = "Your review has been deleted";

    @CrossOrigin
    @GetMapping("/review/{productId}")
    public List<Review> getReview(@PathVariable long productId) {
        return repository.findByProductId(productId);
    }

    @CrossOrigin
    @PostMapping("/review/")
    public Object addReview(@RequestBody Review review) {
        repository.save(review);
        return new FeedBack(waiting_for_approval);

    }

    @CrossOrigin
    @PostMapping("/review/approve")
    public FeedBack approve(@RequestBody Review review) {
        return new FeedBack(approved);
    }

    @CrossOrigin
    @DeleteMapping("/review/{reviewId}")
    public FeedBack delete(@PathVariable long reviewId) {
        repository.deleteById(reviewId);
        return new FeedBack(deleted);
    }
}
