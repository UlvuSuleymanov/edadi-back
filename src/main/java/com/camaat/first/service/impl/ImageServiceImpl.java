package com.camaat.first.service.impl;

import com.camaat.first.entity.Image;
import com.camaat.first.entity.User;
import com.camaat.first.model.ImageResponseModel;
import com.camaat.first.repository.ImageRepository;
import com.camaat.first.repository.UserRepository;
import com.camaat.first.service.ImageService;
import com.camaat.first.utility.AuthUtil;
 import com.camaat.first.service.S3Service;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {


    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;


    private static String photoUrl;




    @Autowired
    public ImageServiceImpl(S3Service s3Service,
                            UserRepository userRepository,
                            ImageRepository imageRepository,
                            @Value("${aws.photoUrl}") String url) {
        this.s3Service = s3Service;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.photoUrl=url;

    }

    @Override
    @Transactional
    public String setImage(MultipartFile  multipartFile, boolean  hasThumb) {


        UUID uuid =  UUID.randomUUID();


        File file= null;
        try {
            file = convertMultiPartToFile(multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        s3Service.setPhoto(uuid.toString(),file);

        if(hasThumb) {
            File smallImage = getSmallPicture(file);
            s3Service.setPhoto("thumb" + uuid.toString(), smallImage);
            smallImage.delete();

        }

        file.delete();



      return   uuid.toString();
    }

    @Override
    public File getSmallPicture(File file) {

        try {
                     Thumbnails.of(file)
                       .size(160,160)
                       .toFile(file);
                     return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public File convertMultiPartToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(multipartFile.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }

    public static String getThumbImage(String name) {
        return photoUrl+"thumb"+name;
     }

    public static String getFullImage(String name) {
        return photoUrl+name;
    }





}
