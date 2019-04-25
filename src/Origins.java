import java.awt.*;

public class Origins{
    Vector2 topLeft, topRight, botLeft, botRight;
    public Origins(Rectangle rectangle, int heightOffset, int skinWidth) {
        topLeft = new Vector2(rectangle.x + skinWidth, rectangle.y + skinWidth + heightOffset);
        topRight = new Vector2(rectangle.x + rectangle.width - skinWidth, rectangle.y + skinWidth + heightOffset);
        botLeft = new Vector2(rectangle.x + skinWidth, rectangle.y + rectangle.height - skinWidth);
        botRight = new Vector2(rectangle.x + rectangle.width - skinWidth, rectangle.y + rectangle.height - skinWidth);
    }
}