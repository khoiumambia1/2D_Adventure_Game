
# 2D Game - [Treasure Hunt]

A fun 2D Java game where you control a player to collect objects and explore the map.

This guide will help anyone run the game **directly in Eclipse**, no extra setup required.

---

## **Step-by-Step Guide to Run the Game**

### 1. **Clone or Download the Repository**

* Click **Code → Download ZIP** on GitHub, or clone it using Git:

```bash
git clone https://github.com/khoiumambia1/2D_Adventure_Game.git
```

* If you downloaded ZIP, extract it to a folder on your computer.

---

### 2. **Open Eclipse**

* Launch **Eclipse IDE**.
* If you don’t have Eclipse, download it from: [Eclipse Downloads](https://www.eclipse.org/downloads/)

---

### 3. **Import the Project**

1. In Eclipse:
   **File → Import → Existing Projects into Workspace → Select root directory → Finish**
2. Make sure the project appears in **Project Explorer**.

---

### 4. **Check the Source Code**

* Make sure the `src` folder contains all your packages, like:
  `entity`, `khouim`, `tile`, etc.
* Do **not change the folder structure**.

---

### 5. **Run the Game**

1. Open `App.java` in the `khouim` package.
2. Right-click → **Run As → Java Application**.
3. The game window should open!

> Tip: If nothing appears, check **Problems** tab in Eclipse for missing files.

---

### 6. **Controls**

Use the keyboard to play the game:

| Action     | Key   |
| ---------- | ----- |
| Move Up    | ↑     |
| Move Down  | ↓     |
| Move Left  | ←     |
| Move Right | →     |

---

### 7. **Gameplay**

* Collect keys and open doors.
* Find the treasure.
* Your actions may be logged in the game console for testing.

---

### 8. **Troubleshooting**

* **Blank screen / no player** → Make sure all image files are in the `res` folder.
* **Errors on `OBJDoor` or `TileManager`** → Ensure the packages and file names match exactly.
* **Eclipse can’t find main class** → Check that `App.java` has `public static void main(String[] args)`.

---

