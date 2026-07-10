package com.staybits.gigmapapi.communities.domain.events;

public class ReportCreatedEvent {
    private final Long threadId;
    private final Long commentId;
    private final Long reporterId;
    private final String reason;

    public ReportCreatedEvent(Long threadId, Long commentId, Long reporterId, String reason) {
        this.threadId = threadId;
        this.commentId = commentId;
        this.reporterId = reporterId;
        this.reason = reason;
    }

    public Long getThreadId() { return threadId; }
    public Long getCommentId() { return commentId; }
    public Long getReporterId() { return reporterId; }
    public String getReason() { return reason; }
}
