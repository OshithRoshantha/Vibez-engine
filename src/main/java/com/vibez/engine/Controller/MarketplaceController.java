package com.vibez.engine.Controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vibez.engine.Model.Marketplace;
import com.vibez.engine.Service.MarketplaceService;

@RestController
@RequestMapping("/vibez")
public class MarketplaceController {
    
    @Autowired
    private MarketplaceService marketplaceService;

    @PostMapping("/product/add/{sellerId}")
    public ResponseEntity<Boolean> addSellProduct(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable ObjectId sellerId, @RequestBody Marketplace newProduct) {
        return ResponseEntity.ok(marketplaceService.addItem(sellerId, newProduct)); 
    }

    @GetMapping("/product/find/{productId}") // find product by id
    public ResponseEntity<Marketplace> getItemById(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable ObjectId productId) {
        return ResponseEntity.ok(marketplaceService.getItemById(productId));
    }

    @GetMapping("/product/findAll/{userId}") // find all products by user id
    public ResponseEntity<List<Marketplace>> getCommunityAndFriendsVisibleItems(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable ObjectId userId) {
        return ResponseEntity.ok(marketplaceService.getProductsExcludingHiddenByFriends(userId));
    }

    @GetMapping("/product/share/{productId}") // generate shareable link
    public ResponseEntity<String> generateShareableLink(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable ObjectId productId) {
        return ResponseEntity.ok(marketplaceService.generateShareableLink(productId));
    }

}
