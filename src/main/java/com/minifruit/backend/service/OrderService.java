package com.minifruit.backend.service;

import com.minifruit.backend.entity.*;
import com.minifruit.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductStockRepository stockRepository;
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    public List<Order> getByBranch(Long branchId) {
        return orderRepository.findByBranchBranchId(branchId);
    }

    public List<Order> getByUser(Long userId) {
        return orderRepository.findByUserUserId(userId);
    }

    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));
    }

    public List<OrderDetail> getDetails(Long orderId) {
        return orderDetailRepository.findByOrderOrderId(orderId);
    }

    @Transactional
    public Order createOrder(Long branchId, Long userId, Long customerId,
                             String paymentMethod, BigDecimal discount,
                             List<Map<String, Object>> items) {

        // Tạo và LƯU hóa đơn TRƯỚC
        Order order = new Order();
        order.setBranch(branchRepository.findById(branchId).orElseThrow());
        order.setUser(userRepository.findById(userId).orElseThrow());
        order.setPaymentMethod(paymentMethod);
        order.setDiscount(discount != null ? discount : BigDecimal.ZERO);
        if (customerId != null) {
            order.setCustomer(customerRepository.findById(customerId).orElse(null));
        }
        orderRepository.save(order); // ← SAVE TRƯỚC KHI VÀO VÒNG LẶP

        BigDecimal total = BigDecimal.ZERO;

        for (Map<String, Object> item : items) {
            Long productId = Long.valueOf(item.get("productId").toString());
            int qty = Integer.parseInt(item.get("quantity").toString());

            ProductStock stock = stockRepository
                    .findByProductProductIdAndBranchBranchId(productId, branchId)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không có trong kho"));

            if (stock.getCurrentStock() < qty)
                throw new RuntimeException("Không đủ hàng: " +
                        stock.getProduct().getProductName());

            Product product = productRepository.findById(productId).orElseThrow();
            BigDecimal unitPrice = product.getSellingPrice();
            total = total.add(unitPrice.multiply(BigDecimal.valueOf(qty)));

            stock.setCurrentStock(stock.getCurrentStock() - qty);
            stockRepository.save(stock);

            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setQuantity(qty);
            detail.setUnitPrice(unitPrice);
            orderDetailRepository.save(detail);
        }

        // Cập nhật tổng tiền SAU vòng lặp
        order.setTotalAmount(total);
        order.setFreeAmount(total.subtract(order.getDiscount()));
        orderRepository.save(order);

        return order;
    }
}