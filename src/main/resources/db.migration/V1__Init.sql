CREATE TABLE `upload_files`
(
    `id`               int unsigned NOT NULL AUTO_INCREMENT,
    `origin_url`       varchar(500)      DEFAULT NULL COMMENT 'Lưu trữ URL gốc của tệp tin, nếu tệp được lấy từ một nguồn trực tuyến',
    `origin_file_path` varchar(500)      DEFAULT NULL COMMENT 'Đường dẫn tệp gốc trên hệ thống lưu trữ',
    `thumb_url`        varchar(500)      DEFAULT NULL COMMENT 'URL của thumbnail',
    `thumb_file_path`  varchar(500)      DEFAULT NULL COMMENT 'Đường dẫn đến thumbnail trên hệ thống lưu trữ',
    `type`             tinyint  NOT NULL COMMENT 'Loại tệp tin: `0`: Hình ảnh, `1`: Video, `2`:PDF',
    `width`            int               DEFAULT NULL COMMENT 'Chiều rộng của tập tin (Áp dụng cho hình ảnh hoặc video) - Pixel',
    `height`           int               DEFAULT NULL COMMENT 'Chiều cao của tập tin (Áp dụng cho hình ảnh hoặc video) - Pixel',
    `duration`         int               DEFAULT NULL COMMENT 'Dung lượng của tệp',
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

CREATE TABLE `staffs`
(
    `id` 			int unsigned NOT NULL AUTO_INCREMENT,
    `branch_id`  	int NOT NULL DEFAULT 0 COMMENT 'Khóa ngoại tham chiếu đến chi nhánh làm việc',
    `user_id`       int unsigned DEFAULT NULL,
    `deleted`       bit(1)       NOT NULL DEFAULT 0,
    `created_at` 	datetime     NOT NULL,
    `updated_at` 	datetime     NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    FOREIGN KEY (`branch_id`) REFERENCES `branches` (`id`)
) ;

CREATE TABLE `users`
(
    `id`         int unsigned NOT NULL AUTO_INCREMENT,
    `code`       varchar(50) NOT NULL COMMENT 'Mã người dùng duy nhất',
    `phone`      varchar(20) NOT NULL,
    `email`      varchar(255)         DEFAULT NULL,
    `name`       varchar(255)         DEFAULT NULL,
    `address`    varchar(255)         DEFAULT NULL,
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
    UNIQUE KEY (`phone`),
    FOREIGN KEY (`avatar_id`) REFERENCES `upload_files` (`id`),
    FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ;

CREATE TABLE `categories`
(
    `id`                int unsigned NOT NULL AUTO_INCREMENT,
    `name`              varchar(255) NOT NULL,
    `slug`              varchar(500) NOT NULL,
    `short_description` tinytext              DEFAULT NULL,
    `status`            tinyint      NOT NULL,
    `type`              tinyint      NOT NULL,
    `image_id`          int unsigned         NOT NULL,
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

CREATE TABLE `discounts`
(
    `id`            		int unsigned NOT NULL AUTO_INCREMENT,
    `discount_percents`     int unsigned NOT NULL DEFAULT 0,
    `description`   		text     DEFAULT NULL,
    `time_started`			date NOT NULL,
    `time_ended`			date NOT NULL,
    `status`        		tinyint NOT NULL,
    `deleted`       		bit(1)   NOT NULL DEFAULT 0,
    `created_at`    		datetime NOT NULL,
    `updated_at`		    datetime NOT NULL,
    `product_id` 			int unsigned   NOT NULL,
    FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
    PRIMARY KEY (`id`)
);

CREATE TABLE `products`
(
    `id`                int unsigned NOT NULL AUTO_INCREMENT,
    `name`              varchar(255) NOT NULL,
    `slug`              varchar(500) NOT NULL,
    `short_description` tinytext              DEFAULT NULL,
    `description`       text                  DEFAULT NULL,
    `original_price`    int          NOT NULL DEFAULT 0,
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
    `product_id`    int DEFAULT NULL,
    `user_id`       int DEFAULT NULL,
    `quantity`      int DEFAULT NULL,
    `deleted`       bit(1)   NOT NULL DEFAULT 0,
    `created_at`    datetime NOT NULL,
    `updated_at`    datetime NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)  ON DELETE CASCADE
);

CREATE TABLE `payments`
(
    `id`                int unsigned NOT NULL AUTO_INCREMENT,
    `payment_method`    tinyint NOT NULL COMMENT '0 - COD;  1 - VNPAY',
    `status`            bit(1) NOT NULL DEFAULT 0 COMMENT '0 - Chưa thanh toán; 1 - Đã thanh toán',
    `deleted`           bit(1)   NOT NULL DEFAULT 0,
    `created_at`        datetime NOT NULL,
    `updated_at`        datetime NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `orders`
(
    `id`            int unsigned NOT NULL AUTO_INCREMENT,
    `user_id`       int unsigned NOT NULL,
    `payment_id`    int unsigned NOT NULL,
    `contact_id`	int NOT NULL,
    `status`        tinyint NOT NULL COMMENT '0 - Chờ vận chuyển, 1 - Đã vận chuyển , 2 - Đã giao hàng , 3 - Đã hủy',
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
    `product_id`    int unsigned NOT NULL,
    `quantity`      int unsigned NOT NULL,
    `deleted`       bit(1)   NOT NULL DEFAULT 0,
    `created_at`    datetime NOT NULL,
    `updated_at`    datetime NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
    FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
);

CREATE TABLE `contacts`
(
    `id`            int unsigned NOT NULL AUTO_INCREMENT,
    `first_name`    varchar(255) NOT NULL,
    `last_name`     varchar(255) NOT NULL,
    `phone`         varchar(20)  NOT NULL,
    `email`         varchar(255) NOT NULL,
    `address`       varchar(255) NOT NULL,
    `subject`       varchar(500) NOT NULL,
    `content`       text         NOT NULL,
    `status`        tinyint      NOT NULL DEFAULT 0,
    `note`          text                  DEFAULT NULL,
    `deleted`       bit(1)       NOT NULL DEFAULT 0,
    `created_at`    datetime     NOT NULL,
    `updated_at`    datetime     NOT NULL,
    PRIMARY KEY (`id`)
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
    `additional_price` 	int unsigned DEFAULT NULL, -- Giá trị gia tăng của option
    `deleted`           bit(1)      NOT NULL DEFAULT 0,
    `created_at`        datetime NOT NULL,
    `updated_at`        datetime NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`product_option_id`) REFERENCES `product_options` (`id`) ON DELETE CASCADE
);
--
-- INSERT INTO `permissions` (`title`, `permission`, `parent_permission`, `is_view`, `is_write`, `is_approval`, `is_decision`, `type`, `status`, `deleted`, `created_at`, `updated_at`)
-- VALUES
--     ('Admin', 'Everything', NULL, 1, 1, 1, 1, 1, 1, 1, NOW(), NOW());
--
-- INSERT INTO `roles` (`name`, `note`, `type`, `status`, `deleted`, `created_at`, `updated_at`)
-- VALUES
--     ('Admin', 'Quản trị viên hệ thống', 1, 1, 0, NOW(), NOW());
--
-- INSERT INTO `role_permission` (`role_id`, `permission_id`, `deleted`, `created_at`, `updated_at`)
-- VALUES
--     (1, 1, 0, NOW(), NOW());
--
-- INSERT INTO `branches` (`name`, `address`, `phone`, `deleted`, `created_at`, `updated_at`)
-- VALUES
--     ('Chi nhánh Hà Nội', '123 Đường Láng, Hà Nội', '0123456789', 0, NOW(), NOW()),
--     ('Chi nhánh Hồ Chí Minh', '456 Đường Nguyễn Văn Linh, TP.HCM', '0987654321', 0, NOW(), NOW());
--
-- INSERT INTO `users` (`code`, `phone`, `email`, `name`, `address`, `password`, `birthday`, `gender`, `role_id`, `avatar_id`, `status`, `deleted`, `created_at`, `updated_at`)
-- VALUES
--     ('USER001', '0123456789', 'admin@gmail.com', 'Nguyễn Văn A', '123 Đường Láng, Hà Nội', '123456', '1990-01-01', 1, 1, NULL, 1, 0, NOW(), NOW());
