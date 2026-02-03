package com.ruoyi;

import com.ruoyi.common.config.YAMLPropertySourceFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.PropertySource;


/**
 * 启动程序
 * 
 * @author ruoyi
 */
@SpringBootApplication
//@MapperScan("com.ruoyi.*.mapper")
//@PropertySource(value = {"file:./application-druid.yml"},encoding = "UTF-8", ignoreResourceNotFound = true, factory = YAMLPropertySourceFactory.class)
public class RuoYiApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(RuoYiApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ 启动成功");
    }
}
