/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package audiavisia;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

/**
 *
 * @author P50
 */
public class Button {
    public String text;
    public float[][] cornerPositions; //topleft[], topright[], botright[], botleft[]
    
    public Button(float[][] corners, String txt) {
        text = txt;
        cornerPositions = corners;
    }
    
    public Button checkBounds(float[] coords){
        if (coords[0] <= cornerPositions[1][0] && coords[0] >= cornerPositions[0][0] && coords[1] >= cornerPositions[0][1] && coords[1] <= cornerPositions[2][1]) {  //assuming a rectangle or square
            return this;
        }
        else return null;
    }
    
    public void click(){
        // clicked
    }
}