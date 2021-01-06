package eshop.Home_Task_7_spring.beans;

import org.springframework.stereotype.Component;

//@Component 1st variant
public class ThirdBeanImpl implements ThirdBean {
    private String data;
    @Override
    public String getData() {
        return this.data;
    }

    @Override
    public void setData(String data) {
        this.data = data;
    }
}
