CREATE TABLE IF NOT EXISTS likes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    liker_id INT NOT NULL,
    liked_user_id INT NOT NULL,
    timestamp DATETIME NOT NULL,
    FOREIGN KEY (liker_id) REFERENCES users(id),
    FOREIGN KEY (liked_user_id) REFERENCES users(id),
    UNIQUE KEY unique_like (liker_id, liked_user_id)
);
