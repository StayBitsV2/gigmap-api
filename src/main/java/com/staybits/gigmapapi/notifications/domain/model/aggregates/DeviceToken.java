package com.staybits.gigmapapi.notifications.domain.model.aggregates;

import com.staybits.gigmapapi.notifications.domain.model.commands.CreateDeviceTokenCommand;
import com.staybits.gigmapapi.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class DeviceToken extends AuditableAbstractAggregateRoot<DeviceToken> {
    private Long userId;
    private String token;

    public DeviceToken() {}

    public DeviceToken(CreateDeviceTokenCommand command) {
        this.userId = command.userId();
        this.token = command.token();       
    }
}