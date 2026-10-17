# Week 1: Modern Java

| Lesson | Topic | Status |
|---|---|---|
| **1** | **Immutability, `equals`/`hashCode`, records** | ← you are here |
| 2 | Enums, switch expressions, pattern matching | later |
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
