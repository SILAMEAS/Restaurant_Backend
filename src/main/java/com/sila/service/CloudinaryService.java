package com.sila.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface CloudinaryService {


    public String uploadFile(MultipartFile file) throws IOException;

    public String deleteImage(String publicId) throws IOException;
}
