package com.vibez.engine.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vibez.engine.Model.Marketplace;
import com.vibez.engine.Service.MarketplaceService;

@CrossOrigin(origins = "*" , allowedHeaders = "*")
@RestController
@RequestMapping("/vibez")
public class MarketplaceController {
    
    @Autowired
    private MarketplaceService marketplaceService;

    @GetMapping("/product/find/{productId}") // find product by id
    public ResponseEntity<Marketplace> getItemById(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String productId) {
        return ResponseEntity.ok(marketplaceService.getItemById(productId));
    }

     @GetMapping("/product/findAll/{userId}") // find all products by user id
    public ResponseEntity<List<Marketplace>> getCommunityAndFriendsVisibleItems(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String userId) {
        return ResponseEntity.ok(marketplaceService.getProductsExcludingHiddenByFriends(userId));
    }

    @GetMapping("/product/share/{productId}") // generate shareable link
    public ResponseEntity<String> generateShareableLink(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String productId) {
        return ResponseEntity.ok(marketplaceService.generateShareableLink(productId));
    }

    @GetMapping("/product/count/{sellerId}") // get active listning Count
    public ResponseEntity<Integer> getActiveListingCount(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String sellerId) {
        return ResponseEntity.ok(marketplaceService.getActiveListingCount(sellerId));
    }

    @GetMapping("/product/my/{sellerId}") // get all products by seller id
    public ResponseEntity<List<Marketplace>> getMyItems(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String sellerId){
        return ResponseEntity.ok(marketplaceService.getMyItems(sellerId));
    }

    @GetMapping("/product/isSeller/{userId}") // is a seller	
    public ResponseEntity<Boolean> isSeller(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String sellerId){
        return ResponseEntity.ok(marketplaceService.isSeller(sellerId));
    }

    @PutMapping("/product/addClick/{productId}") 
    public void addClick(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String productId){
        marketplaceService.addClick(productId);
    }

    @GetMapping("/product/totalClicks/{sellerId}") 
    public ResponseEntity<Integer> getTotalClicks(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable String sellerId){
        return ResponseEntity.ok(marketplaceService.getTotalClicks(sellerId));
    }

    @GetMapping("/search/{userId}/{keyword}")
    public ResponseEntity<List<Marketplace>> searchProduct(@PathVariable String keyword, @PathVariable String userId) {
        return ResponseEntity.ok(marketplaceService.searchProduct(keyword, userId));
    }
}   
