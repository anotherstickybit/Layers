package tech.itpark.http;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Handler {
    void handle(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
