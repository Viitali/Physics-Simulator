package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;

public class VelocityTable extends JPanel {
	private static final long serialVersionUID = 1L;

	public VelocityTable(Controller c)
	{
		super(new BorderLayout());
		JTable jt = new JTable(new VelocityTableModel(c));
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black,2),
				"Velocity",
				TitledBorder.LEFT,
				TitledBorder.TOP));
		
		jt.setPreferredScrollableViewportSize(new Dimension(getWidth(),100));
		jt.setFillsViewportHeight(true);
		
		this.add(new JScrollPane(jt));
		this.add(jt);
		
		
	}
	
}
