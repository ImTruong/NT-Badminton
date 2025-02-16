package com.dev.NT_Badminton.services.product;

import com.dev.NT_Badminton.dto.request.IdsRequest;
import com.dev.NT_Badminton.dto.request.product.CreateProductRequest;
import com.dev.NT_Badminton.dto.request.product.UpdateProductRequest;
import com.dev.NT_Badminton.entities.products.*;
import com.dev.NT_Badminton.entities.products.constant.ProductImageType;
import com.dev.NT_Badminton.repositories.product.*;
import com.dev.NT_Badminton.repositories.uploadFile.UploadFileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductOptionValueRepository productOptionValueRepository;
    private final ProductVariantRepository productVariantsRepository;
    private final ProductVariantOptionValuesRepository productVariantOptionValuesRepository;
    private final UploadFileRepository uploadFileRepository;

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

    @Override
    public boolean checkIfProductHasOption(int productId) {
         List<ProductVariants> productVariants = productVariantsRepository.findByProductIdAndDeleted(productId, false);
         if (productVariants.size() >= 2) return true;
         return !productVariantOptionValuesRepository.findByProductVariantId(productVariants.getFirst().getId()).isEmpty();
    }

    @Override
    public ProductVariants getProductVariantOfNonOptionedProduct(int productId) {
        List<ProductVariants> productVariants = productVariantsRepository.findByProductIdAndDeleted(productId, false);
        return productVariants.getFirst();
    }

    @Override
    public ProductVariants getProductVariantByProductOptionValueIds(List<Integer> productOptionValueIds) {
        Optional<ProductVariants> productVariants = productVariantsRepository.findProductVariantByProductVariantOptionValuesIds(productOptionValueIds);
        if(productVariants.isEmpty())
            throw new EntityNotFoundException("Product variant with option values " + productOptionValueIds + " not found");
        return productVariants.get();
    }

    @Override
    public void ReduceQuantityOfProductVariant(ProductVariants productVariant, int quantity) {
        productVariant.setQuantity(productVariant.getQuantity() - quantity);
        productVariantsRepository.save(productVariant);
    }

    @Override
    public Product getProductDetail(String slug, boolean isForAdmin) {
        return null;
    }

    @Override
    public Product createProduct(CreateProductRequest req) {
        return null;
    }

    @Override
    public Product updatePoduct(UpdateProductRequest req) {
        return null;
    }

    @Override
    public IdsRequest deleteProducts(IdsRequest req) {
        return null;
    }

    @Override
    public IdsRequest restoreProducts(IdsRequest req) {
        return null;
    }

    private void getImageForProduct(Product product) {
        if( product != null){
            List<ProductImage> productImageList = productImageRepository.findAllByProductIdAndDeleted(product.getId(), false);

            if(productImageList != null) {
                for(ProductImageType productImageType : ProductImageType.values()) {
                    List<ProductImage> productImagesByType = productImageList.stream().filter(pi -> pi.getType().equals(productImageType.toValue())).toList();
                    if(!productImagesByType.isEmpty()) {
                        List<Integer> imageIds = new ArrayList<>();

                        productImagesByType.forEach(productImageByType -> imageIds.add(productImageByType.getImageId()));

                        if(!imageIds.isEmpty()) {
                            switch (productImageType) {
                                case MAIN ->
                                    product.setMainImage(uploadFileRepository.findUploadFileByIdAndDeleted(imageIds.getFirst(),false));
                                case COVER ->
                                    product.setCoverImage(uploadFileRepository.findUploadFileByIdAndDeleted(imageIds.getFirst(),false));
                                case OTHER ->
                                    product.setImages(uploadFileRepository.findAllByIdInAndDeleted(imageIds, false));
                            }
                        }
                    }
                }
            }
        }
    }

}
