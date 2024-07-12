/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.tit to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package audiavisia;

import java.nio.DoubleBuffer;
import org.lwjgl.BufferUtils;
import static org.lwjgl.glfw.GLFW.*;

import java.nio.*;

/**
 *
 * @author P50
 */
public class Button {
    public String text;
    public long parentWindow;
    public float[][] cornerGLRatios = new float[4][2]; //topleft[], topright[], botright[], botleft[]  ratios of screen width from centre, Y inverted
    public float[][] cornerCoords = new float[4][2]; //topleft[], topright[], botright[], botleft[]    x and y values
    
    public Button(long window, float[][] corners, String txt) { //constructor
        this.text = txt;
        this.cornerGLRatios = corners;
        this.parentWindow = window;
        
        //get window size values
        IntBuffer widthBuff = BufferUtils.createIntBuffer(1); //set up buffer for glfw to write to
        IntBuffer heightBuff = BufferUtils.createIntBuffer(1);
        
        glfwGetWindowSize(window, widthBuff, heightBuff); //collect window size to calculate corner coords
        
        float winWidth = (float) widthBuff.get(0); //cast to usable float
        float winHeight = (float) heightBuff.get(0);
        
        // calculate each corner coordinate and write to cornerCoords
        for (int i = 0 ; i < 4 ; i++) {
            for (int xyi = 0 ; xyi < 2 ; xyi++) {
                if (xyi == 0) cornerCoords[i][xyi] = winWidth / 2 + (cornerGLRatios[i][xyi] * (winWidth/2)); //x
                else cornerCoords[i][xyi] = winHeight / 2 - (cornerGLRatios[i][xyi] * (winHeight/2));       //y (inverted by design)
            }
        }
    }
    
    public Button checkBounds(float[] mouseCoords) {
        //foreach x or y inside, in each coord
        Boolean inside = true;
        String[] cornerpos = {"topleft", "topright", "botright", "botleft"}; //to make logic more readable
        for (int i = 0 ; i < 4 ; i++) {
            for (int xyi = 0 ; xyi < 2 ; xyi++) {
                if (xyi == 0) {                                 //x
                    if (inside && (cornerpos[i].equals("topleft") || cornerpos[i].equals("botleft")) && mouseCoords[xyi] < cornerCoords[i][xyi]) {
                        // DEBUG
                        //System.out.println("mousex " + mouseCoords[xyi] + " cornerx "+ cornerCoords[i][xyi]);
                        //System.out.println("TOO FAR LEFT");
                        inside = false;
                    }
                    if (inside && (cornerpos[i].equals("topright") || cornerpos[i].equals("botright")) && mouseCoords[xyi] > cornerCoords[i][xyi]) {
                        //System.out.println("TOO FAR RIGHT");
                        inside = false;
                    }
                }
                else if (xyi == 1) {                            //y
                    if (inside && (cornerpos[i].equals("topleft") || cornerpos[i].equals("topright")) && mouseCoords[xyi] < cornerCoords[i][xyi]) {
                        //System.out.println("TOO FAR UP");
                        inside = false;
                    }
                    if (inside && (cornerpos[i].equals("botleft") || cornerpos[i].equals("botright")) && mouseCoords[xyi] > cornerCoords[i][xyi]) {
                        //System.out.println("TOO FAR DOWN");
                        inside = false;
                    }
                }

                //break by forcing for loop (if found to be outside)
                if (!inside) {
                    i = 3;
                }
            }
        }
        
        
        if (inside) {  //assuming a rectangle or square
            return this;
        }
        else return null;
    }
    
    public void click(){
        // clicked
        System.out.println(text + " CLICKED");
    }
    
    // DEBUG METHOD (TO FIND EQUATION TO CONVERT cornerGLRatios to cornerCoords
    public void printCornerCoordsDebug(float winWidth, float winHeight){
        System.out.println();
        
        // for each corner coord, print X and Y values
        String[] cornerpos = {"topleft", "topright", "botright", "botleft"};
        int coordi = 0;
        for (float[] coord : cornerGLRatios) {
            System.out.println("CORNER " + cornerpos[coordi]);
            for (int i = 0 ; i < 2 ; i++) {
                if (i == 0) System.out.println(" X " + (winWidth / 2 + (coord[i] * (winWidth/2))));
                else System.out.println(" Y " + (winHeight / 2 - (coord[i] * (winHeight/2))));
            }
            coordi++;
        }
    }
    
}