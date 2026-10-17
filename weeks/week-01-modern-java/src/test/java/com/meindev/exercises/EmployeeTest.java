package com.meindev.exercises;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.HashMap;

public class EmployeeTest{

    @Test
    public void hasBugTest(){
        Map<Employee, String> roles = new HashMap<>();
        roles.put(new Employee("E1"), "Developer");
        assertThat(roles.get(new Employee("E1")).isEqualTo("Developer"));
    }
}