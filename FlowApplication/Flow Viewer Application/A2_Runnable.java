import javax.swing.SwingUtilities;

public class A2_Runnable {

	/**
	 * This class implements the runnable inteface and starts a thread of
	 * excution of the main program.
	 * @author Sanket Patil
	 */
	public static class A2 implements Runnable{

	    public void run(){
	        new A2_Main();
	    }

	    public static void main(String[] args){
	        SwingUtilities.invokeLater(new A2());
	    }
	}

}
