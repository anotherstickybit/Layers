package tech.itpark.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import tech.itpark.bodyconverter.BodyConverter;
import tech.itpark.exception.ConvertionException;
import tech.itpark.exception.NoAppropriateConverter;
import tech.itpark.http.ContentTypes;
import tech.itpark.model.AuthenticationModel;
import tech.itpark.model.RegistrationModel;
import tech.itpark.model.RemovalModel;
import tech.itpark.model.ResetModel;
import tech.itpark.service.UserService;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    private final List<BodyConverter> converters;

    public void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final var dto = read(RegistrationModel.class, request);
        final var registered = service.register(dto);
        write(registered, ContentTypes.APPLICATION_JSON, response);
    }

    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final var dto = read(AuthenticationModel.class, request);
        final var loggedIn = service.login(dto);
        write(loggedIn, ContentTypes.APPLICATION_JSON, response);
    }

    public void reset(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final var dto = read(ResetModel.class, request);
        final var reset = service.reset(dto);
        write(reset, ContentTypes.APPLICATION_JSON, response);
    }

    public void remove(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final var dto = read(RemovalModel.class, request);
        final var removed = String.valueOf(service.remove(dto));
        write(removed, ContentTypes.STRING, response);

    }

    private <T> T read(Class<T> clazz, HttpServletRequest request) {
        for (final var converter : converters) {
            if (!converter.canRead(request.getContentType(), clazz)) continue;
            try {
                return converter.read(request.getReader(), clazz);
            } catch (IOException e) {
                throw new ConvertionException();
            }
        }
        throw new NoAppropriateConverter("No one appropriate converter for given Content-Type");
    }

    private void write(Object data, String contentType, HttpServletResponse response) {
        for (final var converter : converters) {
            if (!converter.canWrite(contentType, data.getClass())) continue;
            try {
                response.setContentType(contentType);
                converter.write(response.getWriter(), data);
                return;
            } catch (IOException e) {
                e.printStackTrace();
                // TODO: convert to special exception
                throw new RuntimeException(e);
            }
        }
        // TODO: convert to special exception
        throw new RuntimeException("no converters support given content type");
    }
}

