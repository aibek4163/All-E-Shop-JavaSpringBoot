package eshop.Home_Task_7_spring.services;

public interface TestServices {
    String getTestData();
    int getTestDataInt();
    void setTestData(String testData);
    void setTestIntData(int testIntData);

    boolean auth(String email,String password);
}
