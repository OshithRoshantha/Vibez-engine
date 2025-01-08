package com.vibez.engine.Service.Implementation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.vibez.engine.Model.Marketplace;
import com.vibez.engine.Model.User;
import com.vibez.engine.Repository.MarketplaceRepo;
import com.vibez.engine.Service.FriendshipService;
import com.vibez.engine.Service.MarketplaceService;
import com.vibez.engine.Service.UserService;

@Service
public class MarketplaceImplement implements MarketplaceService {

    @Autowired
    private MarketplaceRepo marketplaceRepo;

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private UserService userService;

    public Marketplace addItem(Marketplace newProduct) {
        if (newProduct.getProductPhotos().isEmpty()) {
            String defaultImage = "https://static7.depositphotos.com/1056394/786/v/450/depositphotos_7867981-stock-illustration-vector-cardboard-box.jpg";
            newProduct.getProductPhotos().add(defaultImage);
        }
        String seller = newProduct.getSellerId();
        User sellerDetails = userService.getUserById(seller);
        newProduct.setSellerName(sellerDetails.getUserName());
        newProduct.setSellerProfilePicture(sellerDetails.getProfilePicture());
        newProduct.setTotalClicks(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        String formattedDate = LocalDate.now().format(formatter);
        newProduct.setListedDate(formattedDate);
        return marketplaceRepo.save(newProduct);
    }

    public Marketplace getItemById(String productId) {
        Marketplace product = marketplaceRepo.findByProductId(productId);
        String seller = product.getSellerId();
        User sellerDetails = userService.getUserById(seller);
        product.setSellerName(sellerDetails.getUserName());
        product.setSellerProfilePicture(sellerDetails.getProfilePicture());
        return product;
    }

    public List<Marketplace> getProductsExcludingHiddenByFriends(String userId) {
        List<Marketplace> allProducts = marketplaceRepo.findAll();
        List<Marketplace> productDetails = new ArrayList<>();
        List<String> productIds = new ArrayList<>();
        List<String> friendIds = friendshipService.getFriends(userId);
        for (Marketplace product : allProducts) {
            if(product.isVisibleToFriends()){ //if we hide listing from friends put  true
                if (!friendIds.contains(product.getSellerId())){
                    productIds.add(product.getProductId());
                }
            } else{
                productIds.add(product.getProductId());
            }  
        }
        for (String productId : productIds) {
            productDetails.add(marketplaceRepo.findByProductId(productId));
        }
        return productDetails;
    }

    public String generateShareableLink(String productId) {
        return "http://localhost:8080/vibez/product/find/" + productId;
    }

    public Marketplace updateItem(Marketplace updatedProduct) {
        Marketplace existingProduct = marketplaceRepo.findByProductId(updatedProduct.getProductId());
        existingProduct.setProductTitle(updatedProduct.getProductTitle());
        existingProduct.setProductDesc(updatedProduct.getProductDesc());
        existingProduct.setCondition(updatedProduct.getCondition());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setLocation(updatedProduct.getLocation());
        existingProduct.setProductPhotos(updatedProduct.getProductPhotos());
        existingProduct.setVisibleToFriends(updatedProduct.isVisibleToFriends());
        return marketplaceRepo.save(updatedProduct);
    }

    public String deleteItem(String productId) {
        Marketplace product = marketplaceRepo.findByProductId(productId);
        marketplaceRepo.delete(product);
        return productId;
    }

    public Integer getActiveListingCount(String sellerId) {
        List<Marketplace> allProducts = marketplaceRepo.findAll();
        Integer count = 0;
        for (Marketplace product : allProducts) {
            if (product.getSellerId().equals(sellerId)) {
                count++;
            }
        }
        return count;
    }

    public List<Marketplace> getMyItems(String sellerId) {
        List<Marketplace> allProducts = marketplaceRepo.findAll();
        List<Marketplace> myProducts = new ArrayList<>();
        for (Marketplace product : allProducts) {
            if (product.getSellerId().equals(sellerId)) {
                myProducts.add(product);
            }
        }
        return myProducts;
    }

    public boolean isSeller(String userId) {
        List<Marketplace> allProducts = marketplaceRepo.findAll();
        for (Marketplace product : allProducts) {
            if (product.getSellerId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    public void addClick(String productId) {
        Marketplace product = marketplaceRepo.findByProductId(productId);
        Integer clicks = product.getTotalClicks();
        product.setTotalClicks(clicks + 1);
        marketplaceRepo.save(product);
    }

    public Integer getTotalClicks(String sellerId) {
        List<Marketplace> allProducts = marketplaceRepo.findAll();
        Integer totalClicks = 0;
        for (Marketplace product : allProducts) {
            if (product.getSellerId().equals(sellerId)) {
                totalClicks += product.getTotalClicks();
            }
        }
        return totalClicks;
    }
}