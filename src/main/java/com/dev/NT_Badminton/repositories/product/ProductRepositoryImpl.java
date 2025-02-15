package com.dev.NT_Badminton.repositories.product;

import com.dev.NT_Badminton.entities.products.Product;
import com.dev.NT_Badminton.entities.products.QProduct;
import com.dev.NT_Badminton.repositories.BaseRepository;

import java.util.Optional;

public class ProductRepositoryImpl extends BaseRepository implements ProductRepositoryCustom {

    @Override
    public Optional<Product> findProductById(int productId) {
        return Optional.ofNullable(query()
                .selectFrom(QProduct.product)
                .where(QProduct.product.id.eq(productId))
                .fetchOne());
    }

}
