# Learning Progress

**Started:** 2026-10-07  **Target end:** 2027-04-07
**Current:** Week 1, Modern Java  **Next step:** Week 1 task attempt 1 = 43/90 + explanation pending → REDO. Learner answers 2 design Qs, then resubmits (restore provided test + BigDecimal annualInterestPercent; validate+normalize in compact ctor; remove isZero()?ZERO hacks; funds check before minus; zero transfer; ACTIVE check + single rounding for interest; Locale.ROOT; reuse format() in statement; exception messages; remove System.out; 10+ own tests). Re-run 16 hidden tests (HiddenEdgeCaseTest h01–h16, listed in the 2026-11-14 log) on resubmit. Exercise fixes from L3 still open (TagFormatter, BillSplitter).

## Phase status
| Phase | Weeks | Status | Checkpoint score |
|---|---|---|---|
| 1. Core Java | 0–8 | In progress | – |
| 2. Spring Boot & Data | 9–14 | Locked | – |
| 3. Production Backend | 15–20 | Locked | – |
| 4. Distributed Systems | 21–26 | Locked | – |

## Weekly log
| Week | Topic | Lessons done | Task score | Date completed | Notes |
|---|---|---|---|---|---|
| 0 | Setup | 1/1 | ungraded (would be ~80) | 2026-10-08 | 3/3 check Qs. 4 tests pass. greetAll via streams. Feedback: duplicated greet logic, no empty-list test, `;;`, test naming. |
| 1 | Modern Java | 3/3 (graded task: REDO) | Attempt 1: 43/90 (+ explanation /10 pending) | – | L3 exercises ~70: 42 tests pass but TagFormatter ignores mode (untested), BillSplitter wrong for remainder >1 paisa. L3 check Qs: Q1 ½ (said equals checks reference; really value+scale), Q2 ✅, Q3 ✅. | Learner renamed task Money → IndianRupee (accepted). L1 exercises: 1st try ~65, after fixes ~78. L1 check Qs: 2/3 (Q3 compact ctor ❌). L2 exercises ~82: all correct, 31 tests, 0 warnings. L2 check Qs: Q1 ✅, Q2 ❌ (answered business rule, not compile error), Q3 ✅; E2.4 ✅. |

