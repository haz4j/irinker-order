package hello;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class OrderControllerTest {

    @Autowired
    private TestRestTemplate template;

    @Test
    public void createOrder(){
        ResponseEntity<String> stringResponseEntity = template.postForEntity("/basket/1", Order.builder().productId(1).quantity(1).price(1).build(), String.class);
        System.out.println(stringResponseEntity.getBody());
    }

}
