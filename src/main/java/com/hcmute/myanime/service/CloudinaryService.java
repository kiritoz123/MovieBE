package com.hcmute.myanime.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Boolean deleteFileVideo(String directory) {
        Map paramsOption = ObjectUtils.asMap(
                "resource_type", "video",
                "invalidate" , true);
        try {
            cloudinary.uploader().destroy(directory, paramsOption);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    //    Upload thanh cong return duong dan chua hinh anh
//    Upload khong thanh cong return -1
    public String uploadFile(byte[] fileByteArray, String imgName, String directory) {
        try {
            if(fileByteArray.length == 0) {
                return "-1";
            }
            String fileDir = directory + "/" + imgName; //Dir tren cloudinary: Tạo folder id product và file name là id product
            //Option cho cloudinary
            Map paramsOption =
                    ObjectUtils.asMap("resource_type", "auto",
                            "public_id", fileDir);
            Map uploadResult = cloudinary.uploader().upload(fileByteArray,
                    paramsOption);
            return (String) uploadResult.get("secure_url");
        } catch (Exception e) {
            e.printStackTrace();
            return "-1";
        }
    }
//
//    private File convertMultiPartToFile(MultipartFile file, String fileName) throws IOException {
//        File convFile = new File(fileName);
//        FileOutputStream fos = new FileOutputStream(convFile);
//        fos.write(file.getBytes());
//        fos.close();
//        return convFile;
//    }
}
