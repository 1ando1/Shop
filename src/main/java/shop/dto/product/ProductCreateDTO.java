package shop.dto.product;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import shop.entities.CategoryEntity;

import java.util.List;

@Data
public class ProductCreateDTO {
    private String name;
    private double price;
    private String description;
    private int category_id;
    private List<MultipartFile> files;
}