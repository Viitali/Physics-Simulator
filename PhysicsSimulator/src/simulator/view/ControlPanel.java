package simulator.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.GravityLaws;

public class ControlPanel extends JPanel implements SimulatorObserver {

	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	private JFileChooser fileChooser;
	private SpinnerNumberModel stepsMod;
	private SpinnerNumberModel delayMod;
	private JTextField dTime;
	private volatile Thread _thread;

	private JButton file;
	private JButton gravLaws;
	private JButton run;
	private JButton stop;
	private JButton exit;
	private JSpinner steps;
	private JSpinner delay;
	private JButton moreBody;

	ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		initGUI();
		_ctrl.addObserver(this);
	}

	private void initGUI() {
		// TODO build the tool bar by adding buttons, etc.
		BorderLayout bl = new BorderLayout();
		JToolBar toolbar = new JToolBar();

		this.setLayout(bl); // asignamos layout al panel

		file = new JButton(); // Crea el boton
		file.setIcon(new ImageIcon("resources/icons/open.png"));// para los iconos
		file.setToolTipText("Import a file"); // mensaje que salta cuando dejas el puntero sobre el icono
		file.addActionListener(new FileChooserListener());

		gravLaws = new JButton();
		gravLaws.setIcon(new ImageIcon("resources/icons/physics.png"));
		gravLaws.setToolTipText("Change gravitation law");
		gravLaws.addActionListener(new LawSelectorListener());

		run = new JButton();
		run.setIcon(new ImageIcon("resources/icons/run.png"));
		run.setToolTipText("Start simulation");
		run.addActionListener(new RunListener());

		stop = new JButton();
		stop.setIcon(new ImageIcon("resources/icons/stop.png"));
		stop.setToolTipText("Stop the simulation");
		stop.addActionListener(new StopListener());

		toolbar.add(file); // Añade el boton
		toolbar.add(gravLaws);
		toolbar.addSeparator();
		toolbar.add(stop);
		toolbar.add(run);

		this.add(toolbar, BorderLayout.WEST); // Especifico del border layout
		toolbar.addSeparator();
		toolbar.add(new JLabel("Delay: "));
		delayMod = new SpinnerNumberModel(1, 1, 1000, 1);
		delay = new JSpinner(delayMod);
		delay.setToolTipText("Changes the delay time");
		toolbar.add(delay);

		toolbar.addSeparator();
		toolbar.add(new JLabel("Steps: "));
		stepsMod = new SpinnerNumberModel(500, 0, 10000, 100);
		steps = new JSpinner(stepsMod);
		steps.setToolTipText("Changes the number of steps that simulator executes");
		toolbar.add(steps);

		toolbar.addSeparator();

		toolbar.add(new JLabel("Delta-Time: "));
		dTime = new JTextField(10);
		dTime.setToolTipText("Changes the delta time, only numbers allowed");
		toolbar.add(dTime);
		moreBody = new JButton();
		moreBody.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e) {
						new BodiesPanel(_ctrl);
					}
			
				});
		toolbar.add(moreBody);
		

		JToolBar leftTB = new JToolBar();

		exit = new JButton();
		exit.setIcon(new ImageIcon("resources/icons/exit.png"));
		exit.setToolTipText("Exit");
		exit.addActionListener(new ExitListener());
		leftTB.addSeparator();
		leftTB.add(exit);

		this.add(leftTB, BorderLayout.EAST);

	}

	// other private/protected methods

	private void run_sim(int n, int delay) {

		setButtonsState(false);
		while (n > 0 && !_thread.isInterrupted()) {
			try {
				
				this._ctrl.run(1);
				Thread.sleep(delay);

			} catch (InterruptedException e) {
				return;
			} catch (Exception ee) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						
						JOptionPane.showMessageDialog(null, ee.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						setButtonsState(true);
						return;
					}

				});
			}
			n--;
		}
		setButtonsState(true);
	}

	private void setButtonsState(boolean a) {
		file.setEnabled(a);
		gravLaws.setEnabled(a);
		run.setEnabled(a);
		exit.setEnabled(a);
		steps.setEnabled(a);
		dTime.setEnabled(a);
		delay.setEnabled(a);
	}

	// Buttons ActionListeners
	private class LawSelectorListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			List<GravityLaws> options = new ArrayList<>();
			List<JSONObject> lawsInfo = _ctrl.getGravityLawsFactory().getInfo();
			for (JSONObject o : lawsInfo) {
				options.add(_ctrl.getGravityLawsFactory().createInstance(o));
			}
			GravityLaws opt = (GravityLaws) JOptionPane.showInputDialog(null, "Select a gravity law", "Gravity Laws",
					JOptionPane.DEFAULT_OPTION, null, options.toArray(), options.get(0));
			if (opt != null) {
				int p = options.indexOf(opt);
				_ctrl.setGravityLaws(lawsInfo.get(p));
			}

		}
	}

	private class FileChooserListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// filtra los archivos por extension txt
			FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
			fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir") + "/resources"));
			// guarda la ruta del txt
			fileChooser.setFileFilter(filter);
			fileChooser.setMultiSelectionEnabled(false);

			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

				_ctrl.reset();
				try {
					_ctrl.loadBodies(new FileInputStream(fileChooser.getSelectedFile()));
				} catch (FileNotFoundException e1) { //igual con excepciones que pueda lanzar loadBodies
					//mensaje al usuario en un cuadro de diálogo
					throw new IllegalArgumentException("File not found");
				}
			}
		}
	}

	private class ExitListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int opt = JOptionPane.showConfirmDialog(null, "Do you want to exit the Simulator?", "Physics Simulator",
					JOptionPane.YES_NO_OPTION);
			if (opt == JOptionPane.YES_OPTION)
				System.exit(0);
		}
	}

	private class RunListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				int selectedSteps = (int) steps.getValue();
				int delayValue = (int) delay.getValue();
				_ctrl.setDeltaTime(Double.parseDouble(dTime.getText()));
				if (selectedSteps <= 0)
					JOptionPane.showMessageDialog(null, "Steps must be a positive number", "Error",
							JOptionPane.ERROR_MESSAGE);
				else {
					//mejor comprobar que _thread == null
					if(_thread==null)
						_thread = new Thread(new Runnable() {
						@Override
						public void run() {
							//setButtonsState(false); //en la hebra de Swing
							run_sim(selectedSteps, delayValue);
							//setButtonsState(true); //en la hebra de Swing
							_thread = null;
						}
					});
					_thread.start();
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private class StopListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (_thread != null) {
				_thread.interrupt();
				setButtonsState(true);
			}
		}
	}
	

	// SimulatorObserver methods
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				dTime.setText(Double.toString(dt));
			}
		});
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				dTime.setText(Double.toString(dt));
			}
		});
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				dTime.setText(Double.toString(dt));
			}
		});
	}

	@Override
	public void onGravityLawChanged(String gLawsDesc) {
		// TODO Auto-generated method
	}
}
