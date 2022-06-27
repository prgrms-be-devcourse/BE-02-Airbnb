package com.prgrms.airbnb.domain.common.service;

import com.amazonaws.AmazonServiceException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

  String uploadImg(MultipartFile multipartFile) throws IOException;

  String uploadImgToS3(BufferedImage image, String Filename, String ext)
      throws IllegalStateException, IOException;

  void delete(String path) throws AmazonServiceException;

  private String getUUID() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }
}
