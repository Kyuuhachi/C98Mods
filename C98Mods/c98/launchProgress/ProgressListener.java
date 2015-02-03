package c98.launchProgress;

public abstract class ProgressListener {
	abstract void setProgress(int prog, String msg);
	
	void close() {}
}