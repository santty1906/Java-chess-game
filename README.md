# ♟️ Chess Game in Java

## 🧾 Project Summary

This project implements a **chess game in Java** with a graphical interface using **Swing**.
You can play **1 vs 1** or **against a simple bot**.
The board includes **valid moves**, **piece capturing**, **pawn promotion**, **castling**, **check**, and **checkmate**.

---

## 🎯 Objectives

* Allow 1 vs 1 matches or playing against a bot.
* Validate legal moves for all chess pieces.
* Automatically detect check, checkmate, and pawn promotion.
* Provide an intuitive and visual interface for full chess games.

---

## 🛠️ Tools & Technologies

| Category | Tool / Technology             |
| -------- | ----------------------------- |
| Language | Java 17+                      |
| GUI      | Swing                         |
| Assets   | PNG images (resources folder) |

---

## 👥 Key Files

* `Main.java` → Initial presentation and menu access.
* `MenuJuego.java` → Main menu with game options.
* `TableroAjedrez.java` → Board logic, moves, and special rules.
* `ValidadorMovimiento.java` → Validates legal piece movements.
* `BotFacil.java` → Simple bot logic.
* `resources/` → Piece and logo images.

---

## 👥 Author
Santiago Lopez and the project team of class group 1SF125, Technological University of Panama.

---

## 🚀 Installation & Project Execution

1️⃣ Clone the repository:
```bash
git clone https://github.com/santty1906/chess-java.git
cd chess-java
```

2️⃣ Compile all `.java` files:
```bash
javac *.java
```

3️⃣ Run the application:
```bash
java Main
```
