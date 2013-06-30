import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class ControlPanel extends JPanel {
	private JButton step;
	private JButton run_pause;
	private JButton clear;
	private JButton random;
	private JTextField rate, percent;
	private AnimalWilds wilds;
	private WildsView wildsVisual;
	private JLabel gen;
	private Timer runTimer;

	public ControlPanel(WildsView visual, AnimalWilds life) {
		wilds = life;
		wildsVisual = visual;
		step = new JButton("Step");
		run_pause = new JButton("Run");
		clear = new JButton("Clear");
		random = new JButton("Random");
		rate = new JTextField(3);
		percent = new JTextField(3);
		JLabel rateLabel = new JLabel("Updates/Sec:");
		JLabel percentLabel = new JLabel("World seed:");
		gen = new JLabel("Gen 0");
		add(gen);
		add(step);
		add(clear);
		add(run_pause);
		add(rateLabel);
		add(rate);
		add(random);
		add(percentLabel);
		add(percent);
		runTimer = new Timer(500, new UpdateListener(false));
		ActionListener runPauseListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (run_pause.getText().equals(("Run"))) {
					boolean valid = false;
					try {
						int input = Integer.parseInt(rate.getText());
						if (input <= 1000)
							valid = true;
						else
							JOptionPane.showMessageDialog(ControlPanel.this, "cant do more than 1000 hz");
					} catch (Exception exc) {
						JOptionPane.showMessageDialog(ControlPanel.this, "Not valid hertz input");
					}
					if (valid) {
						run_pause.setText("Pause");
						wilds.run();
						wildsVisual.setEnabled(false);
						runTimer.setDelay(1000 / Integer.parseInt(rate.getText()));
						runTimer.setRepeats(true);
						runTimer.start();
					}
				} else {
					pause();
				}

			}

		};
		rate.addActionListener(runPauseListener);
		run_pause.addActionListener(runPauseListener);

		step.addActionListener(new UpdateListener(false));
		clear.addActionListener(new UpdateListener(true));

		random.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean valid = false;
				try {
					double input = Double.parseDouble(percent.getText());
					if (input < 1)
						valid = true;
					else
						JOptionPane.showMessageDialog(ControlPanel.this, "Rate must be at most 1, but is " + input);
				} catch (Exception exc) {
					JOptionPane.showMessageDialog(ControlPanel.this, "Not valid double input");
				}
				if (valid) {
					wilds.reset(Double.parseDouble(percent.getText()));
					wilds.randomise();
					wildsVisual.repaint();
				}

			}
		});
	}

	private void pause() {
		run_pause.setText("Run");
		runTimer.stop();
		wilds.pause();
	}

	public class UpdateListener implements ActionListener {
		private boolean re;
		private boolean stableDisplay;

		public UpdateListener(boolean reset) {
			re = reset;
			stableDisplay = false;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (!re) {
				wilds.update();
				if (wilds.isStable() && !stableDisplay) {
					stableDisplay = true;
				}
			} else {
				wilds.reset(Double.parseDouble(percent.getText()));
				stableDisplay = false;
				pause();
			}
			wildsVisual.repaint();
			gen.setText("Gen " + wilds.getGenerations());

		}

	}
}
