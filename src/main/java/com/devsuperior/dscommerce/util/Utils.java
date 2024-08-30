package com.devsuperior.dscommerce.util;

import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.projections.ProdutctProjection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    public static List<Product> replace(List<ProdutctProjection> ordered, List<Product> unordered) {
        Map<Long, Product> map = new HashMap<>();
        for (Product product : unordered) {
            map.put(product.getId(), product);
        }

        List<Product> result = new ArrayList<>();

        for (ProdutctProjection projection : ordered) {
            result.add(map.get(projection.getId()));
        }
        return result;
    }
}
