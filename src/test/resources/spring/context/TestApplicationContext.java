package spring.context;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath*:**/junit-context.xml")
public class TestApplicationContext {

}
