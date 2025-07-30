package br.com.gabriel.websocket.handlers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.gabriel.websocket.exceptions.UserAlreadyExistsException;
import br.com.gabriel.websocket.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(HttpServletRequest request, RuntimeException exception) {
        Map<String, Object> errorInfo = new HashMap<String, Object>();
        errorInfo.put("timestamp", LocalDateTime.now().toString());
        errorInfo.put("status", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        errorInfo.put("error", "Erro interno do servidor");
        errorInfo.put("path", request.getRequestURI());
        errorInfo.put("message", exception.getMessage());
        return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).body(errorInfo);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyExistsException(HttpServletRequest request, UserAlreadyExistsException exception) {
        Map<String, Object> errorInfo = new HashMap<String, Object>();
        errorInfo.put("timestamp", LocalDateTime.now().toString());
        errorInfo.put("status", HttpServletResponse.SC_BAD_REQUEST);
        errorInfo.put("error", "Erro interno do servidor");
        errorInfo.put("path", request.getRequestURI());
        errorInfo.put("message", exception.getMessage());
        return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(errorInfo);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(HttpServletRequest request, UserNotFoundException exception) {
        Map<String, Object> errorInfo = new HashMap<String, Object>();
        errorInfo.put("timestamp", LocalDateTime.now().toString());
        errorInfo.put("status", HttpServletResponse.SC_NOT_FOUND);
        errorInfo.put("error", "Erro interno do servidor");
        errorInfo.put("path", request.getRequestURI());
        errorInfo.put("message", exception.getMessage());
        return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(errorInfo);
    }
}
