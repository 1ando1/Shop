package shop.interfaces;

import shop.dto.product.ProductCreateDTO;
import shop.dto.product.ProductEditDTO;
import shop.dto.product.ProductItemDTO;
import shop.entities.ProductImageEntity;
import shop.repositories.ProductImageRepository;

import java.util.List;

public interface ProductService {
    ProductItemDTO create(ProductCreateDTO model);
    List<ProductItemDTO> get();
    ProductItemDTO edit(int id, ProductEditDTO model);
    ProductItemDTO getById(int id);
    void delete(int id);
}