package com.minifruit.backend.controller;

import com.minifruit.backend.entity.Product;
import com.minifruit.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<Product> getAll() {
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @PostMapping
    public Product create(@RequestBody Map<String, Object> body) {
        Product product = new Product();
        product.setSku((String) body.get("sku"));
        product.setBarcode((String) body.get("barcode"));
        product.setProductName((String) body.get("productName"));
        product.setImageUrl((String) body.get("imageUrl"));
        if (body.get("costPrice") != null)
            product.setCostPrice(new java.math.BigDecimal(body.get("costPrice").toString()));
        if (body.get("sellingPrice") != null)
            product.setSellingPrice(new java.math.BigDecimal(body.get("sellingPrice").toString()));
        Long categoryId = body.get("categoryId") != null ?
                Long.valueOf(body.get("categoryId").toString()) : null;
        return productService.create(product, categoryId);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Product data = new Product();
        data.setSku((String) body.get("sku"));
        data.setBarcode((String) body.get("barcode"));
        data.setProductName((String) body.get("productName"));
        data.setImageUrl((String) body.get("imageUrl"));
        if (body.get("costPrice") != null)
            data.setCostPrice(new java.math.BigDecimal(body.get("costPrice").toString()));
        if (body.get("sellingPrice") != null)
            data.setSellingPrice(new java.math.BigDecimal(body.get("sellingPrice").toString()));
        Long categoryId = body.get("categoryId") != null ?
                Long.valueOf(body.get("categoryId").toString()) : null;
        return productService.update(id, data, categoryId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok("Đã xóa sản phẩm");
    }
}