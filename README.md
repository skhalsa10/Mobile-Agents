# Project: Mobile Agents
## Student(s):  Michelle Louie, Siri Khalsa

## Introduction
This application solves the Mobile Agents spec provided as a project 4 in cs 351 at UNM

## Contributions
This is a group project. We both worked very closely togethor on design decisions. Michelle primarily worked on most of the simulation related classes.
really complex classes like Node, Agent, Forrest and most other simulation related. Siri worked mostly on the GUI. Most classes in the Graphics folder.
and primarily the GUI class. We both worked on the Messages.

## Usage

It is very easy to use this application.
- Entry Point: Main Class
- JDK Version: 10

#### Starting the App
Change the directory of your terminal or command prompt  to the directory where you downloaded the jar to.

#####When you download the jar make sure you download the jar from the master branch on csgit

Type `java -jar MobileAgents_mllouie_skhalsa10.jar ./config-file-path.txt` this will start
the application in a mode that is least graphics and resource intensive. So if your machine is old or has low specs run this mode

Type `java -jar MobileAgents_mllouie_skhalsa10.jar ./config-file-path.txt fire` this will launch the application with cool bad ass fire animation
this does take more resources to process especially in a big file where there are tons of nodes on fire due to the amount of particles in the fire. 
We have compromised and made the visual effects of the fire still on the low end in the amount of particles generated per fire.

The application makes a base station log file that gets created on the OS in the same directory as the .jar
It is called MobileAgents.log

#### GUI
The GUI is really simple but there is some things that can be explained here that are not apparently 
obvious immediately.

The GUI is split up into three main panes. 
1. Pane 1 is the biggest pane and it renders a gui representation of the graph. it is tucked into a scroll pane so that we can accept
big config files. You can interract with it by scrolling or panning(with a finger or mouse).
2. Pane 2 is a pane that displays textual information about what is happening. the text is colored for different types of changes

    - White - this text symbolizes messages received from the simulation log.
    - Yellow - this text symbolizes a node that has burst into flame and the surrounding nodes that are now near fire.
    - Green - this text symbolizes Agent related state. either movement or when a new agent is created.
    - Red - this text symbolizes when an Agent dies.
3. Pane 3 contains all the buttons. the S- and S+ button will speed up or slow down the simulation playback.
the Play/Pause button will either play the animation or pause it. and the Z- and Z+ will either Zoom in or Zoom out the contents in Pane 1.

## Project Assumptions
1. We assume that we can only have one base station and one node on fire to start the sim.
2. If a config file contains multiple bases and fire we pick the last occurrence of each and ignore the rest.
3. If the config file has duplicate nodes or edges it will write out an error and exit the application.
4. A node line in the config file must have the word "node" followed by two integers separated by spaces.
5. An initial fire and base station line must have the word "fire" and "station," respectively followed by two integers separated by spaces.
6. Edge lines must have the word "edge" followed by 4 integers separated by spaces.
7. Edges, the initial fire, and base station must be a valid node location.
8. If any of the above conditions (4-7) are not met, an error will printed out and the program will exit. 
9. Although the assignment states that the sensor network is represented as a planar graph, 
we DO NOT check to see if it is a valid planar graph and we wil accept a graph with edges that cross the path of 
other edges. The simulation still functions correctly.

## Versions 
The jar file can be found where the readme is at the root of the project structure.

There is really only one version. The version has two modes. one renders the fire as red circles.
The other renders the circle as a flame animation.

RED DOT mode:
java -jar MobileAgents_mllouie_skhalsa10.jar ./config-file-path.txt

Running an extremely large graph this needed 300 MB of memory to run

Fire animation mode:
java -jar MobileAgents_mllouie_skhalsa10.jar ./config-file-path.txt fire

Running an extremely large graph this needed half a GB of memory to run using fire animation.


## Docs
The design diagram and document are located in the docs directory.
 - Note: When the concurrency bug in Known Issues #1 was fixed, the state was changed from synchronized to unsynchronized. 
Messages were used to change the state of a node with the start fire timer. The design diagram and document do not reflect these changes. 

Test files can be found in the resources directory
 

## Status
### Known Issues
1. There is a bad concurrency bug. that ONLY happens sometimes. It happens more often than not if you run
the program with the big_big_graph.txt will showcase the bug more often than not. It sometimes does not send a message to the GUI
that the node is on fire. This can be seen in the GUI as the FIRE never spreading to some nodes. or tons of agents in places they shouldn't be.THIS HAS BEEN FIXED
2. When reading in the config file, extra white spaces could lead to an invalid config file. 
3. The GUI has a bug that if you press pause on the animation and then change the speed it will start processing the state
and when you unpause it will jump ahead in the animation. - THIS HAS BEEN FIXED
4. The GUI has a bug that if you pause animation and then change the zoom. it does not render the zoom until you press play again THIS HAS BEEN FIXED
5. There is a bug when running this application on an EXTREMELY Large graph. we used giant-close-box.txt config file.
We get a stackoverflow exception during the agents walk. this kills the agent. the simulation continues without an agent.