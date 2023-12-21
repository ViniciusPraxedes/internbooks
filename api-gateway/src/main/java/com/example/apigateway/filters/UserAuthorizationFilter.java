
package com.example.apigateway.filters;

import java.util.Objects;

import com.example.apigateway.logoutToken.JwtService;
import com.example.apigateway.logoutToken.Token;
import com.example.apigateway.logoutToken.TokenRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
@Transactional
public class UserAuthorizationFilter extends AbstractGatewayFilterFactory<UserAuthorizationFilter.Config> {

	@Autowired
	Environment env;
	@Autowired
	JwtService jwtService;
	@Autowired
	TokenRepository tokenRepository;

	public UserAuthorizationFilter() {
		super(Config.class);
	}

	public static class Config {
		// Put configuration properties here
	}

	@Override
	public GatewayFilter apply(Config config) {

		return (exchange, chain) -> {

			//Get request
			ServerHttpRequest request = exchange.getRequest();

			//Checks if there is a jwt token in the authorization header
			if (request.getHeaders().get(HttpHeaders.AUTHORIZATION) == null){
				return onError(exchange,"Make sure to put a Jwt token in your request",HttpStatus.FORBIDDEN);
			}

			//Extract auth header from request;
			String authorizationHeader = Objects.requireNonNull(request.getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);

			//Extract the jwt from the auth header
			String jwt = authorizationHeader.replace("Bearer ", "");


			try{
				//Checks if the jwt token has been expired
				if (jwtService.isTokenExpired(jwt)){
					return onError(exchange,"Token expired",HttpStatus.FORBIDDEN);
				}

				//Checks if token is revoked
				if (tokenRepository.findByToken(jwt).isPresent() && tokenRepository.findByToken(jwt).get().isRevoked()){
					return onError(exchange,"Token in invalid",HttpStatus.FORBIDDEN);
				}

				//If this token is a new token, revoke all tokens except this one
				if (tokenRepository.findByToken(jwt).isEmpty() && !tokenRepository.findAll().isEmpty()){
					tokenRepository.revokeAndExpireAllTokensBySubject(jwtService.extractUsername(jwt));
				}

			}catch (MalformedJwtException e){
				return onError(exchange,"Token bad formatted",HttpStatus.FORBIDDEN);
			}

			//Extract revoked from the jwt
			boolean revoked = (boolean) jwtService.extractAllClaims(jwt).get("revoked");

			//Extract expired from the jwt
			boolean expired = jwtService.isTokenExpired(jwt);

			//Creates this token object to register the revoked token in the database
			Token token = Token.builder()
					.token(jwt)
					.subject(jwtService.extractUsername(jwt))
					.revoked(revoked)
					.expired(expired)
					.build();


			//If the token is present but it is not revoked then save it to the database
			if (tokenRepository.findByToken(jwt).isPresent() && !tokenRepository.findByToken(jwt).get().isRevoked()){

			}else {
				tokenRepository.save(token);

			}

			return chain.filter(exchange);
		};

	}

	//Exception handler
	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");

		return response.writeWith(Mono.just(response.bufferFactory().wrap(err.getBytes())))
				.doOnError(error -> {
					throw new ResponseStatusException(httpStatus, err);
				});
	}



}
