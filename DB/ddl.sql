CREATE DATABASE IF NOT EXISTS library_borrow_system;
USE library_borrow_system;
-- 刪除舊表與舊程序
DROP PROCEDURE IF EXISTS sp_insert_user;
DROP PROCEDURE IF EXISTS sp_select_user_phone;
DROP PROCEDURE IF EXISTS sp_select_all_inventory;
DROP PROCEDURE IF EXISTS sp_select_inventory_status;
DROP PROCEDURE IF EXISTS sp_select_borrowing_record_user_id;
DROP PROCEDURE IF EXISTS sp_select_borrowing_record_user_id_by_inventory_id;
DROP PROCEDURE IF EXISTS sp_update_inventory_status;
DROP PROCEDURE IF EXISTS sp_insert_borrowing_record;
DROP PROCEDURE IF EXISTS sp_update_return_time;

DROP TABLE IF EXISTS borrowing_record;
DROP TABLE IF EXISTS inventory;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS users;

-- 建立資料表
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '使用者 ID',
    phone_number VARCHAR(20) NOT NULL UNIQUE COMMENT '手機',
    password VARCHAR(255) NOT NULL COMMENT '密碼(加鹽Hash)',
    user_name VARCHAR(100) NOT NULL COMMENT '使用者名稱',
    registration_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '註冊日期時間',
    last_login_time TIMESTAMP NULL COMMENT '最後登入時間'
) COMMENT='使用者基本資料表';

CREATE TABLE books (
    isbn VARCHAR(20) PRIMARY KEY COMMENT '國際標準書號',
    name VARCHAR(200) NOT NULL COMMENT '書名',
    author VARCHAR(100) NOT NULL COMMENT '作者',
    introduction TEXT COMMENT '書籍內容簡介'
) COMMENT='書籍基本資料表';

CREATE TABLE inventory (
    inventory_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '庫存',
    isbn VARCHAR(20) NOT NULL COMMENT '國際標準書號',
    store_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '書籍入庫(購買)日期時間',
    status VARCHAR(20) DEFAULT '在庫' COMMENT '書籍狀態：在庫、出借中、整理中、遺失、損毀、廢棄',
    CONSTRAINT fk_inventory_books_isbn
        FOREIGN KEY (isbn) REFERENCES books (isbn) 
        ON UPDATE CASCADE ON DELETE RESTRICT
) COMMENT='實體書籍庫存狀態表';

CREATE TABLE borrowing_record (
    record_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '借閱紀錄 ID',
    user_id INT NOT NULL COMMENT '使用者 ID',
    inventory_id INT NOT NULL COMMENT '庫存 ID',
    borrowing_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '借出日期時間',
    return_time TIMESTAMP NULL COMMENT '歸還日期時間',
    CONSTRAINT fk_borrowing_record_users_user_id
        FOREIGN KEY (user_id) REFERENCES users (user_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_borrowing_record_inventory_inventory_id    
        FOREIGN KEY (inventory_id) REFERENCES inventory(inventory_id)
        ON UPDATE CASCADE ON DELETE RESTRICT
) COMMENT='圖書借閱歷史紀錄表';

-- 建立效能索引
CREATE INDEX idx_user_id ON borrowing_record (user_id);
CREATE INDEX idx_inventory_id ON borrowing_record (inventory_id);

-- ========================== Stored Procedure ==========================
DELIMITER $$

-- 註冊使用者
CREATE PROCEDURE sp_insert_user(
    IN p_phone VARCHAR(20), 
    IN p_password VARCHAR(255), 
    IN p_name VARCHAR(100)
)
BEGIN
	INSERT INTO users (phone_number, password, user_name) VALUES (p_phone, p_password, p_name);
END$$

-- 查詢手機號碼
CREATE PROCEDURE sp_select_user_phone(
    IN p_phone VARCHAR(20)
)
BEGIN
	SELECT * FROM users WHERE phone_number = p_phone;
END$$

-- 查詢所有庫存
CREATE PROCEDURE sp_select_all_inventory()
BEGIN
    SELECT * FROM inventory;
END$$

-- 查詢單一書籍狀態
CREATE PROCEDURE sp_select_inventory_status(
    IN p_inventory_id INT,
    OUT p_status VARCHAR(50)
)
BEGIN
    SELECT status INTO p_status
    FROM inventory 
    WHERE inventory_id = p_inventory_id;
END$$


-- 查詢個人借閱紀錄
CREATE PROCEDURE sp_select_borrowing_record_user_id(
	IN p_user_id INT
)
BEGIN
	SELECT * FROM borrowing_record WHERE user_id = p_user_id;
END$$

-- 查詢該書借閱人
CREATE PROCEDURE sp_select_borrowing_record_user_id_by_inventory_id(
    IN p_inventory_id INT,
    OUT p_user_id INT
)
BEGIN
    SELECT user_id INTO p_user_id
    FROM borrowing_record 
    WHERE inventory_id = p_inventory_id
	 AND return_time IS NULL;
END$$

-- 更新庫存書籍狀態
CREATE PROCEDURE sp_update_inventory_status(
    IN p_inventory_id INT, 
    IN p_status VARCHAR(20)
)
BEGIN
	UPDATE inventory SET status = p_status WHERE inventory_id = p_inventory_id;
END$$

-- 新增借閱紀錄
CREATE PROCEDURE sp_insert_borrowing_record(
    IN p_user_id INT, 
    IN p_inventory_id INT
)
BEGIN
	INSERT INTO borrowing_record (user_id, inventory_id) VALUES (p_user_id, p_inventory_id);
END$$

-- 更新還書時間
CREATE PROCEDURE sp_update_return_time(
    IN p_user_id INT, 
    IN p_inventory_id INT
)
BEGIN
	UPDATE borrowing_record 
	SET return_time = NOW() 
	WHERE user_id = p_user_id 
	 AND inventory_id = p_inventory_id
	 AND return_time IS NULL;
END$$



DELIMITER ;