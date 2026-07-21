package com.naksh.akkaworkshop.domain.commands;

public class CustomerCommands {

    public record CreateCustomer(
            String firstName,
            String lastName) {
    }

    public record DeleteCustomer() {
    }
}
