package com.vibez.engine.Service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibez.engine.Model.Marketplace;
import com.vibez.engine.Repository.MarketplaceRepo;
import com.vibez.engine.Service.FriendshipService;
import com.vibez.engine.Service.MarketplaceService;

@Service
public class MarketplaceImplement implements MarketplaceService {

    @Autowired
    private MarketplaceRepo marketplaceRepo;

    @Autowired
    private FriendshipService friendshipService;

    public Marketplace addItem(Marketplace newProduct) {
        return  marketplaceRepo.save(newProduct);
    }

    public Marketplace getItemById(String productId) {
        return marketplaceRepo.findByProductId(productId);
    }

  /*   public List<Marketplace> getProductsExcludingHiddenByFriends(ObjectId userId) {
        List<Friendship> friends = friendshipService.getFriends(userId);
        List<ObjectId> friendIds = new ArrayList<>();
        for (Friendship friend : friends) {
            friendIds.add(friend.getUserId());
        }
        return marketplaceRepo.findAllSellingProducts(friendIds);
    }
*/
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
}