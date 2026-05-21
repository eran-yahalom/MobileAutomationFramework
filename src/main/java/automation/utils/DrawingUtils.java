package automation.utils;

import io.appium.java_client.AppiumDriver;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.List;

@Log4j2
public class DrawingUtils {

    // ==============================
    //  Drawing helper
    // ==============================
    public static void drawXSignature(AppiumDriver driver, By drawingPadLocator) {

        WebElement drawingPad = driver.findElement(drawingPadLocator);
        Rectangle rect = drawingPad.getRect();

        int leftX = rect.getX() + (int) (rect.getWidth() * 0.2);
        int rightX = rect.getX() + (int) (rect.getWidth() * 0.8);
        int topY = rect.getY() + (int) (rect.getHeight() * 0.2);
        int bottomY = rect.getY() + (int) (rect.getHeight() * 0.8);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");

        Sequence line1 = new Sequence(finger, 1);

        line1.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), leftX, topY));
        line1.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

        line1.addAction(finger.createPointerMove(Duration.ofMillis(400), PointerInput.Origin.viewport(), rightX, bottomY));
        line1.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        Sequence line2 = new Sequence(finger, 2);

        line2.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), rightX, topY));

        line2.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

        line2.addAction(finger.createPointerMove(Duration.ofMillis(400), PointerInput.Origin.viewport(), leftX, bottomY));

        line2.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(List.of(line1));
        driver.perform(List.of(line2));
    }

    public static boolean drawOnDrawingPad(AppiumDriver driver, WebElement drawingPad) {
        try {

            int startX = drawingPad.getLocation().getX() + drawingPad.getSize().getWidth() / 4;
            int startY = drawingPad.getLocation().getY() + drawingPad.getSize().getHeight() / 4;
            int endX = drawingPad.getLocation().getX() + (drawingPad.getSize().getWidth() * 3) / 4;
            int endY = drawingPad.getLocation().getY() + (drawingPad.getSize().getHeight() * 3) / 4;

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence drawSequence = new Sequence(finger, 1);

            drawSequence.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
            drawSequence.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            drawSequence.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), endX, endY));
            drawSequence.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(List.of(drawSequence));

            return true;
        } catch (Exception e) {
            log.info("Failed to draw on the drawing pad: {}", e.getMessage());
            return false;
        }
    }
}
