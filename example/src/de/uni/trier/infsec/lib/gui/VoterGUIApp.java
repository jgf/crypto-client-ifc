package de.uni.trier.infsec.lib.gui;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;

public class VoterGUIApp implements Application {
	private VotingWindow window = null;

	@Override
	public void resume() throws Exception {
	}

	@Override
	public boolean shutdown(boolean arg0) throws Exception {
		if (window != null) {
			window.close();
		}
		return false;
	}

	@Override
	public void startup(Display arg0, Map<String, String> arg1) throws Exception {
		BXMLSerializer xmlser = new BXMLSerializer();
		window = (VotingWindow) xmlser.readObject(VotingWindow.class, "VoterGUI.xml");
		window.open(arg0);
	}

	@Override
	public void suspend() throws Exception {
	}

	public static void main(String[] args) {
		DesktopApplicationContext.main(VoterGUIApp.class, args);
	}

	

}
