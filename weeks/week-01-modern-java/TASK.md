# Week 1 Graded Task: `IndianRupee` + `BankAccount` 🎯

**Time box:** about 3–4 hours. **Score:** out of 100 (see the rubric at the bottom). **Pass:** 65+, **Excellent:** 80+.

This task combines everything from Week 1:
| Concept | Where you'll use it |
|---|---|
| Records, compact constructors, immutability | `IndianRupee` |
| `equals`/`hashCode` (and the BigDecimal scale trap) | `IndianRupee` in a `HashSet` |
| BigDecimal with scale and rounding | money arithmetic, interest |
| Enums with fields | `AccountType` |
| State machine with switch expressions | `AccountStatus` |
| Defensive copies | `BankAccount.transactions()` |
| StringBuilder and `formatted` | `BankAccount.statement()` |

## Starting point (already in your project)
```
src/main/java/com/meindev/task/
  IndianRupee.java                 ← skeleton: implement it
  AccountType.java                 ← skeleton: implement it
  AccountStatus.java               ← skeleton: implement it
  BankAccount.java                 ← skeleton: implement it
  Transaction.java                 ← provided, complete (don't change)
  TransactionType.java             ← provided, complete (don't change)
  InsufficientFundsException.java  ← provided, complete (don't change)
src/test/java/com/meindev/task/
  IndianRupeeProvidedTest.java     ← 16 tests: DON'T modify
  BankAccountProvidedTest.java     ← 24 tests: DON'T modify
```
Right now these 40 tests **fail** (red). Your job: make them all **green**, and add your own tests.

Run only the task tests while you work:
```powershell
mvn test -Dtest="IndianRupee*"
mvn test -Dtest="BankAccount*"
```
> Tip: work **one test at a time**, top to bottom. Start with `IndianRupee`, since `BankAccount` depends on it.

---

## Part A: `IndianRupee` (an immutable value object)
`record IndianRupee(BigDecimal amount)`

