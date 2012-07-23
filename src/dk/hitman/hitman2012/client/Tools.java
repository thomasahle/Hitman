package dk.hitman.hitman2012.client;

import org.vectomatic.dnd.DropPanel;

import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragEnterHandler;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.user.client.ui.UIObject;

public class Tools {
	public static void initDropPanel(final DropPanel dropPanel) {
		dropPanel.addDragOverHandler(new DragOverHandler() {
			public void onDragOver(DragOverEvent event) {
				event.stopPropagation();
				event.preventDefault();
			}
		});
		dropPanel.addDragLeaveHandler(new DragLeaveHandler() {
			public void onDragLeave(DragLeaveEvent event) {
				setBorderColor(dropPanel, "black");
				dropPanel.removeStyleName("drag_hovered");
				event.stopPropagation();
				event.preventDefault();
			}
		});
		dropPanel.addDragEnterHandler(new DragEnterHandler() {
			public void onDragEnter(DragEnterEvent event) {
				setBorderColor(dropPanel, "red");
				dropPanel.addStyleName("drag_hovered");
				event.stopPropagation();
				event.preventDefault();
			}
		});
	}
	private static void setBorderColor(UIObject panel, String color) {
		panel.getElement().getStyle().setBorderColor(color);
	}
}
