package BasketballGame;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.GLCapabilities;

import com.jogamp.opengl.util.Animator;

import javax.imageio.ImageIO;
import javax.swing.*;



public class HelloOpenGL extends Frame {

	public static boolean wPressed, aPressed, sPressed, dPressed, rightPressed, leftPressed, upPressed, downPressed = false;	// Passed to JoglEventListener
	public static boolean firstPress = true;									// Used to check power bar status
	public static JProgressBar powerBar = new JProgressBar();
	static Thread updateThread = new Thread (new updateProgressBar());			// Thread for moving power bar
	static Thread currThread = updateThread;									// Pointers for accessing threads
	static Thread prevThread;													// 
	public static float powerLevel = -1.0f;										// Power level initialized to a negative number so animation doesn't begin
	public static int replaySpeed = 1;
	public static boolean replayModeOn = false;
	
	static JTextField winText;
	
	static Animator anim = null;
	private void setupJOGL(){
	    GLCapabilities caps = new GLCapabilities(null);
	    caps.setDoubleBuffered(true);
	    caps.setHardwareAccelerated(true);
	    
	    GLCanvas canvas = new GLCanvas(caps); 
        add(canvas);

        JoglEventListener jgl = new JoglEventListener();
        canvas.addGLEventListener(jgl); 
        canvas.addKeyListener(jgl); 
        canvas.addMouseListener(jgl);
        canvas.addMouseMotionListener(jgl);

        anim = new Animator(canvas);
        anim.start();

	}
	
    public HelloOpenGL() throws IOException {
    	// Create container for the GUI
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        Container c = frame.getContentPane();
        c.setLayout(new FlowLayout());
        c.setBackground(Color.gray);
        c.setSize(600, 400);
        
        // Create JPanels for the GUI
        JPanel GUIPanel = new JPanel();
        //GUIPanel.setSize(600, 400);
        GUIPanel.setBackground(Color.gray);
        GUIPanel.setLayout(new BorderLayout());
        
        JPanel GUIPanel2 = new JPanel();
        GUIPanel2.setBackground(Color.gray);
        GUIPanel2.setLayout(new BorderLayout());
        
        JPanel GUIPanel3 = new JPanel();
        GUIPanel3.setBackground(Color.gray);
        GUIPanel3.setLayout(new BoxLayout(GUIPanel3, BoxLayout.Y_AXIS));
        
        // Create buttons for the GUI
        addButtons(GUIPanel, GUIPanel2, GUIPanel3);
        
    
        // Create a power bar for the GUI
        GUIPanel2.add(powerBar, BorderLayout.SOUTH);
        
        
        // Add the GUI to the rendering window
        /*c.add(GUIPanel, BorderLayout.WEST);
        c.add(GUIPanel2, BorderLayout.EAST);
        c.add(GUIPanel3, BorderLayout.SOUTH);*/
        c.add(GUIPanel);
        c.add(GUIPanel3);
        c.add(GUIPanel2);
        this.add(c, BorderLayout.SOUTH);
     
        
        // Additional setup for the rendering window
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
        
        setSize(600, 600);
        setLocation(40, 40);

        setVisible(true);

        setupJOGL();
    }
    
