package com.application.config;

import com.application.exception.ServerException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Log4j2
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.error("Unauthorized error: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ServerException serverException = new ServerException(authException.getMessage(),
                HttpStatus.UNAUTHORIZED.series().name(), request.getRequestURL().toString(),
                "Authorize() Method", String.valueOf(HttpStatus.UNAUTHORIZED.value()));

         /*final Map<String, Object> body = new HashMap<>();
        body.put("errMessage", authException.getMessage());
        body.put("errorCode", HttpStatus.UNAUTHORIZED.series().name());
        body.put("requestedUrl", request.getRequestURL().toString());
        body.put("serviceName", "Authorize() Method");
        body.put("statusCode", String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        */

        final ObjectMapper mapper = new ObjectMapper();
        final Map<String, Object> body = mapper.convertValue(serverException, Map.class);

        mapper.writeValue(response.getOutputStream(), body);
    }

}
