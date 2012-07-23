package dk.hitman.hitman2012.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
				for (int i = 0; i < mState.getRegs().size(); i++)
					circle.add(i);
				shuffle(circle);
				mState.targets.clear();
				for (int i = 0; i < mState.getRegs().size()-1; i++)
					mState.targets.put(circle.get(i), circle.get(i+1));
				mState.targets.put(circle.get(mState.getRegs().size()-1), circle.get(0));
				List<Pair<Integer,Integer>> data = new ArrayList<Pair<Integer,Integer>>();
				for (int i = 0; i < mState.getRegs().size(); i++)
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
				
				final LinkedList<File> fq = new LinkedList<File>();
				for (File file : files) {
					fq.add(file);
					
				}
				Collections.sort(fq, new Comparator<File>() {
					@Override
					public int compare(File o1, File o2) {
						return o1.getName().compareToIgnoreCase(o2.getName());
					}
				});
				
				final List<String> images = new ArrayList<String>();
				
				final FileReader reader = new FileReader();
				reader.addLoadEndHandler(new LoadEndHandler() {
					@Override
					public void onLoadEnd(LoadEndEvent event) {
						images.add(createBitmapImage2(reader, fq.poll()));
						if (fq.isEmpty()) {
							createWindow(wrapBodyHtml(createBodyHtml(images)));
						}
						else {
							File file = fq.peek();
							if (file.getType().startsWith("image/")) {
								reader.readAsBinaryString(file);
							}
						}
					}
				});
				File file = fq.peek();
				if (file.getType().startsWith("image/")) {
					reader.readAsBinaryString(file);
				}
				GWT.log("Drop it");
				
				event.stopPropagation();
				event.preventDefault();
			}
		});
		
		Button btnAlfabetiskListe = new Button("Alfabetisk liste");
		btnAlfabetiskListe.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				createWindow(wrapBodyHtml(createAlphaList()));
			}
		});
		horizontalPanel.add(btnAlfabetiskListe);
		dp.addStyleName("gwt-DropPanel");
		horizontalPanel.add(dp);
		dp.setHeight("76px");
		
		Label lblTabBillederFor = new Label("Tab billeder for at printe");
		dp.setWidget(lblTabBillederFor);
		lblTabBillederFor.setSize("100%", "100%");
		
		CellList<Pair<Integer,Integer>> cellList = new CellList<Pair<Integer,Integer>>(new AbstractCell<Pair<Integer,Integer>>(){
			@Override
			public void render(Context context, Pair<Integer,Integer> value, SafeHtmlBuilder sb) {
				String killer = mState.getRegs().get(value.left).navn;
				String target = mState.getRegs().get(value.right).navn;
				sb.appendHtmlConstant("<span class='kill_pair'><span class='killer'>")
					.appendEscaped(killer).appendHtmlConstant("</span><span class='target'>")
					.appendEscaped(target).appendHtmlConstant("</span></span>");
			}
		});
		vp.add(cellList);
		
		dataProv.addDataDisplay(cellList);
		
		return vp;
	}
	
	protected String createAlphaList() {
		StringBuilder sb = new StringBuilder();
		sb.append("<dl>");
		List<Reg> regs = new ArrayList<Reg>(mState.getRegs());
		Collections.sort(regs, new Comparator<Reg>() {
			@Override
			public int compare(Reg o1, Reg o2) {
				return o1.navn.compareToIgnoreCase(o2.navn);
			}
		});
		for (Reg r : regs) {
			sb.append("<dt>").append(r.id).append("</dt>");
			sb.append("<dd>").append(r.navn)
				.append(" - ").append(r.gruppe)
			.append("</dd>");
		}
		sb.append("</dl>");
		return sb.toString();
	}

	protected String wrapBodyHtml(String content) {
		String style = "<style type='text/css'>" +
				"img {height:114mm;}\n" +
				"td { border:1px solid black; font-family:monospace; font-size:20pt}\n" +
				"dt { clear: left; float: left;}\n" +
				"dd { float: left;}\n" +
				"</style>";
		return "<HTML><HEAD>"+style+"<TITLE>Print Mig</TITLE></HEAD><BODY>"+content+"</BODY></HTML>";
	}


	protected String createBodyHtml(List<String> images) {
		for (String s : images)
			GWT.log(s);
		StringBuilder sb = new StringBuilder();
		sb.append("<table>");
		for (int i = 0; i < mState.targets.size(); i++) {
			String img;
			if (mState.targets.get(i) < images.size())
				img = images.get(mState.targets.get(i));
			else img = "<div>No picture</div>";
			Reg a = mState.getRegs().get(i);
			Reg b = mState.getRegs().get(mState.targets.get(i));
			sb.append("<tr><td>").append(img).append("</td><td>")
				.append("Kendes under navnet:<br/>")
				.append("<b>").append(b.navn).append("</b><br/>")
				.append("<br/>")
				.append("Forventet stamsted:<br/>")
				.append("<b>").append(b.gruppe).append(", ").append(b.bydel).append(", ").append(b.kvarter).append("</b><br />")
				.append("<br/>")
				.append("Forventes dræbt af:<br/>")
				.append("<b>").append(a.navn+" ("+a.id+")").append("</b><br />")
				.append("</td></tr>");
		}
		sb.append("</table>");
		//GWT.log(sb.toString());
		return sb.toString();
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
	private String createBitmapImage2(FileReader reader, File file) {
		String result = reader.getStringResult();
		String url = FileUtils.createDataUrl(file.getType(), result);
		StringBuilder sb = new StringBuilder();
		sb.append("<img src='").append(url).append("' />");
		return sb.toString();			
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