package com.dev.NT_Badminton.services.product;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.dto.request.IdsRequest;
import com.dev.NT_Badminton.dto.request.product.CreateProductRequest;
import com.dev.NT_Badminton.dto.request.product.UpdateProductRequest;
import com.dev.NT_Badminton.dto.response.ApiResponse;
import com.dev.NT_Badminton.entities.products.Product;
import com.dev.NT_Badminton.entities.products.ProductImage;
import com.dev.NT_Badminton.repositories.product.ProductImageRepository;
import com.dev.NT_Badminton.repositories.product.ProductRepository;
import com.dev.NT_Badminton.repositories.uploadFile.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final UploadFileRepository uploadFileRepository;

    @Override
    public ApiResponse<List<Product>> getProducts(int page, String name, Integer categoryId, Boolean deleted, ActiveStatus status, Integer startPriceRange, Integer endPriceRange) {
        return null;
    }

    @Override
    public ApiResponse<List<Product>> getRelatedProducts(int page, String slug) {
        return null;
    }

    @Override
    public Product getProductDetail(String slug, boolean isForAdmin) throws Exception {
        return null;
    }

    @Override
    public Product createProduct(CreateProductRequest req) throws Exception {
        return null;
    }

    @Override
    public Product updateProduct(UpdateProductRequest req) throws Exception {
        return null;
    }

    @Transactional
    @Override
    public IdsRequest deleteProduct(IdsRequest req) throws Exception {
        return handleProduct(req, true);
    }

    @Override
    public IdsRequest restoreProduct(IdsRequest req) throws Exception {
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
}
