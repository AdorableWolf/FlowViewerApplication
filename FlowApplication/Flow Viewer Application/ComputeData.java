import java.util.ArrayList;

/**
 * The ComputeData does all of the logical computations required by the Application. 
 * @author Sanket Patil
 *
 */
public class ComputeData{
	
	private ArrayList<Double> timeStamp = new ArrayList<Double>();
	private ArrayList<Integer> bytes = new ArrayList<Integer>();
	
	private ArrayList<Integer> computedBytes = new ArrayList<Integer>();
	private ArrayList<Double> computedTimeStamp = new ArrayList<Double>();
	
	/**
	 * A default Constructor.
	 */
	public ComputeData() {}
	
	/**
	 * A contructor that creates the ComputeData Object with the passed parametes.
	 * @param timeStamp : The timeStamp ArrayList of the IP Address that requires to utilize the methods of the ComputeData Object.
	 * @param bytes : The Bytes ArrayList of the IP Address that requires to utilize the methods of the ComputeData Object.
	 */
	public ComputeData(ArrayList<Double> timeStamp, ArrayList<Integer> bytes) {
		this.timeStamp = timeStamp;
		this.bytes = bytes;
	}
	
	/**
	 * Computes the total bytes sent/received and 2 second interval timeStamps, for every 2 second interval.
	 */
	public void computeTotalBytes() {
		if ( timeStamp.size() > 0) {
			double startTime = timeStamp.get(0);
			int cummulativeBytes = bytes.get(0);
			
			computedBytes.clear();
			
			for ( int i = 1; i <= timeStamp.size() - 1; i++  ) {
				double currentTime = (timeStamp.get(i) - startTime);
				if ( currentTime > 2 ) {
					computedBytes.add(new Integer(cummulativeBytes));
					startTime = timeStamp.get(i);
					computedTimeStamp.add(startTime);
					cummulativeBytes = bytes.get(i);
				}
				else {
					cummulativeBytes += bytes.get(i);
				}
				
			}
		}
	}
	
	/**
	 * Getter method for getting the greatest time int the timeStamp Arraylist.
	 * @return : The last time that an IP Address sent / received an IP Packet.
	 */
	public double getMaxTime() {
		double maxTime = 0;
		for ( int i = 0; i < timeStamp.size(); i++ ) {
			if ( timeStamp.get(i) > maxTime ) {
				maxTime = timeStamp.get(i);
			}
		}
		
		return maxTime;
	}
	
	/**
	 * Getter method for getting the largest value throughout the transmission overall 
	 * of the cummulative byte sent/received for a 2 second interval by the IP Address.
	 * @return : The largest byte that an IP Address sent / received for throughout the transmission overall.
	 */
	public int getMaxByte() {
		int maxByte = 0;
		for ( int i = 0; i < computedBytes.size(); i ++) {
			if ( computedBytes.get(i) > maxByte) {
				maxByte = computedBytes.get(i);
			}
		}
		return maxByte;
	}
	
	/**
	 * Getter method to get the list of computed bytes for an IP Address.
	 * @return : The list of computed bytes for an IP Address.
	 */
	public ArrayList<Integer> getComputedBytes(){
		
		return computedBytes;
	}
	
	/**
	 * Getter method to get the list of computed timeStamp for an IP Address.
	 * @return : The list of computed timeStamps for an IP Address.
	 */
	public ArrayList<Double> getComputedTimeStamp(){
		
		return computedTimeStamp;
	}
}
