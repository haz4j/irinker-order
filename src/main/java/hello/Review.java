package hello;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @JsonProperty("id_review")
    private long reviewId;
    @JsonProperty("id_user")
    private long userId;
    @JsonProperty("id_product")
    private long productId;
    private String text;

}

