package com.flashdrop.auth.application.usecase;

import com.flashdrop.auth.application.dto.UserProfile;
import com.flashdrop.auth.application.port.inbound.GetUserProfileUseCase;
import com.flashdrop.auth.application.port.outbound.UserRepository;
import com.flashdrop.auth.domain.exception.InvalidTokenException;
import com.flashdrop.auth.domain.model.User;

import java.util.UUID;

public class GetUserProfileService implements GetUserProfileUseCase {

    private final UserRepository users;

    public GetUserProfileService(UserRepository users) {
        this.users = users;
    }

    @Override
    public UserProfile getProfile(UUID userId) {
        User u = users.findById(userId).orElseThrow(InvalidTokenException::new);
        return new UserProfile(u.id(), u.name(), u.lastName(), u.email().value(),
                u.phone(), u.rut(), u.photo(), u.roleNames());
    }
}
