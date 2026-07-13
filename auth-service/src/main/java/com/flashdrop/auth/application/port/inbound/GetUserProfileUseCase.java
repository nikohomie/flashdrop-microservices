package com.flashdrop.auth.application.port.inbound;

import com.flashdrop.auth.application.dto.UserProfile;

import java.util.UUID;

public interface GetUserProfileUseCase {
    UserProfile getProfile(UUID userId);
}
