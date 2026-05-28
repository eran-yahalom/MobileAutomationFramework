package automation.utils;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import java.time.Duration;
import java.util.Collections;

public class DrawingUtils {

    /**
     * מצייר קו אופקי יציב בדיוק במרכז פד הציור (באמצע המסך)
     */
    public static void drawOnDrawingPadByScreenSize(AppiumDriver driver) {
        // 1. שליפת מימדי המסך הפיזיים של האמולטור/מכשיר
        int screenWidth = driver.manage().window().getSize().getWidth();
        int screenHeight = driver.manage().window().getSize().getHeight();

        // 2. חישוב נקודות יחסיות (מחוץ לאזור הכותרת והכפתורים התחתונים)
        int startX = (int) (screenWidth * 0.25); // 25% מרוחב המסך מצד שמאל
        int endX = (int) (screenWidth * 0.75);   // 75% מרוחב המסך (לכיוון ימין)
        int centerY = (int) (screenHeight * 0.55); // 55% מגובה המסך (בול במרכז הפד הצהוב)

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence drawSequence = new Sequence(finger, 1);

        // תזוזה לנקודת ההתחלה, לחיצה, גלירה ימינה, ושחרור
        drawSequence.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, centerY));
        drawSequence.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        drawSequence.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), endX, centerY));
        drawSequence.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(drawSequence));
    }

    /**
     * מצייר חתימת X מושלמת בתוך גבולות הגזרה של הלוח
     */
    public static void drawXSignatureByScreenSize(AppiumDriver driver) {
        int screenWidth = driver.manage().window().getSize().getWidth();
        int screenHeight = driver.manage().window().getSize().getHeight();

        // הגדרת קופסת הגזרה הווירטואלית של הלוח
        int leftX = (int) (screenWidth * 0.30);
        int rightX = (int) (screenWidth * 0.70);
        int topY = (int) (screenHeight * 0.40);
        int bottomY = (int) (screenHeight * 0.70);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");

        // קו ראשון: משמאל-למעלה לימין-למטה
        Sequence line1 = new Sequence(finger, 1);
        line1.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), leftX, topY));
        line1.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        line1.addAction(finger.createPointerMove(Duration.ofMillis(400), PointerInput.Origin.viewport(), rightX, bottomY));
        line1.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        // קו שני: מימין-למעלה לשמאל-למטה
        Sequence line2 = new Sequence(finger, 2);
        line2.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), rightX, topY));
        line2.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        line2.addAction(finger.createPointerMove(Duration.ofMillis(400), PointerInput.Origin.viewport(), leftX, bottomY));
        line2.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(line1));
        driver.perform(Collections.singletonList(line2));
    }
}