package ru.ifmo.se.command;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Stack;

public class HistoryCommand extends AbstractCommand implements Command{
    private final Stack<ConcreteCommand> history = new Stack<>();

    public HistoryCommand(String name, BufferedReader reader, PrintWriter printWriter, PrintWriter infoPrinter) {
        super(name, reader, printWriter, infoPrinter);
    }

    public void add(AbstractCommand cmd, String[] args){
        ConcreteCommand concreteCommand = new ConcreteCommand(cmd, args);
        history.push(concreteCommand);
    }
    @Override
    public void execute(String[] args) {
        if (history.size() > 14){
            for (AbstractCommand cmd : history.subList(history.size() - 14, history.size())){
                printer.println(cmd.getName());
            }
        } else{
            for (AbstractCommand cmd : history){
                printer.println(cmd.getName());
            }
        }
    }

    public ConcreteCommand getPrevious(ConcreteCommand currentCommand) {
        if (currentCommand == null){
            return history.isEmpty() ? null : history.peek();
        }

        int index = history.indexOf(currentCommand);
        if (index > 0){
            return history.elementAt(index - 1);
        }
        return currentCommand;
    }

    public ConcreteCommand getNext(ConcreteCommand currentCommand) {
        if (currentCommand == null){
            return history.isEmpty() ? null : history.elementAt(0);
        }

        int index = history.indexOf(currentCommand);
        if (index < history.size() - 1){
            return history.elementAt(index + 1);
        }
        return currentCommand;
    }

    public ConcreteCommand getMatch(String input) {
        if (input.isEmpty()) {
            return null;
        }

        List<ConcreteCommand> matches = history.stream().filter(s -> s.getName().contains(input)).toList();
        if (!matches.isEmpty()) {
            return matches.get(0);
        }

        return null;
    }
}
