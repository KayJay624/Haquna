package haquna;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import haquna.command.CommandFactory;
import haquna.command.get.GetTypeByIdCmd;
import haquna.utils.HaqunaUtils;

public class GetTypeByIdCmdTest {
	
public static CommandFactory cp = new CommandFactory();
	
	public static void setup() {
		HaqunaUtils.clearMemory();
		cp.createCommand("Model = new Model('threat-monitor.hmr')");
	}
		
	@Test
	public void testGetTypeByIdCmdNoModel() {
		setup();
		
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		
		String cmd = "Tab = NoExistingModel.getTypeById('Today')";
		GetTypeByIdCmd sal = (GetTypeByIdCmd) cp.createCommand(cmd);
		String expectedOutput = getErrorStringFormat("No '" + sal.getModelName() + "' XTTModel object in memory");
				
		assertEquals(Haquna.tableMap.containsKey("Tab"), false);
		assertEquals(outContent.toString(), expectedOutput);				
	}
	
	@Test
	public void testGetTypeByIdCmdNoName() {
		setup();
		
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		
		
		String cmd = "Tab = Model.getTypeById('NotExisting')";
		GetTypeByIdCmd sal = (GetTypeByIdCmd) cp.createCommand(cmd);
		String expectedOutput = getErrorStringFormat("No type with '" + sal.getTypeId() + "' id in '" + sal.getModelName() + "' model");
		
		assertEquals(Haquna.tableMap.containsKey("Tab"), false);
		assertEquals(outContent.toString(), expectedOutput);				
	}
	
	@Test
	public void testGetTypeByIdCmdNoVar() {
		setup();
		
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		
		cp.createCommand("Tab = Model.getTableByName('Today')");
		
		String cmd = "Tab = Model.getTypeById('integer')";
		GetTypeByIdCmd sal = (GetTypeByIdCmd) cp.createCommand(cmd);
		String expectedOutput = getErrorStringFormat("Variable name '" + sal.getVarName() + "' already in use");
		
		assertEquals(Haquna.tableMap.containsKey("Tab"), true);
		assertEquals(outContent.toString(), expectedOutput);				
	}
	
	private String getErrorStringFormat(String str) {
		return "\u001B[31m======>" + str + "\"\u001B[0m\n";
	}
}
