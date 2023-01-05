package ru.kortov.topjava.graduation.web.controller;

import org.springframework.web.bind.annotation.RestController;
import ru.kortov.topjava.graduation.dto.RestaurantDTO;
import ru.kortov.topjava.graduation.web.api.RestaurantApi;

import java.util.List;

@RestController
public class RestaurantController implements RestaurantApi {
    @Override
    public List<RestaurantDTO> getRestaurants() {
        return List.of(new RestaurantDTO(1L, "Name1"), new RestaurantDTO(2L, "Name2"));
    }

}
