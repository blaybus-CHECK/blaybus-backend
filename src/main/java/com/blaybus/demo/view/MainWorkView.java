package com.blaybus.demo.view;

import java.util.UUID;

public record MainWorkView(
    UUID id,
    String title,
    String description
//    String[] imageUris
) {
}
