import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * This class provides the foundation for the rest of the application by extedning JFrame
 * and build the GUI or the Flow Volume Viewer Application.
 * @author Sanket Patil
 *
 */
public class A2_Main extends JFrame{

    private static final long serialVersionUID = 1L;
    private JMenuBar mBar;
    private JMenu file;
    private JMenuItem oFile, quit;
    private JComboBox<String> ipAddressComboBox;
    private JPanel optionPanel;
    private JRadioButton sHosts, dHosts;
    private ButtonGroup hostsGroup;
	private ArrayList<Double> timeStamp;
	private ArrayList<Integer> bytes;
    private File f;

    private PharseFile fileToPharse = new PharseFile();
    private Graph g = new Graph();

    /**
     * This constructor creates an instance of the GUI Application
     * with the basic components required for minimum
     * functionality.
     */
    public A2_Main(){
        super("Flow Volume Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(1000, 500);
        setVisible(true);
        setupMenu();
        setupOptionPanel();
        setupComboBox();
        this.add(g);
    }

    private void setupMenu(){
        //Create the menu bar.
        mBar = new JMenuBar();
        setJMenuBar(mBar);

        //Build the first menu.
        file = new JMenu("File");
        file.setMnemonic('F');
        file.setFont(new Font("Arial", Font.PLAIN, 18));
        mBar.add(file);

        //Add MenuItems to the file menu.
        oFile = new JMenuItem("Open Trace File");
        quit = new JMenuItem("Quit");

        //Open Trace File menuitem.
        oFile.setFont(new Font("Arial", Font.PLAIN, 18));
        oFile.setMnemonic('O');
        file.add(oFile);
        file.addSeparator();

        //Event handeller for opening the JFileChooser
        oFile.addActionListener(
            new ActionListener(){
            
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser(".");
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
                    fileChooser.addChoosableFileFilter(filter);
                    fileChooser.setFileFilter(filter);
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
                    int returnValue = fileChooser.showDialog(A2_Main.this, "Open Trace File");
                    if (returnValue == JFileChooser.APPROVE_OPTION){
                        f = fileChooser.getSelectedFile();
                        fileToPharse.ReadFile(f);
                        updateComboBox();
                    }   
                }   
            }
        );

        //Exit Application menuitem.
        quit.setFont(new Font("Arial", Font.PLAIN, 18));
        quit.setMnemonic('Q');
        quit.addActionListener(
            new ActionListener(){
            
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            }
        );
        file.add(quit);
    }

    private void setupOptionPanel(){
  	
        optionPanel = new JPanel();
        optionPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.WEST;
        
        hostsGroup = new ButtonGroup();
        
        sHosts = new JRadioButton("Source Hosts", true);
        sHosts.setFont(new Font("Arial", Font.PLAIN, 18));
        sHosts.addActionListener(
                new ActionListener(){
                
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (ipAddressComboBox.isVisible()){
                            updateComboBox();
                        }
                    }
                }
            );
        
        dHosts = new JRadioButton("Destination Hosts");
        dHosts.setFont(new Font("Arial", Font.PLAIN, 18));
        dHosts.addActionListener(
                new ActionListener(){
                
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(ipAddressComboBox.isVisible()){
                            updateComboBox();
                        }
                        
                    }
                }
            );
        
        
        hostsGroup.add(sHosts);
        hostsGroup.add(dHosts);
        
        optionPanel.add(sHosts, c);
        optionPanel.add(dHosts, c);
        optionPanel.setLocation(0, 0);
        optionPanel.setSize(200, 100);
        
        this.add(optionPanel);
        
    }

	private void setupComboBox(){
        ipAddressComboBox = new JComboBox<String>();
        ipAddressComboBox.setLocation(300, 50);
        ipAddressComboBox.setSize(200, 30);
        ipAddressComboBox.setMaximumRowCount(5);
        ipAddressComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        ipAddressComboBox.setVisible(false);
        ipAddressComboBox.addActionListener(
        		new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if (ipAddressComboBox.getSelectedItem() != null) {
								
							String currentIp = (String) ipAddressComboBox.getSelectedItem();
							if ( currentIp.matches("^192\\.168\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])$") ) {
								timeStamp = fileToPharse.getSrcTimeStamp(currentIp);
								bytes = fileToPharse.getSrcSentBytes(currentIp);
								if ( currentIp.matches("^192\\.168\\.0\\.188$") ) {
									timeStamp.clear();
									bytes.clear();
								}
							}
							else {
								timeStamp = fileToPharse.getHstTimeStamp(currentIp);
								bytes = fileToPharse.getHstReceivedBytes(currentIp);
								if ( currentIp.matches("^10\\.0\\.1\\.188$")) {
									timeStamp.clear();
									bytes.clear();
								}
							}
							g.performRescale(timeStamp, bytes, true);
							g.repaint();
						}
						
					}
        	}
        );
        this.add(ipAddressComboBox);
    }

    private void updateComboBox(){
        ipAddressComboBox.setVisible(true);
        ipAddressComboBox.removeAllItems();
        if(sHosts.isSelected()){
            ArrayList<String> sortedSourceHosts = fileToPharse.getSourceHosts();
            for(int i = 0; i < sortedSourceHosts.size(); i++){
                ipAddressComboBox.addItem(sortedSourceHosts.get(i));
            }
            
        }
        if (dHosts.isSelected()){
            ipAddressComboBox.removeAllItems();
            ipAddressComboBox.setVisible(true);
            ArrayList<String> sortedDestinationHosts = fileToPharse.getDestinationHosts();
            for(int i = 0; i < sortedDestinationHosts.size(); i++){
                ipAddressComboBox.addItem(sortedDestinationHosts.get(i));
            }
        }
        
    }
}