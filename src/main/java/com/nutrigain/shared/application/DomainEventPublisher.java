package com.nutrigain.shared.application;

import com.nutrigain.shared.domain.model.AggregateRoot;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/** Publishes the events collected by an aggregate (in-process, synchronous, same transaction). */
@Component
public class DomainEventPublisher {

    private final ApplicationEventPublisher publisher;

    public DomainEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publishFrom(AggregateRoot aggregate) {
        aggregate.pullDomainEvents().forEach(publisher::publishEvent);
    }
}
