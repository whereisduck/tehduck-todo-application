package com.theduck.todoapp.config.properitiesconifg;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
@Slf4j
public class LoggingContextInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = getUserIdFromPrincipal(authentication.getPrincipal());
        MDC.put("userId", userId);
        return true;//HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private String getUserIdFromPrincipal(Object principal) {
        if (principal instanceof String) {
            // anonymous users will have a String principal with value "anonymousUser"
            return principal.toString();
        }
        if (principal instanceof OidcUser){
            try {
                OidcUser user = (OidcUser) principal;
                if(user.getPreferredUsername()!=null){
                    return user.getPreferredUsername();
                }else if (user.getClaimAsString("name") !=null){
                    return user.getClaimAsString("name");
                }else {
                    log.warn("could not extract userId from principal");
                    return "unkown";
                }
            } catch (Exception e){
                log.warn("could not extract userId from Principal", e);

            }

        }
        return "unknown";

    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.clear();
    }
}
