# Week 0: Your First Maven Project and Git Commit

## 1. What is Maven? (theory, 2 minutes)
**Maven** is a *build tool*. It:
- **downloads libraries** (called *dependencies*) for you, for example JUnit,
- **compiles** your code, **runs tests**, and **packages** it into a `.jar` file.

You describe your project in one file, `pom.xml` (POM = Project Object Model).
Every Java company uses Maven or Gradle, and Spring Boot projects are Maven projects by default.

### The standard folder layout (memorize this)
```
my-project/
├── pom.xml                      ← project description
└── src/
    ├── main/java/               ← your application code
    └── test/java/               ← your test code (same package names)
```
Maven expects this exact layout. That's called **convention over configuration**: follow the
convention and you don't need extra settings.

### Coordinates: how every project is identified
| Field | Meaning | Example |
|---|---|---|
| `groupId` | Organization, written as a reversed domain name | `com.learn` |
| `artifactId` | Project name | `week-00-setup` |
| `version` | Version number; `-SNAPSHOT` = "still in development" | `1.0.0-SNAPSHOT` |

### The lifecycle commands you will use every day
| Command | What it does |
|---|---|
| `mvn compile` | Compile `src/main/java` |
| `mvn test` | Compile, then run all tests in `src/test/java` |
| `mvn package` | Compile, test, then build a `.jar` in `target/` |
| `mvn clean` | Delete the `target/` folder (old build output) |

Each step runs all the steps before it: `package` runs `compile` and then `test` first.

---

## 2. Hands-on: build it yourself (type it, don't copy-paste)

Work inside `C:\SpringBoot\weeks\week-00-setup\`.

### Step 1: `pom.xml`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.learn</groupId>
    <artifactId>week-00-setup</artifactId>
    <version>1.0.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.release>21</maven.compiler.release>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.11.4</version>
            <scope>test</scope>   <!-- only available to test code, not shipped -->
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>  <!-- runs the tests -->
                <version>3.5.2</version>
            </plugin>
        </plugins>
    </build>
</project>
```

### Step 2: application code at `src/main/java/com/learn/Greeter.java`
```java
package com.learn;

public class Greeter {

    public String greet(String name) {
        if (name == null || name.isBlank()) {
            return "Hello, World!";
        }
        return "Hello, " + name.strip() + "!";
    }

    public static void main(String[] args) {
        Greeter greeter = new Greeter();
        System.out.println(greeter.greet("Sundram"));
    }
}
```
> The folder path must match the package: `com.learn` means `com/learn/`.

### Step 3: your first test at `src/test/java/com/learn/GreeterTest.java`
```java
package com.learn;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GreeterTest {

    private final Greeter greeter = new Greeter();

    @Test
    void greetsByName() {
        assertEquals("Hello, Asha!", greeter.greet("Asha"));
    }

    @Test
    void fallsBackToWorldWhenNameIsBlank() {
        assertEquals("Hello, World!", greeter.greet("   "));
    }
}
```
A test follows **Arrange → Act → Assert**: prepare the data, call the method, check the result.

### Step 4: run it (VS Code terminal, inside `week-00-setup`)
```powershell
mvn test
```
Look for `Tests run: 2, Failures: 0` and **BUILD SUCCESS**. The first run downloads libraries, so it is slow.

Run the program:
```powershell
mvn compile
java -cp target/classes com.learn.Greeter
```

---

## 3. Common mistakes
| Mistake | Symptom |
|---|---|
| Folder doesn't match package | `class not found`, or the test doesn't run |
| Test class name doesn't end with `Test` | Surefire ignores it, so you see `Tests run: 0` |
| Running `mvn` outside the folder that has `pom.xml` | `there is no POM in this directory` |
| Forgetting `<scope>test</scope>` | Test libraries end up inside your production jar |

## 4. Quick check questions (answer in chat)
1. Which folder holds tests, and which holds application code?
2. What is the difference between `mvn compile` and `mvn package`?
3. Why does the JUnit dependency have `<scope>test</scope>`?
