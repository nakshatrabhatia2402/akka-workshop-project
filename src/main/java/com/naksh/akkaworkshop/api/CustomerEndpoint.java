package com.naksh.akkaworkshop.api;

import java.util.List;

import com.naksh.akkaworkshop.domain.entities.CustomerEntity;
import com.naksh.akkaworkshop.views.CustomerView;
import com.naksh.akkaworkshop.views.CustomerView.customerEntries;
import com.naksh.akkaworkshop.domain.commands.CustomerCommands.*;

import akka.javasdk.annotations.Acl;
import akka.javasdk.annotations.http.Delete;
import akka.javasdk.annotations.http.Get;
import akka.javasdk.annotations.http.HttpEndpoint;
import akka.javasdk.annotations.http.Post;
import akka.javasdk.client.ComponentClient;

@HttpEndpoint("/api")
@Acl(allow = @Acl.Matcher(principal = Acl.Principal.INTERNET))
public class CustomerEndpoint {

    private final ComponentClient client;

    public record CreateCustomerResponse(String customerId, String FirstName, String LastName) {
    }

    public record CreateCustomerRequest(String customerId, String FirstName, String LastName) {
    }

    public record GetAllCustomersResponse(List<CreateCustomerResponse> response) {
    }

    public CustomerEndpoint(ComponentClient client) {
        this.client = client;
    }

    @Post("/customers")
    public CreateCustomerResponse createCustomer(CreateCustomerRequest request) {
        var customerId = request.customerId;
        var cmd = new CreateCustomer(request.FirstName, request.LastName);
        client.forEventSourcedEntity(customerId)
                .method(CustomerEntity::create)
                .invoke(cmd);

        return new CreateCustomerResponse(customerId, request.FirstName, request.LastName);
    }

    @Delete("/customers/{customerId}")
    public String deleteCustomer(String customerId) {
        var cmd = new DeleteCustomer();
        client.forEventSourcedEntity(customerId)
                .method(CustomerEntity::delete)
                .invoke(cmd);

        return "Ok";
    }

    @Get("/customers")
    public customerEntries getCustomers() {

        return client.forView()
                .method(CustomerView::getAll)
                .invoke();
    }

}