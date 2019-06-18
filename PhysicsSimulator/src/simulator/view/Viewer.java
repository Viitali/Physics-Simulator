package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;
import simulator.misc.Vector;
import simulator.model.Body;

public class Viewer extends JComponent implements SimulatorObserver {
	
	private static final long serialVersionUID = 1L;
// ...
	private int _centerX;
	private int _centerY;
	private double _scale;
	private List<Body> _bodies;
	private boolean _showHelp;

	Viewer(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}

	private void initGUI() {
// add border with title
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "Viewer",
				TitledBorder.LEFT, TitledBorder.TOP));

		_bodies = new ArrayList<>();
		_scale = 1.0;
		_showHelp = true;

		addKeyListener(new KeyListener() {
// ...
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyChar()) {
				case '-':
					_scale = _scale * 1.1;
					break;
				case '+':
					_scale = Math.max(1000.0, _scale / 1.1);			
					break;
				case '=':
					autoScale();
					break;
				case 'h':
					_showHelp = !_showHelp;
					break;
				default:
				}
				repaint();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		addMouseListener(new MouseListener() {
// ...
			@Override
			public void mouseEntered(MouseEvent e) {
				requestFocus();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D gr = (Graphics2D) g;
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//use ’gr’ to draw not ’g’
//calculate the center
		_centerX = getWidth() / 2;
		_centerY = getHeight() / 2;
//draw a cross at center
		gr.setColor(Color.red);
		gr.drawLine(_centerX - 7, _centerY, _centerX + 7, _centerY);
		gr.drawLine(_centerX, _centerY - 7, _centerX, _centerY + 7);
//draw bodies
		for (Body body : this._bodies) {
			int x = _centerX + (int) (body.getPosicion().coordinate(0) / _scale) - 5;
			int y = _centerY + (int) (body.getPosicion().coordinate(1) / _scale) - 5;
			gr.setColor(Color.BLUE);
			gr.fillOval(x, y, 10, 10);
			gr.setColor(Color.BLACK);
			gr.drawString(body.getId(), x - 2, y - 13);
		}
//draw help if _showHelp is true
		if (this._showHelp) {
			long limY = Math.round(this.getBounds().getMinY());
			gr.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12)); // fuente sans_serif , negrita y tam 12
			gr.drawString("h: toggle help, +: zoom-in, -: zoom-out, =: fit", 5, limY * 1 / 8);
			gr.drawString("Scaling ratio: " + this._scale, 5, limY * 1 / 5);
		}
	}

//other private/protected methods
//...
	private void autoScale() {
		double max = 1.0;
		for (Body b : _bodies) {
			Vector p = b.getPosicion();
			for (int i = 0; i < p.dim(); i++)
				max = Math.max(max, Math.abs(b.getPosicion().coordinate(i)));
		}
		double size = Math.max(1.0, Math.min((double) getWidth(), (double) getHeight()));
		_scale = max > size ? 4.0 * max / size : 1.0;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(getWidth(), 500);
	}
//SimulatorObserver methods
//...

	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				_bodies = new ArrayList<>(bodies);
				autoScale();
				repaint();
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
				autoScale();
				repaint();
			}
		});
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				_bodies=new ArrayList<>(bodies);
				//_bodies.add(b);
				autoScale();
				repaint();
			}
		});
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				_bodies=new ArrayList<>(bodies);
				repaint();
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
