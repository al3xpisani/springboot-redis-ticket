package com.example.oncallinvext.controllers;

import com.example.oncallinvext.domain.Category;
import com.example.oncallinvext.repositories.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {
    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @RequestMapping("/categories")
    Iterable<Category> getById(){
        return categoryRepository.findAll();
    }

    @PostMapping("/categories")
    String saveCategory(@RequestBody Category category){
        categoryRepository.save(category);
        log.info("Category saved: {}", category);
        return String.format("Category saved: %s", category);
    }
}
