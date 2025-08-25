package com.dev.NT_Badminton.services.product;

import com.dev.NT_Badminton.dto.request.product.*;
import com.dev.NT_Badminton.dto.response.product.ProductDetailResponse;
import com.dev.NT_Badminton.dto.response.product.SearchProductReponse;
import com.dev.NT_Badminton.entities.products.*;
import com.dev.NT_Badminton.entities.products.constant.ProductImageType;
import com.dev.NT_Badminton.entities.upload_file.UploadFile;
import com.dev.NT_Badminton.entities.users.AppUser;
import com.dev.NT_Badminton.repositories.product.*;
import com.dev.NT_Badminton.services.uploadFile.UploadFileService;
import com.dev.NT_Badminton.services.user.UserService;
import com.dev.NT_Badminton.util.Utils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductOptionValueRepository productOptionValueRepository;
    private final ProductVariantRepository productVariantsRepository;
    private final ProductVariantOptionValuesRepository productVariantOptionValuesRepository;
    private final ModelMapper modelMapper;
    private final ProductImageRepository productImageRepository;
    private final UploadFileService uploadFileService;

    @Override
    public Product getProductById(int productId) {
        Optional<Product> product = productRepository.findProductById(productId);
        if (product.isEmpty())
            throw new EntityNotFoundException("Product with id " + productId + " not found");
        return product.get();
    }

    @Override
    public ProductOption getProductOptionById(int productOptionId) {
        Optional<ProductOption> productOption = productOptionRepository.findById(productOptionId);
        if (productOption.isEmpty())
            throw new EntityNotFoundException("Product option with id " + productOptionId + " not found");
        return productOption.get();
    }

    @Override
    public ProductOptionValue getProductOptionValueById(int productOptionValueId) {
        Optional<ProductOptionValue> productOptionValue = productOptionValueRepository.findById(productOptionValueId);
        if (productOptionValue.isEmpty())
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
        if (productVariants.isEmpty())
            throw new EntityNotFoundException("Product variant with option values " + productOptionValueIds + " not found");
        return productVariants.get();
    }

    @Override
    public ProductVariants getProductVariantById(int productVariantId) {
        Optional<ProductVariants> productVariants = productVariantsRepository.findById(productVariantId);
        if (productVariants.isEmpty())
            throw new EntityNotFoundException("Product variant with id " + productVariantId + " not found");
        return productVariants.get();
    }

    @Override
    public void changeQuantityOfProductDueToOrderAct(Integer orderId, String orderType) {
        if (orderType.equals("cancel"))
            productVariantsRepository.increaseProductVariantQuantityFromCanceledOrder(orderId);
        else
            productVariantsRepository.decreaseProductVariantQuantityFromOrder(orderId);
    }

    @Transactional
    @Override
    public void addOrUpdateProduct(ModifyProductRequest modifyProductRequest) {
        Product product = "CREATE".equals(modifyProductRequest.getType())
                ? modelMapper.map(modifyProductRequest, Product.class)
                : productRepository.findById(modifyProductRequest.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        if ("UPDATE".equals(modifyProductRequest.getType()))
            modelMapper.map(modifyProductRequest, product);
        product.setSlug(Utils.removeCharacterVn(modifyProductRequest.getName()));
        productRepository.save(product);
    }

    @Override
    public void addOrUpdateProductOption(ModifyProductOptionRequest modifyProductOptionRequest) {
        ProductOption productOption = "CREATE".equals(modifyProductOptionRequest.getType())
                ? modelMapper.map(modifyProductOptionRequest, ProductOption.class)
                : productOptionRepository.findById(modifyProductOptionRequest.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product option not found"));
        if ("UPDATE".equals(modifyProductOptionRequest.getType()))
            modelMapper.map(modifyProductOptionRequest, productOption);
        productOptionRepository.save(productOption);
    }

    @Override
    public void addOrUpdateProductOptionValue(ModifyProductOptionValueRequest modifyProductOptionValueRequest) {
        ProductOptionValue productOptionValue = "CREATE".equals(modifyProductOptionValueRequest.getType())
                ? modelMapper.map(modifyProductOptionValueRequest, ProductOptionValue.class)
                : productOptionValueRepository.findById(modifyProductOptionValueRequest.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product option value not found"));
        if ("UPDATE".equals(modifyProductOptionValueRequest.getType()))
            modelMapper.map(modifyProductOptionValueRequest, productOptionValue);
        productOptionValueRepository.save(productOptionValue);
    }

    @Transactional
    @Override
    public void addOrUpdateProductVariant(ModifyProductVariantRequest modifyProductVariantRequest) {
        ProductVariants productVariants = "CREATE".equals(modifyProductVariantRequest.getType())
                ? modelMapper.map(modifyProductVariantRequest, ProductVariants.class)
                : productVariantsRepository.findById(modifyProductVariantRequest.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product variant not found"));
        if ("UPDATE".equals(modifyProductVariantRequest.getType()))
            modelMapper.map(modifyProductVariantRequest, productVariants);
        productVariantsRepository.save(productVariants);
        List<Integer> productIds = productRepository.findProductIdsByOptionValueIds(modifyProductVariantRequest.getProductOptionValueIds(), modifyProductVariantRequest.getProductId());
        if (productIds.size() != 1)
            throw new EntityNotFoundException("Product variant with option values " + modifyProductVariantRequest.getProductOptionValueIds() + " not found");
        else
            productVariantOptionValuesRepository.deleteAllByProductVariantId(productVariants.getId());
        modifyProductVariantRequest.getProductOptionValueIds().forEach(
                productOptionValueId -> {
                    productVariantOptionValuesRepository.save(ProductVariantOptionValues.builder()
                            .productVariantId(productVariants.getId())
                            .productOptionValueId(productOptionValueId)
                            .build());
                }
        );
    }

    @Transactional
    @Override
    public void deleteProduct(int productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        product.setDeleted(true);
        productRepository.save(product);
    }

    @Transactional
    @Override
    public void deleteProductOption(int productOptionId) {
        ProductOption productOption = productOptionRepository.findById(productOptionId)
                .orElseThrow(() -> new EntityNotFoundException("Product option not found"));
        productOption.setDeleted(true);
        productOptionRepository.save(productOption);
    }

    @Transactional
    @Override
    public void deleteProductOptionValue(int productOptionValueId) {
        ProductOptionValue productOptionValue = productOptionValueRepository.findById(productOptionValueId)
                .orElseThrow(() -> new EntityNotFoundException("Product option value not found"));
        productOptionValue.setDeleted(true);
        productOptionValueRepository.save(productOptionValue);
    }

    @Transactional
    @Override
    public void deleteProductVariant(int productVariantId) {
        ProductVariants productVariants = productVariantsRepository.findById(productVariantId)
                .orElseThrow(() -> new EntityNotFoundException("Product variant not found"));
        productVariants.setDeleted(true);
        productVariantsRepository.save(productVariants);
    }

    @Override
    public PageImpl<SearchProductReponse> searchProducts(SearchProductRequest searchProductRequest, Pageable pageable) {
        PageImpl<SearchProductReponse> result = productRepository.findProducts(searchProductRequest, pageable);
        return result;
    }

    @Override
    public ProductDetailResponse getProductDetail(int productId) {
        return productRepository.findProductDetailById(productId);
    }

    @Transactional
    @Override
    public boolean addProductImage(int productId, MultipartFile image, ProductImageType imageType) throws Exception {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        UploadFile uploadFile = uploadFileService.uploadFile(image, "products");
        ProductImage newImage = ProductImage.builder()
                .productId(productId)
                .imageId(uploadFile.getId())
                .type(imageType)
                .build();
        productImageRepository.save(newImage);
        return true;
    }

    @Transactional
    @Override
    public void deleteProductImage(int imageId) throws Exception {
        ProductImage productImage = productImageRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Product image not found"));
        productImageRepository.delete(productImage);
        uploadFileService.deleteFile(uploadFileService.getUploadFileById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Upload file not found")));
    }

    @Override
    public List<String> getAllProductBrands() {
        return productRepository.getAllProductBrands();
    }
}
