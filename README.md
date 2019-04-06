# FlowViewerApplication

This piece of work was on a real life research problem. Our network lab(at the university) hosts the Auckland Satellite Simulator (http://sde.blogs.auckland.ac.nz/). We use it to simulate Internet traffic to small Pacific islands that are connected to the rest of the world via a satellite link.  
To do this, the simulator has a number of machines (“source hosts”) on the “world side” of the simulated satellite link that transmit data in the form of small chunks of up to 1500 bytes called packets. These packets travel via a simulated satellite link to the “island side”. Once on the island side, each packet ends up at a machine there. These machines are called “destination hosts”.  

The simulated satellite link delays packets and occasionally throws some away when there are more packets arriving that it can deal with at the moment. When the link throws packets away, the source hosts respond by sending less data for a while before attempting to send more again. On the island side of the satellite link, our simulator eavesdrops on the incoming packets. It stores summary information about each packet in a trace file. 

The trace file is plain text and each line contains the record for exactly one packet (more on the file format in the next section). What we would like to be able to do is get a graphical display of how much data comes from a particular source host over time, or how much data goes to a particular destination host over the course of an experiment. 

Experiments typically take between 90 seconds and about 11 minutes. This application dsiplays that information in a graphical way.
