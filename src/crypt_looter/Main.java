package crypt_looter;

public class Main{
    public static void main(String[] args){
        MainController mainController = new MainController();
        double last_time = System.nanoTime();
        while(true){
            double time = System.nanoTime();
            // Not actually in nano-seconds - represented in micro-seconds
            double delta_time = (double) ((time - last_time) / 1000000);
            last_time = time;
            mainController.deltaTime = delta_time;
            mainController.update();
        }
    }
}