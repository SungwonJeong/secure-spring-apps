package com.example.securespringapps.storage.db.core.post;

import com.example.securespringapps.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class PostEntity extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Builder
    private PostEntity(String title, String content, Long userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
    }

    public static PostEntity create(String title, String content, Long userId) {
        return PostEntity.builder()
                .title(title)
                .content(content)
                .userId(userId)
                .build();
    }
}
