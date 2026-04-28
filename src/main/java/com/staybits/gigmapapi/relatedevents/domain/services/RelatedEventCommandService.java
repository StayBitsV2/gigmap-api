package com.staybits.gigmapapi.relatedevents.domain.services;

import com.staybits.gigmapapi.relatedevents.domain.model.aggregates.RelatedEvent;
import com.staybits.gigmapapi.relatedevents.domain.model.commands.CreateRelatedEventCommand;
import com.staybits.gigmapapi.relatedevents.domain.model.commands.UpdateRelatedEventCommand;
import com.staybits.gigmapapi.relatedevents.domain.model.commands.DeleteRelatedEventCommand;
import com.staybits.gigmapapi.relatedevents.domain.model.commands.AddParticipantCommand;
import com.staybits.gigmapapi.relatedevents.domain.model.commands.RemoveParticipantCommand;

public interface RelatedEventCommandService {
    RelatedEvent handle(CreateRelatedEventCommand command);
    
    RelatedEvent handle(UpdateRelatedEventCommand command);
    
    boolean handle(DeleteRelatedEventCommand command);
    
    RelatedEvent handle(AddParticipantCommand command);
    
    RelatedEvent handle(RemoveParticipantCommand command);
}
