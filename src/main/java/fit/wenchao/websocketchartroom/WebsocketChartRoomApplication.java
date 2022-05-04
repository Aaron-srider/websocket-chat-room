package fit.wenchao.websocketchartroom;

import fit.wenchao.commonComponentSpringBootStarter.ApplicationContextHolder;
import fit.wenchao.mybatisCodeGen.codegen.MybatisCodeGenerator;
import fit.wenchao.websocketchartroom.ws.ChatEndPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebsocketChartRoomApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsocketChartRoomApplication.class, args);
		//MybatisCodeGenerator.generateStructureCode();
		//for(int i = 0 ;;i++) {
		//	ChatEndPoint userDao = ApplicationContextHolder.getApplicationContext().getBean("chatEndPoint", ChatEndPoint.class);
		//	System.out.println(userDao);
		//}

	}

}
