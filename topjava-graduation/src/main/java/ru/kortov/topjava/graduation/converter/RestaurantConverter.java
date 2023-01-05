package ru.kortov.topjava.graduation.converter;

import org.springframework.stereotype.Component;
import ru.kortov.topjava.graduation.dto.RestaurantDTO;
import ru.kortov.topjava.graduation.model.Restaurant;

@Component
public class RestaurantConverter implements EntityConverter<Restaurant, RestaurantDTO> {
    @Override
    public RestaurantDTO convertToDto(Restaurant entity) {
        return new RestaurantDTO(entity.getId(), entity.getName());
    }

    @Override
    public Restaurant convertToEntity(RestaurantDTO dto) {
        final var restaurant = new Restaurant();
        restaurant.setId(dto.id());
        restaurant.setName(dto.name());
        return restaurant;
    }
}
