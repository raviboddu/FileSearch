package FileSeach.filesearch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootApplication(scanBasePackages={"com.elsevier.search"})//
public class FilesearchApplicationTests {

	@Test
	public void contextLoads() {
	}

}
