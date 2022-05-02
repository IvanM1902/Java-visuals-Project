package ie.tudublin;



public class Main
{
    public static void visualisation()
	{
		String[] a = {"MAIN"};
        processing.core.PApplet.runSketch( a, new Visualisation());
    }
   
    public static void main(String[] args)
    {
        visualisation();
        
    }
}