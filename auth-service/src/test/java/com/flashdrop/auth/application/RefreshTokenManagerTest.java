package com.flashdrop.auth.application;

import com.flashdrop.auth.application.port.outbound.IdGenerator;
import com.flashdrop.auth.application.port.outbound.OpaqueTokenService;
import com.flashdrop.auth.application.port.outbound.RefreshTokenStore;
import com.flashdrop.auth.application.usecase.RefreshTokenManager;
import com.flashdrop.auth.domain.exception.InvalidTokenException;
import com.flashdrop.auth.domain.model.RefreshToken;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RefreshTokenManagerTest {

    private final RefreshTokenStore store = mock(RefreshTokenStore.class);
    private final OpaqueTokenService opaque = mock(OpaqueTokenService.class);
    private final IdGenerator ids = mock(IdGenerator.class);
    private final RefreshTokenManager manager =
            new RefreshTokenManager(store, opaque, ids, Duration.ofDays(30));

    @Test
    void emiteYGuardaSoloElHash() {
        when(opaque.generate()).thenReturn("raw-token");
        when(opaque.hash("raw-token")).thenReturn("hash");
        when(ids.newId()).thenReturn(UUID.randomUUID());

        String raw = manager.issueFor(UUID.randomUUID());

        assertEquals("raw-token", raw);
        verify(store).save(argThat(t -> t.tokenHash().equals("hash") && !t.revoked()));
    }

    @Test
    void rotarRevocaElActualYEmiteUnoNuevo() {
        UUID userId = UUID.randomUUID();
        var current = new RefreshToken(UUID.randomUUID(), userId, "old-hash",
                Instant.now().plusSeconds(60), false);
        when(opaque.hash("old-raw")).thenReturn("old-hash");
        when(store.findByTokenHash("old-hash")).thenReturn(Optional.of(current));
        when(opaque.generate()).thenReturn("new-raw");
        when(opaque.hash("new-raw")).thenReturn("new-hash");
        when(ids.newId()).thenReturn(UUID.randomUUID());

        var rotation = manager.rotate("old-raw");

        assertEquals(userId, rotation.userId());
        assertEquals("new-raw", rotation.newRefreshToken());
        verify(store).revoke(argThat(RefreshToken::revoked));
    }

    @Test
    void rotarTokenRevocadoFalla() {
        var revoked = new RefreshToken(UUID.randomUUID(), UUID.randomUUID(), "h",
                Instant.now().plusSeconds(60), true);
        when(opaque.hash("x")).thenReturn("h");
        when(store.findByTokenHash("h")).thenReturn(Optional.of(revoked));
        assertThrows(InvalidTokenException.class, () -> manager.rotate("x"));
    }
}
