public class Main{
    public static void main(String[] args){
        SwingControlDemo swingControlDemo = new SwingControlDemo();
        swingControlDemo.showEventDemo();
        double last_time = System.nanoTime();
        while(true){
            double time = System.nanoTime();
            double delta_time = (double) ((time - last_time) / 1000000);
            last_time = time;
            swingControlDemo.deltaTime = delta_time;
            swingControlDemo.Update();
        }
    }
}