package ru.kortov.topjava.graduation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

// TODO: remove class
@Schema(name = "Restaurant")
public record RestaurantDTO(@Schema(description = "Identifier") Long id, String name) {
}
