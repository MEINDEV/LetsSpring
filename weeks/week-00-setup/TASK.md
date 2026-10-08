# Week 0 Task: Hello Maven + First Commit (not graded, warm-up)

## Requirements
1. Build the project from [LESSON.md](LESSON.md) by typing it yourself.
2. Add **one more method** to `Greeter`: `String greetAll(List<String> names)`.
   - It returns one line per name, joined with a newline (`\n`).
   - Blank names are greeted as "World".
   - An empty list returns an empty string `""`.
3. Write **at least 2 tests** for `greetAll` (one for the normal case, one edge case).
4. `mvn test` must show **BUILD SUCCESS**.
5. Commit your work from `C:\SpringBoot`:
   ```powershell
   git add .
   git commit -m "feat(week-00): add Greeter with tests"
   ```
   (This is the *conventional commit* style: `type(scope): message`. Types include `feat`, `fix`, `test`, `docs` and `refactor`.)

## When done
Say **"task done"** in chat, and also answer the 3 check questions from the lesson.

**Hint policy:** stuck? Say `hint` and you'll get a nudge first, not the answer.
