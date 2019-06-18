package simulator.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.control.Controller;

public class BodiesPanel extends JFrame{

	private static final long serialVersionUID = 1L;
	private Controller ctrl;
	private JComboBox<String>options;
	private JTextField freq;
	private JTextField loss;
	
	public BodiesPanel(Controller _ctrl) {
		super("add new body");
		this.ctrl=_ctrl;
		iniGui();
	}
	
	private void iniGui()
	{
		
		JPanel jp = new JPanel();
		BoxLayout bly = new BoxLayout(jp, BoxLayout.Y_AXIS);
		jp.setLayout(bly);
		
		
		JPanel typep = new JPanel(new FlowLayout());
		JPanel posp = new JPanel(new FlowLayout());
		JPanel velp = new JPanel(new FlowLayout());
		JPanel idp = new JPanel(new FlowLayout());
		JPanel massp = new JPanel(new FlowLayout());
		JPanel freqp = new JPanel(new FlowLayout());
		JPanel lossp = new JPanel(new FlowLayout());
		
		//combobox
		typep.add(new JLabel("Tipo"));
		List<JSONObject> tipos = ctrl.getBodiesFactory().getInfo();
		options = new BodySelector(tipos);
		typep.add(options);
		jp.add(typep);
		//identificador
		idp.add(new JLabel ("ID: "));
		JTextField id = new JTextField(10);
		idp.add(id);
		jp.add(idp);
		//posiciones
		posp.add(new JLabel("Posicion: "));
		JTextField posX = new JTextField(10);
		JTextField posY = new JTextField(10);
		posp.add(posX);
		posp.add(posY);
		jp.add(posp);
		//velocidad
		velp.add(new JLabel("Velocidad: "));
		JTextField veX = new JTextField(10);
		JTextField veY = new JTextField(10);
		velp.add(veX);
		velp.add(veY);
		jp.add(velp);
		//masa
		massp.add(new JLabel("Masa: "));
		JTextField mas = new JTextField(5);
		massp.add(mas);
		jp.add(massp);
		//freq y factor
		freqp.add(new JLabel("Frecuencia: "));
		freq = new JTextField(10);
		freqp.add(freq);
		jp.add(freqp);
		
		lossp.add(new JLabel("Factor: "));
		loss =new JTextField(10);
		lossp.add(loss);
		jp.add(lossp);
		
		JButton save = new JButton("Meter");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JSONObject obj = tipos.get(options.getSelectedIndex());
				JSONObject data = new JSONObject();
				data.put("id", id.getText());
				JSONArray p = new JSONArray();
				p.put(Double.parseDouble(posX.getText()));
				p.put(Double.parseDouble(posY.getText()));
				data.put("pos", p);
				
				JSONArray v = new JSONArray();
				v.put(Double.parseDouble(veX.getText()));
				v.put(Double.parseDouble(veY.getText()));
				data.put("vel", v);
				
				data.put("mass", Double.parseDouble(mas.getText()));
				
				
				if(obj.has("factor"))
				{
					data.put("freq",Double.parseDouble(freq.getText()));
					data.put("factor", Double.parseDouble(loss.getText()));
				}
				obj.put("data", data);
				
				ctrl.addBody(obj);
			
				BodiesPanel.this.dispose();
			}
		});
		jp.add(save);
		
		
		
		
		this.add(jp);
		this.setVisible(true);
		this.setResizable(true);
		this.pack();
		this.setLocationRelativeTo(null);
	}
	private class BodySelector extends JComboBox<String>
	{
		private static final long serialVersionUID = 1L;

		public BodySelector (List<JSONObject> tipos)
		{
			for(JSONObject a : tipos)
			{
				this.addItem(a.getString("type"));
			}
			this.setSelectedIndex(1);
			this.addActionListener(new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent e) {
						if(tipos.get(BodySelector.this.getSelectedIndex()).getJSONObject("data").has("freq"))
						{
							freq.setEnabled(true);
						}
						else
							freq.setEnabled(false);
						if(tipos.get(BodySelector.this.getSelectedIndex()).getJSONObject("data").has("factor"))
						{
							loss.setEnabled(true);
						}
						else
							loss.setEnabled(false);
						}
					});
		}
	}
	
	
}
