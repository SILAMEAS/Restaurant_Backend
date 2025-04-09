package com.sila.annotation.constraint;

import com.sila.annotation.ValidFile;
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
            setMessage(context, "File is missing or empty.");
            return false;
        }

        if (file.getSize() > maxSize) {
            setMessage(context, "File is too large. Max allowed size is " + (maxSize / 1024 / 1024) + "MB.");
            return false;
        }

        String contentType = file.getContentType();
        if (contentType == null || Arrays.stream(allowedTypes).noneMatch(contentType::equalsIgnoreCase)) {
            setMessage(context, "Only these file types are allowed: " + String.join(", ", allowedTypes));
            return false;
        }

        return true;
    }

    private void setMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
