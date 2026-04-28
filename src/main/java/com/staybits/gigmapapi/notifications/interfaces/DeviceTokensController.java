package com.staybits.gigmapapi.notifications.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.staybits.gigmapapi.notifications.domain.model.aggregates.DeviceToken;
import com.staybits.gigmapapi.notifications.domain.model.commands.CreateDeviceTokenCommand;
import com.staybits.gigmapapi.notifications.domain.services.DeviceTokenCommandService;
import com.staybits.gigmapapi.notifications.interfaces.resources.CreateDeviceTokenResource;
import com.staybits.gigmapapi.notifications.interfaces.resources.DeviceTokenResource;
import com.staybits.gigmapapi.notifications.interfaces.transformers.CreateDeviceTokenCommandFromResourceAssembler;
import com.staybits.gigmapapi.notifications.interfaces.transformers.DeviceTokenResourceFromEntityAssembler;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping(value = "/api/v1/device_tokens", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Device Tokens", description = "Operations related to Device Tokens")
public class DeviceTokensController {
    private final DeviceTokenCommandService deviceTokenCommandService;

    public DeviceTokensController(DeviceTokenCommandService deviceTokenCommandService) {
        this.deviceTokenCommandService = deviceTokenCommandService;
    }

    @PostMapping
    @Operation(
        summary = "Create a new Device Token",
        description = "Create a new Device Token",
        responses = {
            @ApiResponse(responseCode = "201", description = "Device Token created successfully")
        }
    )
    public ResponseEntity<DeviceTokenResource> createDeviceToken(@RequestBody CreateDeviceTokenResource resource) {
        CreateDeviceTokenCommand command = CreateDeviceTokenCommandFromResourceAssembler.toCommandFromResource(resource);
        DeviceToken deviceToken = deviceTokenCommandService.handle(command);

        if (deviceToken == null || deviceToken.getId() == null || deviceToken.getId() <= 0)
            return ResponseEntity.badRequest().build();

        DeviceTokenResource response = DeviceTokenResourceFromEntityAssembler.toResourceFromEntity(deviceToken);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}