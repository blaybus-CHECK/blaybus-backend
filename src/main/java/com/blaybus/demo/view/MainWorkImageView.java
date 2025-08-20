package com.blaybus.demo.view;

import java.time.LocalDateTime;
import java.util.UUID;

public record MainWorkImageView(
    UUID id,
    UUID mainWorkId,
    String imageUrl,
    LocalDateTime registeredTimeUtc
) {
}
