package dk.hitman.hitman2012.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.vectomatic.dnd.DataTransferExt;
import org.vectomatic.dnd.DropPanel;
import org.vectomatic.file.File;
import org.vectomatic.file.FileList;
import org.vectomatic.file.FileReader;
import org.vectomatic.file.FileUtils;
import org.vectomatic.file.events.LoadEndEvent;
import org.vectomatic.file.events.LoadEndHandler;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;

import dk.hitman.hitman2012.shared.Pair;
import com.google.gwt.event.dom.client.DragEnterHandler;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragLeaveEvent;

public class Spil implements Content {

	private Panel mContents;
	private State mState;

	public Spil(State s) {
		mState = s;
	}

	@Override
	public Panel getContents() {
		if (mContents == null)
			mContents = createContents();
		return mContents;
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	private Panel createContents() {
		final VerticalPanel vp = new VerticalPanel();
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(10);
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		vp.add(horizontalPanel);
		
		final ListDataProvider<Pair<Integer,Integer>> dataProv = new ListDataProvider<Pair<Integer,Integer>>();
		
		Button btnNewButton = new Button("Bland Alle");
		btnNewButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				List<Integer> circle = new ArrayList<Integer>();
				for (int i = 0; i < mState.regs.size(); i++)
					circle.add(i);
				shuffle(circle);
				mState.targets.clear();
				for (int i = 0; i < mState.regs.size()-1; i++)
					mState.targets.put(circle.get(i), circle.get(i+1));
				mState.targets.put(circle.get(mState.regs.size()-1), circle.get(0));
				List<Pair<Integer,Integer>> data = new ArrayList<Pair<Integer,Integer>>();
				for (int i = 0; i < mState.regs.size(); i++)
					data.add(Pair.create(i, mState.targets.get(i)));
				dataProv.setList(data);
			}
		});
		
		
		
		
		horizontalPanel.add(btnNewButton);
		final DropPanel dp = new DropPanel();
		Tools.initDropPanel(dp);
		dp.addDropHandler(new DropHandler() {
			public void onDrop(DropEvent event) {
				FileList files = event.getDataTransfer().<DataTransferExt>cast().getFiles();
				
				final File file = files.getItem(0);
				
				final FileReader reader = new FileReader();
				reader.addLoadEndHandler(new LoadEndHandler() {
					@Override
					public void onLoadEnd(LoadEndEvent event) {
						if (reader.getError() == null) {
							Image image = createBitmapImage(reader, file);
							vp.add(image);
						}
					}
				});
				
				if (file.getType().startsWith("image/")) {
					GWT.log(file.toSource()+" "+file.toString()+" "+file.createObjectURL()+" "+file.getName()+" ");
					
					reader.readAsBinaryString(file);
				}
				
				createWindow(createPrintHtml());
				
				event.stopPropagation();
				event.preventDefault();
			}
		});
		dp.addStyleName("gwt-DropPanel");
		horizontalPanel.add(dp);
		dp.setHeight("76px");
		
		Label lblTabBillederFor = new Label("Tab billeder for at printe");
		dp.setWidget(lblTabBillederFor);
		lblTabBillederFor.setSize("100%", "100%");
		
		CellList<Pair<Integer,Integer>> cellList = new CellList<Pair<Integer,Integer>>(new AbstractCell<Pair<Integer,Integer>>(){
			@Override
			public void render(Context context, Pair<Integer,Integer> value, SafeHtmlBuilder sb) {
				String killer = mState.regs.get(value.left).navn;
				String target = mState.regs.get(value.right).navn;
				sb.appendHtmlConstant("<span class='kill_pair'><span class='killer'>")
					.appendEscaped(killer).appendHtmlConstant("</span><span class='target'>")
					.appendEscaped(target).appendHtmlConstant("</span></span>");
			}
		});
		vp.add(cellList);
		
		dataProv.addDataDisplay(cellList);
		
		return vp;
	}
	
	protected String createPrintHtml() {
		return "<HTML><HEAD><TITLE>Print Mig</TITLE></HEAD><BODY>Hello, world!</BODY></HTML>";
	}
	
	
	private Image createBitmapImage(FileReader reader, final File file) {
		String result = reader.getStringResult();
		String url = FileUtils.createDataUrl(file.getType(), result);
		final Image image = new Image();
		image.setVisible(false);
		image.addLoadHandler(new LoadHandler() {
			@Override
			public void onLoad(LoadEvent event) {
				int width = image.getWidth();
				int height = image.getHeight();
				GWT.log("size=" + width + "x" + height);
				float f = 150.0f / Math.max(width, height);
				image.setPixelSize((int)(f * width), (int)(f * height));
				image.setVisible(true);
			}			
		});
		image.setUrl(url);
		return image;			
	}
	
	public static native void createWindow(String html) /*-{   
    	var win = window.open("", "win", "width=1000,height=800"); // a window object
		win.document.open("text/html", "replace");
		win.document.write(html);
		win.document.close();
		win.print();  
    }-*/;

	
	protected void shuffle(List<Integer> circle) {
		Random rnd = new Random();
		for (int i=circle.size(); i>1; i--)
            Collections.swap(circle, i-1, rnd.nextInt(i));
	}
}