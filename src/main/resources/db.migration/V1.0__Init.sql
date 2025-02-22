CREATE DATABASE ntbad;
USE ntbad;
CREATE TABLE `upload_files`
(
    `id`               int unsigned NOT NULL AUTO_INCREMENT,
    `origin_url`       varchar(500)      DEFAULT NULL COMMENT 'Lưu trữ URL gốc của tệp tin, nếu tệp được lấy từ một nguồn trực tuyến',
    `thumb_url`        varchar(500)      DEFAULT NULL COMMENT 'URL của thumbnail',
    `public_id`        varchar(255)      DEFAULT NULL COMMENT 'ID của tệp tin trên hệ thống lưu trữ tệp tin trực tuyến (ví dụ: Cloudinary)',
    `type`             tinyint  NOT NULL COMMENT 'Loại tệp tin: `0`: Hình ảnh, `1`: Video, `2`:PDF',
    `width`            int               DEFAULT NULL COMMENT 'Chiều rộng của tập tin (Áp dụng cho hình ảnh hoặc video) - Pixel',
    `height`           int               DEFAULT NULL COMMENT 'Chiều cao của tập tin (Áp dụng cho hình ảnh hoặc video) - Pixel',
    `size`             bigint            DEFAULT NULL COMMENT 'Kích thước tập tin - tính bằng byte',
    `deleted`          bit(1)   NOT NULL DEFAULT b'0' COMMENT 'Đánh dấu trạng thái xóa của bản ghi: `0`: Chưa xóa, `1`: Đã xóa',
    `created_at`       datetime NOT NULL,
    `updated_at`       datetime NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `permissions`
(
    `id`                int unsigned NOT NULL AUTO_INCREMENT,
    `title`             varchar(255)          DEFAULT NULL COMMENT 'Lưu trữ tiêu đề hoặc tên của quyền, giúp mô tả ngắn gọn về quyền đó',
    `permission`        varchar(255) NOT NULL COMMENT 'Lưu trữ tên quyền, dùng để xác định quyền trong hệ thống. Đây là một giá trị duy nhất trong bảng, đảm bảo không có hai quyền trùng nhau.',
    `parent_permission` varchar(255)          DEFAULT NULL COMMENT 'Lưu trữ quyền cha (parent permission), nếu quyền này thuộc một quyền cấp cao hơn nó',
    `is_view`           bit                   DEFAULT 0 COMMENT '`0`: là không có quyền xem, `1`: là có quyền xem',
    `is_write`          bit                   DEFAULT 0 COMMENT '`0`: là không có quyền ghi, `1`: là có quyền ghi',
    `is_approval`       bit                   DEFAULT 0 COMMENT '`0`: là không có quyền cập nhật, `1`: là có quyền cập nhật',
    `is_decision`       bit                   DEFAULT 0 COMMENT '`0`: là không có quyền xóa, `1`: là có quyền xóa',
    `type`              tinyint      NOT NULL COMMENT '`0`: Quyền của Admin, `1`: Quyền của Agency, `2`: Quyền của service provider',
    `status`            tinyint      NOT NULL DEFAULT '1' COMMENT 'Trạng thái của quyền. `1`: Đang hoạt động, `0`: Không hoạt động',
    `deleted`           bit          NOT NULL DEFAULT 0,
    `created_at`        timestamp NULL NOT NULL,
    `updated_at`        timestamp NULL NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `permission` (`permission`, `type`)
);

CREATE TABLE `roles`
(
    `id`         int unsigned NOT NULL AUTO_INCREMENT,
    `name`       varchar(255) NOT NULL COMMENT 'Tên của vai trò',
    `note`       TEXT                  DEFAULT NULL COMMENT 'Ghi chú bổ sung về vai trò',
    `type`       tinyint      NOT NULL COMMENT 'Loại vai trò, có thể dùng để phân loại vai trò',
    `status`     tinyint      NOT NULL DEFAULT '1' COMMENT 'Trạng thái hoạt động: `0`: Không hoạt động, 1: Hoạt động',
    `deleted`    bit          NOT NULL DEFAULT b'0',
    `created_at` timestamp NULL     DEFAULT NULL,
    `updated_at` timestamp NULL     DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `role_permission`
(
    `id`            int unsigned NOT NULL AUTO_INCREMENT,
    `role_id`       int unsigned NOT NULL COMMENT 'Khóa ngoại tham chiếu đến vai trò',
    `permission_id` int unsigned NOT NULL COMMENT 'Khóa ngoại tham chiếu đến quyền hạn',
    `deleted`       bit NOT NULL DEFAULT 0,
    `created_at`    timestamp NULL     DEFAULT NULL,
    `updated_at`    timestamp NULL     DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`role_id`, `permission_id`),
    FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
    FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`)
) ;

CREATE TABLE `branches`
(
    `id`         int unsigned NOT NULL AUTO_INCREMENT,
    `name`       text NOT NULL,
    `address`    text NOT NULL,
    `phone`      text NOT NULL,
    `deleted`    bit(1)       NOT NULL DEFAULT 0,
    `created_at` datetime     NOT NULL,
    `updated_at` datetime     NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `users`
(
    `id`         int unsigned NOT NULL AUTO_INCREMENT,
    `code`       varchar(50) NOT NULL COMMENT 'Mã người dùng duy nhất',
    `email`      varchar(255)         DEFAULT NULL,
    `name`       varchar(255)         DEFAULT NULL,
    `password`   varchar(255)         DEFAULT NULL COMMENT 'Mật khẩu của người dùng',
    `birthday`   date                 DEFAULT NULL,
    `gender`     tinyint              DEFAULT NULL,
    `role_id`    int unsigned          DEFAULT NULL COMMENT 'Khóa ngoại tham chiếu đến vai trò của người dùng',
    `avatar_id`  int unsigned          DEFAULT NULL COMMENT 'Khóa ngoại tham chiếu đến hình đại diện của người dùng',
    `status`     tinyint     NOT NULL COMMENT 'Trạng thái hoạt động của người dùng (0: Không hoạt động, 1: Hoạt động)',
    `deleted`    bit(1)      NOT NULL DEFAULT 0,
    `created_at` datetime    NOT NULL,
    `updated_at` datetime    NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`code`),
    FOREIGN KEY (`avatar_id`) REFERENCES `upload_files` (`id`),
    FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ;

CREATE TABLE `staffs`
(
    `id` 			int unsigned NOT NULL AUTO_INCREMENT,
    `branch_id`  	int UNSIGNED NOT NULL DEFAULT 0 COMMENT 'Khóa ngoại tham chiếu đến chi nhánh làm việc',
    `user_id`       int unsigned DEFAULT NULL,
    `deleted`       bit(1)       NOT NULL DEFAULT 0,
    `created_at` 	datetime     NOT NULL,
    `updated_at` 	datetime     NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    FOREIGN KEY (`branch_id`) REFERENCES `branches` (`id`)
) ;

CREATE TABLE `categories`
(
    `id`                int unsigned NOT NULL AUTO_INCREMENT,
    `name`              varchar(255) NOT NULL,
    `slug`              varchar(500) NOT NULL,
    `short_description` tinytext              DEFAULT NULL,
    `parent_id`         int unsigned DEFAULT NULL,
    `status`            tinyint      NOT NULL,
    `type`              tinyint      NOT NULL,
    `image_id`          int unsigned         DEFAULT NULL,
    `deleted`           bit(1)       NOT NULL DEFAULT 0,
    `created_at`        datetime     NOT NULL,
    `updated_at`        datetime     NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`slug`),
    FOREIGN KEY (`image_id`) REFERENCES `upload_files` (`id`)
);

CREATE TABLE `blogs`
(
    `id`                int unsigned NOT NULL AUTO_INCREMENT,
    `title`             varchar(255) NOT NULL,
    `slug`              varchar(500) NOT NULL,
    `short_description` tinytext     NOT NULL,
    `description`       text         NOT NULL,
    `image_id`          int unsigned       NOT NULL,
    `category_id`       int unsigned       NOT NULL,
    `user_id`           int unsigned       NOT NULL,
    `status`            tinyint      NOT NULL,
    `deleted`           bit(1)       NOT NULL DEFAULT 0,
    `publish_date`      datetime              DEFAULT NULL,
    `created_at`        datetime     NOT NULL,
    `updated_at`        datetime     NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`slug`),
    FOREIGN KEY (`image_id`) REFERENCES `upload_files` (`id`),
    FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ;

CREATE TABLE `products`
(
    `id`                int unsigned NOT NULL AUTO_INCREMENT,
    `name`              varchar(255) NOT NULL,
    `slug`              varchar(500) NOT NULL,
    `short_description` tinytext              DEFAULT NULL,
    `description`       text                  DEFAULT NULL,
    `brand`             text         NOT NULL,
    `status`            tinyint      NOT NULL,
    `category_id`       int unsigned      NOT NULL,
    `deleted`           bit(1)       NOT NULL DEFAULT 0,
    `created_at`        datetime     NOT NULL,
    `updated_at`        datetime     NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`slug`),
    FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
);

CREATE TABLE `product_options` (
                                   `id`            int unsigned NOT NULL AUTO_INCREMENT,
                                   `name`          varchar(255) NOT NULL, -- Tên của thuộc tính (ví dụ: size, color)
                                   `description`   text DEFAULT NULL,     -- Mô tả tùy chọn (nếu cần)
                                   `product_id`    int unsigned NOT NULL,
                                   `deleted`       bit(1)       NOT NULL DEFAULT 0,
                                   `created_at`    datetime NOT NULL,
                                   `updated_at`    datetime NOT NULL,
                                   PRIMARY KEY (`id`),
                                   FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
);

CREATE TABLE `product_option_values` (
                                         `id`                int unsigned NOT NULL AUTO_INCREMENT,
                                         `product_option_id` int unsigned NOT NULL, -- FK đến bảng product_options
                                         `value`             varchar(255) NOT NULL, -- Giá trị của thuộc tính (ví dụ: "36", "red")
                                         `deleted`           bit(1)      NOT NULL DEFAULT 0,
                                         `created_at`        datetime NOT NULL,
                                         `updated_at`        datetime NOT NULL,
                                         PRIMARY KEY (`id`),
                                         FOREIGN KEY (`product_option_id`) REFERENCES `product_options` (`id`) ON DELETE CASCADE
);

CREATE TABLE `product_variants` (
                                    `id`         INT UNSIGNED NOT NULL AUTO_INCREMENT,
                                    `product_id` INT UNSIGNED NOT NULL,
                                    `sku`        VARCHAR(100) NOT NULL UNIQUE, -- SKU riêng cho từng biến thể
                                    `quantity`   INT UNSIGNED NOT NULL DEFAULT 0,
                                    `price`      INT UNSIGNED NOT NULL DEFAULT 0, -- Giá riêng cho biến thể
                                    `deleted`    BIT(1) NOT NULL DEFAULT 0,
                                    `created_at` DATETIME NOT NULL,
                                    `updated_at` DATETIME NOT NULL,
                                    PRIMARY KEY (`id`),
                                    FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE
);

CREATE TABLE `product_variant_option_values` (
                                                 `id`                    INT UNSIGNED NOT NULL AUTO_INCREMENT,
                                                 `product_variant_id`     INT UNSIGNED NOT NULL,
                                                 `product_option_value_id` INT UNSIGNED NOT NULL,
                                                 `deleted`    BIT(1) NOT NULL DEFAULT 0,
                                                 `created_at` DATETIME NOT NULL,
                                                 `updated_at` DATETIME NOT NULL,
                                                 PRIMARY KEY (`id`),
                                                 FOREIGN KEY (`product_variant_id`) REFERENCES `product_variants` (`id`) ON DELETE CASCADE,
                                                 FOREIGN KEY (`product_option_value_id`) REFERENCES `product_option_values` (`id`) ON DELETE CASCADE
);

CREATE TABLE `discounts`
(
    `id`            		int unsigned NOT NULL AUTO_INCREMENT,
    `discount_percents`     int unsigned NOT NULL DEFAULT 0,
    `description`   		text     DEFAULT NULL,
    `time_started`			date NOT NULL,
    `time_ended`			date NOT NULL,
    `deleted`       		bit(1)   NOT NULL DEFAULT 0,
    `created_at`    		datetime NOT NULL,
    `updated_at`		    datetime NOT NULL,
    `product_id` 			int unsigned   NOT NULL,
    FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
    PRIMARY KEY (`id`)
);

CREATE TABLE `product_images`
(
    `id`         int unsigned NOT NULL AUTO_INCREMENT,
    `product_id` int unsigned   NOT NULL,
    `image_id`   int unsigned  NOT NULL,
    `type`       tinyint  NOT NULL,
    `deleted`    bit(1)   NOT NULL DEFAULT 0,
    `created_at` datetime NOT NULL,
    `updated_at` datetime NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
    FOREIGN KEY (`image_id`) REFERENCES `upload_files` (`id`)
);

CREATE TABLE `rating`
(
    `id`            int unsigned NOT NULL AUTO_INCREMENT,
    `product_id`    int unsigned NOT NULL,
    `user_id`       int unsigned NOT NULL,
    `rate`          int unsigned NOT NULL CHECK (`rate` BETWEEN 0 AND 5),
    `description`   text     DEFAULT NULL,
    `deleted`       bit(1)   NOT NULL DEFAULT 0,
    `created_at`    datetime NOT NULL,
    `updated_at`    datetime NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE ,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);

CREATE TABLE `rating_response`
(
    `id`            int unsigned NOT NULL AUTO_INCREMENT,
    `rating_id`    	int unsigned NOT NULL,
    `staff_id`      int unsigned NOT NULL,
    `description`   text     DEFAULT NULL,
    `deleted`       bit(1)   NOT NULL DEFAULT 0,
    `created_at`    datetime NOT NULL,
    `updated_at`    datetime NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`rating_id`) REFERENCES `rating` (`id`) ON DELETE CASCADE ,
    FOREIGN KEY (`staff_id`) REFERENCES `staffs` (`id`) ON DELETE CASCADE
);
CREATE TABLE `carts`
(
    `id`            int unsigned NOT NULL AUTO_INCREMENT,
    `product_variant_id`    int UNSIGNED DEFAULT NULL,
    `user_id`       int DEFAULT NULL,
    `quantity`      int DEFAULT NULL,
    `deleted`       bit(1)   NOT NULL DEFAULT 0,
    `created_at`    datetime NOT NULL,
    `updated_at`    datetime NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`product_variant_id`) REFERENCES `product_variants` (`id`)  ON DELETE CASCADE
);

CREATE TABLE `contacts`
(
    `id`            int unsigned NOT NULL AUTO_INCREMENT,
    `first_name`    varchar(255) NOT NULL,
    `last_name`     varchar(255) NOT NULL,
    `phone`         varchar(20)  NOT NULL,
    `email`         varchar(255) NOT NULL,
    `city`              int unsigned DEFAULT NULL,
    `district`          int unsigned DEFAULT NULL,
    `street_address`    varchar(255) NOT NULL,
    `note`          text                  DEFAULT NULL,
    `type`          tinyint      NOT NULL, -- 0 - Contact chính của user, 1 - Contact phụ của user
    `deleted`       bit(1)       NOT NULL DEFAULT 0,
    `created_at`    datetime     NOT NULL,
    `updated_at`    datetime     NOT NULL,
    `user_id`       int unsigned DEFAULT NULL, -- Khóa ngoại tham chiếu đến người dùng
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

CREATE TABLE `orders`
(
    `id`            int unsigned NOT NULL AUTO_INCREMENT,
    `user_id`       int unsigned NOT NULL,
    `contact_id`	int unsigned DEFAULT NULL,
    `payment_method` tinyint DEFAULT NULL COMMENT '0 - Thanh toán khi nhận hàng, 1 - Thanh toán qua thẻ',
    `payment_status` tinyint NOT NULL COMMENT '0 - Chưa thanh toán, 1 - Đã thanh toán',
    `delivery_status`        tinyint NOT NULL COMMENT '0 - Chờ vận chuyển, 1 - Đang vận chuyển , 2 - Đã giao hàng , 3 - Đã hủy',
    `deleted`       bit(1)   NOT NULL DEFAULT 0,
    `created_at`    datetime NOT NULL,
    `updated_at`    datetime NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    FOREIGN KEY (`payment_id`) REFERENCES `payments` (`id`),
    FOREIGN KEY (`contact_id`) REFERENCES `contacts` (`id`)
);

CREATE TABLE `order_items`
(
    `id`            int unsigned NOT NULL AUTO_INCREMENT,
    `order_id`      int unsigned NOT NULL,
    `product_variant_id`    int unsigned NOT NULL,
    `quantity`      int unsigned NOT NULL,
    `deleted`       bit(1)   NOT NULL DEFAULT 0,
    `created_at`    datetime NOT NULL,
    `updated_at`    datetime NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
    FOREIGN KEY (`product_variant_id`) REFERENCES `product_variants` (`id`)
);

--
--
-- INSERT INTO `permissions` (`title`, `permission`, `parent_permission`, `is_view`, `is_write`, `is_approval`, `is_decision`, `type`, `status`, `deleted`, `created_at`, `updated_at`)
-- VALUES
--     ('ADMIN', 'Everything', NULL, 1, 1, 1, 1, 1, 1, 1, NOW(), NOW()),
--     ('USER', 'Nothing', NULL, 0, 0, 0, 0, 0, 1, 1, NOW(), NOW());
--
-- INSERT INTO `roles` (`name`, `note`, `type`, `status`, `deleted`, `created_at`, `updated_at`)
-- VALUES
--     ('ADMIN', 'Quản trị viên hệ thống', 1, 1, 0, NOW(), NOW()),
--     ('USER', 'Người dùng', 1, 1, 0, NOW(), NOW());
--
--
-- INSERT INTO `role_permission` (`role_id`, `permission_id`, `deleted`, `created_at`, `updated_at`)
-- VALUES
--     (1, 1, 0, NOW(), NOW()),
--     (2, 2, 0, NOW(), NOW());
--
-- INSERT INTO `branches` (`name`, `address`, `phone`, `deleted`, `created_at`, `updated_at`)
-- VALUES
--     ('Chi nhánh Hà Nội', '123 Đường Láng, Hà Nội', '0123456789', 0, NOW(), NOW()),
--     ('Chi nhánh Hồ Chí Minh', '456 Đường Nguyễn Văn Linh, TP.HCM', '0987654321', 0, NOW(), NOW());
--
-- INSERT INTO upload_files (origin_url, thumb_url, public_id, type, width, height, size, deleted, created_at, updated_at)
-- VALUES
--     ('https://example.com/image1.jpg', 'https://example.com/thumb1.jpg', 'img_12345', 0, 800, 600, 204800, 0, NOW(), NOW()),
--     ('https://example.com/image2.jpg', 'https://example.com/thumb2.jpg', 'img_67890', 0, 1280, 720, 409600, 0, NOW(), NOW());
--
-- INSERT INTO categories (id, name, slug, short_description, parent_id, status, type, image_id, deleted, created_at, updated_at)
-- VALUES
-- -- Danh mục chính
-- (1, 'Công nghệ', 'cong-nghe', 'Chuyên mục về công nghệ', NULL, 1, 0, NULL, 0, NOW(), NOW()),
-- (2, 'Thể thao', 'the-thao', 'Chuyên mục về thể thao', NULL, 1, 0, NULL, 0, NOW(), NOW()),
--
-- (3, 'Giày Cầu Lông', 'giay-cau-long', 'Các loại giày chuyên dụng cho cầu lông', 2, 1, 1, NULL, 0, NOW(), NOW()),
-- (4, 'Vợt Cầu Lông', 'vot-cau-long', 'Các loại vợt phù hợp cho mọi trình độ', 2, 1, 1, NULL, 0, NOW(), NOW()),
-- (5, 'Quần Áo Cầu Lông', 'quan-ao-cau-long', 'Trang phục thể thao cầu lông', 2, 1, 1, NULL, 0, NOW(), NOW()),
-- (6, 'Phụ Kiện Cầu Lông', 'phu-kien-cau-long', 'Các loại phụ kiện như túi đựng, băng cổ tay', 2, 1, 1, NULL, 0, NOW(), NOW()),
--
-- -- Danh mục con
-- (7, 'Giày Yonex', 'giay-yonex', 'Giày cầu lông Yonex chính hãng', 3, 1, 1, NULL, 0, NOW(), NOW()),
-- (8, 'Giày Lining', 'giay-lining', 'Giày cầu lông Lining chính hãng', 3, 1, 1, NULL, 0, NOW(), NOW()),
-- (9, 'Vợt Yonex', 'vot-yonex', 'Vợt cầu lông Yonex chính hãng', 4, 1, 1, NULL, 0, NOW(), NOW()),
-- (10, 'Vợt Lining', 'vot-lining', 'Vợt cầu lông Lining chính hãng', 4, 1, 1, NULL, 0, NOW(), NOW());
--
--
--
-- INSERT INTO products (name, slug, short_description, description, brand, status, category_id, deleted, created_at, updated_at)
-- VALUES
--     ('Giày Cầu Lông Yonex 65Z3', 'giay-yonex-65z3', 'Giày cầu lông cao cấp', 'Công nghệ chống lật cổ chân, bám sân tốt', 'Yonex', 1, 7, 0, NOW(), NOW()),
--     ('Giày Cầu Lông Lining AYAS006', 'giay-lining-ayas006', 'Giày cầu lông chuyên nghiệp', 'Thiết kế thoáng khí, đế cao su chống trơn', 'Lining', 1, 8, 0, NOW(), NOW()),
--     ('Vợt Cầu Lông Yonex Astrox 88D Pro', 'vot-yonex-astrox-88d', 'Vợt tấn công mạnh mẽ', 'Trọng lượng nhẹ, trợ lực cao', 'Yonex', 1, 9, 0, NOW(), NOW()),
--     ('Vợt Cầu Lông Lining Aeronaut 9000C', 'vot-lining-aeronaut-9000c', 'Vợt điều khiển linh hoạt', 'Khung carbon bền, trợ lực tốt', 'Lining', 1, 10, 0, NOW(), NOW()),
--     ('Quần Áo Cầu Lông Yonex 2024', 'quan-ao-yonex-2024', 'Bộ quần áo thể thao', 'Chất liệu thấm hút mồ hôi tốt', 'Yonex', 1, 5, 0, NOW(), NOW());
--
-- INSERT INTO product_options (name, description, product_id, deleted, created_at, updated_at)
-- VALUES
--     ('Size Giày', 'Kích cỡ giày cầu lông', 1, 0, NOW(), NOW()),
--     ('Size Giày', 'Kích cỡ giày cầu lông', 2, 0, NOW(), NOW()),
--     ('Màu Giày', 'Màu sắc giày cầu lông', 1, 0, NOW(), NOW()),
--     ('Màu Giày', 'Màu sắc giày cầu lông', 2, 0, NOW(), NOW()),
--     ('Trọng Lượng Vợt', 'Trọng lượng vợt cầu lông', 3, 0, NOW(), NOW()),
--     ('Trọng Lượng Vợt', 'Trọng lượng vợt cầu lông', 4, 0, NOW(), NOW()),
--     ('Size Áo', 'Size quần áo thể thao', 5, 0, NOW(), NOW()),
--     ('Màu Áo', 'Màu sắc quần áo thể thao', 5, 0, NOW(), NOW());
--
-- INSERT INTO product_option_values (product_option_id, value, deleted, created_at, updated_at)
-- VALUES
--     -- Size Giày
--     (1, '39', 0, NOW(), NOW()),
--     (1, '40', 0, NOW(), NOW()),
--     (2, '41', 0, NOW(), NOW()),
--     (2, '42', 0, NOW(), NOW()),
--     -- Màu Giày
--     (3, 'Trắng/Xanh', 0, NOW(), NOW()),
--     (3, 'Đen/Đỏ', 0, NOW(), NOW()),
--     (4, 'Xanh Lá', 0, NOW(), NOW()),
--     (4, 'Xám/Bạc', 0, NOW(), NOW()),
--     -- Trọng Lượng Vợt
--     (5, '3U (85-89g)', 0, NOW(), NOW()),
--     (5, '4U (80-84g)', 0, NOW(), NOW()),
--     (6, '3U (85-89g)', 0, NOW(), NOW()),
--     (6, '5U (75-79g)', 0, NOW(), NOW()),
--     -- Size Áo
--     (7, 'M', 0, NOW(), NOW()),
--     (7, 'L', 0, NOW(), NOW()),
--     (7, 'XL', 0, NOW(), NOW()),
--     -- Màu Áo
--     (8, 'Trắng', 0, NOW(), NOW()),
--     (8, 'Xanh Dương', 0, NOW(), NOW()),
--     (8, 'Đỏ', 0, NOW(), NOW());
--
-- INSERT INTO product_variants (product_id, sku, quantity, price, deleted, created_at, updated_at)
-- VALUES
--     (1, 'YONEX-65Z3-39-TRANG-XANH', 10, 2000000, 0, NOW(), NOW()),
--     (2, 'LINING-AYAS006-41-XANH-LA', 5, 1800000, 0, NOW(), NOW()),
--     (3, 'YONEX-ASTROX-88D-3U', 12, 3000000, 0, NOW(), NOW()),
--     (4, 'LINING-AERONAUT-9000C-5U', 11, 2800000, 0, NOW(), NOW()),
--     (5, 'YONEX-AO-2024-M-TRANG', 20, 500000, 0, NOW(), NOW());
--
-- INSERT INTO product_variant_option_values (product_variant_id, product_option_value_id, deleted, created_at, updated_at)
-- VALUES
-- -- Giày Yonex 65Z3 (Size: 39, Màu: Trắng/Xanh)
-- (1, 1, 0, NOW(), NOW()),  -- Size 39
-- (1, 5, 0, NOW(), NOW()),  -- Trắng/Xanh
--
-- -- Giày Lining AYAS006 (Size: 41, Màu: Xanh Lá)
-- (2, 3, 0, NOW(), NOW()),  -- Size 41
-- (2, 7, 0, NOW(), NOW()),  -- Xanh Lá
--
-- -- Vợt Yonex Astrox 88D (Trọng Lượng: 3U)
-- (3, 9, 0, NOW(), NOW()),  -- 3U (85-89g)
--
-- -- Vợt Lining Aeronaut 9000C (Trọng Lượng: 5U)
-- (4, 12, 0, NOW(), NOW()), -- 5U (75-79g)
--
-- -- Áo Yonex 2024 (Size: M, Màu: Trắng)
-- (5, 13, 0, NOW(), NOW()), -- Size M
-- (5, 16, 0, NOW(), NOW()); -- Màu Trắng

