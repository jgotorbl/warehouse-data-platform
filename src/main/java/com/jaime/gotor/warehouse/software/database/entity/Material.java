package com.jaime.gotor.warehouse.software.database.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Material
 * <br>
 * <code>com.jaime.gotor.warehouse.software.database.entity.Material</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 19 February 2022
 */
@Entity
@Table(name = "material")
@Data
@NoArgsConstructor
public class Material implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String articleId;
    @Column(nullable = false)
    private Integer amount;
}
