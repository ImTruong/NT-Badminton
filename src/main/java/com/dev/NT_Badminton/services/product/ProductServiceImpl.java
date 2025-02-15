package com.dev.NT_Badminton.services.product;

import com.dev.NT_Badminton.entities.products.Product;
import com.dev.NT_Badminton.entities.products.ProductOption;
import com.dev.NT_Badminton.entities.products.ProductOptionValue;
import com.dev.NT_Badminton.repositories.product.ProductOptionRepository;
import com.dev.NT_Badminton.repositories.product.ProductOptionValueRepository;
import com.dev.NT_Badminton.repositories.product.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductOptionValueRepository productOptionValueRepository;

    @Override
    public Product getProductById(int productId) {
        Optional<Product> product = productRepository.findProductById(productId);
        if(product.isEmpty())
            throw new EntityNotFoundException("Product with id " + productId + " not found");
        return product.get();
    }

    @Override
    public ProductOption getProductOptionById(int productOptionId) {
        Optional<ProductOption> productOption = productOptionRepository.findById(productOptionId);
        if(productOption.isEmpty())
            throw new EntityNotFoundException("Product option with id " + productOptionId + " not found");
        return productOption.get();
    }

    @Override
    public ProductOptionValue getProductOptionValueById(int productOptionValueId) {
        Optional<ProductOptionValue> productOptionValue = productOptionValueRepository.findById(productOptionValueId);
        if(productOptionValue.isEmpty())
            throw new EntityNotFoundException("Product option value with id " + productOptionValueId + " not found");
        return productOptionValue.get();
    }
}