    public void addButtons(JPanel panel, JPanel panel2, JPanel panel3) throws IOException {
    	// Up Button ---------------------------------------
    	Image pic1 =ImageIO.read(new File("uparrow.png"));							// Load arrow Image
    	pic1 = pic1.getScaledInstance( 20, 20,  java.awt.Image.SCALE_SMOOTH ) ;		// Rescale Image to desired button size
    	JButton up = new JButton("UP", new ImageIcon(pic1));						// Create new button with loaded image
    	//up.setSize(20,20);
    	up.addMouseListener(new MouseAdapter() {
        	public void mousePressed(MouseEvent m) {
        		wPressed = true;
        		JoglEventListener.setGUIFlags(readGUIFlags());
        	}
        });
    	up.addMouseListener(new MouseAdapter() {
        	public void mouseReleased(MouseEvent m) {
        		wPressed = false;
        		JoglEventListener.setGUIFlags(readGUIFlags());
        	}
        });
    	panel.add(up, BorderLayout.NORTH);											// Add button to appropriate panel in GUI
    	
    	// Down Button ---------------------------------------
    	Image pic2 =ImageIO.read(new File("downarrow.png"));
    	pic2 = pic2.getScaledInstance( 20, 20,  java.awt.Image.SCALE_SMOOTH ) ;
    	JButton down = new JButton("DOWN", new ImageIcon(pic2));
    	down.addMouseListener(new MouseAdapter() {
        	public void mousePressed(MouseEvent m) {
        		sPressed = true;
        		JoglEventListener.setGUIFlags(readGUIFlags());
        	}
        });
    	down.addMouseListener(new MouseAdapter() {
        	public void mouseReleased(MouseEvent m) {
        		sPressed = false;
        		JoglEventListener.setGUIFlags(readGUIFlags());
        	}
        });
        panel.add(down, BorderLayout.SOUTH);
        
        // Left Button ---------------------------------------
        Image pic3 =ImageIO.read(new File("leftarrow.png"));
        pic3 = pic3.getScaledInstance( 20, 20,  java.awt.Image.SCALE_SMOOTH ) ;
        JButton lookLeft = new JButton("LEFT", new ImageIcon(pic3));
        lookLeft.addMouseListener(new MouseAdapter() {
        	public void mousePressed(MouseEvent m) {
        		aPressed = true;
        		JoglEventListener.setGUIFlags(readGUIFlags());
        	}
        });
        lookLeft.addMouseListener(new MouseAdapter() {
        	public void mouseReleased(MouseEvent m) {
        		aPressed = false;
        		JoglEventListener.setGUIFlags(readGUIFlags());
        	}
        });
        panel.add(lookLeft, BorderLayout.WEST);
        
        // Right Button ---------------------------------------
        Image pic4 =ImageIO.read(new File("rightarrow.png"));
        pic4 = pic4.getScaledInstance( 20, 20,  java.awt.Image.SCALE_SMOOTH ) ;
        JButton lookRight = new JButton("RIGHT", new ImageIcon(pic4));
        lookRight.addMouseListener(new MouseAdapter() {
        	public void mousePressed(MouseEvent m) {
        		dPressed = true;
        		JoglEventListener.setGUIFlags(readGUIFlags());
        	}
        });
        lookRight.addMouseListener(new MouseAdapter() {
        	public void mouseReleased(MouseEvent m) {
        		dPressed = false;
        		JoglEventListener.setGUIFlags(readGUIFlags());
        	}
        });
        panel.add(lookRight, BorderLayout.EAST);
        
        // Move Left Button ---------------------------------------
        JButton left = new JButton("MOVE LEFT", new ImageIcon(pic3));
        left.addMouseListener(new MouseAdapter() {
        	public void mousePressed(MouseEvent m) {
        		leftPressed = true;
        		JoglEventListener.setGUIFlags(readGUIFlags());
        	}
        });
        left.addMouseListener(new MouseAdapter() {
        	public void mouseReleased(MouseEvent m) {
        		leftPressed = false;
        		JoglEventListener.setGUIFlags(readGUIFlags());
        	}
        });
        panel2.add(left, BorderLayout.WEST);
        
        // Move Right Button ---------------------------------------
        JButton right = new JButton("MOVE RIGHT", new ImageIcon(pic4));
        right.addMouseListener(new MouseAdapter() {
        	public void mousePressed(MouseEvent m) {
        		rightPressed = true;
        		JoglEventListener.setGUIFlags(readGUIFlags());
        	}
        });
        right.addMouseListener(new MouseAdapter() {
        	public void mouseReleased(MouseEvent m) {
        		rightPressed = false;
        		JoglEventListener.setGUIFlags(readGUIFlags());
        	}
        });
        panel2.add(right, BorderLayout.EAST);
        
        // Replay Button -------------------------------------
        JButton replay = new JButton("REPLAY");
        replay.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent m) {
        		//TODO
        		if (replayModeOn) {
        			replayModeOn = false;
        		}
        		else {
        			replayModeOn = true;
        		}
        	}
        });
        panel2.add(replay, BorderLayout.NORTH);
        
        // Replay Speed Radio Buttons --------------------------------
        ButtonGroup group = new ButtonGroup();
        JRadioButton fullSpeed = new JRadioButton("Full Speed");
        fullSpeed.setSelected(true);
        fullSpeed.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		replaySpeed = 1;
        	}
        });
        group.add(fullSpeed);
        panel3.add(fullSpeed);
        
        JRadioButton quarterSpeed = new JRadioButton("1/4 Speed");
        quarterSpeed.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		replaySpeed = 4;
        	}
        });
        group.add(quarterSpeed);
        panel3.add(quarterSpeed);
        
        JRadioButton tenthSpeed = new JRadioButton("1/10 Speed");
        tenthSpeed.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		replaySpeed = 10;
        	}
        });
        group.add(tenthSpeed);
        panel3.add(tenthSpeed);
        
        winText = new JTextField();
        winText.setEditable(false);
        panel3.add(winText);
    }
    
    public static boolean getReplayOn() {
    	return replayModeOn;
    }
    
    // Get the replay speed from the GUI radio buttons
    public static int getReplaySpeed() {
    	return replaySpeed;
    }
    
    // Called from JoglEventListener to begin modifying and displaying the power bar
    public static void modifyPowerBar() {
    	if (firstPress == true) {					// Start moving the power bar via a separate thread
			firstPress = false;
			HelloOpenGL.setPowerLevel(-1.0f);		// Power level set to negative number so animation doesn't begin		
			prevThread = currThread;
			currThread = new Thread (new updateProgressBar());
			currThread.start();
    	}
		else {								// Throw an interrupt to the separate thread to stop the power bar and save the current value
			firstPress = true;
			currThread.interrupt();
		}
    }
    
    
    // Sets power level value to be accessed from JoglEventListener
    public static void setPowerLevel(float power) {
    	powerLevel = power;
    }
    
    
    // Accessor method for power level
    public static float getPowerLevel() {
    	return powerLevel;
    }
    
    
    // Updates the visual power bar in the GUI
    public static void updatePowerBar(int value) {
    	powerBar.setValue(value);
    }
    
    
    // Used to provide look around and movement via the GUI buttons to the JoglEventListener class
    public static boolean[] readGUIFlags() {
    	boolean[] flagArray = {false, false, false, false, false, false};
    	if (wPressed)
    		flagArray[0] = true;
    	if (aPressed)
    		flagArray[1] = true;
    	if (sPressed)
    		flagArray[2] = true;
    	if (dPressed)
    		flagArray[3] = true;
    	if (leftPressed)
    		flagArray[4] = true;
    	if (rightPressed)
    		flagArray[5] = true;
    	
    	return flagArray;
    }
    
    public static void winText(){
    	winText.setText("Score!");
    	winText.setBackground(new Color(0.2f, 1.0f, 0.2f));
    }
    
    public static void missText(){
    	winText.setText("Miss!");
    	winText.setBackground(new Color(1.0f, 0.0f, 0.0f));
    }

    public static void main(String[] args) throws IOException {
        HelloOpenGL demo = new HelloOpenGL();
        
        JoglEventListener.setGUIFlags(readGUIFlags());

        demo.setVisible(true);
    }
    
}


