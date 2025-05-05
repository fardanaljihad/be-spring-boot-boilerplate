package com.tujuhsembilan.springboot_boilerplate.model.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.math.BigDecimal;
import java.time.Instant;

@Document(collection = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    private String id;

    @Field("product_name")
    private String name;

    private String description;
    private BigDecimal price;
    private String category;

    @Field("created_ts")
    private Instant createdAt = Instant.now();
}
