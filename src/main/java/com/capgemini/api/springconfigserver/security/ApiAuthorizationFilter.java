package com.capgemini.api.springconfigserver.security;

import org.apache.http.HttpHeaders;
import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.StringUtils.hasText;

@Component
public class ApiAuthorizationFilter extends OncePerRequestFilter {

    @Value("${jwks.url}")
    private String jwkUrl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestedUri = request.getRequestURI();
        if(requestedUri.startsWith("/actuator")){
            filterChain.doFilter(request, response);
        }else {
            String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
            if(!hasText(jwtToken)){
                addErrorResponse(response, "Auth Header not sent", 400);
                return;
            }
            JwtClaims jwtClaims = null;
            try {
                HttpsJwks httpsJkws = new HttpsJwks(jwkUrl);

                // The HttpsJwksVerificationKeyResolver uses JWKs obtained from the HttpsJwks and will select the
                // most appropriate one to use for verification based on the Key ID and other factors provided
                // in the header of the JWS/JWT.
                HttpsJwksVerificationKeyResolver httpsJwksKeyResolver = new HttpsJwksVerificationKeyResolver(httpsJkws);


                // Use JwtConsumerBuilder to construct an appropriate JwtConsumer, which will
                // be used to validate and process the JWT. But, in this case, provide it with
                // the HttpsJwksVerificationKeyResolver instance rather than setting the
                // verification key explicitly.
                JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                        .setVerificationKeyResolver(httpsJwksKeyResolver)
                        .setExpectedAudience("Audience")
                        .setExpectedIssuer("Issuer")
                        .build();
                jwtClaims = jwtConsumer.processToClaims(jwtToken.replace("Bearer ", ""));
            } catch (Exception exc){
                exc.printStackTrace();
                addErrorResponse(response, "Auth Header Signature is not valid ", 400);
                return;
            }

            try {
                if(!jwtClaims.getSubject().equals(requestedUri.split("/")[1])){
                    addErrorResponse(response, "You are not allowed to access this uri", 400);
                    return;
                }
            } catch (MalformedClaimException e) {
                e.printStackTrace();
                addErrorResponse(response, "Invalid Auth header", 400);
                return;

            }
            filterChain.doFilter(request, response);
        }
    }

    private void addErrorResponse(HttpServletResponse response, String message, int code) throws IOException {
        PrintWriter pr = new PrintWriter(response.getOutputStream());
        pr.write("{ \"error\" : \""+message +"\"}");
        response.setStatus(code);
        response.setContentType(APPLICATION_JSON_VALUE);
    }
}
