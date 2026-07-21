package com.naksh.akkaworkshop.api;

import com.naksh.akkaworkshop.domain.commands.PreferencesCommand.SetPreferences;
import com.naksh.akkaworkshop.domain.model.CustomerPreferences;
import com.naksh.akkaworkshop.domain.entities.CustomerPreferencesEntity;

import akka.javasdk.annotations.Acl;
import akka.javasdk.annotations.http.Get;
import akka.javasdk.annotations.http.HttpEndpoint;
import akka.javasdk.annotations.http.Put;
import akka.javasdk.client.ComponentClient;

@HttpEndpoint("/api")
@Acl(allow = @Acl.Matcher(principal = Acl.Principal.INTERNET))
public class PreferencesEndpoint {

    private final ComponentClient client;

    public PreferencesEndpoint(ComponentClient client) {
        this.client = client;
    }

    @Put("/customers/{customerId}/preferences")
    public String setPreferences(String customerId, SetPreferences cmd) {
        return client.forKeyValueEntity(customerId)
                .method(CustomerPreferencesEntity::set)
                .invoke(cmd);
    }

    @Get("/customers/{customerId}/preferences")
    public CustomerPreferences getPreferences(String customerId) {
        return client.forKeyValueEntity(customerId)
                .method(CustomerPreferencesEntity::get)
                .invoke();
    }
}
