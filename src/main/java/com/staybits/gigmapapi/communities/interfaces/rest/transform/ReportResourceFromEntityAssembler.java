package com.staybits.gigmapapi.communities.interfaces.rest.transform;

import com.staybits.gigmapapi.communities.domain.model.entities.Report;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.ReportResource;

public class ReportResourceFromEntityAssembler {
    public static ReportResource toResourceFromEntity(Report report) {
        return new ReportResource(
            report.getId(),
            report.getReason(),
            report.getThreadId(),
            report.getCommentId(),
            report.getReporterId(),
            report.getStatus().name(),
            report.getCreatedAt() != null ? report.getCreatedAt().toString() : null
        );
    }
}
