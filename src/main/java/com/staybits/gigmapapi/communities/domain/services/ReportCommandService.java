package com.staybits.gigmapapi.communities.domain.services;

import com.staybits.gigmapapi.communities.domain.model.entities.Report;
import com.staybits.gigmapapi.communities.domain.model.commands.ReportContentCommand;

public interface ReportCommandService {
    Report handle(ReportContentCommand command);
}
