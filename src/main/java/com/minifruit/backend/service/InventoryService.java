package com.minifruit.backend.service;

import com.minifruit.backend.entity.*;
import com.minifruit.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    private final InventoryTicketRepository ticketRepository;
    private final TicketDetailRepository ticketDetailRepository;
    private final ProductStockRepository stockRepository;
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;

    // Lấy chi tiết một phiếu kho (kèm danh sách sản phẩm)
    public Map<String, Object> getTicketDetail(Long ticketId) {
        InventoryTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu kho"));
        List<TicketDetail> details = ticketDetailRepository.findByTicketTicketId(ticketId);
        Map<String, Object> result = new java.util.LinkedHashMap<>();
        result.put("ticket", ticket);
        result.put("details", details);
        return result;
    }

    // Lấy danh sách phiếu kho
    public List<InventoryTicket> getTickets(Long branchId) {
        if (branchId != null) {
            return ticketRepository.findByBranchBranchIdOrderByCreatedAtDesc(branchId);
        }
        return ticketRepository.findAllByOrderByCreatedAtDesc();
    }

    // Xem tồn kho theo chi nhánh
    public List<ProductStock> getStockByBranch(Long branchId) {
        return stockRepository.findByBranchBranchId(branchId);
    }

    // Cảnh báo hàng sắp hết
    public List<ProductStock> getLowStock(Long branchId) {
        return stockRepository.findByCurrentStockLessThanEqualAndBranchBranchId(5, branchId);
    }

    // Nhập kho
    @Transactional
    public InventoryTicket importStock(Long branchId, Long userId,
                                       String note, List<Map<String, Object>> items) {
        InventoryTicket ticket = new InventoryTicket();
        ticket.setTicketType("IMPORT");
        ticket.setBranch(branchRepository.findById(branchId).orElseThrow());
        ticket.setCreatedBy(userRepository.findById(userId).orElseThrow());
        ticket.setNote(note);
        ticketRepository.save(ticket);

        for (Map<String, Object> item : items) {
            Object productIdVal = item.get("productId");
            if (productIdVal == null) throw new IllegalArgumentException("item.productId is required");
            Object quantityVal = item.get("quantity");
            if (quantityVal == null) throw new IllegalArgumentException("item.quantity is required");
            Long productId = Long.valueOf(productIdVal.toString());
            int qty = Integer.parseInt(quantityVal.toString());
            log.debug("importStock item: productId={}, qty={}", productId, qty);

            TicketDetail detail = new TicketDetail();
            detail.setTicket(ticket);
            detail.setProduct(productRepository.findById(productId).orElseThrow());
            detail.setQuantity(qty);
            ticketDetailRepository.save(detail);

            // Cập nhật tồn kho
            ProductStock stock = stockRepository
                    .findByProductProductIdAndBranchBranchId(productId, branchId)
                    .orElseGet(() -> {
                        ProductStock s = new ProductStock();
                        s.setProduct(productRepository.findById(productId).orElseThrow());
                        s.setBranch(branchRepository.findById(branchId).orElseThrow());
                        s.setCurrentStock(0);
                        return s;
                    });
            stock.setCurrentStock(stock.getCurrentStock() + qty);
            stockRepository.save(stock);
        }
        return ticket;
    }

    // Xuất kho
    @Transactional
    public InventoryTicket exportStock(Long branchId, Long userId,
                                       String note, List<Map<String, Object>> items) {
        InventoryTicket ticket = new InventoryTicket();
        ticket.setTicketType("EXPORT");
        ticket.setBranch(branchRepository.findById(branchId).orElseThrow());
        ticket.setCreatedBy(userRepository.findById(userId).orElseThrow());
        ticket.setNote(note);
        ticketRepository.save(ticket);

        for (Map<String, Object> item : items) {
            Object productIdVal = item.get("productId");
            if (productIdVal == null) throw new IllegalArgumentException("item.productId is required");
            Object quantityVal = item.get("quantity");
            if (quantityVal == null) throw new IllegalArgumentException("item.quantity is required");
            Long productId = Long.valueOf(productIdVal.toString());
            int qty = Integer.parseInt(quantityVal.toString());
            log.debug("exportStock item: productId={}, qty={}", productId, qty);

            ProductStock stock = stockRepository
                    .findByProductProductIdAndBranchBranchId(productId, branchId)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không có trong kho"));

            if (stock.getCurrentStock() < qty)
                throw new RuntimeException("Không đủ hàng trong kho");

            TicketDetail detail = new TicketDetail();
            detail.setTicket(ticket);
            detail.setProduct(productRepository.findById(productId).orElseThrow());
            detail.setQuantity(qty);
            ticketDetailRepository.save(detail);

            stock.setCurrentStock(stock.getCurrentStock() - qty);
            stockRepository.save(stock);
        }
        return ticket;
    }

    // Điều chuyển kho
    @Transactional
    public InventoryTicket transferStock(Long fromBranchId, Long toBranchId,
                                         Long userId, String note,
                                         List<Map<String, Object>> items) {
        InventoryTicket ticket = new InventoryTicket();
        ticket.setTicketType("TRANSFER");
        ticket.setBranch(branchRepository.findById(fromBranchId).orElseThrow());
        ticket.setToBranch(branchRepository.findById(toBranchId).orElseThrow());
        ticket.setCreatedBy(userRepository.findById(userId).orElseThrow());
        ticket.setNote(note);
        ticketRepository.save(ticket);

        for (Map<String, Object> item : items) {
            Object productIdVal = item.get("productId");
            if (productIdVal == null) throw new IllegalArgumentException("item.productId is required");
            Object quantityVal = item.get("quantity");
            if (quantityVal == null) throw new IllegalArgumentException("item.quantity is required");
            Long productId = Long.valueOf(productIdVal.toString());
            int qty = Integer.parseInt(quantityVal.toString());
            log.debug("transferStock item: productId={}, qty={}", productId, qty);

            // Trừ kho nguồn
            ProductStock fromStock = stockRepository
                    .findByProductProductIdAndBranchBranchId(productId, fromBranchId)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không có trong kho nguồn"));
            if (fromStock.getCurrentStock() < qty)
                throw new RuntimeException("Không đủ hàng để điều chuyển");
            fromStock.setCurrentStock(fromStock.getCurrentStock() - qty);
            stockRepository.save(fromStock);

            // Cộng kho đích
            ProductStock toStock = stockRepository
                    .findByProductProductIdAndBranchBranchId(productId, toBranchId)
                    .orElseGet(() -> {
                        ProductStock s = new ProductStock();
                        s.setProduct(productRepository.findById(productId).orElseThrow());
                        s.setBranch(branchRepository.findById(toBranchId).orElseThrow());
                        s.setCurrentStock(0);
                        return s;
                    });
            toStock.setCurrentStock(toStock.getCurrentStock() + qty);
            stockRepository.save(toStock);

            TicketDetail detail = new TicketDetail();
            detail.setTicket(ticket);
            detail.setProduct(productRepository.findById(productId).orElseThrow());
            detail.setQuantity(qty);
            ticketDetailRepository.save(detail);
        }
        return ticket;
    }
}