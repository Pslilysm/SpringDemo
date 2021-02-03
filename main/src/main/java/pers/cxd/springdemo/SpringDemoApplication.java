package pers.cxd.springdemo;

import org.apache.commons.io.IOUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
@Controller
public class SpringDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDemoApplication.class, args);
        InputStream is = SpringDemoApplication.class.getResourceAsStream("/log4j.properties");
        try {
            Log.i("SpringDemoApplication", "main: " + IOUtils.toString(is, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/")
    public String index() throws IOException {
        return IOUtils.toString(getClass().getResourceAsStream("/static/index.html"), StandardCharsets.UTF_8);
    }

}
