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

---
---

## Lesson 2: Enums, switch, pattern matching (about 1.5 h)

Same folders as before (`exercises/`). **Rules for this set:**
- Make every test **fail first** (red), then make it pass (green). Use `List<String>`, never a raw `List`.
- Test method names describe behaviour: `cannotShipCancelledOrder()`, not `test1()`.

### E2.1 `Size` enum with fields (15 min)
Coffee sizes: `SMALL("S", 250)`, `MEDIUM("M", 350)`, `LARGE("L", 500)`, where the numbers are millilitres.
- Accessor `int ml()`.
- `static Size fromCode(String code)`: case-insensitive (`"m"` and `"M"` both give `MEDIUM`). Unknown or null input
  throws `IllegalArgumentException` with a message containing the bad code.
- `Size next()`: the next bigger size. `LARGE.next()` returns `LARGE` (it doesn't grow forever).
  Use a **switch expression** with no `default`.
- **Tests:** at least 4, including lowercase input, unknown code, null code, and `LARGE.next()`.

### E2.2 `TicketStatus` state machine (25 min)
A support-ticket lifecycle: `OPEN → IN_PROGRESS → RESOLVED → CLOSED`.
- `OPEN` can also go straight to `CLOSED` (a duplicate ticket).
- `RESOLVED` can go back to `IN_PROGRESS` (the customer says "still broken").
- `CLOSED` is final.
- Methods: `boolean canMoveTo(TicketStatus next)`, `boolean isFinal()`.
- **Tests:** every allowed move returns `true`, at least 3 forbidden moves return `false`, and `CLOSED.isFinal()`.
  > Bonus: test all allowed moves in one test using a `@ParameterizedTest` with `@CsvSource`.
  > Look it up in the JUnit 5 docs. Learning from documentation is a core skill.

### E2.3 Notification sender (30 min)
```java
public sealed interface Notification permits Email, Sms, Push { }
public record Email(String to, String subject) implements Notification { }
public record Sms(String phone, String text) implements Notification { }
public record Push(String deviceId, String title) implements Notification { }
```
(Put each type in its own file, or nest them like `Payment` in the lesson. Your choice.)

Write `static String describe(Notification n)` using a **switch with record patterns**, with no `default`:
| Input | Output |
|---|---|
| `Email("a@x.com", "Hi")` | `Email to a@x.com: Hi` |
| `Sms` with text of 160 characters or fewer | `SMS to +91999: <text>` |
| `Sms` with text longer than 160 characters | `Long SMS (2 parts) to +91999`. Parts = text length ÷ 160, rounded **up** |
| `Push("dev-1", "Sale!")` | `Push to dev-1: Sale!` |

- **Tests:** one per row, plus the boundary: exactly 160 characters is still a normal SMS, and 161 is 2 parts.
  > Tip: `"a".repeat(161)` builds a 161-character string.
- Hint on rounding up with integers: what is `(161 + 159) / 160`?

### E2.4 Think (answer in chat, no code)
In E2.1, if you had written `default -> LARGE` in `next()`, and someone later adds `EXTRA_LARGE`, what
bug would appear? Would the compiler warn you?

---
When you're done: run `mvn test` (no warnings!), commit (`feat(week-01): lesson 2 exercises`), and say
**"exercises done"** with your answers to the Lesson 2 check questions and E2.4.
