package shop.dto.product;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadListImagesDTO {
    MultipartFile [] images;
}