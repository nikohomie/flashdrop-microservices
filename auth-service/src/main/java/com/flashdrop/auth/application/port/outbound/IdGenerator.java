package com.flashdrop.auth.application.port.outbound;

import java.util.UUID;

public interface IdGenerator {
    UUID newId();
}
