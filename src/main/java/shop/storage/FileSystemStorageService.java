package shop.storage;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Service
public class FileSystemStorageService implements StorageService{
    //Знаходимо шлях до файлу
    private final Path rootLocation;
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }
    @Override
    public void init() {
        //Перевіряємо чи існує папка за таким гляхом, якщо нема створюємо
        try {
            if(!Files.exists(rootLocation))
                Files.createDirectories(rootLocation);
        }
        catch(IOException ex) {
            throw new StorageExeption("Folder creating failed...", ex);
        }
    }

    @Override
    public Resource loadResource(String fileName) {
        try {
            Path file = rootLocation.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable())
                return resource;
            throw new StorageExeption("Problem with working with file..." + fileName);
        }
        catch(MalformedURLException e) {
            throw new StorageExeption("File Not Found..." + fileName);
        }
    }

    //Збереження фотографії в .png або .jpg
    @Override
    public String save(String base64) {
        try {
            if(base64.isEmpty()) {
                throw new StorageExeption("Empty base64");
            }

            UUID uuid = UUID.randomUUID();
            String [] charArray = base64.split(",");

            String extension;
            switch(charArray[0]) {
                case "data:image/png;base64":
                    extension="png";
                    break;
                default:
                    extension="jpg";
                    break;
            }
            String randomFileName = uuid.toString()+"."+extension;

            Base64.Decoder decoder = Base64.getDecoder();

            byte [] bytes = new byte[0];
            //Можливі розміри фотографії
            int [] imageSize = {32,150, 300, 600, 1200};
            try(var byteStream = new ByteArrayInputStream(bytes)) {
                var image = ImageIO.read(byteStream);

                for(int size : imageSize) {
                    String fileSaveItem = rootLocation.toString()+"/"+size+"_"+randomFileName;
                    BufferedImage newImg = ImageUtils.resizeImage(image,
                            extension=="jpg" ? ImageUtils.IMAGE_JPEG: ImageUtils.IMAGE_PNG, size, size);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ImageIO.write(newImg, extension, byteArrayOutputStream);
                    byte [] newBytes = byteArrayOutputStream.toByteArray();
                    FileOutputStream out = new FileOutputStream(fileSaveItem);
                    out.write(newBytes);
                    out.close();
                }
            }

            catch(IOException e) {
                throw new StorageExeption("Problems with image transformation", e);
            }
            return randomFileName;
        }

        catch(StorageExeption e) {
            throw new StorageExeption("Problem with saving and transformation base64",e);
        }
    }

    //Збереження фотографії в .png або .jpg
    @Override
    public String saveMultipartFile(MultipartFile file) {
        try {
            UUID uuid = UUID.randomUUID();
            String extension="jpg";
            String randomFileName = uuid.toString()+"."+extension;
            Base64.Decoder decoder = Base64.getDecoder();
            byte [] bytes = file.getBytes();
            int [] imageSize = {32,150, 300, 600, 1200};
            try(var byteStream = new ByteArrayInputStream(bytes)) {
                var image = ImageIO.read(byteStream);
                for(int size : imageSize) {
                    String fileSaveItem = rootLocation.toString()+"/"+size+"_"+randomFileName;
                    BufferedImage newImg = ImageUtils.resizeImage(image,
                            extension=="jpg" ? ImageUtils.IMAGE_JPEG: ImageUtils.IMAGE_PNG, size, size);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ImageIO.write(newImg, extension, byteArrayOutputStream);
                    byte [] newBytes = byteArrayOutputStream.toByteArray();
                    FileOutputStream out = new FileOutputStream(fileSaveItem);
                    out.write(newBytes);
                    out.close();
                }
            } catch(IOException e) {
                throw new StorageExeption("Problems with image transformation", e);
            }
            return randomFileName;

        } catch(Exception e) {
            throw new StorageExeption("Problem with saving and transformation base64",e);
        }
    }

    //todo
    @Override
    public void removeFile(String name) {

    }

    //todo
    @Override
    public Path load(String name) {
        return null;
    }
}
