package com.staybits.gigmapapi.communities.application.internal.commandservices;

import org.springframework.stereotype.Service;
import com.staybits.gigmapapi.communities.domain.model.commands.ReportContentCommand;
import com.staybits.gigmapapi.communities.domain.model.entities.Report;
import com.staybits.gigmapapi.communities.domain.services.ReportCommandService;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.ReportRepository;

@Service
public class ReportCommandServiceImpl implements ReportCommandService {
    private final ReportRepository reportRepository;

    public ReportCommandServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public Report handle(ReportContentCommand command) {
        var report = new Report(command.reason(), command.threadId(), command.commentId(), command.reporterId());
        try {
            return reportRepository.save(report);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create report: " + e.getMessage(), e);
        }
    }
}
