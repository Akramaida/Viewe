package com.project.viewe.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class UploadVisualComment {
    String videoFileUrl = "C:\\upload\\visual-comment\\video\\";
    String photoFileUrl = "C:\\upload\\visual-comment\\photo\\";

    public String uploadVideoFile(MultipartFile file, String fileName) {
        return getUrl(file, videoFileUrl, fileName);
    }
    public String uploadPhotoFile(MultipartFile file, String fileName) {
        return getUrl(file, photoFileUrl, fileName);
    }

    public String getUrl(MultipartFile file, String fileUrl, String fileName){
        try{
            file.transferTo(new File(fileUrl + fileName));
        }catch (Exception e){
            return "Cannot upload file " + fileName;
        }
        return fileUrl + fileName;
    }
}
