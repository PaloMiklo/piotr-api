package com.api.piotr.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.api.piotr.dto.ImageTableRowDto;
import com.api.piotr.error.ResourceNotFoundException;
import com.api.piotr.repository.ImageRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class ImageTableService {
    private final ImageRepository imageRepository;

    public StreamingResponseBody getImageByProductId(Long id) {
        ImageTableRowDto image = imageRepository.findImageTableByProductId(id)
                .orElseThrow(() -> new ResourceNotFoundException("ImageTable", String.valueOf(id)));

        StreamingResponseBody responseBody = outputStream -> {
            if (image != null) {
                byte[] imageData = image.image();
                outputStream.write(imageData);
            }
        };

        return responseBody;
    }
}
