package ru.ifmo.se.command;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Stack;

public class HistoryCommand extends AbstractCommand implements Command{
    private final Stack<AbstractCommand> history = new Stack<>();

    public HistoryCommand(String name, BufferedReader reader, PrintWriter printWriter, PrintWriter infoPrinter) {
        super(name, reader, printWriter, infoPrinter);
    }

    public void add(AbstractCommand cmd){
        history.push(cmd);
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
}
