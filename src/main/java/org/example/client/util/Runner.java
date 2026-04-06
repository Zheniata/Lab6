package org.example.client.util;

import org.example.client.commands.Add;
import org.example.client.commands.Command;
import org.example.client.commands.Exit;
import org.example.client.commands.Show;
import org.example.client.network.ClientNetworkManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Runner {
    private final Scanner scanner;
    private final ClientNetworkManager networkManager;
    private final Map<String, Command> commands = new HashMap<>();

    public Runner(Scanner scanner, ClientNetworkManager clientNetworkManager) {
        this.scanner = scanner;
        this.networkManager = clientNetworkManager;

        commands.put("add", new Add(scanner, networkManager));
        commands.put("show", new Show(networkManager));
        commands.put("exit", new Exit());
    }

        public void interactiveMode() {
            System.out.println("Введите команду");
            while (true) {
                try {
                    System.out.println("> ");
                    String line = scanner.nextLine().trim();
                    if (line.isEmpty()) {
                        continue;
                    }

                    String[] parts = line.split(" ", 2);
                    String commandName = parts[0];
                    String argument = parts.length > 1 ? parts[1] : null;
                    Command command = commands.get(commandName);
                    if (command == null) {
                        System.out.println("Неизвестная команда: " + commandName);
                        continue;
                    }
                    command.execute(argument);
                } catch (Exception e){
                    System.err.println("Ошибка: " + e.getMessage());
                }
            }
        }
    }