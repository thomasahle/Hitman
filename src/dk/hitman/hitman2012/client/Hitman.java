package dk.hitman.hitman2012.client;

import java.util.ArrayList;
import java.util.List;

import org.vectomatic.dnd.DataTransferExt;
import org.vectomatic.dnd.DropPanel;
import org.vectomatic.file.File;
import org.vectomatic.file.FileList;
import org.vectomatic.file.FileReader;
import org.vectomatic.file.events.LoadEndEvent;
import org.vectomatic.file.events.LoadEndHandler;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.widget.client.TextButton;

import dk.hitman.hitman2012.shared.Pair;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Hitman implements EntryPoint {

	final State state = new State();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		GWT.log("onHitmanLoaded");
		RootLayoutPanel rootPanel = RootLayoutPanel.get();

		rootPanel.setHeight("100%");

		TextButton textButton = new TextButton("Download Data");
		rootPanel.add(textButton);
		rootPanel.setWidgetRightWidth(textButton, 112.0, Unit.PX, 116.0,
				Unit.PX);
		rootPanel.setWidgetTopHeight(textButton, 11.0, Unit.PX, 29.0, Unit.PX);

		Label lblHitmanSystemet = new Label("Hitman Systemet");
		lblHitmanSystemet.addStyleName("heading");
		rootPanel.add(lblHitmanSystemet);
		rootPanel.setWidgetLeftRight(lblHitmanSystemet, 103.0, Unit.PX, 234.0,
				Unit.PX);
		rootPanel.setWidgetTopHeight(lblHitmanSystemet, 0.0, Unit.PX, 100.0,
				Unit.PX);
		lblHitmanSystemet.setWidth("100%");

		final DropPanel dropPanel = new DropPanel();
		dropPanel.addStyleName("gwt-DropPanel");
		rootPanel.add(dropPanel);
		rootPanel.setWidgetLeftWidth(dropPanel, 0.0, Unit.PX, 97.0, Unit.PX);
		rootPanel.setWidgetTopHeight(dropPanel, 0.0, Unit.PX, 100.0, Unit.PX);
		dropPanel.addDropHandler(new DropHandler() {
			public void onDrop(DropEvent event) {
				FileList files = event.getDataTransfer().<DataTransferExt>cast().getFiles();
				
				final FileReader reader = new FileReader();
				reader.addLoadEndHandler(new LoadEndHandler() {
					@Override
					public void onLoadEnd(LoadEndEvent event) {
						state.getRegs().addAll(fromCSV(reader.getStringResult()));
					}
				});
				GWT.log("Got " + files.getLength() + " files.");
				GWT.log("First: " + files.getItem(0).getType());
				for (File file : files) {
					if (file.getType().equals("")
							|| file.getType().equals("text/plain")
							|| file.getType().equals("text/csv")
							|| file.getType().equals("application/csv")) {
						state.getRegs().clear();
						reader.readAsText(file);
						break;
					}
				}
				event.stopPropagation();
				event.preventDefault();
			}
		});
		dropPanel.setHeight("100%");
		Tools.initDropPanel(dropPanel);
		
		Label label_1 = new Label("Drop data here");
		//dropPanel.setWidget(label_1);
		Document document = Document.get();
		dropPanel.getElement().appendChild(document.createDivElement()).appendChild(document.createTextNode("Drop data here"));		
		label_1.setSize("100%", "100%");
		TabLayoutPanel tlp = new TabLayoutPanel(2.5, Unit.EM);
		rootPanel.add(tlp);
		rootPanel.setWidgetLeftRight(tlp, 0.0, Unit.PX, 0.0, Unit.PX);
		rootPanel.setWidgetTopBottom(tlp, 106.0, Unit.PX, 0.0, Unit.PX);
		tlp.setSize("", "100%");
		tlp.add(new Registrering(state).getContents(), "Registrering");

		tlp.add(new Spil(state).getContents(), "Spil");
		textButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				downloadData();
			}
		});
	}

	protected void downloadData() {
		String data = URL.encode(toCSV(state.getRegs()));
		String download_type;
		if (Window.Navigator.getUserAgent().contains("Safari"))
			download_type = "text/plain";
		else download_type = "application/csv";
		String uri = "data:" + download_type + ";charset=utf-8," + data + ",";
		Window.open(uri, "_blank", "");
	}

	protected String toCSV(List<Reg> data) {
		StringBuilder sb = new StringBuilder();
		for (Reg reg : data) {
			sb.append(reg.id).append(",");
			sb.append(reg.navn).append(",");
			sb.append(reg.gruppe).append(",");
			sb.append(reg.kvarter).append(",");
			sb.append(reg.bydel).append(",");
			sb.append("\n");
		}
		return sb.toString();
	}

	protected List<Reg> fromCSV(String data) {
		List<Reg> list = new ArrayList<Reg>();
		for (String line : data.split("\n")) {
			String[] parts = line.split(",");
			int l = parts.length;
			if (parts.length >= 5 && parts[0].matches("\\d+")) {
				String ulejr = parts[--l].trim();
				String kvarter = parts[--l].trim();
				String gruppe = parts[--l].trim();
				String navn = parts[--l].trim();
				while (l > 1)
					navn = parts[--l].trim() +", "+ navn;
				
				list.add(new Reg(list.size(),
						navn, gruppe, kvarter, ulejr));
				state.gruppeCtrl.put(gruppe, Pair.create(kvarter, ulejr));
				state.kvarterCtrl.put(kvarter, ulejr);
			}
			else {
				GWT.log("ERROR!!!" +" "+line);
			}
		}
		return list;
	}
}
