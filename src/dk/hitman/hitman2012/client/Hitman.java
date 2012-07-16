package dk.hitman.hitman2012.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Hitman implements EntryPoint {
	

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		GWT.log("onHitmanLoaded");
		TabLayoutPanel tlp = new TabLayoutPanel(2.5, Unit.EM);
		RootPanel rootPanel = RootPanel.get("content_box");
	rootPanel.setHeight("100%");
			rootPanel.add(tlp);
		tlp.setSize("100%", "100%");Label label = new Label("Page 1");
		tlp.add(new Registrering().getContents(), "Registrering");
		tlp.add(label, "Forside");
		
		tlp.add(new Label("Page 2"), "Spil");
	}
}
