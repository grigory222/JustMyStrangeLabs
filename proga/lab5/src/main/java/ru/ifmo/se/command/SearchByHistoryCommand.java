//package ru.ifmo.se.command;
//
//import ru.ifmo.se.receiver.IoReceiver;
//
//import java.io.BufferedReader;
//import java.io.PrintWriter;
//
//public class SearchByHistoryCommand extends AbstractCommand implements Command{
//    private final HistoryCommand historyCommand;
//    public SearchByHistoryCommand(HistoryCommand historyCommand, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name, HistoryCommand historyCommand1){
//        super(name, reader, printer, infoPrinter);
//        this.historyCommand = historyCommand1;
//    }
//
//    public void execute(String[] args){
//
//    }
//}