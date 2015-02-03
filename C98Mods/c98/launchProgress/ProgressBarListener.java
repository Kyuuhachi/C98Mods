package c98.launchProgress;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class ProgressBarListener extends ProgressListener {
	JProgressBar bar;
	JFrame frame;
	
	public ProgressBarListener() {
		frame = new JFrame();
		bar = new JProgressBar(0, 100);
		bar.setStringPainted(true);
		frame.add(bar);
		frame.getContentPane().setPreferredSize(new Dimension(400, 40));
		frame.pack();
		frame.setTitle("Launching Minecraft...");
		frame.setResizable(false);
		frame.setLocationRelativeTo(null); //Center window
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	@Override public void setProgress(int prog, String msg) {
		bar.setValue(prog);
		bar.setString(msg);
	}
	
	@Override public void close() {
		frame.dispose();
	}
	
}
