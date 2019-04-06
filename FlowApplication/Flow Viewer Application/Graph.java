import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * This class creates a JPanel as a drawing board, for the plotting of the graph.
 * The graph has a x and a y-axis, wherein the x-axis represents the Time[s] and y-axis represents the Volume[bytes].
 * @author Sanket Patil
 *
 */
public class Graph extends JPanel{

    private static final long serialVersionUID = 1L;
    private final int xAxisHatchMarkDistance = 56;
    
    private boolean repaint = false;
    private int x_axisNumberOfTicks = 0;
    private int y_axisNumberOfTicks = 5;
    private ArrayList<String> yAxisLabel = new ArrayList<String>(); 
    private ArrayList<Double> rescaledTimeStamp = new ArrayList<Double>();
    private ArrayList<Double> rescaledBytes	= new ArrayList<Double>();
    
    /**
     * The Graph constructor creates a basic JPanel with a certain Size, Location and BackGround.
     */
    public Graph(){
        this.setSize(1000, 375);
        this.setLocation(0, 100);
        this.setBackground(Color.GRAY);
        
    }
    
    /**
     * The performRescale method rescales the data gathered from reading the tracefile and aids in plotting of the graph.
     * @param timeStamp : The actual timeStamp that needs to be rescaled according to the length of the x-axis for each 2 sencond interval.
     * @param bytes : The actual bytes that need to be rescaled and computed for each 2 second intervals.
     * @param repaintBool : A flag that indicates whether the graph that needs to be drawn is standard one or the one that should show data.
     */
    public void performRescale(ArrayList<Double> timeStamp, ArrayList<Integer> bytes, boolean repaintBool) {
    	repaint = repaintBool;
    	ComputeData data = new ComputeData(timeStamp, bytes);
    	data.computeTotalBytes();
    	
    	ArrayList<Integer> computedBytes = data.getComputedBytes();
    	double time = data.getMaxTime();
    	int maxByte = data.getMaxByte();
    	ArrayList<Double> computedTimeStamp = data.getComputedTimeStamp();
    	
    	rescaleXAxis(time);
    	rescaleYAxis(maxByte);
    	rescaleDataItems(computedTimeStamp, computedBytes, time, maxByte);
    }
    
    private void rescaleXAxis(double time) {
    	double maxTime = time;
    	int largestTick = (int) Math.ceil(maxTime);
    	if ( largestTick > 650 ) {
    		largestTick = 700;
    	}
    	else {
    		largestTick = 650;
    	}
    	x_axisNumberOfTicks = largestTick / 50;
    }
    
    private void rescaleYAxis(int maxByte) {
    	yAxisLabel.clear();
    	if ( maxByte > 0 ) {
    		double binSize = maxByte / 5.0;
        	if ( binSize < 100.0 ) {
        		yAxisLabel.add("0");
        		int finalBinSize = (int)Math.ceil(binSize);
        		int temp = finalBinSize;
        		for ( int i = 1; i <= y_axisNumberOfTicks; i++) {
        			yAxisLabel.add(Integer.toString(finalBinSize));
        			finalBinSize += temp;
        		}
        	}
        	else {
        		binSize = binSize / 1000;
            	int finalBinSize = (int) Math.ceil(binSize) * 1000;
            	int numberOfDigits = String.valueOf(finalBinSize).length();
            	int temp = finalBinSize;
            	yAxisLabel.add("0");
            	for ( int i = 1; i <= y_axisNumberOfTicks; i++) {
            		if ( String.valueOf(finalBinSize).length() < numberOfDigits + 1 ) {
            			String label = Integer.toString(finalBinSize / 1000) + "k";
            			yAxisLabel.add(label);
            			finalBinSize += temp;
            		}
            		else {
            			DecimalFormat df = new DecimalFormat("#.#");
            			String label = df.format(finalBinSize / 1000000.0) + "M";
            			yAxisLabel.add(label);
            			finalBinSize += temp;
            		}
            	}
        	}
    	}
    }
    
    private void rescaleDataItems(ArrayList<Double> timeStamp, ArrayList<Integer>computedBytes, double maxTime, int maxByte) {
    	
    	rescaledBytes.clear();
    	rescaledTimeStamp.clear();
    	for ( int t = 0; t < timeStamp.size(); t++) {
    		double newTime = ( ( timeStamp.get(t) * 840 ) / maxTime ) + 60;
    		double newByte = ( computedBytes.get(t) * 220 ) / (double) maxByte;
    		rescaledTimeStamp.add(newTime);
    		rescaledBytes.add(newByte);
    	}
    	
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if ( repaint && yAxisLabel.size() > 0) {
        	Graphics2D g2 = (Graphics2D)g;
            g2.setColor(Color.WHITE);
            g2.drawString("Volume[Bytes]", 10, 15);
            g2.drawString("Time[s]", 450, 300);
            
            //Draws the y-axis.
            g2.drawLine(60, 20, 60, 250);
            //Draws the x-axis.
            g2.drawLine(60, 250, 900, 250);
            
            //Draws the rescaled labels and hatch marks for the x-axis.
            int labelNo = 0;
            for(int start = 60; start <= 900; start += 840 / x_axisNumberOfTicks){
                String label = Integer.toString(labelNo++ * 50);
                g2.drawLine(start, 250, start, 255);
                g2.drawString(label, start-10, 275);
            }
            
            int counter = 0;
            for ( int i = 250; i >= 30; i-= 220 / y_axisNumberOfTicks) {
            	g2.drawLine(60, i, 55, i);
            	if ( yAxisLabel.get(counter) == "0") {
            		g2.drawString(yAxisLabel.get(counter), 30, i + 5);
            		counter++;
            	}
            	else {
                	g2.drawString(yAxisLabel.get(counter), 20, i+5);
                	counter++;
            	}

            }
            
            g2.setColor(Color.CYAN);
            
            for ( int j = 0; j < rescaledBytes.size() - 1; j++) {
            	Rectangle2D volume = new Rectangle2D.Double(rescaledTimeStamp.get(j), 250 - rescaledBytes.get(j), 2, rescaledBytes.get(j));
            	g2.draw(volume);
            }
        }
        else {
            Graphics2D g2 = (Graphics2D)g;
            g2.setColor(Color.WHITE);
            g2.drawString("Volume[Bytes]", 10, 15);
            g2.drawString("Time[s]", 450, 300);

            //Draws the y-axis.
            g2.drawLine(60, 20, 60, 250);
            //Draws the x-axis.
            g2.drawLine(60, 250, 900, 250);

            //Draws the initial labels and hatch marks for the x-axis.
            int labelNo = 0;
            for(int start = 60; start <= 900; start+=xAxisHatchMarkDistance){
                String label = Integer.toString(labelNo++ * 50);
                g2.drawLine(start, 250, start, 255);
                g2.drawString(label, start-10, 275);
            }

            //Draws the initial hatch marks for the y-axis.
            g2.drawLine(60, 250, 55, 250);
        }   
    }
}
