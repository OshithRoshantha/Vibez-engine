package com.vibez.engine.Controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vibez.engine.Model.Marketplace;
import com.vibez.engine.Service.MarketplaceService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/vibez")
public class MarketplaceController {
    
    @Autowired
    private MarketplaceService marketplaceService;

    @PostMapping("/product")
    public ResponseEntity<Boolean> addSellProduct(@RequestHeader(value = "Authorization", required = true)  String token, @RequestBody Marketplace newProduct) {
        return ResponseEntity.ok(marketplaceService.addtem(newProduct)); 
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Marketplace> getItemById(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable ObjectId productId) {
        return ResponseEntity.ok(marketplaceService.getItemById(productId));
    }

    @GetMapping("/product/{friendIds}")
    public ResponseEntity<List<Marketplace>> getCommunityAndFriendsVisibleItems(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable ObjectId friendId) {
        return ResponseEntity.ok(marketplaceService.getProductsExcludingHiddenByFriends(friendId));
    }

    @GetMapping("/product/share/{productId}")
    public ResponseEntity<String> generateShareableLink(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable ObjectId productId) {
        return ResponseEntity.ok(marketplaceService.generateShareableLink(productId));
    }

}
