package support;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public final class JavaScriptUtils {

    private JavaScriptUtils() {
        // Prevent instantiation
    }

    private static JavascriptExecutor getJsExecutor() {
        return (JavascriptExecutor) DriverManager.getDriver();
    }

    public static void scrollIntoView(WebElement element) {
        getJsExecutor().executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    public static void clickElement(WebElement element) {
        getJsExecutor().executeScript("arguments[0].click();", element);
    }
}
