package com.minifruit.backend.controller;

import com.minifruit.backend.entity.Product;
import com.minifruit.backend.service.ProductService;
import com.minifruit.backend.service.SupabaseStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final SupabaseStorageService supabaseStorageService;

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

    @PostMapping("/{id}/image")
    public ResponseEntity<?> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !List.of("image/jpeg", "image/png", "image/webp").contains(contentType)) {
            return ResponseEntity.badRequest().body("Chỉ chấp nhận ảnh JPEG, PNG hoặc WEBP");
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            return ResponseEntity.badRequest().body("Ảnh không được vượt quá 5MB");
        }
        try {
            String ext = switch (contentType) {
                case "image/png" -> ".png";
                case "image/webp" -> ".webp";
                default -> ".jpg";
            };
            String filename = "product-" + id + "-" + System.currentTimeMillis() + ext;
            String url = supabaseStorageService.uploadImage(filename, file.getBytes(), contentType);
            Product updated = productService.updateImageUrl(id, url);
            return ResponseEntity.ok(Map.of("imageUrl", updated.getImageUrl()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Upload thất bại: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok("Đã xóa sản phẩm");
    }
}