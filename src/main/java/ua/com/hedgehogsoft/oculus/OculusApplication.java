package ua.com.hedgehogsoft.oculus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OculusApplication extends SpringBootServletInitializer
{

   public static void main(String[] args)
   {
      SpringApplication.run(OculusApplication.class, args);
   }

   @Override
   protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
   {
      return application.sources(OculusApplication.class);
   }

   /*@Bean
   public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
       ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
       threadPoolTaskExecutor.setCorePoolSize(5);
       threadPoolTaskExecutor.setMaxPoolSize(10);
       threadPoolTaskExecutor.setQueueCapacity(25);
       return threadPoolTaskExecutor;
   }*/
}
