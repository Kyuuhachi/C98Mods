package c98.launchProgress;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import c98.launchProgress.Progress.ProgressListener;

public class ProgressBarListener extends ProgressListener {
	JProgressBar bar;
	JFrame frame;
	
	public ProgressBarListener(JProgressBar bar, JFrame frame) {
		this.bar = bar;
		this.frame = frame;
	}
	
	@Override public void setProgress(int prog, String msg) {
		bar.setValue(prog);
		bar.setString(msg);
	}
	
	@Override public void close() {
		frame.dispose();
	}
	
}
