package com.flashdrop.auth.domain.model;

import java.util.UUID;

public record Role(UUID id, String name, String route) {
}
