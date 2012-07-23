package dk.hitman.hitman2012.client;

import java.util.Comparator;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import dk.hitman.hitman2012.shared.Pair;

public class Registrering implements Content {

	private Panel mContents;
	private State mState;
	
	
	public Registrering(State s) {
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
		VerticalPanel vp = new VerticalPanel();
		vp.setStyleName("regPanel");

		CellTable<Reg> cellTable = new CellTable<Reg>();
		vp.add(cellTable);

		
		cellTable.setStyleName("table");
		cellTable.setTableLayoutFixed(true);
		cellTable.setPageSize(1000);
		cellTable.setSize("100%", "100%");

		Column<Reg, Number> column_id = new Column<Reg, Number>(
				new NumberCell()) {
			@Override
			public Number getValue(Reg object) {
				return object.id;
			}
		};
		column_id.setSortable(true);
		cellTable.addColumn(column_id, "ID");
		cellTable.setColumnWidth(column_id, "4em");

		Column<Reg, String> column_name = new Column<Reg, String>(
				new EditTextCell()) {
			@Override
			public String getValue(Reg reg) {
				return reg.navn;
			}
		};
		column_name.setSortable(true);
		cellTable.addColumn(column_name, "Name");
		cellTable.setColumnWidth(column_name, "25%");

		Column<Reg, String> column_g = new Column<Reg, String>(
				new EditTextCell()) {
			@Override
			public String getValue(Reg reg) {
				return reg.gruppe;
			}
		};
		column_g.setSortable(true);
		cellTable.addColumn(column_g, "Gruppe");
		cellTable.setColumnWidth(column_g, "25%");
		
		Column<Reg, String> column_k = new Column<Reg, String>(new EditTextCell()) {
			@Override
			public String getValue(Reg object) {
				return (String) object.kvarter;
			}
		};
		column_k.setSortable(true);
		cellTable.addColumn(column_k, "Kvarter");
		cellTable.setColumnWidth(column_k, "25%");

		Column<Reg, String> column_bydel = new Column<Reg, String>(
				new EditTextCell()) {
			@Override
			public String getValue(Reg reg) {
				return (String) reg.bydel;
			}
		};
		column_bydel.setSortable(true);
		cellTable.addColumn(column_bydel, "Bydel");
		cellTable.setColumnWidth(column_bydel, "25%");

		// Connect the table to the data provider.
		mState.dataProvider.addDataDisplay(cellTable);
		ListHandler<Reg> columnSortHandler = new ListHandler<Reg>(mState.getRegs());
		columnSortHandler.setComparator(column_id, new Comparator<Reg>() {
			@Override
			public int compare(Reg o1, Reg o2) {
				return o1.id - o2.id;
			}
		});
		
		columnSortHandler.setComparator(column_name, new Comparator<Reg>() {
			@Override
			public int compare(Reg o1, Reg o2) {
				return o1.navn.compareToIgnoreCase(o2.navn);
			}
		});
		column_name.setFieldUpdater(new FieldUpdater<Reg, String>() {
			public void update(int index, Reg object, String value) {
				object.navn = value;
			}
		});

		columnSortHandler.setComparator(column_g, new Comparator<Reg>() {
			@Override
			public int compare(Reg o1, Reg o2) {
				return o1.gruppe.compareToIgnoreCase(o2.gruppe);
			}
		});
		column_g.setFieldUpdater(new FieldUpdater<Reg, String>() {
			public void update(int index, Reg object, String value) {
				object.gruppe = value;
			}
		});
		
		columnSortHandler.setComparator(column_k, new Comparator<Reg>() {
			@Override
			public int compare(Reg o1, Reg o2) {
				return o1.kvarter.compareToIgnoreCase(o2.kvarter);
			}
		});
		column_k.setFieldUpdater(new FieldUpdater<Reg, String>() {
			public void update(int index, Reg object, String value) {
				object.kvarter = value;
			}
		});
		
		columnSortHandler.setComparator(column_bydel, new Comparator<Reg>() {
			@Override
			public int compare(Reg o1, Reg o2) {
				return o1.bydel.compareToIgnoreCase(o2.bydel);
			}
		});
		column_bydel.setFieldUpdater(new FieldUpdater<Reg, String>() {
			public void update(int index, Reg object, String value) {
				object.bydel = value;
			}
		});

		cellTable.addColumnSortHandler(columnSortHandler);

		// We know that the data is sorted alphabetically by default.
		cellTable.getColumnSortList().push(column_id);
		
		cellTable.setEmptyTableWidget(new Label("Ingen tilmeldere!"));
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		vp.add(horizontalPanel);

		Label lblNy = new Label("Ny: ");
		horizontalPanel.add(lblNy);
		lblNy.setHeight("");

		final TextBox txtbxNavn = new TextBox();

		horizontalPanel.add(txtbxNavn);

		final TextBox txtbxGruppe = new TextBox();
		horizontalPanel.add(txtbxGruppe);
		
		final TextBox txtbxKvarter = new TextBox();
		horizontalPanel.add(txtbxKvarter);
		
		final TextBox txtbxBydel = new TextBox();
		horizontalPanel.add(txtbxBydel);

		txtbxNavn.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER)
					txtbxGruppe.setFocus(true);
			}
		});
		txtbxGruppe.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					txtbxKvarter.setFocus(true);
					Pair<String,String> p = mState.gruppeCtrl.get(txtbxGruppe.getText());
					if (p != null) {
						if (txtbxKvarter.getText().isEmpty())
							txtbxKvarter.setText(p.left);
						if (txtbxBydel.getText().isEmpty())
							txtbxBydel.setText(p.right);
					}
				}
			}
		});

		final Button btnTifj = new Button("Tiføj");
		btnTifj.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Reg reg = new Reg(mState.getRegs().size(), txtbxNavn.getText(),
						txtbxGruppe.getText(), txtbxKvarter.getText(), txtbxBydel.getText());
				
				mState.gruppeCtrl.put(txtbxGruppe.getText(), Pair.create(txtbxKvarter.getText(), txtbxBydel.getText()));
				mState.kvarterCtrl.put(txtbxKvarter.getText(), txtbxBydel.getText());
				
				mState.getRegs().add(reg);
				txtbxNavn.setText("");
				txtbxGruppe.setText("");
				txtbxKvarter.setText("");
				txtbxBydel.setText("");
				txtbxNavn.setFocus(true);
			}
		});
		txtbxKvarter.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					txtbxBydel.setFocus(true);
					String p = mState.kvarterCtrl.get(txtbxKvarter.getText());
					if (p != null) {
						if (txtbxBydel.getText().isEmpty())
							txtbxBydel.setText(p);
					}
				}
			}
		});
		txtbxBydel.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER)
					btnTifj.click();
			}
		});
		horizontalPanel.add(btnTifj);
		
		
		
		return vp;
	}

}
