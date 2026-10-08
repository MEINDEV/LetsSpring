# Week 0: Environment Setup (Windows)

You already have **Git, Docker and VS Code**. You still need **JDK 21** and **Maven**.

## 1. Install JDK 21 and Maven (open PowerShell)
```powershell
winget install EclipseAdoptium.Temurin.21.JDK
winget install Apache.Maven
```
If `Apache.Maven` is not found, run `winget search maven` and install the result from Apache.

**Close and reopen** VS Code and the terminal so the new PATH is picked up.

## 2. Verify
```powershell
java -version     # should show: openjdk version "21.x"
javac -version
mvn -v            # should show: Apache Maven 3.9.x ... Java version: 21
```
If `JAVA_HOME` is missing, set it with the line below (adjust the path to your installed version), then restart the terminal:
```powershell
setx JAVA_HOME "C:\Program Files\Eclipse Adoptium\jdk-21.0.x-hotspot"
```

## 3. VS Code extensions
Install the **Extension Pack for Java** (Microsoft), and later **Spring Boot Extension Pack** (VMware/Broadcom).
```powershell
code --install-extension vscjava.vscode-java-pack
code --install-extension vmware.vscode-boot-dev-pack
```

## 4. Git identity (once)
```powershell
git config --global user.name  "Your Name"
git config --global user.email "you@example.com"
git config --global init.defaultBranch main
```

## 5. Tell your mentor
Once all the commands above print versions, say **"setup done"** in chat. I will verify it,
then we create your first Maven project and make your first Git commit.
