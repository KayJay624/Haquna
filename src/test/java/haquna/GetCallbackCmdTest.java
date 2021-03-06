package haquna;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import haquna.utils.HaqunaUtils;

public class GetCallbackCmdTest {
	public static void setup() {
		HaqunaUtils.clearMemory();
		TestUtils.createAndExecCmd("Model = new Model('threat-monitor.hmr')");
		TestUtils.createAndExecCmd("Attr = Model.getAttributeByName('day')");
	}
		
	@Test
	public void testGetTypeCmd() {
		setup();
				
		String cmd = "Callback = Attr.getCallback()";
		TestUtils.createAndExecCmd(cmd);
						
		assertEquals(Haquna.callbackMap.containsKey("Callback"), true);
		assertEquals(Haquna.callbackMap.get("Callback"), null);
				
	}		
}
