package shop.dto.product;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

//Інформація про ОДИН продукт
@Data
public class ProductItemDTO {
    private int id;
    private String name;
    private double price;
    private String description;
    private String category;
    private int category_id;
    private List<String> files = new ArrayList<>();
}