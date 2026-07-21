package com.naksh.akkaworkshop.domain.entities;

import com.naksh.akkaworkshop.domain.commands.CustomerCommands.CreateCustomer;
import com.naksh.akkaworkshop.domain.commands.CustomerCommands.DeleteCustomer;
import com.naksh.akkaworkshop.domain.events.CustomerEvents;
import com.naksh.akkaworkshop.domain.events.CustomerEvents.customerCreated;
import com.naksh.akkaworkshop.domain.events.CustomerEvents.customerDeleted;
import com.naksh.akkaworkshop.domain.model.CustomerState;
import akka.javasdk.annotations.ComponentId;
import akka.javasdk.eventsourcedentity.EventSourcedEntity;

@ComponentId("Customer")
public class CustomerEntity extends EventSourcedEntity<CustomerState, CustomerEvents> {

    @Override
    public CustomerState emptyState() {
        return CustomerState.empty();
    }

    public Effect<String> create(CreateCustomer cmd) {

        if (currentState().customerId() != null) {
            return effects().error("Customer already exists");
        }

        String customerId = commandContext().entityId();
        var event = new customerCreated(customerId, cmd.firstName(), cmd.lastName());

        return effects()
                .persist(event)
                .thenReply(__ -> customerId);

    }

    public Effect<String> delete(DeleteCustomer cmd) {
        var id = currentState().customerId();
        var firstName = currentState().FirstName();
        var lastName = currentState().LastName();
        var event = new customerDeleted(id, firstName, lastName);

        return effects().persist(event).thenReply(__ -> id);
    }

    @Override
    public CustomerState applyEvent(CustomerEvents event) {

        return switch (event) {
            case customerCreated e -> new CustomerState(e.customerId(), e.FirstName(), e.LastName(), false);
            case customerDeleted e -> new CustomerState(e.customerId(), e.FirstName(), e.LastName(), true);
        };
    }
}
