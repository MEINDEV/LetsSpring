package com.meindev.exercises;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.HashMap;

public class EmployeeTest{

    @Test
    public void hasBugTest(){
        Map<Employee, String> roles = new HashMap<>();
        roles.put(new Employee("E1"), "Developer");
        assertEquals(roles.get(new Employee("E1")),"Developer");
    }
}