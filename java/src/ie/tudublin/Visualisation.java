package ie.tudublin;

import ddf.minim.AudioBuffer;
import ddf.minim.AudioInput;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;

public class Visualisation extends PApplet {
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

    public void settings() {
        size(1200, 700, P3D);
    }

    public void setup() {
        minim = new Minim(this);

        ap = minim.loadFile("bidibou.wav", 1200);
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
        }
    }

    public void draw() {
        // background(0);
        float halfH = height / 2;
        float average = 0;
        float sum = 0;
        off += 1;
        // Calculate sum and average of the samples
        // Also lerp each element of buffer;
        for (int i = 0; i < ab.size(); i++) {
            sum += abs(ab.get(i));
            lerpedBuffer[i] = lerp(lerpedBuffer[i], ab.get(i), 0.05f);
        }
        average = sum / (float) ab.size();

        smoothedAmplitude = lerp(smoothedAmplitude, average, 0.1f);

        float cx = width / 2;
        float cy = height / 2;

        
        switch (mode) {

            //this code shows 4 lines, starting from the middle that led to 4 corners of the screen
            case 0:
                background(0);
                for (int i = 0; i < ab.size(); i++) {
                    float c = map(i, 0, ab.size(), 0, 255);
                    stroke(c, 255, 255);
                    float f = lerpedBuffer[i] * halfH * 2.0f;

                    line(width / 2, height / 2, f + width / height, 0);// Top Left
                    line(width / 2, height / 2, f + width, 0);// Top Right
                    line(width / 2, height / 2, 0, f + height);// Bottom Left
                    line(width / 2, height / 2, 1200, f + height);// Bottom Right
                }
                break;

            //interface of a unseen circle with dots around it that are manipulated by the moving bit of the sound
            case 1: {
                background(0);
                int grid = 20;
                float radius = map(smoothedAmplitude, 0, 0.1f, 50, 300);
                int points = (int) map(mouseX, 0, 255, 3, 10);
                int sides = points * 2;
                for (int i = grid; i < ab.size() - grid; i += grid) {
                    for (int j = grid; j < ab.size() - grid; j += grid) {

                        float f = lerpedBuffer[i] * halfH * 3.0f;
                        noStroke();
                        fill(255);
                        rect(i + f, j + f, 8, 8); // change last 2 parameters to make circles smaller
                        translate(1260, 300, -1);
                        float r = (i % 2 == 0) ? radius : radius;
                        float theta = map(i, 0, sides, 0, TWO_PI);
                        float x = cx + sin(theta) + r / 4;
                        float y = cy - cos(theta) + r / 4;
                        strokeWeight(5);
                        noFill();
                        rotate(-10);
                        circle(y, x, f);

                    }
                }
            }
                break;
            //interface of a cube that rotates on x, y, z, axis and changes by the bit of the sound
            case 2:
                background(0);

                translate(width / 2, height / 2, -off_max);
                rotateX((float) (frameCount * .01));
                rotateY((float) (frameCount * .01));
                rotateZ((float) (frameCount * .01));

                for (int xo = -off_max; xo <= off_max; xo += 60) {
                    for (int yo = -off_max; yo <= off_max; yo += 60) {
                        for (int zo = -off_max; zo <= off_max; zo += 60) {
                            float f = lerpedBuffer[off_max] * halfH + 5.0f;
                            pushMatrix();
                            translate(xo, yo, zo);
                            fill(colorFromOffset(xo), colorFromOffset(yo),
                                    colorFromOffset(zo));
                            box((float) (f + (Math.sin(frameCount / 20.0)) * 15));
                            popMatrix();
                        }
                    }
                }

                break;
            
                //single 3D triangle rotating on the x, y, z axis and changing its shape by the bit of the music
            case 3:
                background(0);
                for (int i = 0; i < ab.size(); i++)
                {
                float c = map(i, 0, ab.size(), 0, 255);
                stroke(c, 255, 255);
                fill(0);
                }
                translate(width / 2, height / 2, 0);
                rotateX((float) (frameCount * .01));
                rotateY((float) (frameCount * .01));
                rotateZ((float) (frameCount * .05));
                float p = lerpedBuffer[off_max] * halfH + 80.0f;
                pushMatrix();

                beginShape();
                vertex(-p, -p, -p);
                vertex(p, -p, -p);
                vertex(0, 0, p);

                vertex(p, -p, -p);
                vertex(p, p, -p);
                vertex(0, 0, p);

                vertex(p, p, -p);
                vertex(-p, p, -p);
                vertex(0, 0, p);

                vertex(-p, p, -p);
                vertex(-p, -p, -p);
                vertex(0, 0, p);

                directionalLight(0, 255, 0, 0, -1, 0);

                endShape();
                popMatrix();

                break;
            //multiple circles drawn inside a circle that are moving by the changing sound
            case 4: {
                background(0);
                int grid = 20;
                float radius = map(smoothedAmplitude, 0, 0.1f, 50, 300);
                int points = (int) map(1, 0, 255, 3, 10);
                int sides = points * 2;
                for (int i = grid; i < ab.size() - grid; i += grid) {
                    for (int j = grid; j < ab.size() - grid; j += grid) {
                        translate(1260, 300, 7);
                        float f = lerpedBuffer[i] * halfH * 3.0f;
                        noStroke();
                        fill(255);
                        stroke(255, 255);
                        float g = map(ab.get(i), -1, 1, 0, 255);
                        float r = (i % 2 == 0) ? radius : radius;
                        float theta = map(i, 0, sides, 0, TWO_PI);
                        float x = cx + sin(theta) + r / 4;
                        stroke(g, 255, 255);
                        strokeWeight(2);
                        noFill();
                        rotate(-10);
                        circle(x, y, f);
                    }
                }
            }

                break;
                //some other circles inside another circle that change by the upstreaming of the sound
            case 5: {
                background(0);
                float radius = map(smoothedAmplitude, 0, 0.1f, 50, 300);
                int points = (int) map(500, 0, 255, 3, 10);
                int sides = points * 2;

                for (int i = 0; i < ab.size(); i++) {
                    float g = map(ab.get(i), -1, 1, 0, 255);
                    float r = (i % 2 == 0) ? radius : radius;
                    float theta = map(i, 0, sides, 0, TWO_PI);
                    float x = cx + sin(theta) * r / 4;
                    float y = cy - cos(theta) * r / 4;
                    stroke(g, 255, 255);
                    float f = lerpedBuffer[i] + halfH + 0.5f;
                    strokeWeight(2);
                    noFill();
                    circle(x, y, f);
                }
            }

                break;

                //sun shape with circle inside it and lines,and dotes, that change its size by the change in the song
            case 6: {
                background(0);
                int grid = 20;
                float radius = map(smoothedAmplitude, 0, 0.1f, 50, 300);
                int points = (int) map(10, 0, 255, 3, 10);
                int sides = points * 2;
                for (int i = grid; i < ab.size() - grid; i += grid) {
                    for (int j = grid; j < ab.size() - grid; j += grid) {
                        translate(width / 2, height / 2);
                        float f = lerpedBuffer[i] * halfH * 1.5f;
                        noStroke();
                        fill(255);
                        rect(i - f, j - f, 3, 3);
                        stroke(255, 255);
                        line(i / 4 - f, j / 4 - f, i / 4, j / 4);
                        translate(300, 600);
                        float g = map(ab.get(i), -1, 1, 0, 255);
                        float r = (i % 2 == 0) ? radius : radius;
                        float theta = map(i, 0, sides, 0, PI);
                        float x = cx + sin(theta) + r / 2;
                        float y = cy - cos(theta) + r / 2;
                        translate(10, 10);
                        stroke(g, 255, 255);
                        strokeWeight(2);
                        noFill();
                        rotate(10);
                        circle(x, y, f*2);

                    }
                }
            }
                break;

        }

    }
    //off set the color 
    private float colorFromOffset(int xo) {
        return (int) ((offset + off_max) / (2.0 * off_max) * 255);
    }
}