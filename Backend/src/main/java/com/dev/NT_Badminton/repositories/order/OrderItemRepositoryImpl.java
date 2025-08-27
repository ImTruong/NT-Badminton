package com.dev.NT_Badminton.repositories.order;

import com.dev.NT_Badminton.dto.response.order.OrderItemResponse;
import com.dev.NT_Badminton.entities.discounts.QDiscount;
import com.dev.NT_Badminton.entities.orders.QOrder;
import com.dev.NT_Badminton.entities.orders.QOrderItems;
import com.dev.NT_Badminton.entities.products.ProductOptionValue;
import com.dev.NT_Badminton.entities.products.QProduct;
import com.dev.NT_Badminton.entities.products.QProductImage;
import com.dev.NT_Badminton.entities.products.QProductVariants;
import com.dev.NT_Badminton.entities.products.constant.ProductImageType;
import com.dev.NT_Badminton.entities.upload_file.QUploadFile;
import com.dev.NT_Badminton.repositories.BaseRepository;
import com.dev.NT_Badminton.repositories.discount.DiscountRepository;
import com.dev.NT_Badminton.repositories.product.ProductOptionValueRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OrderItemRepositoryImpl extends BaseRepository implements OrderItemRepositoryCustom {

    private final DiscountRepository discountRepository;
    private final ProductOptionValueRepository productOptionValueRepository;

    public List<OrderItemResponse> getAllItemResponseByOrderId(Integer orderId) {
        QOrderItems qOrderItems = QOrderItems.orderItems;
        QProduct qProduct = QProduct.product;
        QProductVariants qProductVariants = QProductVariants.productVariants;
        QProductImage qProductImage = QProductImage.productImage;
        QUploadFile qUploadFile = QUploadFile.uploadFile;
        QDiscount qDiscount = QDiscount.discount;
        QOrder qOrder = QOrder.order;

        JPAQueryFactory query = query();
        JPAQuery<OrderItemResponse> jpaQuery =
                query.select(Projections.constructor(
                                OrderItemResponse.class,
                                qProductVariants.id,
                                qUploadFile.originUrl,
                                qProductVariants.price,
                                qProduct.name,
                                qOrderItems.quantity
                        ))
                        .from(qOrder)
                        .join(qOrderItems).on(qOrderItems.orderId.eq(qOrder.id))
                        .join(qProductVariants).on(qProductVariants.id.eq(qOrderItems.productVariantId))
                        .join(qProduct).on(qProduct.id.eq(qProductVariants.productId))
                        .leftJoin(qProductImage).on(qProductImage.productId.eq(qProduct.id))
                        .leftJoin(qUploadFile).on(qProductImage.imageId.eq(qUploadFile.id))
                        .leftJoin(qDiscount).on(qDiscount.productId.eq(qProduct.id))
                        .where(qOrder.id.eq(orderId),
                                qProductImage.type.eq(ProductImageType.MAIN).or(qProductImage.isNull())
                        );

        List<OrderItemResponse> result = jpaQuery.fetch();
        result.forEach(orderItemResponse -> {
            orderItemResponse.setProductOptions(productOptionValueRepository.getProductVariantOptionValuesByProductVariantId(orderItemResponse.getProductVariantId()));
            Integer discountPercentage = discountRepository.getNewestUnexpiredDiscountWithHighestPercentage(orderItemResponse.getProductVariantId());
            orderItemResponse.setPrice(orderItemResponse.getPrice() * (100 - discountPercentage) / 100);
        });
        return result;
    }

}
