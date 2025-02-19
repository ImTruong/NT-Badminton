package com.dev.NT_Badminton.services.product;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.dto.request.IdsRequest;
import com.dev.NT_Badminton.dto.request.product.CreateProductRequest;
import com.dev.NT_Badminton.dto.request.product.ProductOptionRequest;
import com.dev.NT_Badminton.dto.request.product.UpdateProductRequest;
import com.dev.NT_Badminton.dto.response.ApiResponse;
import com.dev.NT_Badminton.entities.categories.Category;
import com.dev.NT_Badminton.entities.products.*;
import com.dev.NT_Badminton.entities.products.constant.ProductImageType;
import com.dev.NT_Badminton.repositories.category.CategoryRepository;
import com.dev.NT_Badminton.repositories.product.*;
import com.dev.NT_Badminton.repositories.uploadFile.UploadFileRepository;
import com.dev.NT_Badminton.util.Utils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final CategoryRepository categoryRepository;

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
    public ApiResponse<List<Product>> getProducts(int page, String name, Integer categoryId ,Boolean deleted, ActiveStatus status, Integer startPriceRange, Integer endPriceRange) throws Exception {
        List<Product> productList = productRepository.getProducts(page,name,categoryId,deleted,status,startPriceRange,endPriceRange,null);
        long count = productRepository.countProducts(name,categoryId,deleted,status,startPriceRange,endPriceRange,null);
        if (productList != null) {
            throw new Exception("Product not found!");
        }

        productList.forEach(this::getImageForProduct);

        return new ApiResponse<>(true,productList,count);
    }

    @Override
    public ApiResponse<List<Product>> getRelatedProductList(int page, String slug) throws Exception {
        Optional<Product> product = productRepository.findBySlugAndDeleted(slug, false);
        if(product.isPresent()) {
            List<Product> productList = productRepository.getProducts(page,null,product.get().getCategoryId(),false,ActiveStatus.ACTIVE,null,null,slug);
            long count = productRepository.countProducts(null, product.get().getCategoryId(), false, ActiveStatus.ACTIVE,null,null,slug);

            productList.forEach(this::getImageForProduct);
            return new ApiResponse<>(true,productList,count);
        }
        return new ApiResponse<>(true,null,0);
    }

    @Override
    public Product getProductDetail(String slug, boolean isForAdmin) throws Exception {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new Exception("Product not found!"));

        if(!isForAdmin && (product.getStatus().equals(ActiveStatus.INACTIVE)) || product.isDeleted())
            throw new Exception("Product not found!");

        Optional<Category> category = categoryRepository.findByIdAndDeleted(product.getCategoryId(), false);
        category.ifPresent(product::setCategory);

        getImageForProduct(product);

        product.setMinPriceOption(productVariantsRepository.getMinProductVariantsPriceByProductId(product.getId()));

        return product;
    }

    @Transactional
    @Override
    public Product createProduct(CreateProductRequest req) throws Exception{
        Product product = productRepository.save(Product.builder().name(req.getName().trim())
                .shortDescription(req.getShortDescription().trim())
                .description(req.getDescription().trim())
                .status(req.getStatus())
                .categoryId(req.getCategoryId())
                .slug(Utils.removeCharacterVn(req.getName().trim() + Utils.randomString(8)))
                .build());

        List<ProductImage> productImageList = new ArrayList<>();
        if(!uploadFileRepository.existsByIdAndDeleted(req.getMainImageId(), false)) {
            throw new Exception("Main image not found!");
        }
        else {
            ProductImage productImage = new ProductImage();
            productImage.setProductId(product.getId());
            productImage.setImageId(req.getMainImageId());
            productImage.setType(ProductImageType.MAIN);
            productImageList.add(productImage);
        }

        if(!uploadFileRepository.existsByIdAndDeleted(req.getCoverImageId(), false)) {
            throw new Exception("Cover image not found!");
        }
        else {
            ProductImage productImage = new ProductImage();
            productImage.setProductId(product.getId());
            productImage.setImageId(req.getCoverImageId());
            productImage.setType(ProductImageType.COVER);
            productImageList.add(productImage);
        }

        if(!req.getImageIds().isEmpty()) {
            for( Integer imageId : req.getImageIds()) {
                if(!uploadFileRepository.existsByIdAndDeleted(imageId, false)) {
                    throw new Exception("Image not found!");
                }

                ProductImage productImage = new ProductImage();
                productImage.setProductId(product.getId());
                productImage.setImageId(imageId);
                productImage.setType(ProductImageType.OTHER);
                productImageList.add(productImage);
            }
        }
        productImageRepository.saveAll(productImageList);

        for(ProductOptionRequest optionRequest : req.getOptionRequests()){
            ProductOption option = new ProductOption();
            option.setProductId(product.getId());
            option.setDescription(optionRequest.getDescription().trim());
            option.setName(optionRequest.getName().trim());
            for(String value : optionRequest.getValues()) {
                ProductOptionValue optionValue = new ProductOptionValue();
                optionValue.setValue(value);
                optionValue.setProduct_option_id(option.getId());
                productOptionValueRepository.save(optionValue);
            }
        }
        return product;
    }
    @Transactional
    @Override
    public Product updatePoduct(UpdateProductRequest req) throws Exception {
        Product product = productRepository.findById(req.getId())
                .orElseThrow(() -> new Exception("product not found!"));
        List<ProductImage> productImageList = productImageRepository.findAllByProductIdAndDeleted(product.getId(), false);
        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setShortDescription(req.getShortDescription());
        product.setStatus(req.getStatus());
        product.setCategoryId(req.getCategoryId());

        if (productImageList != null && !productImageList.isEmpty()) {
            productImageList.forEach(pI -> {
                try {
                    switch (pI.getType()) {
                        case MAIN -> {
                            if (pI.getImageId() != req.getMainImageId()) {
                                if (!uploadFileRepository.existsByIdAndDeleted(req.getMainImageId(), false)) {
                                    throw new Exception("Main image not found!");
                                } else {
                                    ProductImage productImage = new ProductImage();
                                    productImage.setProductId(product.getId());
                                    productImage.setImageId(req.getMainImageId());
                                    productImage.setType(ProductImageType.MAIN);

                                    productImageRepository.save(productImage);
                                }

                                productImageRepository.delete(pI);
                            }
                        }
                        case COVER -> {
                            if (pI.getImageId() != req.getCoverImageId()) {
                                if (!uploadFileRepository.existsByIdAndDeleted(req.getCoverImageId(), false)) {
                                    throw new Exception("Main image not found!");
                                } else {
                                    ProductImage productImage = new ProductImage();
                                    productImage.setProductId(product.getId());
                                    productImage.setImageId(req.getMainImageId());
                                    productImage.setType(ProductImageType.COVER);

                                    productImageRepository.save(productImage);
                                }

                                productImageRepository.delete(pI);
                            }
                        }
                        case OTHER -> {
                            if (req.getImageIds() != null && !req.getImageIds().isEmpty()) {
                                if (!req.getImageIds().contains(pI.getImageId())) {
                                    productImageRepository.delete(pI);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        if (req.getImageIds() != null && !req.getImageIds().isEmpty()) {
            List<ProductImage> productImageNewList = new ArrayList<>();

            for (Integer imageId : req.getImageIds()) {
                if (!uploadFileRepository.existsByIdAndDeleted(imageId, false)) {
                    throw new Exception("Image not found!");
                } else if (!productImageRepository.existsByProductIdAndImageId(product.getId(), imageId)) {
                    ProductImage productImage = new ProductImage();
                    productImage.setProductId(product.getId());
                    productImage.setImageId(imageId);
                    productImage.setType(ProductImageType.OTHER);
                    productImageNewList.add(productImage);
                }
            }

            if (!productImageNewList.isEmpty()) {
                productImageRepository.saveAll(productImageNewList);
            }
        }

        return product;
    }

    @Transactional
    @Override
    public IdsRequest deleteProducts(IdsRequest req) throws Exception {
        return handleProduct(req, true);
    }

    @Override
    public IdsRequest restoreProducts(IdsRequest req) throws Exception {
        return handleProduct(req, false);
    }

    private IdsRequest handleProduct(IdsRequest req, boolean isDeleted) throws Exception {
        List<Product> productList = new ArrayList<>();

        for (Integer id : req.getIds()) {
            Product product = productRepository.findById(id).orElseThrow(() -> new Exception("Product not found!"));

            productList.add(product);
        }

        if (!productList.isEmpty()) {
            productList.forEach(product -> {
                List<ProductImage> productImageList = productImageRepository.findAllByProductId(product.getId());

                if (productImageList != null && !productImageList.isEmpty()) {
                    productImageList.forEach(productImage -> {
                        productImage.setDeleted(isDeleted);
                    });

                    productImageRepository.saveAll(productImageList);
                }

                product.setDeleted(isDeleted);
            });

            productRepository.saveAll(productList);
        }

        return req;
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
