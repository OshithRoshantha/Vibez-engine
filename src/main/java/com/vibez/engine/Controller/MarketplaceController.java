package com.vibez.engine.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/item")
    public ResponseEntity<Boolean> addSellProduct(@RequestHeader(value = "Authorization", required = true)  String token, @RequestBody Marketplace newItem) {
        return ResponseEntity.ok(marketplaceService.addtem(newItem)); 
    }

}
