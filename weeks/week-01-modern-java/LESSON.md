# Week 1: Modern Java

| Lesson | Topic | Status |
|---|---|---|
| 1 | Immutability, `equals`/`hashCode`, records | ✅ done |
| 2 | Enums, switch expressions, pattern matching | ✅ done |
| **3** | **`var`, text blocks, String vs StringBuilder, BigDecimal for money** | ← you are here |
| Task | Immutable `IndianRupee` and `BankAccount` (graded) | after lesson 3 |

Project: `weeks/week-01-modern-java` (the `pom.xml` is ready, with JUnit 5 + **AssertJ**).
Type the examples into `src/main/java/com/meindev/examples/`.

---

# Lesson 1: Immutability, equals/hashCode, and Records

## 1. Theory (short)

**Immutable object** = once it is created, its data can **never change**.
You already use immutable objects every day: `String`, `Integer`, `LocalDate`, `List.of(...)`.

Why industry loves immutability:
| Benefit | Meaning |
|---|---|
| **No surprises** | No other code can change your object behind your back |
| **Thread-safe for free** | Many threads can read it at the same time with no locks (Week 6) |
| **Safe as a `HashMap` key / in a `HashSet`** | Its hash code never changes (you'll see the bug below) |
| **Easy to test and reason about** | Same object means same state, forever |

**Rule of thumb used in industry:** make everything immutable *unless* you have a good reason not to.

---

## 2. Example 1: the bug that mutable objects cause
Create `examples/MutablePointBug.java`:
```java
package com.meindev.examples;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MutablePointBug {

    static class Point {               // MUTABLE: has setters
        private int x;
        private int y;

        Point(int x, int y) { this.x = x; this.y = y; }

        void setX(int x) { this.x = x; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point other)) return false;   // pattern matching instanceof (Java 16+)
            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() { return Objects.hash(x, y); }
    }

    public static void main(String[] args) {
        Set<Point> visited = new HashSet<>();
        Point p = new Point(1, 2);
        visited.add(p);

        System.out.println(visited.contains(p));   // true

        p.setX(99);                                 // mutate AFTER adding to the set

        System.out.println(visited.contains(p));   // false !!  the set "lost" the object
        System.out.println(visited.size());        // 1 - it is still in there, just unreachable
    }
}
```
**Why?** A `HashSet` puts each object in a "bucket" chosen by its `hashCode()`. Changing `x`
changes the hash code, so the set now looks in the **wrong bucket**.
Bugs like this show up in production and are hard to find. Immutable objects make them impossible.

---

## 3. The `equals` / `hashCode` contract (must know for interviews)
1. If `a.equals(b)` is true, then `a.hashCode() == b.hashCode()` **must** be true.
2. Two objects with the same hash code are *not* necessarily equal (a *collision* is allowed).
3. **If you override `equals`, you must override `hashCode`.**

Example 2: break rule 3 and see what happens. Create `examples/EqualsOnlyBug.java`:
```java
package com.meindev.examples;

import java.util.HashSet;
import java.util.Set;

public class EqualsOnlyBug {

    static final class Email {
        private final String value;
        Email(String value) { this.value = value; }

        @Override
        public boolean equals(Object o) {
            return o instanceof Email other && value.equals(other.value);
        }
        // hashCode() NOT overridden: it uses Object's version (based on memory identity)
    }

    public static void main(String[] args) {
        Email a = new Email("asha@mail.com");
        Email b = new Email("asha@mail.com");

        System.out.println(a.equals(b));          // true

        Set<Email> unique = new HashSet<>();
        unique.add(a);
        unique.add(b);
        System.out.println(unique.size());        // 2 !!  "duplicates" in a Set
    }
}
```
**Your turn:** add `hashCode()` using `Objects.hashCode(value)` and run it again. The size should become `1`.

---

## 4. Example 3: an immutable class the classic way (the 5 rules)
Before Java 16 you wrote immutable classes like this. You will still see this style in older code at work.
Create `examples/MoneyClassic.java`:
```java
package com.meindev.examples;

import java.util.Objects;

public final class MoneyClassic {                 // Rule 1: final class, so no subclass can add mutability

    private final long amountInPaise;             // Rule 2: private final fields
    private final String currency;

    public MoneyClassic(long amountInPaise, String currency) {
        if (amountInPaise < 0) {
            throw new IllegalArgumentException("amount must be >= 0");
        }
        this.amountInPaise = amountInPaise;
        this.currency = Objects.requireNonNull(currency, "currency");   // fail fast on null
    }

    public long getAmountInPaise() { return amountInPaise; }   // Rule 3: getters only, no setters
    public String getCurrency() { return currency; }

    public MoneyClassic plus(MoneyClassic other) {             // Rule 4: "changes" return a NEW object
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("currency mismatch");
        }
        return new MoneyClassic(amountInPaise + other.amountInPaise, currency);
    }

    // Rule 5: defensive copies of mutable fields (none here; see Example 5)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MoneyClassic other)) return false;
        return amountInPaise == other.amountInPaise && currency.equals(other.currency);
    }

    @Override
    public int hashCode() { return Objects.hash(amountInPaise, currency); }

    @Override
    public String toString() { return "MoneyClassic[" + amountInPaise + " " + currency + "]"; }
}
```
That's about 45 lines for 2 fields. Most of it is *boilerplate* (repetitive code that adds no logic).

---

## 5. Example 4: records (Java 16+) do the same thing in a few lines
```java
public record IndianRupee(long amountInPaise, String currency) { }
```
That single line automatically gives you:
| Generated | Notes |
|---|---|
| `private final` fields | immutable by default |
| Canonical constructor `IndianRupee(long, String)` | |
| Accessors `amountInPaise()`, `currency()` | **No `get` prefix!** |
| `equals`, `hashCode`, `toString` | correct, based on all components |

A record is a **final** class, and it **cannot extend** another class (it *can* implement interfaces).

Now let's make it production-quality. Create `examples/IndianRupee.java`:
```java
package com.meindev.examples;

import java.util.Objects;

public record IndianRupee(long amountInPaise, String currency) {

    // COMPACT constructor: no parameter list, it runs BEFORE fields are assigned.
    // Use it for validation and normalization.
    public IndianRupee {
        if (amountInPaise < 0) {
            throw new IllegalArgumentException("amount must be >= 0, got " + amountInPaise);
        }
        Objects.requireNonNull(currency, "currency must not be null");
        currency = currency.strip().toUpperCase();   // normalize: " inr " becomes "INR"
    }

    // Static factory: a readable name instead of "new"
    public static IndianRupee inr(long rupees) {
        return new IndianRupee(rupees * 100, "INR");
    }

    public IndianRupee plus(IndianRupee other) {
        requireSameCurrency(other);
        return new IndianRupee(amountInPaise + other.amountInPaise, currency);
    }

    public boolean isGreaterThan(IndianRupee other) {
        requireSameCurrency(other);
        return amountInPaise > other.amountInPaise;
    }

    private void requireSameCurrency(IndianRupee other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException(currency + " vs " + other.currency);
        }
    }
}
```
Try it in a `main` method or a quick test:
```java
IndianRupee a = IndianRupee.inr(100);
IndianRupee b = new IndianRupee(10000, " inr ");
System.out.println(a);              // IndianRupee[amountInPaise=10000, currency=INR]
System.out.println(a.equals(b));    // true: compared by VALUE, not by memory
System.out.println(a.plus(b));      // a new IndianRupee object; a and b are unchanged
```
> 💡 We store money as `long` paise here to avoid floating-point errors. **Never use `double` for money.**
> In Lesson 3 you'll learn `BigDecimal`, the industry standard.

---

## 6. Example 5: the trap where records are only *shallowly* immutable
Create `examples/Team.java`:
```java
package com.meindev.examples;

import java.util.List;

public record Team(String name, List<String> members) {

    public Team {
        members = List.copyOf(members);   // defensive copy, which is also unmodifiable (and rejects nulls)
    }
}
```
Without the `List.copyOf` line:
```java
List<String> list = new ArrayList<>(List.of("Asha", "Ravi"));
Team team = new Team("Backend", list);
list.add("Intruder");                 // changes the team from the outside!
team.members().add("Hacker");         // also allowed!
```
With `List.copyOf`, the first line no longer affects the team, and the second throws `UnsupportedOperationException`.
**The record's fields are final, but the List *object* inside can still change.** Always copy mutable components.

---

## 7. Where records are used in industry
| ✅ Use records for | ❌ Don't use records for |
|---|---|
| DTOs (request and response bodies in REST APIs, Week 10) | JPA entities (they need mutability and a no-arg constructor, Week 12) |
| Value objects: `IndianRupee`, `Email`, `Address` | Classes that need to extend another class |
| Events and messages (Kafka, Week 22) | Objects whose state must change over time (for example a `BankAccount` balance) |
| Map keys, configuration (`@ConfigurationProperties`) | |

## 8. Common mistakes
| Mistake | Fix |
|---|---|
| Calling `money.getCurrency()` on a record | Record accessors are `money.currency()` |
| Storing a `List` or array in a record without copying it | `List.copyOf(...)` in the compact constructor |
| Array components (`int[]`) in records | Arrays use *reference* equality, so `equals` breaks. Use `List<Integer>` |
| Overriding `equals` but not `hashCode` | Always both, or just use a record |
| Using `double` for money | `long` minor units, or `BigDecimal` |

---

## 9. Quick check questions (answer in chat)
1. In Example 1, why does `visited.contains(p)` return `false` after `setX(99)`?
2. A record has a field `List<String> tags`. Is the record fully immutable? What must you add?
3. What is the difference between a *compact constructor* and a normal constructor in a record?

Then do [EXERCISES.md](EXERCISES.md).

---
---

# Lesson 2: Enums, Switch Expressions, Pattern Matching

## 1. Enums: a fixed set of named values

### The problem enums solve
```java
// ❌ Old style: "magic" constants
static final int STATUS_CREATED = 0;
static final int STATUS_PAID = 1;
void ship(int status) { ... }
ship(42);        // compiles! but 42 means nothing
```
An **enum** is a type with a **fixed list of instances**, all known at compile time:
```java
public enum OrderStatus { CREATED, PAID, SHIPPED, DELIVERED, CANCELLED }

void ship(OrderStatus status) { ... }
ship(42);        // ❌ compile error, which is exactly what we want
```
Enums are **type-safe**, **immutable singletons** (exactly one `PAID` object exists), and they work with `==`.

### Built-in methods
| Method | Example | Result |
|---|---|---|
| `values()` | `OrderStatus.values()` | array of all constants, in declared order |
| `valueOf(String)` | `OrderStatus.valueOf("PAID")` | `PAID`, or throws `IllegalArgumentException` for an unknown name |
| `name()` | `PAID.name()` | `"PAID"` |
| `ordinal()` | `PAID.ordinal()` | `1` (its position) ⚠️ see the mistakes table |

---

## 2. Example 1: enums with fields, a constructor and methods
Enums are full classes. Create `examples/Plan.java`:
```java
package com.meindev.examples;

import java.util.Arrays;

public enum Plan {
    FREE("free", 0, 1),
    PRO("pro", 49_900, 10),          // _ in numbers is just for readability: 49900 paise = ₹499
    ENTERPRISE("enterprise", 199_900, 1_000);

    private final String code;
    private final long monthlyPricePaise;
    private final int maxUsers;

    // An enum constructor is always private; you can't call "new Plan(...)"
    Plan(String code, long monthlyPricePaise, int maxUsers) {
        this.code = code;
        this.monthlyPricePaise = monthlyPricePaise;
        this.maxUsers = maxUsers;
    }

    public long monthlyPricePaise() { return monthlyPricePaise; }
    public int maxUsers() { return maxUsers; }

    public boolean allows(int users) {
        return users <= maxUsers;
    }

    // Lookup by an external code (for example from an API request).
    // Industry practice: don't expose valueOf("PRO") to users, because names are internal.
    public static Plan fromCode(String code) {
        return Arrays.stream(values())
                .filter(plan -> plan.code.equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown plan: " + code));
    }
}
```
```java
Plan plan = Plan.fromCode("Pro");
System.out.println(plan);                   // PRO
System.out.println(plan.allows(25));        // false
System.out.println(plan == Plan.PRO);       // true: == is safe for enums
```

### EnumSet and EnumMap
Special, very fast collections for enum keys:
```java
Set<Plan> paidPlans = EnumSet.of(Plan.PRO, Plan.ENTERPRISE);
Map<Plan, Integer> subscribers = new EnumMap<>(Plan.class);
subscribers.put(Plan.PRO, 120);
```

---

## 3. Switch expressions (Java 14+)

### The old switch *statement*, and its trap
```java
String label;
switch (status) {
    case CREATED:
        label = "New";
        break;
    case PAID:
        label = "Paid";            // ⚠️ forgot break: "falls through" to the next case!
    case SHIPPED:
        label = "On the way";
        break;
    default:
        label = "Other";
}
```

### The modern switch *expression*
```java
String label = switch (status) {
    case CREATED -> "New";
    case PAID -> "Paid";
    case SHIPPED, DELIVERED -> "On the way / done";     // several labels on one line
    case CANCELLED -> {
        System.out.println("cancelled order seen");
        yield "Cancelled";                              // "yield" returns a value from a block
    }
};
```
| Feature | Why it matters |
|---|---|
| `->` arrow | **No fall-through**, and no `break` needed |
| It's an **expression** | It produces a value, so you can assign it or `return` it directly |
| **Exhaustive** | For an enum, if you miss a constant the code **does not compile** |

> 🎯 **Industry tip:** for enums, **don't add `default`**. Then if someone adds a new constant
> (`REFUNDED`) next year, the compiler shows you **every switch that must handle it**.
> With `default`, the new value silently goes into "Other", which is a hidden bug.

---

## 4. Example 2: a real-world state machine
Order status rules are a common interview and production problem. Create `examples/OrderStatus.java`:
```java
package com.meindev.examples;

import java.util.EnumSet;
import java.util.Set;

public enum OrderStatus {
    CREATED, PAID, SHIPPED, DELIVERED, CANCELLED;

    public Set<OrderStatus> allowedNext() {
        return switch (this) {
            case CREATED -> EnumSet.of(PAID, CANCELLED);
            case PAID -> EnumSet.of(SHIPPED, CANCELLED);
            case SHIPPED -> EnumSet.of(DELIVERED);
            case DELIVERED, CANCELLED -> EnumSet.noneOf(OrderStatus.class);   // final states
        };
    }

    public boolean canMoveTo(OrderStatus next) {
        return allowedNext().contains(next);
    }

    public boolean isFinal() {
        return allowedNext().isEmpty();
    }
}
```
```java
System.out.println(OrderStatus.CREATED.canMoveTo(OrderStatus.PAID));       // true
System.out.println(OrderStatus.SHIPPED.canMoveTo(OrderStatus.CANCELLED));  // false
System.out.println(OrderStatus.DELIVERED.isFinal());                        // true
```

---

## 5. Pattern matching (Java 16–21)

### 5a. `instanceof` pattern (you've already used it in `equals`)
```java
// Old
if (obj instanceof String) {
    String s = (String) obj;          // manual cast
    System.out.println(s.length());
}
// New: test and cast in one step
if (obj instanceof String s) {
    System.out.println(s.length());
}
```

### 5b. Switch on **types** (Java 21)
```java
static String describe(Object obj) {
    return switch (obj) {
        case null -> "null value";                         // handle null explicitly
        case Integer i when i < 0 -> "negative int " + i;  // "when" = an extra condition (a guard)
        case Integer i -> "int " + i;
        case String s -> "text of length " + s.length();
        default -> "something else";
    };
}
```
> Order matters: put the **more specific** case (`Integer i when i < 0`) **before** the general one.

### 5c. Record patterns: take a record apart inside the `case`
```java
record Point(int x, int y) { }

static String where(Object obj) {
    return switch (obj) {
        case Point(int x, int y) when x == 0 && y == 0 -> "origin";
        case Point(int x, int y) -> "point at " + x + "," + y;   // x and y extracted directly
        default -> "not a point";
    };
}
```

---

## 6. Example 3: sealed types + records + switch = a powerful combination
A **sealed** interface lists *exactly* which types may implement it. You'll study `sealed` in depth
in Week 2. For now: because the compiler knows **all** the subtypes, a switch over them can be
exhaustive with **no `default`**, just like with enums.

Create `examples/Payment.java`:
```java
package com.meindev.examples;

public sealed interface Payment permits Payment.Card, Payment.Upi, Payment.Cash {

    record Card(String last4Digits, long amountPaise) implements Payment { }
    record Upi(String upiId, long amountPaise) implements Payment { }
    record Cash(long amountPaise) implements Payment { }

    // Fee rules: card 2%, UPI free up to ₹2000 then 0.5%, cash free
    static long feePaise(Payment payment) {
        return switch (payment) {
            case Card(String last4, long amount) -> amount * 2 / 100;
            case Upi(String id, long amount) when amount <= 200_000 -> 0;
            case Upi(String id, long amount) -> amount * 5 / 1000;
            case Cash cash -> 0;
        };   // no default: add a new Payment type and this stops compiling until you handle it
    }
}
```
```java
System.out.println(Payment.feePaise(new Payment.Card("4242", 100_000)));     // 2000 (₹20)
System.out.println(Payment.feePaise(new Payment.Upi("asha@upi", 150_000)));  // 0
System.out.println(Payment.feePaise(new Payment.Upi("asha@upi", 400_000)));  // 2000
```
This style (data in records, behaviour in an exhaustive switch) is called **data-oriented programming**.
It's increasingly common in modern Java codebases.

---

## 7. Common mistakes
| Mistake | Why it's bad | Fix |
|---|---|---|
| Storing `ordinal()` in a database | Re-ordering the constants silently corrupts the data | Store `name()` (JPA: `@Enumerated(EnumType.STRING)`, Week 12) |
| `valueOf(userInput)` with no handling | Throws on unknown input, and exposes internal names | A custom `fromCode()` with a clear error |
| `default` in an enum switch | Hides newly added constants | Leave it out and let the compiler check exhaustiveness |
| General case before a specific one | `case Integer i` before `case Integer i when i < 0` doesn't compile (the second can never match) | Specific first |
| Using `switch` on Strings for statuses | Typos compile fine | Use an enum |

## 8. Quick check questions (answer in chat)
1. Why is it a bad idea to store `ordinal()` in a database?
2. You add `REFUNDED` to `OrderStatus`. What happens to the `allowedNext()` switch, and why is that **good**?
3. In Example 3, what does `when` do, and why must `Upi(...) when amount <= 200_000` come **before** the plain `Upi(...)` case?

Then do **Lesson 2** in [EXERCISES.md](EXERCISES.md).

---
---

# Lesson 3: `var`, Text Blocks, Strings, and BigDecimal for Money

## 1. `var`: local variable type inference (Java 10+)
The compiler works out the type from the right-hand side. **It is still static typing**, so the type is fixed at compile time.
```java
var names = new ArrayList<String>();          // ArrayList<String>
var count = 10;                               // int
var plan = Plan.fromCode("pro");              // Plan
```
| ✅ Allowed | ❌ Not allowed |
|---|---|
| Local variables inside methods | Fields, method parameters, return types |
| `for (var item : items)` | `var x;` with no value (nothing to infer from) |
| | `var x = null;` |

**When to use it (industry guideline):** use `var` when the type is **obvious** from the right side.
```java
var customer = new Customer("Asha");           // ✅ obvious
var result = processor.process(order);         // ❌ what type is result? The reader has to guess
var list = new ArrayList<>();                  // ⚠️ trap: this is ArrayList<Object>!
```

---

## 2. Text blocks (Java 15+)
Multi-line strings without `\n` and `+`. They are very common for **SQL, JSON and test data**.
```java
// Old
String json = "{\n" +
              "  \"name\": \"Asha\",\n" +
              "  \"plan\": \"PRO\"\n" +
              "}";

// New
String json = """
        {
          "name": "Asha",
          "plan": "PRO"
        }
        """;
```
Rules:
- The content starts on the line **after** the opening `"""`.
- Common leading spaces are removed. The position of the **closing `"""`** decides the left margin.
- A closing `"""` on its own line adds a final `\n`. Put it at the end of the last line to avoid that.
- Fill in values with `.formatted(...)`:
```java
String sql = """
        SELECT id, name
        FROM customers
        WHERE plan = '%s' AND active = %b
        """.formatted("PRO", true);
```
> ⚠️ Never build SQL from **user input** this way: that's how *SQL injection* happens. In Week 11 you'll use
> parameterized queries. `formatted` is fine for log messages, test data and fixed text.

---

## 3. Strings: the 3 things every backend developer must know

### 3a. `==` compares references; `equals` compares content
```java
String a = "java";
String b = "java";
String c = new String("java");
String d = "ja" + "va".strip();     // computed at runtime

System.out.println(a == b);         // true  (both point to the same pooled literal)
System.out.println(a == c);         // false (c is a new object)
System.out.println(a == d);         // false
System.out.println(a.equals(d));    // true  ✅ always use equals for content
```
`a == b` working "sometimes" is exactly what makes this bug dangerous: it passes small tests and fails in production.

### 3b. Concatenating in a loop is slow; use `StringBuilder`
`String` is immutable, so every `+=` in a loop **copies the whole string** into a new object.
```java
// ❌ O(n²): for 100,000 items this is very slow
String csv = "";
for (String name : names) {
    csv += name + ",";
}

// ✅ O(n): StringBuilder is a MUTABLE buffer
var sb = new StringBuilder();
for (String name : names) {
    if (!sb.isEmpty()) sb.append(',');
    sb.append(name);
}
String csv = sb.toString();

// ✅✅ The simplest, if you just need to join:
String csv = String.join(",", names);
// or with streams: names.stream().collect(Collectors.joining(","))
```
> `"Hello, " + name + "!"` in a **single expression** is fine, because the compiler optimizes it. The problem is only `+=` **inside loops**.

### 3c. Useful String methods (Java 11+)
| Method | Example | Result |
|---|---|---|
| `isBlank()` | `"  ".isBlank()` | `true` |
| `strip()` | `"  hi ".strip()` | `"hi"` (Unicode-aware `trim`) |
| `repeat(n)` | `"-".repeat(5)` | `"-----"` |
| `lines()` | `"a\nb".lines().count()` | `2` |
| `formatted(...)` | `"%s has %d".formatted("Asha", 3)` | `"Asha has 3"` |
| `String.join` | `String.join("/", "a", "b")` | `"a/b"` |

### Example 1: a receipt builder
Create `examples/ReceiptPrinter.java`:
```java
package com.meindev.examples;

import java.util.List;

public class ReceiptPrinter {

    record Line(String item, int qty, long pricePaise) { }

    static String print(String customer, List<Line> lines) {
        var sb = new StringBuilder();
        sb.append("Customer: ").append(customer).append('\n');
        sb.append("-".repeat(30)).append('\n');

        long total = 0;
        for (var line : lines) {
            long lineTotal = line.qty() * line.pricePaise();
            total += lineTotal;
            sb.append("%-15s x%-3d %8.2f%n".formatted(line.item(), line.qty(), lineTotal / 100.0));
        }

        sb.append("-".repeat(30)).append('\n');
        sb.append("%-20s %8.2f%n".formatted("TOTAL", total / 100.0));
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.print(print("Asha", List.of(
                new Line("Coffee", 2, 15_000),
                new Line("Sandwich", 1, 22_050))));
    }
}
```
> `%-15s` means "string, left-aligned, 15 characters wide". `%8.2f` means "number, 8 characters wide, 2 decimals". `%n` is a newline.
> (`/ 100.0` is acceptable here because it's only for **display**. Never do arithmetic with money in `double`.)

---

## 4. BigDecimal: the industry standard for money

### Why not `double`?
```java
System.out.println(0.1 + 0.2);            // 0.30000000000000004
System.out.println(1.10 - 1.00);          // 0.10000000000000009
```
`double` stores numbers in **binary**, and most decimal fractions (like 0.1) can't be stored exactly.
For a bank, "almost correct" means **wrong**. That's why every payment system uses `BigDecimal` (or `long` paise, like you did in Lesson 1).

### The 5 BigDecimal rules
```java
import java.math.BigDecimal;
import java.math.RoundingMode;

// Rule 1: create from a STRING (or valueOf), NEVER with new BigDecimal(double)
new BigDecimal(0.1);           // 0.1000000000000000055511151231257827021181583404541015625 ❌
new BigDecimal("0.1");         // 0.1 ✅
BigDecimal.valueOf(0.1);       // 0.1 ✅

// Rule 2: it's IMMUTABLE. add() returns a NEW object (just like String.toUpperCase)
BigDecimal price = new BigDecimal("100.00");
price.add(new BigDecimal("5"));                     // ❌ result thrown away
price = price.add(new BigDecimal("5"));             // ✅ 105.00

// Rule 3: compare with compareTo(), NOT equals()
new BigDecimal("2.0").equals(new BigDecimal("2.00"));          // false! equals also checks the SCALE
new BigDecimal("2.0").compareTo(new BigDecimal("2.00")) == 0;  // true ✅

// Rule 4: divide() needs a scale and a rounding mode
new BigDecimal("10").divide(new BigDecimal("3"));                        // ArithmeticException! 3.333... never ends
new BigDecimal("10").divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP); // 3.33 ✅

// Rule 5: set the scale (number of decimals) explicitly for money
new BigDecimal("12.345").setScale(2, RoundingMode.HALF_UP);   // 12.35
```
**Scale** = the number of digits after the decimal point. `"2.00"` has scale 2.
**HALF_UP** = normal school rounding (0.5 rounds up). **HALF_EVEN** ("banker's rounding") is used in some finance systems to avoid bias.

### Example 2: GST calculation
Create `examples/GstCalculator.java`:
```java
package com.meindev.examples;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class GstCalculator {

    private static final BigDecimal GST_RATE = new BigDecimal("0.18");   // 18%
    private static final int PAISE_SCALE = 2;

    private GstCalculator() { }    // utility class: no instances

    public static BigDecimal gstOn(BigDecimal netAmount) {
        return netAmount.multiply(GST_RATE).setScale(PAISE_SCALE, RoundingMode.HALF_UP);
    }

    public static BigDecimal grossOf(BigDecimal netAmount) {
        return netAmount.add(gstOn(netAmount));
    }

    public static void main(String[] args) {
        var net = new BigDecimal("999.99");
        System.out.println(gstOn(net));     // 180.00  (179.9982 rounded)
        System.out.println(grossOf(net));   // 1179.99
    }
}
```

---

## 5. Common mistakes
| Mistake | Fix |
|---|---|
| `if (status == "ACTIVE")` | `"ACTIVE".equals(status)` (puts the literal first, so it's also null-safe) |
| `+=` on a String in a loop | `StringBuilder` or `String.join` |
| `new BigDecimal(0.1)` | `new BigDecimal("0.1")` |
| `bd1.equals(bd2)` for amounts | `bd1.compareTo(bd2) == 0` |
| `amount.add(x);` and ignoring the result | `amount = amount.add(x);` |
| `var list = new ArrayList<>();` | `var list = new ArrayList<String>();` or `List<String> list = new ArrayList<>();` |
| `double` for money | `BigDecimal` with an explicit scale and rounding |

## 6. Quick check questions (answer in chat)
1. Why does `new BigDecimal("2.0").equals(new BigDecimal("2.00"))` return `false`? What should you use instead?
2. Why is `+=` on a String inside a loop slow, while `"Hi " + name` on one line is fine?
3. Give one example where `var` makes code **harder** to read.

Then do **Lesson 3** in [EXERCISES.md](EXERCISES.md). After that comes the **graded weekly task**.
