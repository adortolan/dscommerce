package com.devsuperior.dscommerce;

import com.devsuperior.dscommerce.entities.Category;

public class CategoryFactory {

    public static Category createCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Eletronics");
        return category;
    }

    public static Category createCategory(Long id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        return category;
    }
}
