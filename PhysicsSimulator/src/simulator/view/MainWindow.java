package simulator.view;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simulator.control.Controller;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	public MainWindow(Controller ctrl)
	{
		super("Physics Simulator");
		
		JPanel panel = new JPanel();
		BoxLayout box = new BoxLayout(panel,BoxLayout.Y_AXIS);
		this.setContentPane(panel);
		
		panel.setLayout(box);
		
		panel.add(new ControlPanel(ctrl));
		panel.add(new BodiesTable(ctrl));
		panel.add(new VelocityTable(ctrl));
		panel.add(new Viewer(ctrl));
		panel.add(new StatusBar(ctrl));
		this.setVisible(true); //visible
		this.pack(); //ajusta tamaño
		this.setLocationRelativeTo(null); //para centrar
		this.setDefaultCloseOperation(EXIT_ON_CLOSE); //para cerrar al salir
	}
}
