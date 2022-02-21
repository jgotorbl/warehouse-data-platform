package com.jaime.gotor.warehouse.software.database.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Article
 * <br>
 * <code>com.jaime.gotor.warehouse.software.database.entity.Article</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 19 February 2022
 */
@Entity
@Table(name = "article")
@Data
public class Article {

    @Id
    private String articleId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer stock;


}
