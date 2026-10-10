# Week 1 Exercises

## Lesson 1: Records and immutability (about 1.5 h)

Put code in `src/main/java/com/meindev/exercises/` and tests in `src/test/java/com/meindev/exercises/`.
Use **AssertJ** in your tests:
```java
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

assertThat(actual).isEqualTo(expected);
assertThat(list).containsExactly("a", "b");
assertThatThrownBy(() -> new Temperature(-300))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("absolute zero");
```

### E1.1 `Temperature` record (15 min)
- `record Temperature(double celsius)`
- Throw `IllegalArgumentException` (message contains "absolute zero") if `celsius < -273.15`.
- Method `double fahrenheit()` → `celsius * 9 / 5 + 32`.
- Static factory `Temperature ofFahrenheit(double f)`.
- **Tests:** a valid value, the boundary `-273.15` (allowed), below the boundary (throws), and a round trip with `ofFahrenheit(212).celsius()` giving `100.0`.
  > Comparing doubles: use `isCloseTo(100.0, within(0.0001))`. Why do you think that's needed?

### E1.2 Fix the `Employee` bug (10 min)
Type this, then **predict** the output before you run it:
```java
class Employee {
    private final String id;
    Employee(String id) { this.id = id; }
    @Override public boolean equals(Object o) {
        return o instanceof Employee e && id.equals(e.id);
    }
}
// in a test:
Map<Employee, String> roles = new HashMap<>();
roles.put(new Employee("E1"), "Developer");
System.out.println(roles.get(new Employee("E1")));
```
1. Write a test that **proves the bug**: it should expect `"Developer"` and fail.
2. Fix it in **two** ways: (a) add `hashCode`, (b) convert it to a record. Keep (b).

### E1.3 Truly immutable `Team` (20 min)
- `record Team(String name, List<String> members)`
- `name` must not be null or blank, and `members` must not be null.
- Write **2 tests that prove immutability**:
  1. Changing the original list after construction does **not** change `team.members()`.
  2. `team.members().add("x")` throws `UnsupportedOperationException`.
- Add a "wither": `Team withMember(String newMember)` that returns a **new** `Team` and leaves the original unchanged. Test that too.

### E1.4 Think (no code, answer in chat)
`String` is immutable. So what does this print, and why?
```java
String s = "java";
s.toUpperCase();
System.out.println(s);
```

---
When you're done: run `mvn test`, commit (`feat(week-01): lesson 1 exercises`), and say **"exercises done"**
together with your answers to the lesson's check questions and E1.4.
