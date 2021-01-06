package eshop.Home_Task_7_spring.config;

import eshop.Home_Task_7_spring.beans.FirstBean;
import eshop.Home_Task_7_spring.beans.ItemTransport;
import eshop.Home_Task_7_spring.beans.ThirdBean;
import eshop.Home_Task_7_spring.beans.ThirdBeanImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {
    @Bean
    public FirstBean firstBean(){
        return new FirstBean();
    }

    @Bean
    public FirstBean secondBean(){
        return new FirstBean("Aibek",20);
    }

    @Bean
    public ThirdBean thirdBean(){
        return new ThirdBeanImpl();
    }

    @Bean
    public ItemTransport itemTransport(){
        return new ItemTransport();
    }
}
