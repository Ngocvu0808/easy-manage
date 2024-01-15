package com.example.product.service.impl;

import com.example.product.dto.request.AddCartRequest;
import com.example.product.dto.request.AddCartRequest.CartProduct;
import com.example.product.dto.response.GetCartByUserIdResponse;
import com.example.product.dto.response.GetCartByUserIdResponse.ProductInCart;
import com.example.product.entity.CartDetailEntities;
import com.example.product.entity.CartEntities;
import com.example.product.entity.Product;
import com.example.product.repo.CartDetailRepository;
import com.example.product.repo.CartRepository;
import com.example.product.repo.ProductRepository;
import com.example.product.service.iface.CartService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
  private final CartRepository cartRepository;
  private final CartDetailRepository cartDetailRepository;
  private final ProductRepository productRepository;

  public CartServiceImpl(CartRepository cartRepository, CartDetailRepository cartDetailRepository,
      ProductRepository productRepository) {
    this.cartRepository = cartRepository;
    this.cartDetailRepository = cartDetailRepository;
    this.productRepository = productRepository;
  }

  public int addCart(AddCartRequest request) {
    List<CartDetailEntities> cartDetailList = new ArrayList<>();
    List<CartProduct> cartProducts = request.getProducts();
    int result = 0;
    if (cartProducts != null && cartProducts.size()>0) {
      Optional<CartEntities> cartOptional = cartRepository.findByCustomerIdAndDeleted(request.getUserId(), 0);
      CartEntities cart = new CartEntities();
      if (cartOptional.isPresent()) {
        cart = cartOptional.get();
        List<CartDetailEntities> cartDetailFound = cartDetailRepository.findAllByCartIdAndDeleted(cart.getId(), 0);
        if (cartDetailFound != null && cartDetailFound.size()>0) {
          for (CartDetailEntities each : cartDetailFound) {
            each.setDeleted(1);
          }
          cartDetailRepository.saveAll(cartDetailFound);
        }
      } else {
        cart.setCustomerId(request.getUserId());
        cart = cartRepository.save(cart);
      }
      for (CartProduct each : cartProducts) {
        CartDetailEntities cartDetail = new CartDetailEntities();
        cartDetail.setAmount(each.getAmount());
        cartDetail.setProductId(each.getProductId());
        cartDetail.setCartId(cart.getId());
        cartDetailList.add(cartDetail);
      }
      cartDetailRepository.saveAll(cartDetailList);
      result = cart.getId();
    }
    return result;
  }

  @Override
  public GetCartByUserIdResponse getCartByUserId(int userId) {
    Optional<CartEntities> cartOptional = cartRepository.findByCustomerIdAndDeleted(userId, 0);
    if (cartOptional.isPresent()) {
      CartEntities cart = cartOptional.get();
      GetCartByUserIdResponse result = new GetCartByUserIdResponse();
      result.setProducts(new ArrayList<>());
      result.setId(cart.getId());
      result.setUserId(cart.getCustomerId());
      List<CartDetailEntities> cartDetailList = cartDetailRepository.findAllByCartIdAndDeleted(cart.getId(), 0);
      if (cartDetailList != null && cartDetailList.size()>0) {
        for (CartDetailEntities each : cartDetailList) {
          ProductInCart product = new ProductInCart();
          product.setAmount(each.getAmount());
          Optional<Product> productOptional = productRepository.findById(each.getProductId());
          if (productOptional.isPresent()) {
            Product productFound = productOptional.get();
            product.setProduct(productFound);
          }
          result.getProducts().add(product);
        }
        return result;
      }
    }
    return null;
  }


}
