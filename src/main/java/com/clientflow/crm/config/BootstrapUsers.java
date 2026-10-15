
package com.clientflow.crm.config;

import com.clientflow.crm.entity.AppUser;
import com.clientflow.crm.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BootstrapUsers {

    @Bean
    ApplicationRunner bootstrapAccounts(
            AppUserRepository repository,
            PasswordEncoder passwordEncoder,
            @Value("${app.bootstrap.admin.username:}") String adminUsername,
            @Value("${app.bootstrap.admin.password:}") String adminPassword,
            @Value("${app.bootstrap.sales.username:}") String salesUsername,
            @Value("${app.bootstrap.sales.password:}") String salesPassword) {

        return args -> {
            createAccount(
                    repository, passwordEncoder,
                    adminUsername, adminPassword, "ADMIN"
            );

            createAccount(
                    repository, passwordEncoder,
                    salesUsername, salesPassword, "SALES"
            );
        };
    }

    private void createAccount(
            AppUserRepository repository,
            PasswordEncoder passwordEncoder,
            String username,
            String password,
            String role) {

        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {
            return;
        }

        if (repository.existsByUsernameIgnoreCase(username)) {
            return;
        }

        AppUser account = new AppUser(
                username.trim(),
                passwordEncoder.encode(password),
                role
        );

        repository.save(account);
    }
}
