public class Vector2 {
    public float x;
    public float y;
    public float magnitude;

    public  Vector2(float _x, float _y){
        x = _x;
        y = _y;

        magnitude = (float) Math.sqrt(x * x + y * y);
    }

    public void Normalize(){
        x = x/magnitude;
        y = y/magnitude;
        magnitude = 1;
    }
}
