package com.api_gateway.filter;

import com.api_gateway.helper.KeyUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private static final String SECRET = "e3d46b8f-92e0-4baf-b199-82d0a5ee52d9";

    Logger logger = LoggerFactory.getLogger(JwtGatewayFilterFactory.class);
    @Override
    public GatewayFilter apply(Object config) {

        return (exchange, chain)->{
            String authheader = exchange.getRequest().
                    getHeaders().getFirst("Authorization");
            String token;

            if(authheader == null || !authheader.startsWith("Bearer ")){
                logger.debug("Received token: {}");
                return exchange.getResponse().setComplete()  ;
            }
            token = authheader.substring(7);

            try{

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(KeyUtils.getPublicKey("public.pem"))
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                List<String> roles = claims.get("role", List.class);
                String roleHeader = String.join(",", roles);

                System.out.println("before....");
                ServerHttpRequest customHttpRequest =  exchange.getRequest().mutate()
                        .header("X-Gateway-Auth",SECRET)
                        .header("X-User",claims.getSubject())
                        .header("X-Role",roleHeader)
                        .build();

                System.out.println("After...");
                return chain.filter(exchange.mutate().request(customHttpRequest).build());

            } catch (JwtException e) {

                logger.info("Invalid signature", e);
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            } catch (Exception e) {
                logger.error("❌ Unexpected error in JwtGatewayFilter", e);
                throw new RuntimeException(e);
            }


        };
    }
}
