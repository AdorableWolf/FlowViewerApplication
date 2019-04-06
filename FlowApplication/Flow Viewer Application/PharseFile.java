import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/**
 * The primary function of this class is to read the tracefile and segregate the data accordingly in a set of HashMap for later use in the
 * Compute Data class.
 * @author Sanket Patil
 *
 */
public class PharseFile{

    private HashSet<String> sourceHosts;
    private HashSet<String> destinationHosts;
    
    private HashMap<String, ArrayList<Double>> srcTimeStamp;
    private HashMap<String, ArrayList<Integer>> srcSentBytes;
    
    private HashMap<String, ArrayList<Double>> hstTimeStamp;
    private HashMap<String, ArrayList<Integer>> hstReceivedBytes;
    
    /**
     * A default PharseFile Constructor.
     */
    public PharseFile(){}

    /**
     * This method performs a single read on the tracefile object and seives the data to generate information.
     * @param file : File object that contains the path of the tracefile to perform the read on.
     */
    public void ReadFile(File file){
        sourceHosts = new HashSet<String>();
        destinationHosts = new HashSet<String>();
        
        srcTimeStamp = new HashMap<String, ArrayList<Double>>();
        srcSentBytes = new HashMap<String, ArrayList<Integer>>();
        
        hstTimeStamp = new HashMap<String, ArrayList<Double>>();
        hstReceivedBytes = new HashMap<String, ArrayList<Integer>>();
        
        try{
            Scanner fileScanner = new Scanner(file);
            while(fileScanner.hasNextLine()){
                String line = fileScanner.nextLine();
                String[] currentLineArray = line.split("\t");
                if(currentLineArray[2] != null){
                    sourceHosts.add(currentLineArray[2]);
                }
                if(currentLineArray[4] != null){
                    destinationHosts.add(currentLineArray[4]);
                }
                try {
                	if ( (srcTimeStamp.containsKey(currentLineArray[2]) && srcSentBytes.containsKey(currentLineArray[2])) == false) {
                    	ArrayList<Double> timeStamp = new ArrayList<Double>();
                    	ArrayList<Integer> sentBytes = new ArrayList<Integer>();
                    	
                    	sentBytes.add(new Integer(currentLineArray[7]));
                    	timeStamp.add(new Double(currentLineArray[1]));
                    	
                    	srcTimeStamp.put(currentLineArray[2], timeStamp);
                    	srcSentBytes.put(currentLineArray[2], sentBytes);
                    }
                    
                    else {
                    	ArrayList<Double> current = srcTimeStamp.get(currentLineArray[2]);
                    	ArrayList<Integer> currentBytes = srcSentBytes.get(currentLineArray[2]);
                    	
                    	currentBytes.add(new Integer(currentLineArray[7]));
                    	current.add(new Double(currentLineArray[1]));
                    	
                    	srcTimeStamp.put(currentLineArray[2], current);
                    	srcSentBytes.put(currentLineArray[2], currentBytes);
                    }
                	
                	if ( (hstTimeStamp.containsKey(currentLineArray[4]) && hstReceivedBytes.containsKey(currentLineArray[4])) == false) {
                    	ArrayList<Double> hTimeStamp = new ArrayList<Double>();
                    	ArrayList<Integer> rBytes = new ArrayList<Integer>();
                    	
                    	rBytes.add(new Integer(currentLineArray[7]));
                    	hTimeStamp.add(new Double(currentLineArray[1]));
                    	
                    	hstTimeStamp.put(currentLineArray[4], hTimeStamp);
                    	hstReceivedBytes.put(currentLineArray[4], rBytes);
                    }
                    
                    else {
                    	ArrayList<Double> hCurrent = hstTimeStamp.get(currentLineArray[4]);
                    	ArrayList<Integer> rCurrentBytes = hstReceivedBytes.get(currentLineArray[4]);
                    	
                    	rCurrentBytes.add(new Integer(currentLineArray[7]));
                    	hCurrent.add(new Double(currentLineArray[1]));
                    	
                    	hstTimeStamp.put(currentLineArray[2], hCurrent);
                    	hstReceivedBytes.put(currentLineArray[2], rCurrentBytes);
                    }
                	
                }catch(IndexOutOfBoundsException i) {
                	continue;
                }
            }
            fileScanner.close();
        }catch(FileNotFoundException e){
            System.exit(0);
        }
        
    }
    
