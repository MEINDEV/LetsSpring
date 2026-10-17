# Learning Progress

**Started:** 2026-10-07  **Target end:** 2027-04-07
**Current:** Week 1, Modern Java  **Next step:** Learner: 5-min L1 cleanup (Temperature boundary `<=` regression + boundary/valid/fahrenheit tests, raw types → no compiler warnings, EmployeeTest arg order), then Lesson 2 (LESSON.md §Lesson 2) + EXERCISES E2.1–E2.4 + 3 check Qs. After that: Lesson 3 (var, text blocks, StringBuilder, BigDecimal) then graded task (IndianRupee + BankAccount).

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
| 1 | Modern Java | 1/3 (L2 in progress) | – | – | Learner renamed task Money → IndianRupee (accepted). L1 exercises: 1st try ~65, after fixes ~78. Check Qs: Q1 ✅, Q2 ✅, Q3 ❌ (thinks compact = records only, normal = classes). |

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
- Raw types (`List` without `<String>`)
- AssertJ argument order: assertThat(actual).isEqualTo(expected)
- Skipping parts of the spec (missing tests: boundary, valid value, fahrenheit(); 3 lesson check Qs unanswered)
- Commit messages for non-code commits ("week-01 starter" → `docs(week-01): ...`)

## Strong areas
- Maven basics: understands compile/package and test scope
- Git: conventional commit message used correctly on first try
- Responds well to feedback: fixed all 4 review items in one pass
- Already comfortable with basic streams (map + Collectors.joining) and ternary

## Session history
- 2026-10-07: Platform created. Learner profile: OOP basics, ~10 h/week, Java 21 + Maven, VS Code. JDK and Maven not yet installed.
- 2026-10-07: Installed Temurin JDK 21.0.12 (winget), Maven 3.9.16 (C:\Users\sundramkr\tools, on user PATH), VS Code Java + Spring Boot extension packs. Verified java/javac/mvn.
- 2026-10-07: Git email + default branch set; C:\SpringBoot git-initialized with .gitignore. Week 0 LESSON.md and TASK.md written. git user.name still unset.
- 2026-10-08: Week 0 review. Learner set git name (Sundram Sontirkey), used package com.meindev (accepted). mvn test passes 2/2. Missing: greetAll + its tests. Feedback given on test class typo, pom xmlns typo, whitespace edge case.
- 2026-10-08: Week 0 resubmitted: all fixes done, greetAll with streams, 4/4 tests pass, committed 201bf08. Week 0 COMPLETE.
- 2026-10-08: DRY challenge solved correctly (`.map(this::greet)`); note: should be `refactor:` not `feat:`. Generated Week 1 project (pom with JUnit+AssertJ), LESSON.md Lesson 1 (immutability, equals/hashCode, records), EXERCISES.md Lesson 1. Examples verified to compile/run.
- 2026-10-17: L1 exercises reviewed (commit ea88f0e). 6/6 tests pass. Good: Employee→record, withMember returns new Team, AssertJ usage, E1.4 correct (prints "java"). Fixes requested: Team compact constructor/copy/validation, Temperature static factory + compact ctor + spelling celsius, missing tests, raw types, EqualsOnlyBug missing package + hashCode step, check Qs.
- 2026-10-17: L1 fixes reviewed (commit 946c132). 8/8 tests pass. Fixed well: Team copy + validation + red-green test for original-list mutation, static ofFahrenheit, isCloseTo, EqualsOnlyBug package+hashCode, pom cleanup. Remaining: Temperature `<=` boundary regression, raw types (unchecked warnings), EmployeeTest arg order. Generated Lesson 2 (enums, switch expressions, pattern matching, sealed preview) + E2.1–E2.4; examples verified.
