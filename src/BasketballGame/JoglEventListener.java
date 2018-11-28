package BasketballGame;



import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileReader;
import java.nio.*;
import java.util.Timer;
import java.util.Vector;

import java.nio.IntBuffer;
import java.nio.ByteBuffer;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.*;



public class JoglEventListener implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {
	 
	float backrgb[] = new float[4]; 
	float rot; 
	Texture mytex1 = null, mytex2 = null, mytex3 = null, mytex4 = null, mytex5 = null; 
	int texID1, texID2, texID3, texID4, texID5;
	
	int windowWidth, windowHeight;
	float orthoX=40;

	int mouseX0, mouseY0;
	float picked_x = 0.0f, picked_y = 0.0f;
	
	public static boolean wPressed, aPressed, sPressed, dPressed, rightPressed, leftPressed, upPressed, downPressed = false;
	float moveX = 0; float moveZ = 0;
	float xRotation = 0, yRotation = 90;
	float lookX = 0, lookY = 0, lookZ;
	float alpha, beta;
	
	float forward = 0, Strafe = 0;
	
	float YMomentum = 0.0f;
	boolean GravitySwitch = false;
	
	float time = 0.0f;
	float yVelocity = 0.0f;
	float xVelocity = 0.0f;
	float zVelocity = 0.0f;
	float yPosition = 3.0f;
	float xPosition = 0.0f;
	float zPosition = 2.0f;
	
	float xLow = -4.0f;
	float xHigh = 4.0f;
	float yLow = 10.0f;
	float yHigh = 12.0f;
	float zLow = 38.0f;
	float zHigh = 42.0f;
	
	float yRotHold = -10000.0f;
	float xRotHold;
	
	float xPosStore, zPosStore;
	float xRotStore, yRotStore;
	float replayTimer = 0.0f;
	float powerStore;
	boolean replaySwitch = false;
	float replaySpeed = 1.0f;
	float replaySpeedStore;
	
	float power;
	
	// Shaders
	private int vertexShaderProgram;
	private int fragmentShaderProgram;
	private int shaderprogram;
	
	// Lighting variables
	float white []    = { 1.0f, 1.0f, 1.0f };
	float black []	  = { 0.0f, 0.0f, 0.0f };
	float gray []     = { 0.7f, 0.7f, 0.7f };
	float darkGray [] = { 0.4f, 0.4f, 0.4f };
	float orange []	  =	{ 1.0f, 0.55f, 0.0f };
	float mShininess[]      = { 90 }; //set the shininess of the material
	float noSpecular[]		= {0};	//Shininess of 0 for no specular reflections
	
	float specularLight[] = {1.0f, 1.0f, 1.0f};
	float noSpecularLight[] = {0.0f, 0.0f, 0.0f};
	float ambientLight[] = 		{ 0.2f, 0.2f, 0.2f };
	float diffuseLight[] =		{ 0.9f, 0.9f, 0.9f };
	
	
	float[] vertArr = new float[] {100.0f, 100.0f, 100.0f,
								   -100.0f, 100.0f, 100.0f,
								   -100.0f, -100.0f, 100.0f,
								   100.0f, -100.0f, 100.0f,
								   
								   100.0f, 100.0f, 100.0f,
								   -100.0f, 100.0f, 100.0f,
								   -100.0f, 100.0f, -100.0f,
								   100.0f, 100.0f, -100.0f,
								   
								   100.0f, 100.0f, 100.0f,
								   100.0f, -100.0f, 100.0f,
								   100.0f, -100.0f, -100.0f,
								   100.0f, 100.0f, -100.0f,
								   
								   -100.0f, -100.0f, -100.0f,
								   100.0f, -100.0f, -100.0f,
								   100.0f, 100.0f, -100.0f,
								   -100.0f, 100.0f, -100.0f,
								   
								   -100.0f, -100.0f, -100.0f,
								   100.0f, -100.0f, -100.0f,
								   100.0f, -100.0f, 100.0f,
								   -100.0f, -100.0f, 100.0f,
								   
								   -100.0f, -100.0f, -100.0f,
								   -100.0f, 100.0f, -100.0f,
								   -100.0f, 100.0f, 100.0f,
								   -100.0f, -100.0f, 100.0f
								   };
	
	FloatBuffer vertArrBuff = Buffers.newDirectFloatBuffer(vertArr);
	
