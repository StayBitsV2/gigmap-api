package com.staybits.gigmapapi.communities.interfaces.rest.transform;

import com.staybits.gigmapapi.communities.domain.model.commands.ReportContentCommand;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.CreateReportResource;

public class CreateReportCommandFromResourceAssembler {
    public static ReportContentCommand toCommandFromResource(CreateReportResource resource) {
        return new ReportContentCommand(resource.threadId(), resource.commentId(), resource.reporterId(), resource.reason());
    }
}
