package dev.wortel.meshok.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class ImageFileValidator implements ConstraintValidator<ValidImage, MultipartFile[]> {
    @Override
    public boolean isValid(MultipartFile[] files, ConstraintValidatorContext context) {
        if (files == null || files.length == 0) {
            return false;
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                return false;
            }

            String contentType = file.getContentType();
            if (!isSupportedContentType(contentType)) {
                return false;
            }
        }
        return true;
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType.equals("image/png")
                || contentType.equals("image/jpg")
                || contentType.equals("image/jpeg")
                || contentType.equals("image/webp");
    }
}