package com.example.oncallinvext.repositories;

import com.example.oncallinvext.domain.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, String> {
}
