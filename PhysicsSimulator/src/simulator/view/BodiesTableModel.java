package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Body;

public class BodiesTableModel extends AbstractTableModel implements SimulatorObserver{

	
	private static final long serialVersionUID = 1L;
	private List<Body> _bodies;
	private final static String[] attributes = { "Id", "Mass", "Position", "Velocity", "Acceleration" };
	
	public BodiesTableModel(Controller ctrl) {
		_bodies= new ArrayList<Body>();
		ctrl.addObserver(this);	
	}
	
	@Override
	public int getRowCount() {
		return _bodies.size();
	}
	@Override
	public int getColumnCount() {
	 return attributes.length;
	}
	@Override
	public String getColumnName(int column) {
		return attributes[column];
	}
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Body b=_bodies.get(rowIndex);
		String dato = attributes[columnIndex];
		
		switch(dato)
		{
		case "Id":
			return b.getId();
		case "Mass":
			return b.getMass();
		case "Position":
			return b.getPosicion();
		case "Velocity":
			return b.getVelocity();
		case "Acceleration":
			return b.getAcceleration();
		default:
			return null;
		}
	}
	
	
	// SimulatorObserver methods
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				_bodies=new ArrayList<>(bodies);
				fireTableStructureChanged();
			}
		});	
	}
	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				_bodies=new ArrayList<>(bodies);
				//_bodies.clear();
				fireTableStructureChanged();
			}
		});
	}
	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				//_bodies.add(b);
				_bodies=new ArrayList<>(bodies);
				fireTableStructureChanged();
			}
		});
	}
	@Override
	public void onAdvance(List<Body> bodies, double time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				_bodies=new ArrayList<>(bodies);
				fireTableStructureChanged();
			}
		});
	}
	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onGravityLawChanged(String gLawsDesc) {
		// TODO Auto-generated method stub
	}

}
