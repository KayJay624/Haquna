package haquna;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import haquna.command.CommandFactory;
import haquna.completer.FunctionNameCompleter;
import haquna.completer.HaqunaDelimiter;
import haquna.completer.HaqunaCompleter;
import haquna.completer.ParameterCompleter;
import haquna.completer.RunCompleter;
import haquna.completer.VarNamesCompleter;
import heart.WorkingMemory;
import heart.xtt.Attribute;
import heart.xtt.Rule;
import heart.xtt.Table;
import heart.xtt.Type;
import heart.xtt.XTTModel;
import jline.console.ConsoleReader;
import jline.console.completer.AggregateCompleter;
import jline.console.completer.CandidateListCompletionHandler;
import jline.console.completer.Completer;
import jline.console.completer.FileNameCompleter;
import jline.console.completer.StringsCompleter;

public class Haquna {
	public static final String varName = "[A-Z]([A-Z|a-z|0-9|_])*";
	public static final String attrNamePattern = "[a-z|_]+";
	public static final String attrValuePattern = "[a-z|A-Z|0-9|_|.]+[/]?[0-9]*";
	
	public static boolean wasSucces = false;
	
	public static Map<String, XTTModel> modelMap = new HashMap<String, XTTModel>();
	public static Map<String, Table> tableMap = new HashMap<String, Table>();
	public static Map<String, Attribute> attribiuteMap = new HashMap<String, Attribute>();
	public static Map<String, Type> typeMap = new HashMap<String, Type>();
	public static Map<String, Rule> ruleMap = new HashMap<String, Rule>();
	public static Map<String, String> callbackMap = new HashMap<String, String>();
	public static Map<String, WorkingMemory> wmMap = new HashMap<String, WorkingMemory>();
	
	public static List<Completer> completers = new LinkedList<Completer>();
	
	public static void main(String[] args) throws IOException {
        try {
            ConsoleReader reader = new ConsoleReader();
            reader.setPrompt("\u001B[35;1m" + "HaQuNa> " + "\u001B[0m");
                    
            String line;
            PrintWriter out = new PrintWriter(reader.getOutput());
            	
            CommandFactory cmdFactory = new CommandFactory();
            
            setupCompleter(reader);
            
            while ((line = reader.readLine()) != null) {
            	           	                            
                cmdFactory.createCommand(line);
                
                if(wasSucces) {
                	paintItGreeen(line);
                
                } else {
                	paintItRed(line);
                }
                wasSucces = false;
                out.flush();
                
                if(line.equalsIgnoreCase("pwd")) {
            		out.println("Working directory: " + System.getProperty("user.dir"));
            	}          	
            	if(line.equals("ls")) {
            		Path dir = Paths.get(System.getProperty("user.dir"));
            		try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*")) {
            			for(Path file : stream) {
            				out.println(file.getFileName());
            			}
            		}
            	}
                if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {
                    break;
                }
                if (line.equalsIgnoreCase("cls")) {
                    reader.clearScreen();
                }
                            
                //myCompleter(reader);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
	
	public static boolean isVarUsed(String varName) {
		if(modelMap.containsKey(varName) ||
		   tableMap.containsKey(varName) ||
		   attribiuteMap.containsKey(varName) || 
		   typeMap.containsKey(varName) ||
		   ruleMap.containsKey(varName) ||
		   callbackMap.containsKey(varName) ||
		   wmMap.containsKey(varName)) {
			
			return true;
		
		} else {
			return false;
		}		
	}
	
	public static void setupCompleter(ConsoleReader reader) {
		for(Completer c : reader.getCompleters()){
			reader.removeCompleter(c);
		}
		
		completers = new LinkedList<Completer>();
        completers.add(new VarNamesCompleter());
        completers.add(new FunctionNameCompleter());
		HaqunaCompleter argComp1 = new HaqunaCompleter(new HaqunaDelimiter('.'), completers);
        
		completers = new LinkedList<Completer>();
        completers.add(new VarNamesCompleter());
        completers.add(new VarNamesCompleter());
        completers.add(new FunctionNameCompleter());
        HaqunaCompleter argComp2 = new HaqunaCompleter(new HaqunaDelimiter('='), completers);
        
        completers = new LinkedList<Completer>();
        completers.add(new VarNamesCompleter());
        completers.add(new VarNamesCompleter());
        completers.add(new FunctionNameCompleter());
        completers.add(new ParameterCompleter());
        HaqunaCompleter argComp3 = new HaqunaCompleter(new HaqunaDelimiter('=', '.','\'','('), completers);
        
        completers = new LinkedList<Completer>();
        completers.add(new VarNamesCompleter());
        completers.add(new StringsCompleter("xload"));
        completers.add(new FileNameCompleter());
        HaqunaCompleter argComp4 = new HaqunaCompleter(new HaqunaDelimiter('=','(','\''), completers);
        
        completers = new LinkedList<Completer>();
        completers.add(new VarNamesCompleter());
        completers.add(new StringsCompleter("new"));
        completers.add(new FunctionNameCompleter());
        completers.add(new ParameterCompleter());       
        HaqunaCompleter argComp5 = new HaqunaCompleter(new HaqunaDelimiter('=', '('), completers);
        
        completers = new LinkedList<Completer>();
        completers.add(new VarNamesCompleter());
        completers.add(new FunctionNameCompleter());
        completers.add(new ParameterCompleter());         
		HaqunaCompleter argComp6 = new HaqunaCompleter(new HaqunaDelimiter('.', '(','\''), completers);
        
		  
        completers = new LinkedList<Completer>();
        completers.add(new VarNamesCompleter());
        completers.add(new StringsCompleter("run"));
        completers.add(new RunCompleter());
        completers.add(new RunCompleter());
        completers.add(new RunCompleter());
        completers.add(new RunCompleter());
        completers.add(new RunCompleter());
        completers.add(new RunCompleter());
        completers.add(new RunCompleter());
        HaqunaCompleter argComp7 = new HaqunaCompleter(new HaqunaDelimiter(',','=','[',']','.', '(','\''), completers);
       
		
        argComp1.setStrict(true);
        argComp2.setStrict(true);
        argComp3.setStrict(true);
        argComp4.setStrict(true);
        argComp5.setStrict(true);
        argComp6.setStrict(true);
        argComp7.setStrict(true);
        
        CandidateListCompletionHandler handler = new CandidateListCompletionHandler();        
        handler.setPrintSpaceAfterFullCompletion(false);
        
        AggregateCompleter aggComp = new AggregateCompleter(argComp1, argComp2, argComp3, argComp4, argComp5, argComp6, argComp7);
        reader.setCompletionHandler(handler);
        reader.addCompleter(aggComp);
	}
	
	public static void paintItGreeen(String line) {
		System.out.println("\u001B[32m======>" + line + "\"\u001B[0m");
	}
	
	public static void paintItRed(String line) {
		System.out.println("\u001B[31m======>" + line + "\"\u001B[0m");
	}
}
	

