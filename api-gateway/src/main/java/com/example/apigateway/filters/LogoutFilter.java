package com.example.apigateway.filters;

import com.example.apigateway.logoutToken.JwtService;
import com.example.apigateway.logoutToken.Token;
import com.example.apigateway.logoutToken.TokenRepository;
import com.google.common.net.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class LogoutFilter extends AbstractGatewayFilterFactory<LogoutFilter.Config> {

    @Autowired
    JwtService jwtService;

    @Autowired
    TokenRepository tokenRepository;

    public static class Config {
        // ...
    }
    public LogoutFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        System.out.println("Logout filter is being executed");

        return (exchange, chain) -> {

            //Get request
            ServerHttpRequest request = exchange.getRequest();

            //Checks if there is a jwt token in the authorization header
            if (request.getHeaders().get(org.springframework.http.HttpHeaders.AUTHORIZATION) == null){
                return onError(exchange,"Make sure to put a Jwt token in your request",HttpStatus.BAD_REQUEST);
            }

            //Extract auth header from request;
            String authorizationHeader = Objects.requireNonNull(request.getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);

            //Extract the jwt from the auth header
            String jwt = authorizationHeader.replace("Bearer ", "");

            if (tokenRepository.findByToken(jwt).isPresent() && tokenRepository.findByToken(jwt).get().isRevoked()){
                return onError(exchange,"Token has already been invalidated",HttpStatus.BAD_REQUEST);
            }else {
                tokenRepository.revokeAndExpireTokenByToken(jwt);
            }

            //If this token does not exists in the database but the user still wants to invalidate it, creates a new token object with revoked true and saves it
            Token token = Token.builder()
                    .token(jwt)
                    .subject(jwtService.extractUsername(jwt))
                    .revoked(true)
                    .expired(true)
                    .build();

            tokenRepository.save(token);

            return chain.filter(exchange);
        };
    }

    //Exception handler
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().add(org.springframework.http.HttpHeaders.CONTENT_TYPE, "application/json");

        return response.writeWith(Mono.just(response.bufferFactory().wrap(err.getBytes())))
                .doOnError(error -> {
                    throw new ResponseStatusException(httpStatus, err);
                });
    }


}