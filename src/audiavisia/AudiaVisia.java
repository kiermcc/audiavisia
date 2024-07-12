/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package audiavisia;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 *
 * @author P50
 */
public class AudiaVisia {

	// The window handle
	private long window;
        public String version = "0.1";
        public String windowTitle = "AudiaVisia" + version;
        public int winWidth = 480;
        public int winHeight = 640;
        public float winWidthR = (float) 0.001f * winWidth;
        public float winHeightR = (float) 0.001f * winHeight;
        public Button[] buttons; 
        

	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init();
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable

		// Create the window
		window = glfwCreateWindow(winWidth, winHeight, windowTitle, NULL, NULL); //replace first NULL arg with glfwGetPrimaryMonitor() for fullscreen
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
                        }
		});
                
                // # SETUP MOUSE CALLBACK
                glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
                        if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
                                DoubleBuffer posXBuff = BufferUtils.createDoubleBuffer(1); //set up buffer for glfw to write to
                                DoubleBuffer posYBuff = BufferUtils.createDoubleBuffer(1);
                                
                                glfwGetCursorPos(window, posXBuff, posYBuff); // pass buffer for cursor pos vals
                                
                                float[] mouseXYPos = {(float) posXBuff.get(0), (float) posYBuff.get(0)}; //cast to usable float
                                
                                // DEBUG
                                //System.out.println("mouse X = " + mouseXYPos[0] + " and Y = " + mouseXYPos[1]); 
                                //buttons[0].printCornerCoordsDebug((float) winWidth, (float) winHeight);
                                
                                if (buttons != null) { // if buttons are initialised
                                    for (Button b : buttons) { // check each button b
                                        if (b.checkBounds(mouseXYPos) != null) b.click(); //is in bounds? then click
                                    }
                                }

                        }
                });

		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
                
                
                // # INITIALISE ELEMENTS
                int numberOfButtons = 6;
                buttons = new Button[numberOfButtons];
                for (int x = 0 ; x < numberOfButtons ; x++){ // make buttons
                    float[][] coords = new float[4][2]; //corners of one button defined
                    for (int coord = 0 ; coord < 4 ; coord++) {  // defining each corner coord
                        for (int i = 0 ; i < 2 ; i ++) { // defining each x and y
                            switch (coord) {
                                case 0 -> {
                                    //top left
                                    if (i == 0) coords[coord][i] = .5f*winHeightR; //x
                                    else if (i == 1) coords[coord][i] = 1.5f*winWidthR-x*.15f; //y
                                }
                                case 1 -> {
                                    //top right
                                    if (i == 0) coords[coord][i] = 1.5f*winHeightR; //x
                                    else if (i == 1) coords[coord][i] = 1.5f*winWidthR-x*.15f; //y
                                }
                                case 2 -> {
                                    //bot right
                                    if (i == 0) coords[coord][i] = 1.5f*winHeightR; //x
                                    else if (i == 1) coords[coord][i] = 1.2f*winWidthR-x*.15f; //y
                                }
                                case 3 -> {
                                    //bot left
                                    if (i == 0) coords[coord][i] = .5f*winHeightR; //x
                                    else if (i == 1) coords[coord][i] = 1.2f*winWidthR-x*.15f; //y
                                }
                            }
                        }
                    }
                    
                    buttons[x] = new Button(window, coords, "Button " + x);
                }
	}

	private void loop() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Set the clear color
		glClearColor(.7f, .7f, .7f, 0.0f); //refer to glClear below

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( !glfwWindowShouldClose(window) ) {
			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
                        
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer (sets everything to glClearColour)
                        
                        
                        
                        
                        // # RENDER HERE
                        glBegin(GL_QUADS);
                            glColor3f(0.5f, 0.5f, 0.5f);                         // cool trapesium background
                            glVertex2f(-1f, 1f);  //top left
                            glVertex2f(1f, 1f); //top right
                            glVertex2f(.5f, -.5f); //bottom right
                            glVertex2f(-.5f, -.5f); //bottom left
                        glEnd();
                       
                        
                        glBegin(GL_QUADS);
                            glColor3f(0f, 0f, 0f);                               // display background
                            glVertex2f(-1.5f*winHeightR, 1.5f*winWidthR);  //top left
                            glVertex2f(.5f*winHeightR, 1.5f*winWidthR); //top right
                            glVertex2f(.5f*winHeightR, -.5f*winWidthR); //bottom right
                            glVertex2f(-1.5f*winHeightR, -.5f*winWidthR); //bottom left   
                        glEnd();
                        
                        
                        // render each button
                        if (buttons != null) {
                            for (Button b : buttons) {
                                if (b != null) {
                                    float[][] corners = b.cornerGLRatios;
                                    glBegin(GL_QUADS);
                                        glColor3f(1f, 1f, 1f);
                                        glVertex2f(corners[0][0], corners[0][1]);  //top left
                                        glVertex2f(corners[1][0], corners[1][1]); //top right
                                        glVertex2f(corners[2][0], corners[2][1]); //bottom right
                                        glVertex2f(corners[3][0], corners[3][1]); //bottom left
                                    glEnd();
                                }
                            }
                        }
                        
                        
                        
                        
			glfwSwapBuffers(window); // swap the color buffers (there are two: front end is shown, back end drawn to on GPU)
		}
	}

	public static void main(String[] args) {
		new AudiaVisia().run();
	}

}