package dk.hitman.hitman2012.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.view.client.ListDataProvider;

import dk.hitman.hitman2012.shared.Pair;

public class State {
	public final ListDataProvider<Reg> dataProvider = new ListDataProvider<Reg>();
	public final List<Reg> regs = dataProvider.getList();
	public final Map<String,Pair<String,String>> gruppeCtrl = new HashMap<String,Pair<String,String>>();
	public final Map<String,String> kvarterCtrl = new HashMap<String,String>();
	public final Map<Integer,Integer> targets = new HashMap<Integer,Integer>();
	public final Set<Integer> deads = new HashSet<Integer>();
}
