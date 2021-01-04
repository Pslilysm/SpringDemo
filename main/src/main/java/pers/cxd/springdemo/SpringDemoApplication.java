package pers.cxd.springdemo;

import android.util.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
public class SpringDemoApplication {

    private final String TAG = Log.TAG + SpringDemoApplication.class.getSimpleName();

    public static void main(String[] args) {
        SpringApplication.run(SpringDemoApplication.class, args);
    }

    @RequestMapping("/")
    public String index(){
        Log.v(TAG, "index: ");
        return "index.html";
    }

}
