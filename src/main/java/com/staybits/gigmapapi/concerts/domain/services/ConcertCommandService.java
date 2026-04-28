package com.staybits.gigmapapi.concerts.domain.services;

import com.staybits.gigmapapi.concerts.domain.model.aggregates.Concert;
import com.staybits.gigmapapi.concerts.domain.model.commands.CreateConcertCommand;
import com.staybits.gigmapapi.concerts.domain.model.commands.UpdateConcertCommand;
import com.staybits.gigmapapi.concerts.domain.model.commands.DeleteConcertCommand;
import com.staybits.gigmapapi.concerts.domain.model.commands.AddAttendeeCommand;
import com.staybits.gigmapapi.concerts.domain.model.commands.RemoveAttendeeCommand;

/**
 * Concert command service
 * <p>
 *     This interface represents the service to handle concert commands.
 * </p>
 */
public interface ConcertCommandService {
    /**
     * Handle create concert command
     * @param command the {@link CreateConcertCommand} command
     * @return the created {@link Concert} entity
     */
    Concert handle(CreateConcertCommand command);

    /**
     * Handle update concert command
     * @param command the {@link UpdateConcertCommand} command
     * @return the updated {@link Concert} entity or null if not found
     */
    Concert handle(UpdateConcertCommand command);

    /**
     * Handle delete concert command
     * @param command the {@link DeleteConcertCommand} command
     * @return true if deleted successfully, false otherwise
     */
    boolean handle(DeleteConcertCommand command);

    /**
     * Handle add attendee command
     * @param command the {@link AddAttendeeCommand} command
     * @return the updated {@link Concert} entity
     */
    Concert handle(AddAttendeeCommand command);

    /**
     * Handle remove attendee command
     * @param command the {@link RemoveAttendeeCommand} command
     * @return the updated {@link Concert} entity
     */
    Concert handle(RemoveAttendeeCommand command);
}
