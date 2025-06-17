package dev.wortel.meshok.error;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With")) ||
                request.getContentType() != null &&
                        request.getContentType().contains("application/json");
    }

    private HttpStatus resolveHttpStatus(ErrorType errorType) {
        return switch (errorType) {
            case VALIDATION -> HttpStatus.BAD_REQUEST;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case BUSINESS -> HttpStatus.CONFLICT;
            case SECURITY -> HttpStatus.FORBIDDEN;
            case SYSTEM -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    private ModelAndView prepareErrorView(BaseException ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", ex.getMessage());
        mav.setStatus(resolveHttpStatus(ex.getErrorType()));

        switch (ex.getErrorType()) {
            case VALIDATION -> mav.setViewName("error/400");
            case NOT_FOUND -> mav.setViewName("error/404");
            case SECURITY -> mav.setViewName("error/403");
            default -> mav.setViewName("error/500");
        }

        return mav;
    }

    @ExceptionHandler(BaseException.class)
    public Object handleBaseException(BaseException ex, HttpServletRequest request) {
        log.error("Handled exception: {}", ex.getMessage(), ex);

        if (isAjaxRequest(request)) {
            return ResponseEntity.status(resolveHttpStatus(ex.getErrorType()))
                    .body(new ErrorResponse(ex.getErrorType(), ex.getMessage()));
        }
        return prepareErrorView(ex);
    }

    @ExceptionHandler(Exception.class)
    public Object handleException(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);

        if (isAjaxRequest(request)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(ErrorType.SYSTEM, "Internal server error"));
        }

        ModelAndView mav = new ModelAndView("error/500");
        mav.addObject("errorMessage", "An unexpected error occurred");
        mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return mav;
    }
}