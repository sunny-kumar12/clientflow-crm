
package com.clientflow.crm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/favicon.ico"
                        ).permitAll()

                        // Only Admin may archive customers.
                        .requestMatchers(
                                HttpMethod.POST,
                                "/customers/*/archive"
                        ).hasRole("ADMIN")

                        // Both authenticated roles can use the CRM.
                        .requestMatchers(
                                "/dashboard",
                                "/customers",
                                "/customers/**"
                        ).hasAnyRole("ADMIN", "SALES")

                        .requestMatchers(
                                org.springframework.http.HttpMethod.POST,
                                "/leads/*/archive"
                        ).hasRole("ADMIN")

                        .requestMatchers("/settings", "/settings/**")
                        .hasAnyRole("ADMIN", "SALES")

                        .requestMatchers("/leads", "/leads/**")
                        .hasAnyRole("ADMIN", "SALES")


                        .requestMatchers("/pipeline")
                        .hasAnyRole("ADMIN", "SALES")

                        .requestMatchers(
                                org.springframework.http.HttpMethod.POST,
                                "/pipeline/*/status"
                        ).hasAnyRole("ADMIN", "SALES")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}