## Weak areas (revisit in warm-ups)
- Reading the full task requirements before submitting (Week 0: skipped greetAll)
- Attention to small typos (test class name, pom xmlns, `;;`)
- DRY: duplicated greet() logic inside greetAll instead of reusing it
- Test naming: names should describe behaviour (e.g. greetsBlankNamesAsWorld)
- Code formatting (spaces around operators / before `{`), so encourage Shift+Alt+F. RECURRING in Week 1 → suggested formatOnSave
- Tests that pass for the wrong reason (TeamTest used List.of input, so defensive copy was never tested)
- Static vs instance methods (ofFahrenheit made instance → `new Temperature(343).ofFahrenheit(212)`) (fixed after feedback)
- Boundary conditions: changed `<` to `<=` and broke the "-273.15 allowed" rule; no boundary test caught it
- Compact vs canonical constructor in records (Q3 wrong; still writes full canonical ctor). Re-ask in warm-up
- Missing @Override on equals/hashCode
- Follow-through on review items: Temperature boundary fix ignored twice. Start next review by checking it
- Test name doesn't match what it asserts (TicketStatusTest.resolvedCanMoveToInProgress checks RESOLVED→CLOSED)
- JUnit assertEquals order is (expected, actual), opposite of AssertJ's assertThat(actual)
- Commit messages still inconsistent ("docst(week-01) :", "Fix  minor issues...")
- Exhaustive-switch idea: connect "new enum constant" → "compile error" (L2 Q2)
- PATTERN (3rd time): tests only cover the happy path, so bugs hide in untested branches (TagFormatter AS_IS, BillSplitter 7 people, Team in L1). Push "test every branch / parameter value"
- Commit content ≠ commit message (43e85f5 claims TicketStatus/Notification changes that aren't in it). Teach `git diff --staged` before commit
- BigDecimal.equals = value AND scale (not reference)
- Fixing symptoms instead of root cause: added `isZero() ? ZERO : x` and `quantity==0 → ZERO` hacks instead of normalizing scale in the compact constructor
- Modified a provided test + changed public API (annualInterestPercent → IndianRupee) to make it pass. Explain "tests are the contract; ask, don't edit"
- Choosing types by meaning: a percentage is not money
- Validation order: called minus() (which throws IAE) before the funds check → wrong exception type
- Exceptions without messages; System.out in domain code; snake_case locals
- Raw types (`List` without `<String>`)
- AssertJ argument order: assertThat(actual).isEqualTo(expected)
- Skipping parts of the spec (missing tests: boundary, valid value, fahrenheit(); 3 lesson check Qs unanswered)
- Commit messages for non-code commits ("week-01 starter" → `docs(week-01): ...`)

## Strong areas
- Maven basics: understands compile/package and test scope
- Git: conventional commit message used correctly on first try
- Responds well to feedback: fixed all 4 review items in one pass
- Already comfortable with basic streams (map + Collectors.joining) and ternary
- Shows initiative: wrote tests for lesson examples (OrderStatusTest, PaymentTest), discovered SoftAssertions alone
- Boundary testing done well in E2.3 (160 vs 161 chars); ceiling-division trick understood
- Sealed interface + record patterns used correctly first try

## Session history
- 2026-10-07: Platform created. Learner profile: OOP basics, ~10 h/week, Java 21 + Maven, VS Code. JDK and Maven not yet installed.
- 2026-10-07: Installed Temurin JDK 21.0.12 (winget), Maven 3.9.16 (C:\Users\sundramkr\tools, on user PATH), VS Code Java + Spring Boot extension packs. Verified java/javac/mvn.
- 2026-10-07: Git email + default branch set; C:\SpringBoot git-initialized with .gitignore. Week 0 LESSON.md and TASK.md written. git user.name still unset.
- 2026-10-08: Week 0 review. Learner set git name (Sundram Sontirkey), used package com.meindev (accepted). mvn test passes 2/2. Missing: greetAll + its tests. Feedback given on test class typo, pom xmlns typo, whitespace edge case.
- 2026-10-08: Week 0 resubmitted: all fixes done, greetAll with streams, 4/4 tests pass, committed 201bf08. Week 0 COMPLETE.
- 2026-10-08: DRY challenge solved correctly (`.map(this::greet)`); note: should be `refactor:` not `feat:`. Generated Week 1 project (pom with JUnit+AssertJ), LESSON.md Lesson 1 (immutability, equals/hashCode, records), EXERCISES.md Lesson 1. Examples verified to compile/run.
- 2026-10-17: L1 exercises reviewed (commit ea88f0e). 6/6 tests pass. Good: Employee→record, withMember returns new Team, AssertJ usage, E1.4 correct (prints "java"). Fixes requested: Team compact constructor/copy/validation, Temperature static factory + compact ctor + spelling celsius, missing tests, raw types, EqualsOnlyBug missing package + hashCode step, check Qs.
- 2026-10-17: L1 fixes reviewed (commit 946c132). 8/8 tests pass. Fixed well: Team copy + validation + red-green test for original-list mutation, static ofFahrenheit, isCloseTo, EqualsOnlyBug package+hashCode, pom cleanup. Remaining: Temperature `<=` boundary regression, raw types (unchecked warnings), EmployeeTest arg order. Generated Lesson 2 (enums, switch expressions, pattern matching, sealed preview) + E2.1–E2.4; examples verified.
- 2026-10-22: L2 exercises reviewed (commit 36de371). 31/31 tests, no warnings. Raw types fixed. Temperature `<=` NOT fixed. Generated Lesson 3 (var, text blocks, String ==/StringBuilder, BigDecimal, GST example) + E3.1–E3.4; all lesson claims verified by running code.
- 2026-10-24: L3 exercises reviewed (commit 8ab4264). 42/42 tests pass. Good: BigDecimal learning tests w/ isEqualByComparingTo + .as(), text block email exact, SoftAssertions. Bugs found by probing: TagFormatter always UPPER; split(100,7) → [14.32, 14.28×6] (should be [14.29×4, 14.28×3]), negative total accepted. Follow-ups: Temperature `<` fixed ✅ (no boundary test), EmployeeTest fixed ✅, TicketStatus test + Notification NOT changed despite commit msg. Created graded TASK.md + skeletons (IndianRupee, AccountType, AccountStatus, BankAccount) + provided types + 40 provided tests (verified green vs reference solution, red vs skeleton).
- 2026-11-14: Graded task attempt 1 (commits 72f25ea, 1c20c60, ec87186). mvn test 88/88 green BUT BankAccountProvidedTest.savingsAndCurrentRules was edited (annualInterestPercent changed to IndianRupee). Hidden tests 0/16 pass (all 16 validated green vs reference): h01 ctor negative, h02 of(-5), h03 ctor 1.005, h04 ctor scale, h05 ZERO==of(0), h06 minus→ZERO, h07 percent(0)→ZERO, h08 Locale.GERMANY format, h09 withdraw>balance→ISF, h10 transfer>balance→ISF, h11 zero transfer, h12 frozen interest, h13 interest 1012.38→3.37 (double rounding gives 3.38), h14 TRANSFER_OUT column alignment, h15 IllegalThreadStateException typo, h16 exception messages. Own tests: 6 (need 10). Scores: correctness 22/40, quality 12/25, tests 5/15, practices 4/10. Good: List.copyOf history, atomic transfer ordering, enum state machine, incremental commits, no double.
