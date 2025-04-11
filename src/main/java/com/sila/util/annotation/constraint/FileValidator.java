package com.sila.util.annotation.constraint;


import com.sila.util.annotation.ValidFile;
import com.sila.exception.FileValidationException; // Import your custom exception
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    private long maxSize;
    private String[] allowedTypes;

    @Override
    public void initialize(ValidFile constraintAnnotation) {
        this.maxSize = constraintAnnotation.maxSize();
        this.allowedTypes = constraintAnnotation.allowedTypes();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            throw new FileValidationException("File is missing or empty.");
        }

        if (file.getSize() > maxSize) {
            throw new FileValidationException("File is too large. Max allowed size is " + (maxSize / 1024 / 1024) + "MB.");
        }

        String contentType = file.getContentType();
        if (contentType == null || Arrays.stream(allowedTypes).noneMatch(contentType::equalsIgnoreCase)) {
            throw new FileValidationException("Only these file types are allowed: " + String.join(", ", allowedTypes));
        }

        return true; // Validation passed
    }
}
