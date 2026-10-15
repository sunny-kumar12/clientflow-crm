package com.clientflow.crm.service;

import com.clientflow.crm.dto.SettingsForm;
import com.clientflow.crm.entity.AppUser;
import com.clientflow.crm.repository.AppUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SettingsService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public SettingsService(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder) {

        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public AppUser getCurrentUser(UserDetails userDetails) {

        return appUserRepository
                .findByUsernameIgnoreCase(userDetails.getUsername())
                .orElseThrow(() ->
                        new IllegalArgumentException("User account not found"));
    }

    @Transactional
    public void changePassword(
            UserDetails userDetails,
            SettingsForm form) {

        AppUser user = getCurrentUser(userDetails);

        if (!passwordEncoder.matches(
                form.getCurrentPassword(),
                user.getPasswordHash())) {

            throw new IllegalArgumentException(
                    "Current password is incorrect"
            );
        }

        if (!form.getNewPassword()
                .equals(form.getConfirmPassword())) {

            throw new IllegalArgumentException(
                    "New password and confirmation password do not match"
            );
        }

        if (passwordEncoder.matches(
                form.getNewPassword(),
                user.getPasswordHash())) {

            throw new IllegalArgumentException(
                    "New password must be different from your current password"
            );
        }

        user.setPasswordHash(
                passwordEncoder.encode(form.getNewPassword())
        );

        appUserRepository.save(user);
    }
}