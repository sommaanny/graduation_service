package graduation_service.graduation.exception;

import graduation_service.graduation.dto.responseDto.ApiResponse;
import graduation_service.graduation.dto.responseDto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler  {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleNoSuchElementException(NoSuchElementException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse("NOT_FOUND", request.getRequestURI(), LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND).body(ApiResponse.failure(ex.getMessage(), errorResponse));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleIllegalStateException(IllegalStateException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse("CONFLICT", request.getRequestURI(), LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.CONFLICT).body(ApiResponse.failure(ex.getMessage(), errorResponse));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse("BAD_REQUEST", request.getRequestURI(), LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(ex.getMessage(), errorResponse));
    }

    @ExceptionHandler(TranscriptParsingException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleTranscriptParsingException(TranscriptParsingException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse("PARSING_ERROR", request.getRequestURI(), LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.failure(ex.getMessage(), errorResponse));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {

        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponse errorResponse = new ErrorResponse("VALID_ERROR", request.getRequestURI(), LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(errorMessage, errorResponse));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String message = "요청 본문의 형식이 올바르지 않습니다. JSON 형식이나 데이터 타입을 확인해주세요.";

        ErrorResponse errorResponse = new ErrorResponse("HTTP_MESSAGE_ERROR", request.getRequestURI(), LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(message, errorResponse));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex, HttpServletRequest request) {

        String field = ex.getParameterName();
        String message = "필수 파라미터 '" + field + "'가 누락되었습니다.";

        ErrorResponse errorResponse = new ErrorResponse("MISSING_PARAMETER_ERROR", request.getRequestURI(), LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(message, errorResponse));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        String message = String.format("요청 파라미터 '%s'의 값 타입이 올바르지 않습니다.", ex.getName());

        ErrorResponse errorResponse = new ErrorResponse("ARGUMENT_MISS_MATCH_ERROR", request.getRequestURI(), LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(message, errorResponse));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request) {

        String message = String.format("요청하신 URI '%s'는 존재하지 않습니다.", request.getRequestURI());

        ErrorResponse errorResponse = new ErrorResponse("URI_FOUND_ERROR", request.getRequestURI(), LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(message, errorResponse));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleGenericException(HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse("INTERNAL_SERVER_ERROR", request.getRequestURI(), LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.failure("예상치 못한 오류가 발생했습니다.", errorResponse));
    }


}
