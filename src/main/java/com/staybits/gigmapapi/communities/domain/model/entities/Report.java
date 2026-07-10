package com.staybits.gigmapapi.communities.domain.model.entities;

import com.staybits.gigmapapi.communities.domain.model.valueobjects.ReportStatus;
import com.staybits.gigmapapi.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "reports")
public class Report extends AuditableModel {
    @Column(columnDefinition = "TEXT", nullable = false)
    private String reason;

    @Column(name = "thread_id")
    private Long threadId;

    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "reporter_id", nullable = false)
    private Long reporterId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status = ReportStatus.PENDING;

    public Report() {}

    public Report(String reason, Long threadId, Long commentId, Long reporterId) {
        this.reason = reason;
        this.threadId = threadId;
        this.commentId = commentId;
        this.reporterId = reporterId;
        this.status = ReportStatus.PENDING;
    }
}
