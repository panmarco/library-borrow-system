package tw.com.marco.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 徹底關閉 CSRF 防禦 (前後端分離的 403 元兇)
            .csrf(csrf -> csrf.disable())
            // 放行規則
            .authorizeHttpRequests(auth -> auth
            	// 允許存取靜態資源 (HTML, CSS, JS)
            	.requestMatchers("/", "/login.html", "/library.html", "/register.html", "/css/**", "/js/**").permitAll()
                .requestMatchers("/user/**", "/library/**").permitAll()
                .anyRequest().authenticated()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}