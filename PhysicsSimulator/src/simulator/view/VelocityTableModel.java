package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Body;

public class VelocityTableModel extends AbstractTableModel implements SimulatorObserver {
	private static final long serialVersionUID = 1L;

	private static final int NUM_COL = 2;
	private List<String> idList;
	private List<Double> avgVel;
	private int n;
	
	public VelocityTableModel(Controller ctrl)
	{
		idList = new ArrayList<>();
		avgVel = new ArrayList<>();
		n=0;
		ctrl.addObserver(this);
	}

	@Override
	public int getColumnCount() {
		return NUM_COL;
	}

	@Override
	public int getRowCount() {
		return avgVel.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		switch(column)
		{
		case 0: return idList.get(row);
		case 1: return avgVel.get(row);
		default:return null;
		}
	}
	@Override
	public String getColumnName(int column)
	{
		return column == 0 ? "ID" : "Average Velocity"; 
	}

	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				for(int i=0;i<bodies.size();i++)
				{
					idList.add(bodies.get(i).getId());
					avgVel.add(bodies.get(i).getVelocity().magnitude());
				}
				fireTableStructureChanged();
			}
			
		});
		
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				n=0;
				idList.clear();
				avgVel.clear();
				fireTableStructureChanged();
			}
			
		});
		
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				idList.add(b.getId());
				avgVel.add(b.getVelocity().magnitude());
				fireTableStructureChanged();
			}
			
		});
		
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				n++;
				for(int i=0;i<bodies.size();i++)
				{
					double old = avgVel.get(i);
					double neW = (old * (n-1) + bodies.get(i).getVelocity().magnitude())/n;
					avgVel.set(i, neW);	
				}
				fireTableStructureChanged();
			}
			
		});
		
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
	}
	@Override
	public void onGravityLawChanged(String gLawsDesc) {
	}

}