	float[] texCoordArr = new float[] {1/2f, 2/3f,
									   1/4f, 2/3f,
									   1/4f, 1/3f,
									   1/2f, 1/3f,
									   
									   1/2f, 2/3f,
									   1/4f, 2/3f,
									   1/4f, 1f,
									   1/2f, 1f,
									   
									   1/2f, 2/3f,
									   1/2f, 1/3f,
									   3/4f, 1/3f,
									   3/4f, 2/3f,
									   
									   1f, 1/3f,
									   3/4f, 1/3f,
									   3/4f, 2/3f,
									   1f, 2/3f,
									   
									   1/4f, 0f,
									   1/2f, 0f,
									   1/2f, 1/3f,
									   1/4f, 1/3f,
									   
									   0f, 1/3f,
									   0f, 2/3f,
									   1/4f, 2/3f,
									   1/4f, 1/3f
									   };
	
	FloatBuffer texCoordArrBuff = Buffers.newDirectFloatBuffer(texCoordArr);

	/*int indexArr[] = new int[] {0, 1, 4, 2,
								0, 3, 6, 2,
								0, 1, 5, 3,
								7, 6, 3, 5,
								7, 6, 2, 4,
								7, 5, 1, 4};
	
	IntBuffer indexArrBuff = Buffers.newDirectIntBuffer(indexArr);*/
	
	float colorArr[] = new float[] {1.0f, 0.0f, 0.0f,
									0.0f, 1.0f, 0.0f,
									0.0f, 0.0f, 1.0f,
									1.0f, 0.0f, 0.0f,
									0.0f, 1.0f, 0.0f,
									0.0f, 0.0f, 1.0f,
									1.0f, 0.0f, 0.0f,
									0.0f, 1.0f, 0.0f,
									0.0f, 0.0f, 1.0f,
									1.0f, 0.0f, 0.0f,
									0.0f, 1.0f, 0.0f,
									0.0f, 0.0f, 1.0f,
									1.0f, 0.0f, 0.0f,
									0.0f, 1.0f, 0.0f,
									0.0f, 0.0f, 1.0f,
									1.0f, 0.0f, 0.0f,
									0.0f, 1.0f, 0.0f,
									0.0f, 0.0f, 1.0f,
									1.0f, 0.0f, 0.0f,
									0.0f, 1.0f, 0.0f,
									0.0f, 0.0f, 1.0f,
									1.0f, 0.0f, 0.0f,
									0.0f, 1.0f, 0.0f,
									0.0f, 0.0f, 1.0f
									};
	
	FloatBuffer colorArrBuff = Buffers.newDirectFloatBuffer(colorArr);
    
	private GLU glu = new GLU();
	
	GLUquadric qOBJ, qOBJ1, qOBJ2;
	
	private GLUT glut = new GLUT();

	 public void displayChanged(GLAutoDrawable gLDrawable, 
	            boolean modeChanged, boolean deviceChanged) {
	    }
	 	
	 private void attachShaders(GL2 gl) throws Exception {
		 vertexShaderProgram = gl.glCreateShader(GL2.GL_VERTEX_SHADER);
	     fragmentShaderProgram = gl.glCreateShader(GL2.GL_FRAGMENT_SHADER);

	        
	     String[] vsrc = loadShaderSrc("lighting.vert");
	  	 gl.glShaderSource(vertexShaderProgram, 1, vsrc, null, 0);
	  	 gl.glCompileShader(vertexShaderProgram);


	  	  
	  	 String[] fsrc = loadShaderSrc("lighting.frag");
	  	 gl.glShaderSource(fragmentShaderProgram, 1, fsrc, null, 0);
	  	 gl.glCompileShader(fragmentShaderProgram);

	     shaderprogram = gl.glCreateProgram();
	     gl.glAttachShader(shaderprogram, vertexShaderProgram);
	     gl.glAttachShader(shaderprogram, fragmentShaderProgram);
	     gl.glLinkProgram(shaderprogram);
	     gl.glValidateProgram(shaderprogram);
	     IntBuffer intBuffer = IntBuffer.allocate(1);
	     gl.glGetProgramiv(shaderprogram, GL2.GL_LINK_STATUS,intBuffer);
	     if (intBuffer.get(0)!=1){
	        gl.glGetProgramiv(shaderprogram, GL2.GL_INFO_LOG_LENGTH,intBuffer);
	        int size = intBuffer.get(0);
	        System.err.println("Program link error: ");
	        if (size>0){
	           ByteBuffer byteBuffer = ByteBuffer.allocate(size);
	           gl.glGetProgramInfoLog(shaderprogram, size, intBuffer, byteBuffer);
	           for (byte b:byteBuffer.array()){
	              System.err.print((char)b);
	           }
	        } else {
	           System.out.println("Unknown");
	        }
	        System.exit(1);
	     }
	     gl.glUseProgram(shaderprogram);
	  }
	 
