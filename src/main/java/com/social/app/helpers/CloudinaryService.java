package com.social.app.helpers;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    @Value("${cloudinary.CLOUD_NAME}")
    private String CLOUD_NAME;
    @Value("${cloudinary.API_KEY}")
    private String API_KEY;
    @Value("${cloudinary.API_SECRET}")
    private String API_SECRET;
    public CloudinaryService() {
        Map config = new HashMap();
        config.put("cloud_name", CLOUD_NAME);
        config.put("api_key", API_KEY);
        config.put("api_secret", API_SECRET);
        this.cloudinary = new Cloudinary(config);
    }
    public String upload(MultipartFile file) {
            try {
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(),Map.of());
                System.out.println(uploadResult);
                String url = uploadResult.get("secure_url").toString();
                System.out.println(url);
                return url;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
    }
    private String getPublicIdFromUrl(String secureUrl){
        String[] urlParts =  secureUrl.split("/");
        String fileName =  urlParts[urlParts.length-1];
        return fileName.split(".")[0];
    }
    public void deleteByUrl(String secureUrl){
        try {
            cloudinary.uploader().destroy(
                    getPublicIdFromUrl(secureUrl),
                    Map.of());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
