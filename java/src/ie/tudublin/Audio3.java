package ie.tudublin;

import ddf.minim.AudioBuffer;
import ddf.minim.AudioInput;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;

public class Audio3 extends PApplet
{
    Minim minim;
    AudioPlayer ap;
    AudioInput ai;
    AudioBuffer ab;

    int mode = 0;

    float[] lerpedBuffer;
    float y = 0;
    float smoothedY = 0;
    float smoothedAmplitude = 0;
    float offset;

    int off_max = 300;

    public void keyPressed() {
		if (key >= '0' && key <= '9') {
			mode = key - '0';
		}
		if (keyCode == ' ') {
            if (ap.isPlaying()) {
                ap.pause();
            } else {
                ap.rewind();
                ap.play();
            }
        }
	}
            int unit = 40;
        int count;
        Module[] mods;

    public void settings()
    {
        size(1200, 700, P3D);
        //fullScreen(P3D, SPAN);
    }

    public void setup()
    {
        minim = new Minim(this);
        // Uncomment this to use the microphone
        // ai = minim.getLineIn(Minim.MONO, width, 44100, 16);
        // ab = ai.mix; 
        ap = minim.loadFile("badinga.wav", 1200);
        ap.play();
        ab = ap.mix;
        colorMode(HSB);

        y = height / 2;
        smoothedY = y;

        lerpedBuffer = new float[width];
      }
    

    float off = 0;

    public void polygon(float x, float y, float radius, int npoints) {
        float angle = TWO_PI / npoints;
        beginShape();
        for (float a = 0; a < TWO_PI; a += angle) {
          float sx = x + cos(a) * radius;
          float sy = y + sin(a) * radius;
          vertex(sx, sy);
        }}

