package eshop.Home_Task_7_spring.services.impl;

import eshop.Home_Task_7_spring.services.TestServices;
import org.springframework.stereotype.Service;

@Service
public class TestServicesImpl implements TestServices {
    private String testData;
    private int testIntdata;
    @Override
    public String getTestData() {
        return "Some Test Data " + this.testData;
    }

    @Override
    public int getTestDataInt() {
        return this.testIntdata;
    }

    @Override
    public void setTestData(String testData) {
        this.testData = testData;
    }

    @Override
    public void setTestIntData(int testIntData) {
        this.testIntdata = testIntData;
    }

    @Override
    public boolean auth(String email, String password) {
        return false;
    }
}
