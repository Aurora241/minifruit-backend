package com.minifruit.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String sku;
    private String barcode;
    private String productName;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
    private String imageUrl;
    private Boolean status = true;
}
