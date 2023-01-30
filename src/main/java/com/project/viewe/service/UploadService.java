package com.project.viewe.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class UploadService {
    String videoFileUrl = "C:\\upload\\video\\";
    String photoFileUrl = "C:\\upload\\photo\\";
    String thumbnailFileUrl = "C:\\upload\\thumbnail\\";
    public String uploadVideoFile(MultipartFile file, String fileName) {
        return getUrl(file, videoFileUrl, fileName);
    }
    public String uploadPhotoFile(MultipartFile file, String fileName) {
        return getUrl(file, photoFileUrl, fileName);
    }
    public String uploadThumbnailFile(MultipartFile file, String fileName) {
        return getUrl(file, thumbnailFileUrl, fileName);
    }

    public String getUrl(MultipartFile file, String fileUrl, String fileName){
        try{
            file.transferTo(new File(fileUrl + fileName));
        }catch (Exception e){
            return "Cannot upload file " + fileName;
        }
        return fileUrl + fileName;
    }

    public String uploadProfileFile(String fileName, MultipartFile multipartFile) {
        return getUrl(multipartFile, photoFileUrl, fileName);
    }
}
