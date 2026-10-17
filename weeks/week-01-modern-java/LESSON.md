# Week 1: Modern Java

| Lesson | Topic | Status |
|---|---|---|
| 1 | Immutability, `equals`/`hashCode`, records | ✅ done |
| **2** | **Enums, switch expressions, pattern matching** | ← you are here |
| 3 | `var`, text blocks, String vs StringBuilder, BigDecimal for money | later |
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
