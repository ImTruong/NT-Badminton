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
    `deleted`           bit(1)       NOT NULL DEFAULT 0,
    `created_at`        datetime     NOT NULL,
    `updated_at`        datetime     NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`slug`)
);

CREATE TABLE `products`
(
    `id`                int unsigned NOT NULL AUTO_INCREMENT,
    `name`              varchar(255) NOT NULL,
    `slug`              varchar(500) NOT NULL,
    `short_description` tinytext              DEFAULT NULL,
    `description`       text                  DEFAULT NULL,
    `brand`             text         NOT NULL,
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

CREATE TABLE `cities`
(
    `id`            int unsigned NOT NULL AUTO_INCREMENT,
    `name`          varchar(255) NOT NULL,
    `deleted`       bit(1)       NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
);

CREATE TABLE `districts`
(
    `id`            int unsigned NOT NULL AUTO_INCREMENT,
    `name`          varchar(255) NOT NULL,
    `deleted`       bit(1)       NOT NULL DEFAULT 0,
    `city_id`       int unsigned DEFAULT NULL, -- Khóa ngoại tham chiếu đến thành phố
    PRIMARY KEY (`id`),
    FOREIGN KEY (`city_id`) REFERENCES `cities` (`id`) ON DELETE SET NULL
);

CREATE TABLE `wards`
(
    `id`            int unsigned NOT NULL AUTO_INCREMENT,
    `name`          varchar(255) NOT NULL,
    `deleted`       bit(1)       NOT NULL DEFAULT 0,
    `district_id`   int unsigned DEFAULT NULL, -- Khóa ngoại tham chiếu đến quận/huyện
    PRIMARY KEY (`id`),
    FOREIGN KEY (`district_id`) REFERENCES `districts` (`id`) ON DELETE SET NULL
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
    `street_address`    varchar(255) DEFAULT NULL,
    `note`          text                  DEFAULT NULL,
    `type`          varchar(20)      NOT NULL,
    `deleted`       bit(1)       NOT NULL DEFAULT 0,
    `created_at`    datetime     NOT NULL,
    `updated_at`    datetime     NOT NULL,
    `user_id`       int unsigned DEFAULT NULL, -- Khóa ngoại tham chiếu đến người dùng
    `city_id`       int unsigned DEFAULT NULL, -- Khóa ngoại tham chiếu đến thành phố
    `district_id`   int unsigned DEFAULT NULL, -- Khóa ngoại tham chiếu đến quận/huyện
    `ward_id`       int unsigned DEFAULT NULL, -- Khóa ngoại tham chiếu đến phường/xã
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`city_id`) REFERENCES `cities` (`id`) ON DELETE SET NULL,
    FOREIGN KEY (`district_id`) REFERENCES `districts` (`id`) ON DELETE SET NULL,
    FOREIGN KEY (`ward_id`) REFERENCES `wards` (`id`) ON DELETE SET NULL
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
-- -- Danh mục chính
-- INSERT INTO categories(id, name, slug, short_description, parent_id, deleted, created_at, updated_at)
-- VALUES
-- -- Cha
-- (1, 'Giày', 'giay', 'Các loại giày thể thao cầu lông', NULL, b'0', NOW(), NOW()),
-- (2, 'Vợt Cầu Lông', 'vot-cau-long', 'Các loại vợt phù hợp cho mọi trình độ', NULL, b'0', NOW(), NOW()),
-- (3, 'Quần Áo Cầu Lông', 'quan-ao-cau-long', 'Trang phục thể thao cầu lông', NULL, b'0', NOW(), NOW()),
-- (4, 'Phụ Kiện Cầu Lông', 'phu-kien-cau-long', 'Các loại phụ kiện như túi đựng, băng cổ tay', NULL, b'0', NOW(), NOW()),
--
-- -- Con của Giày
-- (5, 'Giày Yonex', 'giay-yonex', 'Giày cầu lông Yonex chính hãng', 1, b'0', NOW(), NOW()),
-- (6, 'Giày Lining', 'giay-lining', 'Giày cầu lông Lining chính hãng', 1, b'0', NOW(), NOW()),
--
-- -- Con của Vợt Cầu Lông
-- (7, 'Vợt Yonex', 'vot-yonex', 'Vợt cầu lông Yonex chính hãng', 2, b'0', NOW(), NOW()),
-- (8, 'Vợt Lining', 'vot-lining', 'Vợt cầu lông Lining chính hãng', 2, b'0', NOW(), NOW());
--
-- -- Sản phẩm (giữ nguyên categories đã có)
-- INSERT INTO products (name, slug, short_description, description, brand, category_id, deleted, created_at, updated_at)
-- VALUES
--     ('Giày Cầu Lông Yonex 65Z3', 'giay-yonex-65z3', 'Giày cầu lông cao cấp', 'Công nghệ chống lật cổ chân, bám sân tốt', 'Yonex', 5, 0, NOW(), NOW()),
--     ('Giày Cầu Lông Lining AYAS006', 'giay-lining-ayas006', 'Giày cầu lông chuyên nghiệp', 'Thiết kế thoáng khí, đế cao su chống trơn', 'Lining', 6, 0, NOW(), NOW()),
--     ('Vợt Cầu Lông Yonex Astrox 88D Pro', 'vot-yonex-astrox-88d', 'Vợt tấn công mạnh mẽ', 'Trọng lượng nhẹ, trợ lực cao', 'Yonex', 7, 0, NOW(), NOW()),
--     ('Vợt Cầu Lông Lining Aeronaut 9000C', 'vot-lining-aeronaut-9000c', 'Vợt điều khiển linh hoạt', 'Khung carbon bền, trợ lực tốt', 'Lining', 8, 0, NOW(), NOW()),
--     ('Quần Áo Cầu Lông Yonex 2024', 'quan-ao-yonex-2024', 'Bộ quần áo thể thao', 'Chất liệu thấm hút mồ hôi tốt', 'Yonex', 3, 0, NOW(), NOW());
--
-- -- Product Options
-- INSERT INTO product_options (name, description, product_id, deleted, created_at, updated_at)
-- VALUES
--     -- Giày Yonex 65Z3 (product_id = 1)
--     ('Size Giày', 'Kích cỡ giày cầu lông', 1, 0, NOW(), NOW()),
--     ('Màu Giày', 'Màu sắc giày cầu lông', 1, 0, NOW(), NOW()),
--
--     -- Giày Lining AYAS006 (product_id = 2)
--     ('Size Giày', 'Kích cỡ giày cầu lông', 2, 0, NOW(), NOW()),
--     ('Màu Giày', 'Màu sắc giày cầu lông', 2, 0, NOW(), NOW()),
--
--     -- Vợt Yonex Astrox 88D (product_id = 3)
--     ('Trọng Lượng Vợt', 'Trọng lượng vợt cầu lông', 3, 0, NOW(), NOW()),
--     ('Màu Vợt', 'Màu sắc vợt cầu lông', 3, 0, NOW(), NOW()),
--
--     -- Vợt Lining Aeronaut 9000C (product_id = 4)
--     ('Trọng Lượng Vợt', 'Trọng lượng vợt cầu lông', 4, 0, NOW(), NOW()),
--     ('Màu Vợt', 'Màu sắc vợt cầu lông', 4, 0, NOW(), NOW()),
--
--     -- Quần Áo Yonex 2024 (product_id = 5)
--     ('Size Áo', 'Size quần áo thể thao', 5, 0, NOW(), NOW()),
--     ('Màu Áo', 'Màu sắc quần áo thể thao', 5, 0, NOW(), NOW());
--
-- -- Product Option Values
-- INSERT INTO product_option_values (product_option_id, value, deleted, created_at, updated_at)
-- VALUES
--     -- Size Giày Yonex (option_id = 1)
--     (1, '39', 0, NOW(), NOW()),
--     (1, '40', 0, NOW(), NOW()),
--     (1, '41', 0, NOW(), NOW()),
--     (1, '42', 0, NOW(), NOW()),
--
--     -- Màu Giày Yonex (option_id = 2)
--     (2, 'Trắng/Xanh', 0, NOW(), NOW()),
--     (2, 'Đen/Đỏ', 0, NOW(), NOW()),
--     (2, 'Xanh/Vàng', 0, NOW(), NOW()),
--
--     -- Size Giày Lining (option_id = 3)
--     (3, '39', 0, NOW(), NOW()),
--     (3, '40', 0, NOW(), NOW()),
--     (3, '41', 0, NOW(), NOW()),
--     (3, '42', 0, NOW(), NOW()),
--     (3, '43', 0, NOW(), NOW()),
--
--     -- Màu Giày Lining (option_id = 4)
--     (4, 'Xanh Lá', 0, NOW(), NOW()),
--     (4, 'Xám/Bạc', 0, NOW(), NOW()),
--     (4, 'Đen/Trắng', 0, NOW(), NOW()),
--
--     -- Trọng Lượng Vợt Yonex (option_id = 5)
--     (5, '3U (85-89g)', 0, NOW(), NOW()),
--     (5, '4U (80-84g)', 0, NOW(), NOW()),
--
--     -- Màu Vợt Yonex (option_id = 6)
--     (6, 'Đen/Đỏ', 0, NOW(), NOW()),
--     (6, 'Xanh/Vàng', 0, NOW(), NOW()),
--
--     -- Trọng Lượng Vợt Lining (option_id = 7)
--     (7, '3U (85-89g)', 0, NOW(), NOW()),
--     (7, '4U (80-84g)', 0, NOW(), NOW()),
--     (7, '5U (75-79g)', 0, NOW(), NOW()),
--
--     -- Màu Vợt Lining (option_id = 8)
--     (8, 'Xanh Dương', 0, NOW(), NOW()),
--     (8, 'Đỏ/Đen', 0, NOW(), NOW()),
--
--     -- Size Áo (option_id = 9)
--     (9, 'S', 0, NOW(), NOW()),
--     (9, 'M', 0, NOW(), NOW()),
--     (9, 'L', 0, NOW(), NOW()),
--     (9, 'XL', 0, NOW(), NOW()),
--
--     -- Màu Áo (option_id = 10)
--     (10, 'Trắng', 0, NOW(), NOW()),
--     (10, 'Xanh Dương', 0, NOW(), NOW()),
--     (10, 'Đỏ', 0, NOW(), NOW()),
--     (10, 'Đen', 0, NOW(), NOW());
--
-- -- Product Variants - Tạo đầy đủ các tổ hợp
-- INSERT INTO product_variants (product_id, sku, quantity, price, deleted, created_at, updated_at)
-- VALUES
--     -- Giày Yonex 65Z3 (4 size x 3 màu = 12 variants)
--     (1, 'YONEX-65Z3-39-TRANG-XANH', 8, 2000000, 0, NOW(), NOW()),
--     (1, 'YONEX-65Z3-39-DEN-DO', 6, 2000000, 0, NOW(), NOW()),
--     (1, 'YONEX-65Z3-39-XANH-VANG', 5, 2000000, 0, NOW(), NOW()),
--     (1, 'YONEX-65Z3-40-TRANG-XANH', 10, 2000000, 0, NOW(), NOW()),
--     (1, 'YONEX-65Z3-40-DEN-DO', 7, 2000000, 0, NOW(), NOW()),
--     (1, 'YONEX-65Z3-40-XANH-VANG', 4, 2000000, 0, NOW(), NOW()),
--     (1, 'YONEX-65Z3-41-TRANG-XANH', 12, 2000000, 0, NOW(), NOW()),
--     (1, 'YONEX-65Z3-41-DEN-DO', 9, 2000000, 0, NOW(), NOW()),
--     (1, 'YONEX-65Z3-41-XANH-VANG', 6, 2000000, 0, NOW(), NOW()),
--     (1, 'YONEX-65Z3-42-TRANG-XANH', 15, 2000000, 0, NOW(), NOW()),
--     (1, 'YONEX-65Z3-42-DEN-DO', 8, 2000000, 0, NOW(), NOW()),
--     (1, 'YONEX-65Z3-42-XANH-VANG', 3, 2000000, 0, NOW(), NOW()),
--
--     -- Giày Lining AYAS006 (5 size x 3 màu = 15 variants)
--     (2, 'LINING-AYAS006-39-XANH-LA', 5, 1800000, 0, NOW(), NOW()),
--     (2, 'LINING-AYAS006-39-XAM-BAC', 4, 1800000, 0, NOW(), NOW()),
--     (2, 'LINING-AYAS006-39-DEN-TRANG', 6, 1800000, 0, NOW(), NOW()),
--     (2, 'LINING-AYAS006-40-XANH-LA', 7, 1800000, 0, NOW(), NOW()),
--     (2, 'LINING-AYAS006-40-XAM-BAC', 8, 1800000, 0, NOW(), NOW()),
--     (2, 'LINING-AYAS006-40-DEN-TRANG', 5, 1800000, 0, NOW(), NOW()),
--     (2, 'LINING-AYAS006-41-XANH-LA', 10, 1800000, 0, NOW(), NOW()),
--     (2, 'LINING-AYAS006-41-XAM-BAC', 6, 1800000, 0, NOW(), NOW()),
--     (2, 'LINING-AYAS006-41-DEN-TRANG', 9, 1800000, 0, NOW(), NOW()),
--     (2, 'LINING-AYAS006-42-XANH-LA', 12, 1800000, 0, NOW(), NOW()),
--     (2, 'LINING-AYAS006-42-XAM-BAC', 7, 1800000, 0, NOW(), NOW()),
--     (2, 'LINING-AYAS006-42-DEN-TRANG', 8, 1800000, 0, NOW(), NOW()),
--     (2, 'LINING-AYAS006-43-XANH-LA', 4, 1800000, 0, NOW(), NOW()),
--     (2, 'LINING-AYAS006-43-XAM-BAC', 3, 1800000, 0, NOW(), NOW()),
--     (2, 'LINING-AYAS006-43-DEN-TRANG', 5, 1800000, 0, NOW(), NOW()),
--
--     -- Vợt Yonex Astrox 88D (2 trọng lượng x 2 màu = 4 variants)
--     (3, 'YONEX-ASTROX-88D-3U-DEN-DO', 8, 3000000, 0, NOW(), NOW()),
--     (3, 'YONEX-ASTROX-88D-3U-XANH-VANG', 6, 3000000, 0, NOW(), NOW()),
--     (3, 'YONEX-ASTROX-88D-4U-DEN-DO', 10, 3000000, 0, NOW(), NOW()),
--     (3, 'YONEX-ASTROX-88D-4U-XANH-VANG', 7, 3000000, 0, NOW(), NOW()),
--
--     -- Vợt Lining Aeronaut 9000C (3 trọng lượng x 2 màu = 6 variants)
--     (4, 'LINING-AERONAUT-9000C-3U-XANH-DUONG', 5, 2800000, 0, NOW(), NOW()),
--     (4, 'LINING-AERONAUT-9000C-3U-DO-DEN', 7, 2800000, 0, NOW(), NOW()),
--     (4, 'LINING-AERONAUT-9000C-4U-XANH-DUONG', 9, 2800000, 0, NOW(), NOW()),
--     (4, 'LINING-AERONAUT-9000C-4U-DO-DEN', 6, 2800000, 0, NOW(), NOW()),
--     (4, 'LINING-AERONAUT-9000C-5U-XANH-DUONG', 8, 2800000, 0, NOW(), NOW()),
--     (4, 'LINING-AERONAUT-9000C-5U-DO-DEN', 4, 2800000, 0, NOW(), NOW()),
--
--     -- Quần Áo Yonex 2024 (4 size x 4 màu = 16 variants)
--     (5, 'YONEX-AO-2024-S-TRANG', 15, 500000, 0, NOW(), NOW()),
--     (5, 'YONEX-AO-2024-S-XANH-DUONG', 12, 500000, 0, NOW(), NOW()),
--     (5, 'YONEX-AO-2024-S-DO', 8, 500000, 0, NOW(), NOW()),
--     (5, 'YONEX-AO-2024-S-DEN', 10, 500000, 0, NOW(), NOW()),
--     (5, 'YONEX-AO-2024-M-TRANG', 20, 500000, 0, NOW(), NOW()),
--     (5, 'YONEX-AO-2024-M-XANH-DUONG', 18, 500000, 0, NOW(), NOW()),
--     (5, 'YONEX-AO-2024-M-DO', 15, 500000, 0, NOW(), NOW()),
--     (5, 'YONEX-AO-2024-M-DEN', 16, 500000, 0, NOW(), NOW()),
--     (5, 'YONEX-AO-2024-L-TRANG', 25, 500000, 0, NOW(), NOW()),
--     (5, 'YONEX-AO-2024-L-XANH-DUONG', 22, 500000, 0, NOW(), NOW()),
--     (5, 'YONEX-AO-2024-L-DO', 18, 500000, 0, NOW(), NOW()),
--     (5, 'YONEX-AO-2024-L-DEN', 20, 500000, 0, NOW(), NOW()),
--     (5, 'YONEX-AO-2024-XL-TRANG', 12, 500000, 0, NOW(), NOW()),
--     (5, 'YONEX-AO-2024-XL-XANH-DUONG', 10, 500000, 0, NOW(), NOW()),
--     (5, 'YONEX-AO-2024-XL-DO', 8, 500000, 0, NOW(), NOW()),
--     (5, 'YONEX-AO-2024-XL-DEN', 9, 500000, 0, NOW(), NOW());
--
-- -- Product Variant Option Values - Liên kết từng variant với các option values tương ứng
-- INSERT INTO product_variant_option_values (product_variant_id, product_option_value_id, deleted, created_at, updated_at)
-- VALUES
--     -- Giày Yonex 65Z3 variants (12 variants)
--     -- Variant 1: Size 39 + Trắng/Xanh
--     (1, 1, 0, NOW(), NOW()),   -- Size 39
--     (1, 5, 0, NOW(), NOW()),   -- Trắng/Xanh
--     -- Variant 2: Size 39 + Đen/Đỏ
--     (2, 1, 0, NOW(), NOW()),   -- Size 39
--     (2, 6, 0, NOW(), NOW()),   -- Đen/Đỏ
--     -- Variant 3: Size 39 + Xanh/Vàng
--     (3, 1, 0, NOW(), NOW()),   -- Size 39
--     (3, 7, 0, NOW(), NOW()),   -- Xanh/Vàng
--     -- Variant 4: Size 40 + Trắng/Xanh
--     (4, 2, 0, NOW(), NOW()),   -- Size 40
--     (4, 5, 0, NOW(), NOW()),   -- Trắng/Xanh
--     -- Variant 5: Size 40 + Đen/Đỏ
--     (5, 2, 0, NOW(), NOW()),   -- Size 40
--     (5, 6, 0, NOW(), NOW()),   -- Đen/Đỏ
--     -- Variant 6: Size 40 + Xanh/Vàng
--     (6, 2, 0, NOW(), NOW()),   -- Size 40
--     (6, 7, 0, NOW(), NOW()),   -- Xanh/Vàng
--     -- Variant 7: Size 41 + Trắng/Xanh
--     (7, 3, 0, NOW(), NOW()),   -- Size 41
--     (7, 5, 0, NOW(), NOW()),   -- Trắng/Xanh
--     -- Variant 8: Size 41 + Đen/Đỏ
--     (8, 3, 0, NOW(), NOW()),   -- Size 41
--     (8, 6, 0, NOW(), NOW()),   -- Đen/Đỏ
--     -- Variant 9: Size 41 + Xanh/Vàng
--     (9, 3, 0, NOW(), NOW()),   -- Size 41
--     (9, 7, 0, NOW(), NOW()),   -- Xanh/Vàng
--     -- Variant 10: Size 42 + Trắng/Xanh
--     (10, 4, 0, NOW(), NOW()),  -- Size 42
--     (10, 5, 0, NOW(), NOW()),  -- Trắng/Xanh
--     -- Variant 11: Size 42 + Đen/Đỏ
--     (11, 4, 0, NOW(), NOW()),  -- Size 42
--     (11, 6, 0, NOW(), NOW()),  -- Đen/Đỏ
--     -- Variant 12: Size 42 + Xanh/Vàng
--     (12, 4, 0, NOW(), NOW()),  -- Size 42
--     (12, 7, 0, NOW(), NOW()),  -- Xanh/Vàng
--
--     -- Giày Lining AYAS006 variants (15 variants)
--     -- Size 39
--     (13, 8, 0, NOW(), NOW()),  -- Size 39
--     (13, 12, 0, NOW(), NOW()), -- Xanh Lá
--     (14, 8, 0, NOW(), NOW()),  -- Size 39
--     (14, 13, 0, NOW(), NOW()), -- Xám/Bạc
--     (15, 8, 0, NOW(), NOW()),  -- Size 39
--     (15, 14, 0, NOW(), NOW()), -- Đen/Trắng
--     -- Size 40
--     (16, 9, 0, NOW(), NOW()),  -- Size 40
--     (16, 12, 0, NOW(), NOW()), -- Xanh Lá
--     (17, 9, 0, NOW(), NOW()),  -- Size 40
--     (17, 13, 0, NOW(), NOW()), -- Xám/Bạc
--     (18, 9, 0, NOW(), NOW()),  -- Size 40
--     (18, 14, 0, NOW(), NOW()), -- Đen/Trắng
--     -- Size 41
--     (19, 10, 0, NOW(), NOW()), -- Size 41
--     (19, 12, 0, NOW(), NOW()), -- Xanh Lá
--     (20, 10, 0, NOW(), NOW()), -- Size 41
--     (20, 13, 0, NOW(), NOW()), -- Xám/Bạc
--     (21, 10, 0, NOW(), NOW()), -- Size 41
--     (21, 14, 0, NOW(), NOW()), -- Đen/Trắng
--     -- Size 42
--     (22, 11, 0, NOW(), NOW()), -- Size 42
--     (22, 12, 0, NOW(), NOW()), -- Xanh Lá
--     (23, 11, 0, NOW(), NOW()), -- Size 42
--     (23, 13, 0, NOW(), NOW()), -- Xám/Bạc
--     (24, 11, 0, NOW(), NOW()), -- Size 42
--     (24, 14, 0, NOW(), NOW()), -- Đen/Trắng
--     -- Size 43
--     (25, 12, 0, NOW(), NOW()), -- Size 43
--     (25, 12, 0, NOW(), NOW()), -- Xanh Lá
--     (26, 12, 0, NOW(), NOW()), -- Size 43
--     (26, 13, 0, NOW(), NOW()), -- Xám/Bạc
--     (27, 12, 0, NOW(), NOW()), -- Size 43
--     (27, 14, 0, NOW(), NOW()), -- Đen/Trắng
--
--     -- Vợt Yonex Astrox 88D variants (4 variants)
--     -- 3U variants
--     (28, 15, 0, NOW(), NOW()), -- 3U
--     (28, 17, 0, NOW(), NOW()), -- Đen/Đỏ
--     (29, 15, 0, NOW(), NOW()), -- 3U
--     (29, 18, 0, NOW(), NOW()), -- Xanh/Vàng
--     -- 4U variants
--     (30, 16, 0, NOW(), NOW()), -- 4U
--     (30, 17, 0, NOW(), NOW()), -- Đen/Đỏ
--     (31, 16, 0, NOW(), NOW()), -- 4U
--     (31, 18, 0, NOW(), NOW()), -- Xanh/Vàng
--
--     -- Vợt Lining Aeronaut 9000C variants (6 variants)
--     -- 3U variants
--     (32, 19, 0, NOW(), NOW()), -- 3U
--     (32, 21, 0, NOW(), NOW()), -- Xanh Dương
--     (33, 19, 0, NOW(), NOW()), -- 3U
--     (33, 22, 0, NOW(), NOW()), -- Đỏ/Đen
--     -- 4U variants
--     (34, 20, 0, NOW(), NOW()), -- 4U
--     (34, 21, 0, NOW(), NOW()), -- Xanh Dương
--     (35, 20, 0, NOW(), NOW()), -- 4U
--     (35, 22, 0, NOW(), NOW()), -- Đỏ/Đen
--     -- 5U variants
--     (36, 21, 0, NOW(), NOW()), -- 5U
--     (36, 21, 0, NOW(), NOW()), -- Xanh Dương
--     (37, 21, 0, NOW(), NOW()), -- 5U
--     (37, 22, 0, NOW(), NOW()), -- Đỏ/Đen
--
--     -- Quần Áo Yonex 2024 variants (16 variants)
--     -- Size S
--     (38, 23, 0, NOW(), NOW()), -- Size S
--     (38, 26, 0, NOW(), NOW()), -- Trắng
--     (39, 23, 0, NOW(), NOW()), -- Size S
--     (39, 27, 0, NOW(), NOW()), -- Xanh Dương
--     (40, 23, 0, NOW(), NOW()), -- Size S
--     (40, 28, 0, NOW(), NOW()), -- Đỏ
--     (41, 23, 0, NOW(), NOW()), -- Size S
--     (41, 29, 0, NOW(), NOW()), -- Đen
--     -- Size M
--     (42, 24, 0, NOW(), NOW()), -- Size M
--     (42, 26, 0, NOW(), NOW()), -- Trắng
--     (43, 24, 0, NOW(), NOW()), -- Size M
--     (43, 27, 0, NOW(), NOW()), -- Xanh Dương
--     (44, 24, 0, NOW(), NOW()), -- Size M
--     (44, 28, 0, NOW(), NOW()), -- Đỏ
--     (45, 24, 0, NOW(), NOW()), -- Size M
--     (45, 29, 0, NOW(), NOW()), -- Đen
--     -- Size L
--     (46, 25, 0, NOW(), NOW()), -- Size L
--     (46, 26, 0, NOW(), NOW()), -- Trắng
--     (47, 25, 0, NOW(), NOW()), -- Size L
--     (47, 27, 0, NOW(), NOW()), -- Xanh Dương
--     (48, 25, 0, NOW(), NOW()), -- Size L
--     (48, 28, 0, NOW(), NOW()), -- Đỏ
--     (49, 25, 0, NOW(), NOW()), -- Size L
--     (49, 29, 0, NOW(), NOW()), -- Đen
--     -- Size XL
--     (50, 26, 0, NOW(), NOW()), -- Size XL
--     (50, 26, 0, NOW(), NOW()), -- Trắng
--     (51, 26, 0, NOW(), NOW()), -- Size XL
--     (51, 27, 0, NOW(), NOW()), -- Xanh Dương
--     (52, 26, 0, NOW(), NOW()), -- Size XL
--     (52, 28, 0, NOW(), NOW()), -- Đỏ
--     (53, 26, 0, NOW(), NOW()), -- Size XL
--     (53, 29, 0, NOW(), NOW()); -- Đen
--
-- -- Cities
-- INSERT INTO cities (name) VALUES
--                               ('Hà Nội'),
--                               ('Hồ Chí Minh'),
--                               ('Đà Nẵng');
--
-- -- Districts
-- INSERT INTO districts (name, city_id) VALUES
--                                           ('Ba Đình', 1),      -- Hà Nội
--                                           ('Hoàn Kiếm', 1),
--                                           ('Quận 1', 2),       -- HCM
--                                           ('Quận 3', 2),
--                                           ('Hải Châu', 3),     -- Đà Nẵng
--                                           ('Thanh Khê', 3);
--
-- -- Wards (phường/xã)
-- INSERT INTO wards (name, district_id) VALUES
--                                           ('Phường Phúc Xá', 1),      -- Ba Đình, HN
--                                           ('Phường Trúc Bạch', 1),
--                                           ('Phường Hàng Bạc', 2),     -- Hoàn Kiếm, HN
--                                           ('Phường Hàng Đào', 2),
--
--                                           ('Phường Bến Nghé', 3),     -- Quận 1, HCM
--                                           ('Phường Bến Thành', 3),
--                                           ('Phường Võ Thị Sáu', 4),   -- Quận 3, HCM
--                                           ('Phường 6', 4),
--
--                                           ('Phường Thạch Thang', 5), -- Hải Châu, Đà Nẵng
--                                           ('Phường Hải Châu I', 5),
--                                           ('Phường Thanh Khê Đông', 6), -- Thanh Khê, Đà Nẵng
--                                           ('Phường An Khê', 6);
--
--
