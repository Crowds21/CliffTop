package test;

import com.springclifftop.api.FeiShuAPI;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@RunWith(SpringRunner.class)

class FeiShuAPITest {

    @Test
    void getAppAccessToken() {
        FeiShuAPI fs = new FeiShuAPI();
        fs.getTenantAccessToken();
    }
}