    /**
     * This method sorts the IP Address based of the postioning of the their octect in the valid IP Address namespace. 
     * @param ipAddresses : The Set of IP Address to sort ( Destination / Hosts )
     * @return An ArrayList<String> of IP Address that are properly sorted.
     */
    public ArrayList<String> sortIpAddresses(HashSet<String> ipAddresses){
        String temp;
        List<String> ipAdds = new ArrayList<String>(ipAddresses);
        ipAdds.remove("");
        for(int i = 0; i < ipAdds.size(); i++){

            for(int j = 1; j < ipAdds.size(); j++){
                String[] instr1 = ipAdds.get(j - 1).split("\\.");
                String[] instr2 = ipAdds.get(j).split("\\.");
                if( Integer.parseInt(instr1[0]) > Integer.parseInt(instr2[0]) ){
                    temp = ipAdds.get(j - 1);
                    ipAdds.set(j - 1, ipAdds.get(j));
                    ipAdds.set(j, temp);
                }else if( Integer.parseInt(instr1[0]) == Integer.parseInt(instr2[0])
                            && Integer.parseInt(instr1[1]) > Integer.parseInt(instr2[1]) ){
                    temp = ipAdds.get(j - 1);
                    ipAdds.set(j - 1, ipAdds.get(j));
                    ipAdds.set(j, temp);
                }else if ( Integer.parseInt(instr1[0]) == Integer.parseInt(instr2[0])
                            && Integer.parseInt(instr1[1]) == Integer.parseInt(instr2[1])
                            && Integer.parseInt(instr1[2]) > Integer.parseInt(instr2[2]) ){
                    temp = ipAdds.get(j - 1);
                    ipAdds.set(j - 1, ipAdds.get(j));
                    ipAdds.set(j, temp);
                }else if ( Integer.parseInt(instr1[0]) == Integer.parseInt(instr2[0]) 
                            && Integer.parseInt(instr1[1]) == Integer.parseInt(instr2[1]) 
                            && Integer.parseInt(instr1[2]) == Integer.parseInt(instr2[2])
                            && Integer.parseInt(instr1[3]) > Integer.parseInt(instr2[3]) ){
                    temp = ipAdds.get(j - 1);
                    ipAdds.set(j - 1, ipAdds.get(j));
                    ipAdds.set(j, temp);
                }

            }
        }
        return (ArrayList<String>)ipAdds;
    }
    
    /**
     * Getter method to retieve the sorted SourceHosts. 
     * @return : Sorted SourceHosts
     */
    public ArrayList<String> getSourceHosts(){
        return sortIpAddresses(sourceHosts);
    }
    
    /**
     * Getter method to retieve the sorted DestinationHosts.
     * @return Sorted DestinationHosts
     */
    public ArrayList<String> getDestinationHosts(){
        return sortIpAddresses(destinationHosts);
    }
    
    /**
     * Getter method to return the SourceHosts' read timeStamp values from the srcTimeStamp HashMap.
     * @param ipAddress : the key (ip address) for which the timeStamp has to be retrieved.
     * @return ArrayList of the TimeStamp for that IP Address.
     */
    public ArrayList<Double> getSrcTimeStamp(String ipAddress){
		
    	return srcTimeStamp.get(ipAddress);
    }
    
    /**
     * Getter method to return the DestinationHosts' read timeStamp values from the hstTimeStamp HashMap.
     * @param ipAddress : the key (ip address) for which the timeStamp has to be retrieved.
     * @return ArrayList of the TimeStamp for that IP Address.
     */
    public ArrayList<Double> getHstTimeStamp(String ipAddress){
    	
    	return hstTimeStamp.get(ipAddress);
    }
    
    /**
     * Getter method to return the SourceHosts' read Bytes from the srcSentBytes HashMap.
     * @param ipAddress : the key (ip address) for which the SentBytes has to be retrieved.
     * @return ArrayList of the SentBytes for that IP Address.
     */
    public ArrayList<Integer> getSrcSentBytes(String ipAddress){
    	
    	return srcSentBytes.get(ipAddress);
    }
    
    /**
     * Getter method to return the DestinationHosts' read Bytes from the hstReceivedBytes HashMap.
     * @param ipAddress : the key (ip address) for which the ReceivedBytes has to be retrieved.
     * @return ArrayList of the ReceivedBytes for that IP Address.
     */
    public ArrayList<Integer> getHstReceivedBytes(String ipAddress){
    	
    	return hstReceivedBytes.get(ipAddress);
    }
}