package shop.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import shop.dto.category.CategoryItemDTO;
import shop.dto.category.CategoryCreateDTO;
import shop.dto.category.UpdateCategoryDTO;
import shop.entities.CategoryEntity;
import shop.interfaces.CategoryService;
import shop.mapper.CategoryMapper;
import shop.repositories.CategoryRepository;
import shop.storage.StorageService;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final StorageService storageService;
    @Override
    public CategoryItemDTO create(CategoryCreateDTO model) {
        var fileName = storageService.saveMultipartFile(model.getFile());//storageService.save(model.getBase64());
        CategoryEntity category = categoryMapper.CategoryByCreateDTO(model);
        category.setImage(fileName);
        categoryRepository.save(category);
        var result = categoryMapper.categoryItemByCategory(category);
        return result;
    }

    @Override
    public List<CategoryItemDTO> get() {
        var list = categoryRepository.findAll();
        return categoryMapper.categoryItemByCategories(list);
    }

    @Override
    public CategoryItemDTO update(int id, UpdateCategoryDTO model) {
        var catOptional = categoryRepository.findById(id);
        if(catOptional.isPresent())
        {
            var cat = catOptional.get();
            cat.setName(model.getName());
            categoryRepository.save(cat);
            var result = categoryMapper.categoryItemByCategory(cat);
            return result;
        }
        return null;
    }

    @Override
    public void delete(int id) {
        CategoryEntity category = categoryRepository.findById(id).get();
        storageService.removeFile(category.getImage());
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryItemDTO get(int id) {
        var catOptional = categoryRepository.findById(id);
        if(catOptional.isPresent())
        {
            return categoryMapper.categoryItemByCategory(catOptional.get());
        }
        return null;
    }
}