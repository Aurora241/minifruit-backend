package com.minifruit.backend.service;

import com.minifruit.backend.entity.Product;
import com.minifruit.backend.repository.CategoryRepository;
import com.minifruit.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<Product> getAll() {
        return productRepository.findByStatus(true);
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
    }

    public Product create(Product product, Long categoryId) {
        if (categoryId != null) {
            product.setCategory(categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục")));
        }
        return productRepository.save(product);
    }

    public Product update(Long id, Product data, Long categoryId) {
        Product product = getById(id);
        product.setProductName(data.getProductName());
        product.setSku(data.getSku());
        product.setBarcode(data.getBarcode());
        product.setCostPrice(data.getCostPrice());
        product.setSellingPrice(data.getSellingPrice());
        product.setImageUrl(data.getImageUrl());
        if (categoryId != null) {
            product.setCategory(categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục")));
        }
        return productRepository.save(product);
    }

    public void delete(Long id) {
        Product product = getById(id);
        product.setStatus(false);
        productRepository.save(product);
    }
}