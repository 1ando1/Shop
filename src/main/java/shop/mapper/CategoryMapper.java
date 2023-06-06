package shop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import shop.dto.category.CategoryCreateDTO;
import shop.dto.category.CategoryItemDTO;
import shop.entities.CategoryEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "image", ignore = true)
    CategoryEntity CategoryByCreateDTO(CategoryCreateDTO dto);
    @Mapping(source = "description", target = "description")
    CategoryItemDTO categoryItemByCategory(CategoryEntity entity);
    List<CategoryItemDTO> categoryItemByCategories(List<CategoryEntity> entities);
}
