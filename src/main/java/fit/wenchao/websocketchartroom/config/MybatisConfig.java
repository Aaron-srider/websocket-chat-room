package fit.wenchao.websocketchartroom.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(value = "fit.wenchao.websocketchartroom.dao.mapper")
public class MybatisConfig {
 
}