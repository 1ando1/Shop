package shop.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import shop.dto.product.ProductCreateDTO;
import shop.dto.product.ProductEditDTO;
import shop.dto.product.ProductItemDTO;
import shop.entities.CategoryEntity;
import shop.entities.ProductEntity;
import shop.entities.ProductImageEntity;
import shop.interfaces.ProductService;
import shop.mapper.ProductMapper;
import shop.repositories.ProductImageRepository;
import shop.repositories.ProductRepository;
import shop.storage.StorageService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final StorageService storageService;
    private final ProductMapper productMapper;
    @Override
    public ProductItemDTO create(ProductCreateDTO model) {
        //Створюємо продукт
        var p = new ProductEntity();
        //Створюємо категорію
        var cat = new CategoryEntity();
        //Вказуємо проперті продукта
        cat.setId(model.getCategory_id());
        p.setName(model.getName());
        p.setDescription(model.getDescription());
        p.setPrice(model.getPrice());
        p.setDateCreated(new Date());
        p.setCategory(cat);
        p.setDeleted(false);
        productRepository.save(p);
        //Робота з фото
        int priority=1;
        for (var img : model.getFiles()) {
            var file = storageService.saveMultipartFile(img);
            ProductImageEntity pi = new ProductImageEntity();
            pi.setName(file);
            pi.setDateCreated(new Date());
            pi.setPriority(priority);
            pi.setDeleted(false);
            pi.setProduct(p);
            productImageRepository.save(pi);
            priority++;
        }
        return null;
    }

    @Override
    public List<ProductItemDTO> get() {
        var products = productRepository.findAll();
        var result = new ArrayList<ProductItemDTO>();
        for (var p:products) {
            var item = productMapper.ProductItemDTOByProduct(p);
            for(var img : p.getProductImages())
                item.getFiles().add(img.getName());
            result.add(item);
        }
        return result;
    }
    @Override
    public ProductItemDTO edit(int id, ProductEditDTO model) {
        var p = productRepository.findById(id);
        //якщо по такому id - є продукт
        if(p.isPresent())
        {
            //отримуємо сам продукт
            var product = p.get();
            //Якщо користувач видадяв фото із списку - шукаємо фото по імені
            for (var name: model.getRemoveFiles()) {
                var pi = productImageRepository.findByName(name);
                if(pi!=null)
                {
                    //видаляємо саме фото товару
                    productImageRepository.delete(pi);
                    //видаляємо файли даного фото
                    storageService.removeFile(name);
                }
            }
            var cat = new CategoryEntity();
            //категорія товару, вказуємо для нього id
            cat.setId(model.getCategory_id());
            //змінуюємо імя товару
            product.setName(model.getName());
            //змінуюємо опис товару
            product.setDescription(model.getDescription());
            //змінуюємо ціну товару
            product.setPrice(model.getPrice());
            //змінуюємо категорію товару
            product.setCategory(cat);
            //Зберігаємо дані про товар
            productRepository.save(product);
            //Отримуємо список нових фото до товару
            var productImages = product.getProductImages();
            //визначаємо пріорітет фото у послідовнссті
            int priority=1;
            for (var pi : productImages)
            {
                //шукаємо макисальний пріорітет
                if(pi.getPriority()>priority)
                //нові фото ставимо у кінець черги.
                priority=pi.getPriority();
            }
            priority++;
            ///Зберігаємо нові фото
            for (var img : model.getFiles()) {
                var file = storageService.saveMultipartFile(img);
                ProductImageEntity pi = new ProductImageEntity();
                pi.setName(file);
                pi.setDateCreated(new Date());
                pi.setPriority(priority);
                pi.setDeleted(false);
                pi.setProduct(product);
                productImageRepository.save(pi);
                priority++;
            }
        }

        return null;
    }
    @Override
    public void delete(int id) {
        ProductEntity product = productRepository.findById(id).get();
        for(var img : product.getProductImages())
            storageService.removeFile(img.getName());;
        productRepository.deleteById(id);
    }

    @Override
    public ProductItemDTO getById(int id) {
        var productOptinal = productRepository.findById(id);
        if(productOptinal.isPresent())
        {
            var product = productOptinal.get();
            var data =  productMapper.ProductItemDTOByProduct(product);
            for(var img : product.getProductImages())
                data.getFiles().add(img.getName());
            return data;
        }
        return null;
    }
}