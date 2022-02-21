package com.jaime.gotor.warehouse.software.database.repository;

import com.jaime.gotor.warehouse.software.database.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ArticlesReposiroty
 * <br>
 * <code>com.jaime.gotor.warehouse.software.database.repository.ArticlesReposiroty</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 19 February 2022
 */
@Repository
public interface ArticlesRepository extends JpaRepository<Article, String> {

}
