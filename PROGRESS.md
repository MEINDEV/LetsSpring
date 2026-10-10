# Learning Progress

**Started:** 2026-10-07  **Target end:** 2027-04-07
**Current:** Week 1, Modern Java  **Next step:** Learner does Week 1 Lesson 1 (LESSON.md) + EXERCISES.md E1.1–E1.4, answers 3 check Qs. Then Lesson 2 (enums, switch expressions, pattern matching).

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
| 1 | Modern Java | 0/3 | – | – | |

## Weak areas (revisit in warm-ups)
- Reading the full task requirements before submitting (Week 0: skipped greetAll)
- Attention to small typos (test class name, pom xmlns, `;;`)
- DRY: duplicated greet() logic inside greetAll instead of reusing it
- Test naming: names should describe behaviour (e.g. greetsBlankNamesAsWorld)
- Code formatting (spaces around operators / before `{`), so encourage Shift+Alt+F

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
