package shop.interfaces;

import shop.dto.category.CategoryCreateDTO;
import shop.dto.category.CategoryItemDTO;
import shop.dto.category.UpdateCategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryItemDTO create(CategoryCreateDTO model);
    List<CategoryItemDTO> get();
    CategoryItemDTO update(int id, UpdateCategoryDTO model);
    void delete(int id);
    CategoryItemDTO get(int id);
}