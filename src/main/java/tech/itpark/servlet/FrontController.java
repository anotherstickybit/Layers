package tech.itpark.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tech.itpark.bodyconverter.GsonBodyConverter;
import tech.itpark.bodyconverter.StringBodyConverter;
import tech.itpark.controller.UserController;
import tech.itpark.di.container.Container;
import tech.itpark.exception.InitializationException;
import tech.itpark.http.Handler;
import tech.itpark.repository.UserRepositoryJDBCImpl;
import tech.itpark.service.UserServiceDefaultImpl;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FrontController extends HttpServlet {
    private Map<String, Map<String, Handler>> routes;
    private final Handler notFoundHandler =
            (request, response) -> response.sendError(404, "Page not found");

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final var path = request.getServletPath();
        final var method = request.getMethod();

        final var handler = Optional.ofNullable(routes.get(method))
                .map(o -> o.get(path))
                .orElseGet(() -> notFoundHandler);

        try {
            handler.handle(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() throws ServletException {
        try {
            final var cxt = new InitialContext();
            final var ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/db");
            UserController controller = new UserController(
                    new UserServiceDefaultImpl(new UserRepositoryJDBCImpl(ds)),
                    List.of(new GsonBodyConverter(new Gson()), new StringBodyConverter()));

            routes = Map.of(
                    "POST", Map.of(
                            "/api/register", controller::register,
                            "/api/login", controller::login,
                            "/api/reset", controller::reset
                    ),
                    "DELETE", Map.of(
                            "/api/remove", controller::remove
                    )
            );

        } catch (Exception e) {
            throw new InitializationException();
        }
    }
}
