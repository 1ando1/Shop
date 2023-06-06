package shop.dto.product;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductEditDTO {
    private String name;
    private double price;
    private String description;
    private int category_id;
    //фото, які користувач видали при радагувані
    private List<String> removeFiles = new ArrayList<>();
    //нові фото, які ми додаємо у товар
    private List<MultipartFile> files = new ArrayList<>();
}