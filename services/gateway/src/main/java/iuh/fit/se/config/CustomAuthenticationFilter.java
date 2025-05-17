//package iuh.fit.se.config;
//
//import java.io.IOException;
//import java.security.Principal;
//
////import org.springframework.security.core.Authentication;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class CustomAuthenticationFilter extends OncePerRequestFilter {
//
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		Principal userPrincipal = request.getUserPrincipal();
////		if (userPrincipal instanceof Authentication auth) {
////			log.info("User Authorities: {}", auth.getAuthorities());
////		} else {
////			 log.info("No Authentication found in Principal: {}", userPrincipal);
////		}
//	}
//
//}
//
////package iuh.fit.se.dto;
////
////import java.util.List;
////
//////import org.springframework.boot.context.properties.ConfigurationProperties;
//////import org.springframework.stereotype.Component;
////
////import lombok.Data;
////
//////@Component
//////@ConfigurationProperties(prefix = "cors")
////@Data
////public class CorsPropertiesDto {
////	private List<String> allowedOrigins;
////	private List<String> allowedMethods;
////}
