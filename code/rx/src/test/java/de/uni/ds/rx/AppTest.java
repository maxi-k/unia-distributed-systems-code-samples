/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package de.uni.ds.rx;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test void appHasGui() {
        App app = new App();
        assertNotNull(app.gui, "app should have a gui");
    }
}
