package fit.iuh.se.filter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

public class JwtFilter implements WebFilter {
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // Lấy header Authorization từ request
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Trích xuất JWT token (bỏ phần "Bearer ")
            String jwt = authHeader.substring(7);
            
            // Thêm JWT vào Reactor Context
            return chain.filter(exchange)
                .contextWrite(Context.of("jwt", jwt));
        }
        
        // Nếu không có JWT, vẫn tiếp tục xử lý request
        return chain.filter(exchange);
    }
}