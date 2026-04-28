package com.staybits.gigmapapi.concerts.interfaces.rest.transform;

import com.staybits.gigmapapi.concerts.domain.model.commands.AddAttendeeCommand;
import com.staybits.gigmapapi.concerts.interfaces.rest.resources.AttendeeResource;

public class AddAttendeeCommandFromResourceAssembler {
    
    public static AddAttendeeCommand toCommandFromResource(AttendeeResource resource) {
        if (resource == null) return null;
        return new AddAttendeeCommand(resource.concertId(), resource.userId());
    }
}
