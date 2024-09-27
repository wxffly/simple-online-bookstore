package com.reins.bookstore.controller;

import com.reins.bookstore.entity.CartItem;
import com.reins.bookstore.models.ApiResponseBase;
import com.reins.bookstore.service.CartService;
import com.reins.bookstore.utils.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@Tag(name = "Cart", description = "cart API")
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping
    @Operation(summary = "get user's cart items")
    public ResponseEntity<List<CartItem>> getUserItems() {
        Long userId = SessionUtils.getUserId();
        return ResponseEntity.ok(cartService.getUserCartItems(userId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete item")
    public ResponseEntity<ApiResponseBase> deleteItem(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.deleteItem(id, SessionUtils.getUserId()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "update item")
    public ResponseEntity<ApiResponseBase> updateItem(@PathVariable Long id, @RequestParam Integer number) {
        return ResponseEntity.ok(cartService.updateItem(id, number, SessionUtils.getUserId()));
    }

    @PutMapping
    @Operation(summary = "add item")
    public ResponseEntity<ApiResponseBase> addCartItem(@RequestParam Long bookId) {
        return ResponseEntity.ok(cartService.addItem(SessionUtils.getUserId(), bookId));
    }
}
