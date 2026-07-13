package com.flashdrop.auth.infrastructure.adapter.outbound.security;

import com.flashdrop.auth.application.port.outbound.IdGenerator;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * Genera UUID versión 7 (ordenados por tiempo): únicos globalmente entre
 * bases separadas y con buena localidad de índice en PostgreSQL.
 */
@Component
public class UuidV7Generator implements IdGenerator {

    private final SecureRandom random = new SecureRandom();

    @Override
    public UUID newId() {
        long ts = System.currentTimeMillis();
        byte[] b = new byte[16];
        random.nextBytes(b);

        // 48 bits de timestamp (big-endian)
        b[0] = (byte) (ts >>> 40);
        b[1] = (byte) (ts >>> 32);
        b[2] = (byte) (ts >>> 24);
        b[3] = (byte) (ts >>> 16);
        b[4] = (byte) (ts >>> 8);
        b[5] = (byte) ts;

        b[6] = (byte) ((b[6] & 0x0F) | 0x70);       // versión 7
        b[8] = (byte) ((b[8] & 0x3F) | 0x80);       // variante RFC 4122

        long msb = 0, lsb = 0;
        for (int i = 0; i < 8; i++) msb = (msb << 8) | (b[i] & 0xFF);
        for (int i = 8; i < 16; i++) lsb = (lsb << 8) | (b[i] & 0xFF);
        return new UUID(msb, lsb);
    }
}
