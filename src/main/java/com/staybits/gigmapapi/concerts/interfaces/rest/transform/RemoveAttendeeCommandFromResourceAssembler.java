package com.staybits.gigmapapi.concerts.interfaces.rest.transform;

import com.staybits.gigmapapi.concerts.domain.model.commands.RemoveAttendeeCommand;
import com.staybits.gigmapapi.concerts.interfaces.rest.resources.AttendeeResource;

public class RemoveAttendeeCommandFromResourceAssembler {
    
    public static RemoveAttendeeCommand toCommandFromResource(AttendeeResource resource) {
        if (resource == null) return null;
        return new RemoveAttendeeCommand(resource.concertId(), resource.userId());
    }
}