    public void draw()
    {
        //background(0);
        float halfH = height / 2;
        float average = 0;
        float sum = 0;
        off += 1;
        // Calculate sum and average of the samples
        // Also lerp each element of buffer;
        for(int i = 0 ; i < ab.size() ; i ++)
        {
            sum += abs(ab.get(i));
            lerpedBuffer[i] = lerp(lerpedBuffer[i], ab.get(i), 0.05f);
        }
        average= sum / (float) ab.size();

        smoothedAmplitude = lerp(smoothedAmplitude, average, 0.1f);
        
        float cx = width / 2;
        float cy = height / 2;

        


        switch (mode) {
			case 0:
                background(0);
                for(int i = 0 ; i < ab.size() ; i ++)
                {
                    //float c = map(ab.get(i), -1, 1, 0, 255);
                    float c = map(i, 0, ab.size(), 0, 255);
                    stroke(c, 255, 255);
                    float f = lerpedBuffer[i] * halfH * 2.0f;
                    //println(f);
                    //line(halfH-f/5, i-f, halfH + f, i-f); 
                    line(halfH+f, i/2, halfH - f, i*c); 
                    line(width/2, height/2,width ,f+height);
                    line(0, 0, 300, f+halfH);
                    line(width/2, height/2,f+width ,0);
                    line(width/2, height/2,0 ,f+height);  
                    //line(i*f, f, f, f);                 
                }
                break;
        case 1:
        {
            background(0);
            int grid = 20;   
            float radius = map(smoothedAmplitude, 0, 0.1f, 50, 300);		
            int points = (int)map(mouseX, 0, 255, 3, 10);
            int sides = points * 2;
            for(int i = grid ; i < ab.size() - grid ; i += grid)
            {
                for(int j = grid; j < ab.size() - grid; j += grid)
                {
                    
                    float f = lerpedBuffer[i] * halfH * 3.0f;
                    noStroke();
                    fill(255);
                    rect(i-f, j-f, 3, 3);
                    stroke(255, 255);
                    translate(1260, 300, -1);
                    line(i/4-f, j/4-f, i*4, j*4);
                    
                    float g = map(ab.get(i), -1, 1, 0, 255);
                    float r = (i % 2 == 0) ? radius : radius; 
                    //float r = radius;
                    float theta = map(i, 0, sides, 0, TWO_PI);
                    float x = cx + sin(theta) + r/4;
                    float y = cy - cos(theta) + r/4;
                    stroke(g, 255, 255);
                    //float f = lerpedBuffer[i] + halfH + 0.5f;
                    strokeWeight(2);
                    noFill();
                    rotate(-10);
                    circle(x, y, f);
                    
                    
                    
                } 
            }
        }

            break;
        case 2:
        background(0);
        translate(width / 2, height / 2, -off_max);
        rotateX((float) (frameCount * .01));
        rotateY((float) (frameCount * .01));
        rotateZ((float) (frameCount * .01));
        
        for (int xo = -off_max; xo <= off_max; xo += 50) {
          for (int yo = -off_max; yo <= off_max; yo += 50) {
            for (int zo = -off_max; zo <= off_max; zo += 50) {
              pushMatrix();
              translate(xo, yo, zo);
              rotateX((float) (frameCount * .02));
              rotateY((float) (frameCount * .02));
              rotateZ((float) (frameCount * .02));
              fill(colorFromOffset(xo), colorFromOffset(yo), 
                colorFromOffset(zo));
              box((float) (20 + (Math.sin(frameCount / 20.0)) * 15));
              popMatrix();
            }
          }
        }
            break;
            //not bad
        case 3:
            background(0);
            float lastAngle = 0;
            for (int i = 0; i < ab.size(); i++) {
              float c = map(i, 0, ab.size(), 0, 255);
              stroke(c, 255, 255); //sets up the color
              float f = lerpedBuffer[i] * halfH * 6.0f; //makes the cirlce bigger
              arc(width/2, height/2, f, f, lastAngle, lastAngle+radians(height));
              lastAngle += radians(lerpedBuffer[i]);

            }
            break;
        
        case 4:
        {
            background(0);
            int grid = 20;   
            float radius = map(smoothedAmplitude, 0, 0.1f, 50, 300);		
            int points = (int)map(1, 0, 255, 3, 10);
            int sides = points * 2;
            for(int i = grid ; i < ab.size() - grid ; i += grid)
            {
                for(int j = grid; j < ab.size() - grid; j += grid)
                {
                    translate(1260, 300, 7);
                    float f = lerpedBuffer[i] * halfH * 3.0f;
                    noStroke();
                    fill(255);
                    stroke(255, 255);
                    float g = map(ab.get(i), -1, 1, 0, 255);
                    float r = (i % 2 == 0) ? radius : radius; 
                    //float r = radius;
                    float theta = map(i, 0, sides, 0, TWO_PI);
                    float x = cx + sin(theta) + r/4;
                    stroke(g, 255, 255);
                    //float f = lerpedBuffer[i] + halfH + 0.5f;
                    strokeWeight(2);
                    noFill();
                    rotate(-10);
                    circle(x, y, f);
                } 
            }
        }
                  
            break;
            case 5:
            background(0);
            strokeWeight(2);
            for(int i = 0 ; i < ab.size() ; i +=10)
            {
                //float c = map(ab.get(i), -1, 1, 0, 255);
                float cc = map(i, 0, ab.size(), 0, 255);
                stroke(cc, 255, 255);
                float f = lerpedBuffer[i] * halfH * 4.0f;
                circle(i, halfH + f,  halfH - f); // put i 3rd parameter
                fill(cc);
                //circle(i, halfH + f, 100);                    
                //circle(i, halfH - f, 100);                   
            }
            break;
            // the good one ye
            
            case 6:
            {
            background(0);
            float radius = map(smoothedAmplitude, 0, 0.1f, 50, 300);		
            int points = (int)map(500, 0, 255, 3, 10);
            int sides = points * 2;

            for(int i = 0 ; i < ab.size() ; i ++)
            {
                float g = map(ab.get(i), -1, 1, 0, 255);
                float r = (i % 2 == 0) ? radius : radius; 
                // float r = radius;
                float theta = map(i, 0, sides, 0, TWO_PI);
                float x = cx + sin(theta) * r/4;
                float y = cy - cos(theta) * r/4;
                stroke(g, 255, 255);
                float f = lerpedBuffer[i] + halfH + 0.5f;
                strokeWeight(2);
                noFill();
                circle(x, y, f);                  
            }
        }
        
            break;
            case 7:
            {
                for(int i = 0 ; i < ab.size() ; i ++)
                {
                float color = map(ab.get(i), -1, 1, 0, 255);
                background(color);
                }
                int grid = 20;   
                float radius = map(smoothedAmplitude, 0, 0.1f, 50, 300);		
                int points = (int)map(10, 0, 255, 3, 10);
                int sides = points * 2;        
                for(int i = grid ; i < ab.size() - grid ; i += grid)
                {
                    for(int j = grid; j < ab.size() - grid; j += grid)
                    {
                        translate(width/2, height/2);
                        float f = lerpedBuffer[i] * halfH * 3.0f;
                        noStroke();
                        fill(255);
                        rect(i-f, j-f, 3, 3);
                        stroke(255, 255);
                        line(i/4-f, j/4-f, i/4, j/4);
                        translate(300, 600);
                        float g = map(ab.get(i), -1, 1, 0, 255);
                        float r = (i % 2 == 0) ? radius : radius; 
                        //float r = radius;
                        float theta = map(i, 0, sides, 0, TWO_PI);
                        float x = cx + sin(theta) + r/4;
                        float y = cy - cos(theta) + r/4;
                        translate(10, 10);
                        stroke(g, 255, 255);
                        //float f = lerpedBuffer[i] + halfH + 0.5f;
                        strokeWeight(2);
                        noFill();
                        rotate(10);
                        circle(x, y, f);
                        //rotate(-10);
                        
                    } 
                }
            }
            break;
            case 8:
            background(0);
            for(int i = 0 ; i < ab.size() ; i ++)
            {

                float u = map(ab.get(i), -1, 1, 0, 255);
                stroke(u, 255, 255);
                float f = lerpedBuffer[i] * halfH + 1.0f;
                line(halfH + f, halfH * f, i, i);
            }
            break;


            case 9:
            background(0);
            for(int i = 0 ; i < ab.size() ; i ++)
            {

                float u = map(ab.get(i), -1, 1, 0, 255);
                stroke(u, 100, 255);
                float f = lerpedBuffer[i] + halfH + 4.0f;
                rect(f, f, f, f);

            }
            break;


        }
        

    }

    private float colorFromOffset(int xo) {
        return (int) ((offset + off_max) / (2.0 * off_max) * 255);
    }  
}