	 private String[] loadShaderSrc(String name){
	        StringBuilder sb = new StringBuilder();
	        try{
	           InputStream is = JoglEventListener.class.getClassLoader().getResourceAsStream(name);
	           InputStreamReader isr = new InputStreamReader(is);
	           BufferedReader br = new BufferedReader(isr);
	           String line;
	           while ((line = br.readLine())!=null){
	              sb.append(line);
	              sb.append('\n');
	           }
	           is.close();
	        }
	        catch (Exception e){
	        	System.out.println("Exception in loadShaderSrc");
	           e.printStackTrace();
	        }
	      //  System.out.println("Shader is "+sb.toString());
	        return new String[]{sb.toString()};
	     }
	 


	 public void init(GLAutoDrawable gLDrawable) {
	        GL2 gl = gLDrawable.getGL().getGL2();
	        //gl.glShadeModel(GL.GL_LINE_SMOOTH);              // Enable Smooth Shading
	        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);    // Black Background
	        gl.glClearDepth(1.0f);                      // Depth Buffer Setup
	        gl.glEnable(GL.GL_DEPTH_TEST);              // Enables Depth Testing
	        gl.glDepthFunc(GL.GL_LEQUAL);               // The Type Of Depth Testing To Do
	        
	      //Lighting setup
	  		float lpos[] = {-5.0f, 10.0f, -10.0f, 0.0f};
	  		
