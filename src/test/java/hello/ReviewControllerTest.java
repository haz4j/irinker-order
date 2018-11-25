package hello;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.List;

import static hello.ReviewController.approved;
import static hello.ReviewController.deleted;
import static hello.ReviewController.waiting_for_approval;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ReviewControllerTest {

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private ReviewRepository repository;

    @Test
    public void createReview() {
        String feedback = template.postForEntity("/review/",
                Review.builder()
                        .productId(1)
                        .reviewId(1)
                        .userId(1)
                        .text("text")
                        .build(),
                FeedBack.class)
                .getBody().getMessage();
        Assert.assertThat(feedback, is(waiting_for_approval));
        Assert.assertThat(repository.findByProductId(1).size(), is(1));

        List<Review> review = template.exchange(
                "/review/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Review>>() {
        }
        ).getBody();
        Assert.assertThat(review.size(), is(1));

        feedback = template.postForEntity("/review/approve",
                ReviewShort.builder()
                        .reviewId(1)
                        .build(),
                FeedBack.class)
                .getBody().getMessage();
        Assert.assertThat(feedback, Matchers.is(approved));
        Assert.assertThat(repository.findByProductId(1).size(), Matchers.is(1));

        template.delete("/review/1");
        Assert.assertThat(repository.findByProductId(1).size(), Matchers.is(0));
    }
}
