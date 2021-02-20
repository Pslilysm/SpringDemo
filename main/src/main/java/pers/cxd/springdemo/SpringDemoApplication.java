package pers.cxd.springdemo;

import android.util.Log;
import com.zaxxer.hikari.HikariDataSource;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.ibatis.binding.MapperProxy;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pers.cxd.springdemo.mapper.ApplicationMapper;
import pers.cxd.springdemo.socket.ServerSocketService;
import pers.cxd.springdemo.util.refrection.ReflectionUtil;

import java.io.IOException;
import java.lang.reflect.Proxy;

@SpringBootApplication
@RestController
public class SpringDemoApplication {

    final static String TAG = SpringDemoApplication.class.getSimpleName();

    public static void main(String[] args) {
        SpringApplication.run(SpringDemoApplication.class, args);
        setDBPoolSize();
        acceptSocket();
    }

    private static void setDBPoolSize(){
        OkHttpClient client = new OkHttpClient.Builder()
                .proxy(java.net.Proxy.NO_PROXY)
                .build();
        Request request = new Request.Builder()
                .url("http://localhost/setDBPoolSize?poolSize=100")
                .build();
        try (Response response = client.newCall(request).execute()) {
//            String ret = response.body().string();
//            Log.i(TAG, "main: " + ret);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void acceptSocket(){
        try {
            ServerSocketService.getInstance().accept(5050);
        } catch (IOException e) {
            Log.e(TAG, "main: ", e);
            System.exit(-1);
        }
    }

    @Autowired
    ApplicationMapper mApplicationMapper;

    @RequestMapping("/setDBPoolSize")
    public Object init(@RequestParam("poolSize") int poolSize){
        int ret = mApplicationMapper.ping();
        Log.i(TAG, "init: " + ret);
        MapperProxy<?> mapperProxy = (MapperProxy<?>) Proxy.getInvocationHandler(mApplicationMapper);
        try {
            SqlSessionTemplate sqlSession = ReflectionUtil.getField(mapperProxy , "sqlSession");
            DefaultSqlSessionFactory sqlSessionFactory = (DefaultSqlSessionFactory) sqlSession.getSqlSessionFactory();
            Configuration configuration = ReflectionUtil.getField(sqlSessionFactory, "configuration");
            Environment environment = configuration.getEnvironment();
            HikariDataSource hikariDataSource = (HikariDataSource) environment.getDataSource();
            Log.i(TAG, "init: before " + hikariDataSource.getMaximumPoolSize());
            hikariDataSource.setMaximumPoolSize(poolSize);
            Log.i(TAG, "init: after " + hikariDataSource.getMaximumPoolSize());
        } catch (ReflectiveOperationException e) {
            Log.e(TAG, "init: ", e);
        }
        return "set pool size success";
    }

}