	  		//NOTE- Specular, Ambient, and Diffuse Lighting parameters are set as a global
	  			
	  		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);
	  		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0);
	  		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lpos, 0);
	  		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specularLight, 0);

	        
	        
	        
	        // load the textures;
	        try {
	        	 //mytex = TextureIO.newTexture(new File("C:/Users/markn/Desktop/skybox_texture.jpg"), false);
	        	 mytex1 = TextureIO.newTexture(new File("./skybox.jpg"), false);
	        	 mytex2 = TextureIO.newTexture(new File("./balldimpled.png"), false);
	        	 mytex3 = TextureIO.newTexture(new File("./court.png"), false);
	        	 mytex4 = TextureIO.newTexture(new File("./BB_Board.jpg"), false);
	        	 mytex5 = TextureIO.newTexture(new File("./wall.jpg"), false);
	        	 texID1 = mytex1.getTextureObject();
	        	 texID2 = mytex2.getTextureObject();
	        	 texID3 = mytex3.getTextureObject();
	        	 texID4 = mytex4.getTextureObject();
	        	 texID5 = mytex5.getTextureObject();
	        	 
	        	 gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);  
	             
	             
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	        
	        // Attach the shaders
	        try {
		         attachShaders(gl);
		      } catch (Exception e) {
		         e.printStackTrace();
		    }
	       
	    }


	    
	    public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, 
	            int height) {
	        final GL2 gl = gLDrawable.getGL().getGL2();
	        windowWidth = width;
	        windowHeight = height;
	        if (height <= 0) // avoid a divide by zero error!
	            height = 1;
	        final float h = (float) width / (float) height;
	        gl.glViewport(width/2*0, 0, width, height);
	        gl.glMatrixMode(GL2.GL_PROJECTION);
	        gl.glLoadIdentity();
	        glu.gluPerspective(45.0f, h, 0.1, 300.0);
	        
	        gl.glMatrixMode(GL2.GL_MODELVIEW);
	        
	        gl.glLoadIdentity();
	     
	        /*glu.gluLookAt(0, 0, -75,
	        			  0, 0, -1,
	        			  0, 1, 0);*/
	    }

		@Override
		public void display(GLAutoDrawable gLDrawable) {
			final GL2 gl = gLDrawable.getGL().getGL2();
			gl.glClearColor(0, 0, 1, 1);
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
	        gl.glLoadIdentity();
	        
	        replaySwitch = HelloOpenGL.getReplayOn();
	        
	        if( replaySwitch ){
	        	replayTimer += 1.0f;
	        	
	        	replaySpeed = HelloOpenGL.getReplaySpeed();
	        	if(replaySpeed != replaySpeedStore){
	        		replayTimer = 0.0f;
	        	}
	        	
	        	
	        	replaySpeedStore = replaySpeed;
	        	checkKeys();
		        glu.gluLookAt(30.0f, 20.0f, -30.0f, 5000 * Math.sin(Math.toRadians(yRotation)) * Math.sin(Math.toRadians(xRotation)),
						5000 * Math.cos(Math.toRadians(yRotation)),
						5000 * Math.sin(Math.toRadians(yRotation)) * Math.cos(Math.toRadians(xRotation)),
						0, 1, 0);
		        
		        DrawSkybox(texID1, gl);
	        	
				yVelocity = (float) (50.0f * powerStore - 9.8f * replayTimer/(15.0f * replaySpeed));
						
				yPosition = (float) (3.0f + (Math.cos(Math.toRadians(yRotStore))*((yVelocity + 10.0f)/2.0f) * replayTimer/(15.0f * replaySpeed)));
						
				xVelocity = (float)(30.0f * powerStore - 1.0f*replayTimer/(15.0f * replaySpeed));
						
				xPosition = (float) (0.0f +  (Math.sin(Math.toRadians(yRotStore)) * Math.sin(Math.toRadians(xRotStore)) *((xVelocity + 20.0f)/2.0f) * replayTimer/(15.0f * replaySpeed)));
						
				zVelocity = (float)(30.0f * powerStore - 1.0f*replayTimer/(15.0f * replaySpeed));
				 
				zPosition = (float) (2.0f + (Math.sin(Math.toRadians(yRotStore)) * Math.cos(Math.toRadians(xRotStore)) * ((zVelocity + 20.0f)/2.0f) * replayTimer/(15.0f * replaySpeed)));
					
						if( xPosition <= xHigh && xPosition >= xLow){
							if( zPosition <= zHigh && zPosition >= zLow){
								if( yPosition <= yHigh && yPosition >= yLow){
										replayTimer = 0.0f;
								}
							}
						}

		        
		        gl.glPushMatrix();
		        gl.glTranslatef(xPosition, yPosition, zPosition);
		        DrawBall(qOBJ, texID2, gl);
		        gl.glPopMatrix();
		        

		        
		        gl.glTranslatef(-xPosStore, 0.0f, zPosStore);
		        
		        xLow = -4.0f - xPosStore;
		        xHigh = 4.0f - xPosStore;
		        
		        zLow = 38.0f + zPosStore;
		        zHigh = 42.0f + zPosStore;
		        
		        DrawCourt(texID2, gl);
		        
		        DrawWalls(texID5, gl);
		        
		        DrawGoal(qOBJ1, qOBJ2, texID4, gl);
			     
	        } else {
	        
	        // Check for keyboard presses
	        checkKeys();
	        
	        glu.gluLookAt(0, 5, 0, 5000 * Math.sin(Math.toRadians(yRotation)) * Math.sin(Math.toRadians(xRotation)),
					5000 * Math.cos(Math.toRadians(yRotation)),
					5000 * Math.sin(Math.toRadians(yRotation)) * Math.cos(Math.toRadians(xRotation)),
					0, 1, 0);
	        
	        
	        DrawSkybox(texID1, gl);
        	power = HelloOpenGL.getPowerLevel();
        	if(power >= 0.0f){
        		GravitySwitch = true;
        	}
	        if(GravitySwitch == true){

	        	 	if(yRotHold == -10000.0f){
		        		xRotHold = xRotation;
		        		yRotHold = yRotation;
		        	}
					time += 1.0f;
					yVelocity = (float) (50.0f * power - 9.8f * time/15.0f);
					//
					if(yPosition <= 1.0f){
						HelloOpenGL.missText();
						GravitySwitch = false;
						HelloOpenGL.setPowerLevel(-1.0f);
						time = 0.0f;
						yVelocity = 0.0f;
						xVelocity = 0.0f;
						zVelocity = 0.0f;
						yPosition = 3.0f;
						xPosition = 0.0f;
						zPosition = 2.0f;
						yRotHold = -10000.0f;
					}
					yPosition = (float) (3.0f + (Math.cos(Math.toRadians(yRotHold))*((yVelocity + 10.0f)/2.0f) * time/15.0f));
					
					xVelocity = (float)(30.0f * power - 1.0f*time/15.0f);
					//  
					if(xPosition >= 99.0f){
						HelloOpenGL.missText();
						GravitySwitch = false;
						HelloOpenGL.setPowerLevel(-1.0f);
						time = 0.0f;
						yVelocity = 0.0f;
						xVelocity = 0.0f;
						zVelocity = 0.0f;
						yPosition = 3.0f;
						xPosition = 0.0f;
						zPosition = 2.0f;
						yRotHold = -10000.0f;
					} else if (xPosition <= -99.0f){
						HelloOpenGL.missText();
						GravitySwitch = false;
						HelloOpenGL.setPowerLevel(-1.0f);
						time = 0.0f;
						yVelocity = 0.0f;
						xVelocity = 0.0f;
						zVelocity = 0.0f;
						yPosition = 3.0f;
						xPosition = 0.0f;
						zPosition = 2.0f;
						yRotHold = -10000.0f;
					}
					xPosition = (float) (0.0f +  (Math.sin(Math.toRadians(yRotHold)) * Math.sin(Math.toRadians(xRotHold)) *((xVelocity + 20.0f)/2.0f) * time/15.0f));
					
					zVelocity = (float)(30.0f * power - 1.0f*time/15.0f);
					// 
					if(zPosition >= 99.0f){
						HelloOpenGL.missText();
						GravitySwitch = false;
						HelloOpenGL.setPowerLevel(-1.0f);
						time = 0.0f;
						yVelocity = 0.0f;
						xVelocity = 0.0f;
						zVelocity = 0.0f;
						yPosition = 3.0f;
						xPosition = 0.0f;
						zPosition = 2.0f;
						yRotHold = -10000.0f;
					} else if (zPosition <= -99.0f){
						HelloOpenGL.missText();
						GravitySwitch = false;
						HelloOpenGL.setPowerLevel(-1.0f);
						time = 0.0f;
						yVelocity = 0.0f;
						xVelocity = 0.0f;
						zVelocity = 0.0f;
						yPosition = 3.0f;
						xPosition = 0.0f;
						zPosition = 2.0f;
						yRotHold = -10000.0f;
					}
					zPosition = (float) (2.0f + (Math.sin(Math.toRadians(yRotHold)) * Math.cos(Math.toRadians(xRotHold)) * ((zVelocity + 20.0f)/2.0f) * time/15.0f));
				
					if( xPosition <= xHigh && xPosition >= xLow){
						if( zPosition <= zHigh && zPosition >= zLow){
							if( yPosition <= yHigh && yPosition >= yLow){
								HelloOpenGL.winText();
								xPosStore = moveX;
								zPosStore = moveZ;
								xRotStore = xRotHold;
								yRotStore = yRotHold;
								powerStore = power;
								
								GravitySwitch = false;
								HelloOpenGL.setPowerLevel(-1.0f);
								time = 0.0f;
								yVelocity = 0.0f;
								xVelocity = 0.0f;
								zVelocity = 0.0f;
								yPosition = 3.0f;
								xPosition = 0.0f;
								zPosition = 2.0f;
								yRotHold = -10000.f;
							}
						}
					}
				}

	        
	        gl.glPushMatrix();
	        gl.glTranslatef(xPosition, yPosition, zPosition);
	        DrawBall(qOBJ, texID2, gl);
	        gl.glPopMatrix();
	        

	        
	        gl.glTranslatef(-moveX, 0.0f, moveZ);
	        
	        if (xLow != -4.0f - moveX){
	        	xLow = -4.0f - moveX;
	        	xHigh = 4.0f - moveX;
	        }
	        
	        if(zLow != 38.0 + moveZ){
	        	zLow = 38.0f + moveZ;
	        	zHigh = 42.0f + moveZ;
	        }
	        
	        DrawCourt(texID2, gl);
	        
	        DrawWalls(texID5, gl);
	        
	        DrawGoal(qOBJ1, qOBJ2, texID4, gl);
		}
		}
	    
		
		public void checkKeys() {
			if (wPressed)
				yRotation -= 2;
			if (aPressed)
				xRotation += 2;
			if (sPressed)
				yRotation += 2;
			if (dPressed)
				xRotation -= 2;
			if (leftPressed)
				moveX += 0.1;
			if (rightPressed)
				moveX -= 0.1;
			if (upPressed)
				moveZ -= 0.1;
			if (downPressed)
				moveZ += 0.1;
			
		    //Clamp y values if necessary
			if (yRotation > 176)
				yRotation = 176;
			else if (yRotation < 4)
				yRotation = 4;
		}
		
		public static void setGUIFlags(boolean[] flags) {
			if (flags[0])
				wPressed = true;
			else
				wPressed = false;
			if (flags[1])
				aPressed = true;
			else
				aPressed = false;
			if (flags[2])
				sPressed = true;
			else
				sPressed = false;
			if (flags[3])
				dPressed = true;
			else
				dPressed = false;
			if (flags[4])
				leftPressed = true;
			else
				leftPressed = false;
			if (flags[5])
				rightPressed = true;
			else
				rightPressed = false;
		}

		@Override
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();

		    switch( keyCode ) { 
		        case KeyEvent.VK_LEFT:
		        	leftPressed = true;
		            break;
		        case KeyEvent.VK_RIGHT :
		        	rightPressed = true;
		            break;
		        case KeyEvent.VK_UP :
		        	upPressed = true;
		        	break;
		        case KeyEvent.VK_DOWN :
		        	downPressed = true;
		        	break;
		        case KeyEvent.VK_W:
		        	wPressed = true;
		        	break;
		        case KeyEvent.VK_A:
		        	aPressed = true;
		        	break;
		        case KeyEvent.VK_S:
		        	sPressed = true;
		        	break;
		        case KeyEvent.VK_D:
		        	dPressed = true;
		        	break;
		        case KeyEvent.VK_G:
		        	GravitySwitch = true;
		        	break;
		        case KeyEvent.VK_SPACE:
		        	HelloOpenGL.modifyPowerBar();

		        	break;
		    }
		}

		@Override
		public void keyReleased(KeyEvent e) {
			int keyCode = e.getKeyCode();
			
			switch( keyCode ) { 
	        case KeyEvent.VK_LEFT:
	            leftPressed = false;
	            break;
	        case KeyEvent.VK_RIGHT :
	            rightPressed = false;
	            break;
	        case KeyEvent.VK_UP :
	        	upPressed = false;
	        	break;
	        case KeyEvent.VK_DOWN :
	        	downPressed = false;
	        	break;
	        case KeyEvent.VK_W:
	        	wPressed = false;
	        	break;
	        case KeyEvent.VK_A:
	        	aPressed = false;
	        	break;
	        case KeyEvent.VK_S:
	        	sPressed = false;
	        	break;
	        case KeyEvent.VK_D:
	        	dPressed = false;
	        	break;
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {}
		@Override
		public void mouseMoved(MouseEvent e) {}
		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		@Override
		public void dispose(GLAutoDrawable arg0) {}
		@Override
		public void keyTyped(KeyEvent e) {}

//		public boolean loadOBJ(String path, Vector out_vert, Vector out_uv, Vector out_norm){
//			Vector vertIndices = new Vector(); 
//			Vector uvIndices = new Vector();
//			Vector normIndices = new Vector();
//			
//			Vector temp_vert = new Vector(3);
//			Vector temp_uv = new Vector(3);
//			Vector temp_norm = new Vector(3);
//			
//			try{
//				BufferedReader reader = new BufferedReader(new FileReader(path));
//				String line;
//				char startChar;
//				while((line = reader.readLine()) != null){
//					startChar = line.charAt(0);
//					if(startChar == 'v'){
//						
//					}
//				}
//			}
//			catch (Exception e)
//			{
//				e.printStackTrace();
//				return false;
//			}
//			
//		}

		public void DrawBall(GLUquadric qOBJ, int texID, GL2 gl){
			 gl.glEnable(GL.GL_TEXTURE_2D);
             //mytex2.bind(gl);
             //gl.glActiveTexture(GL.GL_TEXTURE1);
             gl.glBindTexture(GL.GL_TEXTURE_2D, texID2);
             
	       	 	
        	 gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
             gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
             gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
             gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
             gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
             
             gl.glBlendFunc(GL2.GL_SRC_ALPHA,GL2.GL_ONE);
		        
		    //gl.glColor3f(1.0f, 0.5f, 0.0f);
			qOBJ = glu.gluNewQuadric();
	       	glu.gluQuadricNormals(qOBJ, GLU.GLU_SMOOTH);
	       	glu.gluQuadricTexture(qOBJ, true);
			
	       	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);
	       	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0);
	  		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, darkGray, 0);
	  		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, mShininess, 0);
	  		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specularLight, 0);
	       	
	       	glu.gluSphere(qOBJ, 1.0f, 100, 100);
			
			gl.glDisable(GL.GL_TEXTURE_2D);
			
		}
		
		public void DrawSkybox(int texID ,GL2 gl){
			gl.glEnable(GL.GL_TEXTURE_2D);
            
            //mytex1.bind(gl);
            //gl.glActiveTexture(GL.GL_TEXTURE0);
            gl.glBindTexture(GL.GL_TEXTURE_2D, texID);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
            gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
            
            gl.glBlendFunc(GL2.GL_SRC_ALPHA,GL2.GL_ONE);
            
            gl.glDisable(GL.GL_TEXTURE_2D);
			
			//gl.glColor3f(1.0f, 0.5f, 0.0f);
			gl.glEnable(GL.GL_TEXTURE_2D);
		    gl.glActiveTexture(GL.GL_TEXTURE0);
		    gl.glBindTexture(GL.GL_TEXTURE, texID);
			
			gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
	        //gl.glEnableClientState(GL2.GL_INDEX_ARRAY);
	        gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
	        
	        gl.glVertexPointer(3, GL2.GL_FLOAT, 0, vertArrBuff);
	        //gl.glIndexPointer(GL2.GL_INT, 0, indexArrBuff);
	        gl.glTexCoordPointer(2, GL2.GL_FLOAT, 0, texCoordArrBuff);
	        
	        gl.glEnable(GL.GL_TEXTURE_2D);
	        //gl.glActiveTexture(GL.GL_TEXTURE0);
	        gl.glBindTexture(GL.GL_TEXTURE_2D, texID1);
	        
	        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, white, 0);
	       	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, black, 0);
	  		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, white, 0);
	  		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, noSpecular, 0);
	  		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, noSpecularLight, 0);
	        
	        gl.glDrawArrays(GL2.GL_QUADS, 0, 24);
	        
	        gl.glDisable(GL.GL_TEXTURE_2D);
	        
	        gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
	        //gl.glDisableClientState(GL2.GL_INDEX_ARRAY);
	        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        	        
	        gl.glDisable(GL.GL_TEXTURE_2D);
		}
		
		public void DrawCourt(int texID, GL2 gl){
			gl.glEnable(GL.GL_TEXTURE_2D);
            //mytex2.bind(gl);
            //gl.glActiveTexture(GL.GL_TEXTURE1);
            gl.glBindTexture(GL.GL_TEXTURE_2D, texID3);
            
	       	 	
       	 	gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
            gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
            
            gl.glBlendFunc(GL2.GL_SRC_ALPHA,GL2.GL_ONE);
            
            gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);
	       	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0);
	  		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, white, 0);
	  		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, mShininess, 0);
	  		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specularLight, 0);
			
			gl.glBegin(GL2.GL_QUADS);
			
			gl.glTexCoord2f(1.0f, 1.0f);
			gl.glColor3f(1.0f, 0.2f, 0.0f);
			gl.glVertex3f(100.0f, 0.0f, 100.0f);
			
			gl.glTexCoord2f(1.0f, -1.0f);
			gl.glColor3f(1.0f, 0.2f, 0.0f);
			gl.glVertex3f(100.0f, 0.0f, -100.0f);
			
			gl.glTexCoord2f(-1.0f, -1.0f);
			gl.glColor3f(1.0f, 0.2f, 0.0f);
			gl.glVertex3f(-100.0f, 0.0f, -100.0f);
			
			gl.glTexCoord2f(-1.0f, 1.0f);
			gl.glColor3f(1.0f, 0.2f, 0.0f);
			gl.glVertex3f(-100.0f, 0.0f, 100.0f);
			
			gl.glEnd();
		}
		
		public void DrawWalls(int texID, GL2 gl){
			gl.glEnable(GL.GL_TEXTURE_2D);
            //mytex2.bind(gl);
            //gl.glActiveTexture(GL.GL_TEXTURE1);
            gl.glBindTexture(GL.GL_TEXTURE_2D, texID5);
            
	       	 	
       	 	gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
            gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
            
            gl.glBlendFunc(GL2.GL_SRC_ALPHA,GL2.GL_ONE);
            
            gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);
	       	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0);
	  		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, white, 0);
	  		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, mShininess, 0);
	  		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specularLight, 0);
			
			gl.glBegin(GL2.GL_QUADS);
			
			gl.glTexCoord2f( 1.0f, 0.0f);
			gl.glColor3f(1.0f, 0.2f, 0.0f);
			gl.glVertex3f(85.0f, 0.0f, 85.0f);
			
			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glColor3f(1.0f, 0.2f, 0.0f);
			gl.glVertex3f(85.0f, 0.0f, -85.0f);
			
			gl.glTexCoord2f(0.0f, 1.0f);
			gl.glColor3f(1.0f, 0.2f, 0.0f);
			gl.glVertex3f(85.0f, 10.0f, -85.0f);
			
			gl.glTexCoord2f(1.0f, 1.0f);
			gl.glColor3f(1.0f, 0.2f, 0.0f);
			gl.glVertex3f(85.0f, 10.0f, 85.0f);
			
			gl.glTexCoord2f(1.0f, 0.0f);
			gl.glColor3f(1.0f, 0.2f, 0.0f);
			gl.glVertex3f(85.0f, 0.0f, 85.0f);
			
			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glColor3f(1.0f, 0.2f, 0.0f);
			gl.glVertex3f(-85.0f, 0.0f, 85.0f);
			
			gl.glTexCoord2f(0.0f, 1.0f);
			gl.glColor3f(1.0f, 0.2f, 0.0f);
			gl.glVertex3f(-85.0f, 10.0f, 85.0f);
			
			gl.glTexCoord2f(1.0f, 1.0f);
			gl.glColor3f(1.0f, 0.2f, 0.0f);
			gl.glVertex3f(85.0f, 10.0f, 85.0f);
			
			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glColor3f(1.0f, 0.2f, 0.0f);
			gl.glVertex3f(-85.0f, 0.0f, -85.0f);
			
			gl.glTexCoord2f(1.0f, 0.0f);
			gl.glColor3f(1.0f, 0.2f, 0.0f);
			gl.glVertex3f(85.0f, 0.0f, -85.0f);
			
			gl.glTexCoord2f(1.0f, 1.0f);
			gl.glColor3f(1.0f, 0.2f, 0.0f);
			gl.glVertex3f(85.0f, 10.0f, -85.0f);
			
			gl.glTexCoord2f(0.0f, 1.0f);
			gl.glColor3f(1.0f, 0.2f, 0.0f);
			gl.glVertex3f(-85.0f, 10.0f, -85.0f);
			
			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glColor3f(1.0f, 0.2f, 0.0f);
			gl.glVertex3f(-85.0f, 0.0f, -85.0f);
			
			gl.glTexCoord2f(1.0f, 0.0f);
			gl.glColor3f(1.0f, 0.2f, 0.0f);
			gl.glVertex3f(-85.0f, 0.0f, 85.0f);
			
			gl.glTexCoord2f(1.0f, 1.0f);
			gl.glColor3f(1.0f, 0.2f, 0.0f);
			gl.glVertex3f(-85.0f, 10.0f, 85.0f);
			
			gl.glTexCoord2f(0.0f, 1.0f);
			gl.glColor3f(1.0f, 0.2f, 0.0f);
			gl.glVertex3f(-85.0f, 10.0f, -85.0f);
			
			gl.glEnd();
		}
		
		public void DrawGoal(GLUquadric qOBJ1, GLUquadric qOBJ2, int texID, GL2 gl){
			gl.glEnable(GL.GL_TEXTURE_2D);
            //mytex2.bind(gl);
            //gl.glActiveTexture(GL.GL_TEXTURE1);
            gl.glBindTexture(GL.GL_TEXTURE_2D, texID4);
            
	       	 	
       	 	gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
            gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
            
            gl.glBlendFunc(GL2.GL_SRC_ALPHA,GL2.GL_ONE);
            
            gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);
	       	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0);
	  		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, white, 0);
	  		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, mShininess, 0);
	  		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specularLight, 0);
			
			gl.glBegin(GL2.GL_QUADS);
			
			gl.glTexCoord2f(0.0f, 0.0f);
			//gl.glColor3f(1.0f, 0.2f, 0.0f);
			gl.glVertex3f(-2.9372298f, 10.0f, 40.0f);
			
			gl.glTexCoord2f(1.0f, 0.0f);
			//gl.glColor3f(1.0f, 0.2f, 0.0f);
			gl.glVertex3f(2.9372298f, 10.0f, 40.0f);
			
			gl.glTexCoord2f(1.0f, 1.0f);
			//gl.glColor3f(1.0f, 0.2f, 0.0f);
			gl.glVertex3f(2.9372298f, 14.0f, 40.0f);
			
			gl.glTexCoord2f(0.0f, 1.0f);
			//gl.glColor3f(1.0f, 0.2f, 0.0f);
			gl.glVertex3f(-2.9372298f, 14.0f, 40.0f);
			
			gl.glEnd();
			
			qOBJ1 = glu.gluNewQuadric();
			qOBJ2 = glu.gluNewQuadric();
	       	//glu.gluQuadricNormals(qOBJ, GLU.GLU_SMOOTH);
	       	
			
	       	gl.glPushMatrix();
	       	gl.glColor3f(0.0f, 0.0f, 0.0f);
			gl.glTranslatef(0.0f, 0.0f, 40.0f);
	       	gl.glRotatef(-90, 1.0f, 0.0f, 0.0f);
			glu.gluCylinder(qOBJ1, .1, .1, 10.0, 50, 50);
			gl.glPopMatrix();
			
			
			gl.glPushMatrix();
			gl.glColor3f(1.0f, 0.0f, 0.0f);
			gl.glTranslatef(0.0f, 10.5f, 38.7f);
			gl.glRotatef(90, 1.0f, 0, 0);
			glu.gluDisk(qOBJ2, 1.2, 1.4, 50, 50);
			gl.glPopMatrix();
		}
}