// Separate thread to update the progress bar in the GUI and save the value once it's been interrupted
class updateProgressBar implements Runnable {
	@Override
	public void run() {
		int progress = 0;
    	while (progress < 100) {
            //Sleep for 10 milliseconds
            try {
                Thread.sleep(10);
                progress++;
                HelloOpenGL.updatePowerBar(Math.min(progress, 100));
            } catch (InterruptedException ignore) {		// Triggers when space bar has been pressed a second time
            	float power = (float) progress / 100f;
            	HelloOpenGL.setPowerLevel(power);		// Save current power level
               	break;
            }
        }
    	if (progress == 100) {
    		while (progress > 0) {							// Counts down from 100 after the power bar has reached the max
    			try {
    				Thread.sleep(10);
    				progress--;
    				HelloOpenGL.updatePowerBar(Math.min(progress, 100));
    			} catch (InterruptedException ignore) {
    				float power = (float) progress / 100f;
            		HelloOpenGL.setPowerLevel(power);
               		break;
    			}
    		}
    	}
    	if (progress == 0)
    		HelloOpenGL.setPowerLevel(0.0f);	// If both while loops fully execute, the power is set to 0
	}
}

class MyWin extends WindowAdapter {
	 public void windowClosing(WindowEvent e)
   {
       System.exit(0);
   }
}