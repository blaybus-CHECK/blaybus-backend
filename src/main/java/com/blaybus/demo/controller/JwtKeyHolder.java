package com.blaybus.demo.controller;

import javax.crypto.SecretKey;

public record JwtKeyHolder(SecretKey key) {
}
