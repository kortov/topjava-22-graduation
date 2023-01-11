package ru.kortov.topjava.graduation.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kortov.topjava.graduation.dto.RestaurantDTO;

import java.util.List;

// TODO: remove class
@Tag(name = "Restaurant admin api")
public interface RestaurantApi {

    @Operation(summary = "Get restaurants list", description = "Get")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "401", description = "Unauthorised"),
        @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @GetMapping("/api/v1/admin/restaurants")
    List<RestaurantDTO> getRestaurants();
}
