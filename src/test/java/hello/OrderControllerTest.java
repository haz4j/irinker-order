package hello;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class OrderControllerTest {

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private OrderRepository repository;

    @Before
    public void setUp() {
        repository.deleteAll();
        template.postForEntity("/basket/1",
                Order.builder()
                        .productId(1)
                        .quantity(2)
                        .price(1)
                        .build(),
                FullBasket.class);
    }

    @Test
    public void createOrder() {
        Long total = template.postForEntity("/basket/2",
                Order.builder()
                        .productId(1)
                        .quantity(1)
                        .price(1)
                        .build(),
                FullBasket.class)
                .getBody().getFullPrice();
        Assert.assertThat(total, Matchers.is(1L));
        Assert.assertThat(repository.findAllByUserId(2).size(), Matchers.is(1));
    }

    @Test
    public void getOrder() {
        FullBasket fullBasket = template.getForEntity("/basket/1", FullBasket.class).getBody();
        Assert.assertThat(fullBasket.getBasket().size(), Matchers.is(1));
        Assert.assertThat(fullBasket.getFullPrice(), Matchers.is(2L));
    }

    @Test
    public void addTheSameOrder() {
        Long total = template.postForEntity("/basket/1",
                Order.builder()
                        .productId(1)
                        .quantity(2)
                        .price(2)
                        .build(),
                FullBasket.class)
                .getBody().getFullPrice();

        Assert.assertThat(total, Matchers.is(4L));

        FullBasket fullBasket = template.getForEntity("/basket/1", FullBasket.class).getBody();
        Assert.assertThat(fullBasket.getBasket().size(), Matchers.is(1));
        Assert.assertThat(fullBasket.getFullPrice(), Matchers.is(4L));
    }

    @Test
    public void addAnotherOrder() {
        Long total = template.postForEntity("/basket/1",
                Order.builder()
                        .productId(2)
                        .quantity(3)
                        .price(3)
                        .build(),
                FullBasket.class)
                .getBody().getFullPrice();

        Assert.assertThat(total, Matchers.is(11L));

        FullBasket fullBasket = template.getForEntity("/basket/1", FullBasket.class).getBody();
        Assert.assertThat(fullBasket.getBasket().size(), Matchers.is(2));
        Assert.assertThat(fullBasket.getFullPrice(), Matchers.is(11L));
    }

    @Test
    public void deleteOrder() {
        Long total = template.postForEntity("/basket/1/delete",
                Order.builder()
                        .productId(1)
                        .quantity(1)
                        .build(),
                FullBasket.class)
                .getBody().getFullPrice();

        Assert.assertThat(total, Matchers.is(1L));

        FullBasket fullBasket = template.getForEntity("/basket/1", FullBasket.class).getBody();
        Assert.assertThat(fullBasket.getBasket().size(), Matchers.is(1));
        Assert.assertThat(fullBasket.getFullPrice(), Matchers.is(1L));
    }
}
