package de.blaumeise03.projectmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;

public class LoggingInterceptor implements HandlerInterceptor {
    Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
        Principal userPrincipal = request.getUserPrincipal();
        HttpSession session = request.getSession(false);
        String username = "N/A";
        if(userPrincipal != null) username = userPrincipal.getName();
        String sessionID = "N/A";
        if (session != null) {
            sessionID = session.getId();
            int i = Math.max(sessionID.length() - 8, 0);
            sessionID = sessionID.substring(0, i) + "********";
        }
        logger.info(
                "Processed {} request [{}] from user '{}' (IP Hash '{}', session '{}') to: '{}: {}'",
                request.getMethod(),
                response.getStatus(),
                username,
                request.getRemoteAddr().hashCode(),
                sessionID,
                request.getMethod(),
                request.getRequestURI()
        );
        logger.debug("IP for Hash {} is " + request.getRemoteAddr(), request.getRemoteAddr().hashCode());
        logger.debug("Request cookies: " + request.getHeader("cookie"));
        if (!response.getHeaders("set-cookie").isEmpty())
            logger.debug("Response Set-Cookie: " + response.getHeaders("set-cookie"));
    }
}
