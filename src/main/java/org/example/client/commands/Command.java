package org.example.client.commands;

import java.util.Scanner;

public abstract class Command {
    protected final String name;
    protected final Scanner scanner;

    public Command(String name, Scanner scanner){
        this.name = name;
        this.scanner = scanner;
    }

    public void execute(){}
}
