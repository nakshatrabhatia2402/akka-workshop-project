package com.naksh.akkaworkshop.domain.entities;

import com.naksh.akkaworkshop.domain.commands.PreferencesCommand.SetPreferences;
import com.naksh.akkaworkshop.domain.model.CustomerPreferences;

import akka.javasdk.annotations.ComponentId;
import akka.javasdk.keyvalueentity.KeyValueEntity;

@ComponentId("customer-preferences")
public class CustomerPreferencesEntity extends KeyValueEntity<CustomerPreferences> {

    @Override
    public CustomerPreferences emptyState() {
        return CustomerPreferences.defaults();
    }

    public Effect<String> set(SetPreferences cmd) {
        var next = new CustomerPreferences(
                cmd.theme(),
                cmd.locale(),
                cmd.marketingOptIn());
        return effects().updateState(next).thenReply("Ok");
    }

    public Effect<CustomerPreferences> get() {
        return effects().reply(currentState());
    }
}
