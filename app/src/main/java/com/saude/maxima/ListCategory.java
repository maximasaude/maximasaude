package com.saude.maxima;

import com.saude.maxima.Adapters.Category.Category;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Junnyor on 24/12/2017.
 */

public class ListCategory implements Serializable {
    public List<Category> categoryList;
    public static final String key = "categories";
    public ListCategory(List<Category> categoryList){
        this.categoryList = categoryList;
    }
}
