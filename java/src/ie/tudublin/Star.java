background(0);
for(int i = 0 ; i < ab.size() ; i ++)
{
    //float c = map(ab.get(i), -1, 1, 0, 255);
    float c = map(i, 0, ab.size(), 0, 255);
    stroke(c, 10, 100); //sets up the color
    float f = lerpedBuffer[i] * 5.0f + halfH;
    noFill();
    translate(cx, cy, 0);
    box(f);                 
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