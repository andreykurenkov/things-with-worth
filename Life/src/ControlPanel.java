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
	private GameOfLife myLife;
	private LifePanel lifeVisual;
	private JLabel gen;
	private Timer runTimer;

	public ControlPanel(LifePanel visual, GameOfLife life, boolean simple) {
		myLife = life;
		lifeVisual = visual;
		step = new JButton("Step");
		run_pause = new JButton("Run");
		clear = new JButton("Clear");
		random = new JButton("Random");
		rate = new JTextField(3);
		percent = new JTextField(3);
		JLabel rateLabel = new JLabel("Hz Rate:");
		JLabel percentLabel = new JLabel("Live ratio:");
		JButton toggleWrap = new JButton("Toggle Wrap");
		toggleWrap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				myLife.toggleWrap();
			}
		});
		gen = new JLabel("Gen  0");
		add(gen);
		add(step);
		add(clear);
		if (!simple) {
			add(run_pause);
			add(rateLabel);
			add(rate);
			add(random);
			add(percentLabel);
			add(percent);
			add(toggleWrap);
		}
		runTimer = new Timer(500, new UpdateListener(false));
		run_pause.addActionListener(new ActionListener() {
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
						myLife.run();
						lifeVisual.disable();
						runTimer.setDelay(1000 / Integer.parseInt(rate.getText()));
						runTimer.setRepeats(true);
						runTimer.start();
					}
				} else {
					pause();
				}

			}

		});

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
					myLife.reset();
					myLife.randomise(Double.parseDouble(percent.getText()));
					lifeVisual.repaint();
				}

			}
		});
	}

	private void pause() {
		lifeVisual.enable();
		run_pause.setText("Run");
		runTimer.stop();
		myLife.pause();
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
				myLife.update();
				if (myLife.isStable() && !stableDisplay) {
					stableDisplay = true;
					JOptionPane
							.showMessageDialog(ControlPanel.this,
									"It is stable!\nNo further changes, except oscillation, will occur.\nGen count is stopped, thought enjoy the lightshow.");
				}
			} else {
				myLife.reset();
				stableDisplay = false;
				pause();
			}

			lifeVisual.repaint();
			gen.setText("Gen " + myLife.getGenerations());

		}

	}
}
