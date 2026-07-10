package com.staybits.gigmapapi.connections.domain.model.aggregates;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.connections.domain.model.valueobjects.ConnectionRequestStatus;
import com.staybits.gigmapapi.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "connection_requests")
public class ConnectionRequest extends AuditableAbstractAggregateRoot<ConnectionRequest> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    private User target;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConnectionRequestStatus status = ConnectionRequestStatus.PENDING;

    public ConnectionRequest() {
        super();
    }

    public ConnectionRequest(User requester, User target) {
        this();
        this.requester = requester;
        this.target = target;
        this.status = ConnectionRequestStatus.PENDING;
    }

    public void accept() {
        if (status != ConnectionRequestStatus.PENDING) {
            throw new IllegalStateException("Cannot accept a request that is not PENDING");
        }
        this.status = ConnectionRequestStatus.ACCEPTED;
    }

    public void reject() {
        if (status != ConnectionRequestStatus.PENDING) {
            throw new IllegalStateException("Cannot reject a request that is not PENDING");
        }
        this.status = ConnectionRequestStatus.REJECTED;
    }

    public boolean isPending() {
        return status == ConnectionRequestStatus.PENDING;
    }
}
