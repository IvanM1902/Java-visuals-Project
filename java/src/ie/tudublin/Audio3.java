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
                    float f = lerpedBuffer[i] * halfH * 4.0f;
                    line(i, halfH + f, i, halfH - f);                    
                }
                break;
        case 1:
            background(0);
            for(int i = 0 ; i < ab.size() ; i ++)
            {
                //float c = map(ab.get(i), -1, 1, 0, 255);
                float c = map(i, 0, ab.size(), 0, 255);
                stroke(c, 255, 255);
                float f = lerpedBuffer[i] * halfH * 4.0f;
                line(i, halfH + f, halfH - f, i);                    
            }
            break;
        case 2:
            background(0);
            for(int i = 0 ; i < ab.size() ; i ++)
            {
                //float c = map(ab.get(i), -1, 1, 0, 255);
                float c = map(i, 0, ab.size(), 0, 255);
                stroke(c, 10, 100); //sets up the color
                float f = lerpedBuffer[i] * 5.0f * halfH;
                noFill();
                translate(cx, cy, 0);
                box(f);                 
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
        background(0);
        for(int i = 0 ; i < ab.size() ; i ++)
        {
            float c = map(ab.get(i), -1, 1, 0, 255);
            stroke(c, 255, 255);
            float f = lerpedBuffer[i] * halfH * 4.0f;


            //rect(f--, f--, i--, i--);
            
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
                circle(i, halfH + f, 100);                    
                circle(i, halfH - f, 100);                   
            }
            break;
            // the good one ye
            
            case 6:
            {
            background(0);
            float radius = map(smoothedAmplitude, 0, 0.1f, 50, 300);		
            int points = (int)map(mouseX, 0, 255, 3, 10);
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
            background(0);
            int grid = 40;   
            float radius = map(smoothedAmplitude, 0, 0.1f, 50, 300);		
            int points = (int)map(mouseX, 0, 255, 3, 10);
            int sides = points * 10;        
            for(int i = grid ; i < ab.size() - grid ; i += grid)
            {
                for(int j = grid; j < ab.size() - grid; j += grid)
                {
                    float f = lerpedBuffer[i] * halfH * 3.0f;
                    noStroke();
                    fill(255);
                    rect(i-f, j-f, 3, 3);
                    stroke(255, 255);
                    line(i/4-f, j/4-f, i/4, j/4);
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
}
