package dk.hitman.hitman2012.client;

import java.util.Comparator;
import java.util.List;

import org.vectomatic.dnd.DropPanel;

import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.widget.client.TextButton;

public class Registrering implements Content {

	private Panel mContents;
	private ListDataProvider<Reg> dataProvider;

	@Override
	public Panel getContents() {
		if (mContents == null)
			mContents = createContents();
		return mContents;
	}

	class Reg {
		public Reg(int i, String n, String g, String u) {
			id = i;
			navn = n;
			gruppe = g;
			ulejr = u;
		}

		public final int id;
		public final String navn;
		public final String gruppe;
		public final String ulejr;
		
		public int compareTo(Reg other, int col) {
			if (this == other) return 0;
			if (col == 0) return id - other.id;
			if (col == 1) return navn.compareTo(other.navn);
			if (col == 2) return gruppe.compareTo(other.gruppe);
			return ulejr.compareTo(other.ulejr);
		}
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	private Panel createContents() {
		VerticalPanel vp = new VerticalPanel();

		Label lblRegistrering = new Label("Registrering");
		vp.add(lblRegistrering);

		CellTable<Reg> cellTable = new CellTable<Reg>();
		vp.add(cellTable);
		cellTable.setWidth("100%");

		Column<Reg, Number> column_1 = new Column<Reg, Number>(new NumberCell()) {
			@Override
			public Number getValue(Reg object) {
				return object.id;
			}
		};
		column_1.setSortable(true);
		cellTable.addColumn(column_1, "ID");
		cellTable.setColumnWidth(column_1, "30px");

		Column<Reg, String> column_2 = new Column<Reg, String>(
				new EditTextCell()) {
			@Override
			public String getValue(Reg reg) {
				return reg.navn;
			}
		};
		column_2.setSortable(true);
		cellTable.addColumn(column_2, "Name");
		cellTable.setColumnWidth(column_2, "33%");

		Column<Reg, String> column_g = new Column<Reg, String>(new EditTextCell()) {
			@Override
			public String getValue(Reg reg) {
				return reg.gruppe;
			}
		};
		column_g.setSortable(true);
		cellTable.addColumn(column_g, "Gruppe");
		cellTable.setColumnWidth(column_g, "33%");

		Column<Reg, String> column_3 = new Column<Reg, String>(
				new EditTextCell()) {
			@Override
			public String getValue(Reg reg) {
				return reg.ulejr;
			}
		};
		column_3.setSortable(true);
		cellTable.addColumn(column_3, "Underlejr");
		cellTable.setColumnWidth(column_3, "33%");

		dataProvider = new ListDataProvider<Reg>();

		// Connect the table to the data provider.
		dataProvider.addDataDisplay(cellTable);

		List<Reg> list = dataProvider.getList();
		list.add(new Reg(0, "A", "B", "C"));
		list.add(new Reg(1, "A", "B", "C"));

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		vp.add(horizontalPanel);

		final TextBox txtbxNavn = new TextBox();

		txtbxNavn.setText("Navn");
		horizontalPanel.add(txtbxNavn);

		final TextBox txtbxGruppe = new TextBox();
		txtbxGruppe.setText("Gruppe");
		horizontalPanel.add(txtbxGruppe);

		final TextBox txtbxUlejr = new TextBox();
		horizontalPanel.add(txtbxUlejr);

		txtbxNavn.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER)
					txtbxGruppe.setFocus(true);
			}
		});
		txtbxGruppe.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER)
					txtbxUlejr.setFocus(true);
			}
		});

		final Button btnTifj = new Button("Tiføj");
		btnTifj.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				List<Reg> list = dataProvider.getList();
				Reg reg = new Reg(list.size(), txtbxNavn.getText(), txtbxGruppe
						.getText(), txtbxUlejr.getText());
				list.add(reg);
				txtbxNavn.setText("");
				txtbxGruppe.setText("");
				txtbxUlejr.setText("");
				dataProvider.flush();
				txtbxNavn.setFocus(true);
			}
		});
		txtbxUlejr.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER)
					btnTifj.click();
			}
		});
		horizontalPanel.add(btnTifj);
		
		
		ListHandler<Reg> columnSortHandler = new ListHandler<Reg>(list);
		columnSortHandler.setComparator(column_1,
				new Comparator<Reg>() {
					@Override
					public int compare(Reg o1, Reg o2) {
						return o1.compareTo(o2, 0);
					}
				});
		
		columnSortHandler.setComparator(column_2,
				new Comparator<Reg>() {
					@Override
					public int compare(Reg o1, Reg o2) {
						return o1.compareTo(o2, 1);
					}
				});
		
		columnSortHandler.setComparator(column_g,
				new Comparator<Reg>() {
					@Override
					public int compare(Reg o1, Reg o2) {
						return o1.compareTo(o2, 2);
					}
				});
		
		columnSortHandler.setComparator(column_2,
				new Comparator<Reg>() {
					@Override
					public int compare(Reg o1, Reg o2) {
						return o1.compareTo(o2, 3);
					}
				});
		
		cellTable.addColumnSortHandler(columnSortHandler);

		// We know that the data is sorted alphabetically by default.
		cellTable.getColumnSortList().push(column_1);

		
		
		
		
		DropPanel dpanel = new DropPanel();
		dpanel.addStyleName("gwt-DropPanel");
		vp.add(dpanel);

		Label lblDropDataHere = new Label("Drop data here");
		dpanel.setWidget(lblDropDataHere);
		lblDropDataHere.setSize("100%", "100%");

		TextButton txtbtnDownloadData = new TextButton("Download Data");
		txtbtnDownloadData.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				downloadData();
			}
		});
		vp.add(txtbtnDownloadData);

		return vp;
	}

	protected void downloadData() {
		String data = URL.encode(toCSV(dataProvider.getList()));
		Window.open("data:text/plain;charset=utf-8," + data, "_blank", "");
	}

	protected String toCSV(List<Reg> data) {
		StringBuilder sb = new StringBuilder();
		for (Reg reg : data) {
			sb.append(reg.id).append(",");
			sb.append(reg.navn).append(",");
			sb.append(reg.gruppe).append(",");
			sb.append(reg.ulejr).append(",");
			sb.append("\n");
		}
		return sb.toString();
	}

}