**Rules (enforce them in the compact constructor):**
1. `amount` must not be `null` → throw `NullPointerException` (hint: `Objects.requireNonNull`).
2. Negative amounts are not allowed → throw `IllegalArgumentException`.
3. More than 2 *meaningful* decimal places is not allowed (`1.005` → `IllegalArgumentException`),
   but trailing zeros are fine (`1.500` is OK, because it's really `1.5`). Hint: look up `stripTrailingZeros()`.
4. **Always store the amount with scale 2**, so that `of("5")` equals `of("5.00")`.
   *Think:* why does rule 4 make `equals`/`hashCode` work correctly? (Lesson 3: `2.0` vs `2.00`.)

**Methods:**
| Method | Behaviour |
|---|---|
| `of(String)`, `of(long rupees)`, `ofPaise(long paise)` | static factories. `ofPaise(12345)` gives `123.45` |
| `ZERO` | constant (already declared) |
| `plus`, `minus` | return a **new** `IndianRupee`. A negative result from `minus` must throw (do you get this for free from the constructor?) |
| `times(int quantity)` | negative quantity → `IllegalArgumentException` |
| `percent(BigDecimal p)` | `amount × p ÷ 100`, rounded `HALF_UP` to 2 decimals |
| `isGreaterThan`, `isLessThan`, `isZero`, `compareTo` | comparisons by value |
| `format()` | `"₹1,179.99"`. Use `String.format(Locale.ROOT, "%,.2f", amount)` so the output doesn't depend on the computer's language settings |

## Part B: `AccountType` and `AccountStatus`
- `AccountType`: `SAVINGS` (minimum balance ₹1,000, 4% interest per year), `CURRENT` (₹0, 0%).
  Store the values as **enum fields** set through a constructor, like `Plan` in Lesson 2.
- `AccountStatus.canMoveTo`: only `ACTIVE → FROZEN`, `FROZEN → ACTIVE` and `ACTIVE → CLOSED` are allowed.
  `CLOSED` is final. Use a **switch expression with no `default`**.

## Part C: `BankAccount` (a mutable entity)
A bank account's balance *must* change over time, so this is a normal class, **not** a record (Lesson 1, Section 7).
But keep its **inside safe**: no setters, and no way for outside code to change its history.

| Operation | Rules |
|---|---|
| **Constructor** | `accountNumber` and `holderName` not null or blank → otherwise `IllegalArgumentException`. `type` and `openingDeposit` not null. The opening deposit must be ≥ the type's minimum balance → otherwise `IllegalArgumentException`. The new account is `ACTIVE` and has **one** `DEPOSIT` transaction. |
| `deposit(amount)` | Account must be `ACTIVE` → otherwise `IllegalStateException`. The amount must be > 0 → otherwise `IllegalArgumentException`. |
| `withdraw(amount)` | Same checks. The balance after must stay ≥ the minimum balance → otherwise `InsufficientFundsException`, with a message containing `"Insufficient funds"`. **A failed withdrawal changes nothing and records nothing.** |
| `transferTo(target, amount)` | Self-transfer → `IllegalArgumentException`. **Both** accounts must be `ACTIVE`. Records `TRANSFER_OUT` on the source and `TRANSFER_IN` on the target. **If anything fails, neither account changes** (money must never disappear!). |
| `applyMonthlyInterest()` | Interest = `balance × annualInterestPercent ÷ 100 ÷ 12`, rounded `HALF_UP` to 2 decimals. Records `INTEREST`, unless the interest is zero (then record nothing). Returns the interest credited. Account must be `ACTIVE`. |
| `freeze()`, `unfreeze()` | Use `AccountStatus.canMoveTo`. An illegal move → `IllegalStateException`. |
| `close()` | Only from `ACTIVE`. **Pays out** the whole balance (records a `WITHDRAWAL` of it, and the balance becomes ₹0.00), sets the status to `CLOSED`, and returns the payout. |
| `transactions()` | Returns the history in order. The caller **must not** be able to modify it. |
| `statement()` | The exact format is below. |

**Every** `Transaction` records `(type, amount, balanceAfter)`.

### Statement format (exact)
```
Account: SB-1001 | SAVINGS | Asha Rao
Status: ACTIVE
----------------------------------------
DEPOSIT          ₹5,000.00     ₹5,000.00
WITHDRAWAL       ₹1,200.50     ₹3,799.50
DEPOSIT            ₹250.00     ₹4,049.50
----------------------------------------
Balance: ₹4,049.50
```
- Separator lines are 40 dashes.
- Each transaction line uses the format `"%-12s%14s%14s"` → type, amount, balanceAfter.
- ⚠️ **End every line with `\n`, not `%n`.** On Windows `%n` produces `\r\n`, so the test would fail. (I made this
  exact mistake while preparing the test, and it's a classic cross-platform bug.)

---

## Part D: your own tests (15 points)
Create `IndianRupeeTest.java` and `BankAccountTest.java` with **at least 10 tests in total**, covering cases the
provided tests **don't** check. Some ideas (find more yourself):
- `of("0.10")` vs `ofPaise(10)`; `percent` with 0%; `plus` of two large amounts.
- Withdrawing from a `FROZEN` account; transferring **from** a frozen account.
- Interest on a SAVINGS account that's exactly at the minimum balance.
- After `close()`, the payout plus all earlier withdrawals add up to the total deposits.
- `transactions()` called *before* a deposit: does that returned list change after the deposit? (Decide what's correct and test it.)

Use good test names, Arrange / Act / Assert, and AssertJ.

## Submission checklist
- [ ] `mvn clean test` → **BUILD SUCCESS**, all tests green, **no compiler warnings**
- [ ] No `UnsupportedOperationException("TODO")` left anywhere
- [ ] Code formatted (Shift+Alt+F), no commented-out dead code, no unused imports
- [ ] Commits in small steps with conventional messages, for example:
  `feat(week-01): implement IndianRupee value object`, `feat(week-01): add BankAccount transfers`, `test(week-01): add edge-case tests for BankAccount`
- [ ] Say **"task done"** in chat. I will then ask you **2 questions about your design** (10 points)

## Rubric
| Area | Points | What I look for |
|---|---|---|
| Correctness | 40 | all 40 provided tests pass, and all rules are met (I'll also try a few hidden edge cases) |
| Code quality | 25 | compact constructor, no duplicated validation (private helpers!), clear names, small methods |
| Your tests | 15 | 10+ meaningful edge-case tests, red→green, honest names |
| Industry practices | 10 | immutability, no `double` anywhere, exceptions with helpful messages, clean commits |
| Explanation | 10 | 2 design questions in chat |

**Hints policy:** say `hint` (or `hint A3`, `hint transfer`, and so on) and you'll get a nudge first, not the answer.
