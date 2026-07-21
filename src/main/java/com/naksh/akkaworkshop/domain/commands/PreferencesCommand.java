package com.naksh.akkaworkshop.domain.commands;

public class PreferencesCommand {

    public record SetPreferences(
            String theme,
            String locale,
            boolean marketingOptIn) {
    }
}
