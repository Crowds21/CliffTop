

import main.utils.TerminalUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TerminalUtilsTest {

    @Test
    void termianlOutputWithBlue() {
        for(int i = 31 ; i < 37 ; i++){
            TerminalUtils. colorfulOutputInTerminal(String.valueOf(i),i);
        }

    }

    @Test
    void colorfulOutputInTerminal() {
    }

    @Test
    void testColorfulOutputInTerminal() {
    }
}