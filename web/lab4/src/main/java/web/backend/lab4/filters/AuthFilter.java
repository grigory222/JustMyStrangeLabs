package web.backend.lab4.filters;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Cookie;
import web.backend.lab4.auth.JwtProvider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Provider
public class AuthFilter implements jakarta.ws.rs.container.ContainerRequestFilter {

    @Inject
    private JwtProvider jwtProvider;


    private static final Set<String> SKIP_PATHS = new HashSet<>(Arrays.asList(
            "/auth/signup",
            "/auth/login",
            "/auth/logout",
            "/auth/refresh"));

    @Override
    public void filter(ContainerRequestContext requestContext){
        var path = requestContext.getUriInfo().getPath();
        if (SKIP_PATHS.contains(path)) {
            return;
        }

        // Получаем токен из cookie
        Cookie cookie = requestContext.getCookies().get("access_token");

        if (cookie == null || cookie.getValue().isEmpty()) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Access token is missing")
                    .build());
            return;
        }

        String accessToken = cookie.getValue();

        // Проверяем валидность токена
        if (jwtProvider.isTokenExpired(accessToken)) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid or expired access token")
                    .build());
        }

        // Если токен валиден, разрешаем выполнение запроса
    }
}