package shop.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.dto.category.CategoryCreateDTO;
import shop.dto.category.CategoryItemDTO;
import shop.entities.CategoryEntity;
import shop.mapper.CategoryMapper;
import shop.repositories.CategoryRepository;
import shop.storage.StorageService;
import shop.dto.category.UpdateCategoryDTO;
import shop.interfaces.CategoryService;


import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/categories")
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final StorageService storageService;
    private final CategoryService categoryService;
    //Показуємо всі категорії
    @GetMapping
    public ResponseEntity<List<CategoryItemDTO>> index() {
        var list = categoryRepository.findAll();
        var model = categoryMapper.categoryItemByCategories(list);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }
    //Створюємо нову категорію
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryItemDTO> create(@ModelAttribute CategoryCreateDTO model) {
        var fileName = storageService.saveMultipartFile(model.getFile());
        CategoryEntity category = categoryMapper.CategoryByCreateDTO(model);
        category.setImage(fileName);
        categoryRepository.save(category);
        var result = categoryMapper.categoryItemByCategory(category);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
    //Видаляємо категорію
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int categoryId) {
        categoryService.delete(categoryId);
        return new ResponseEntity<>("Category deleted...", HttpStatus.OK);
    }
    //Редагуємо категорію
    @PutMapping("{id}")
    public ResponseEntity<CategoryItemDTO> update(@PathVariable("id") int categoryId,
                                                  @RequestBody UpdateCategoryDTO model) {
        var result = categoryService.update(categoryId, model);
        if(result!=null)
        {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
