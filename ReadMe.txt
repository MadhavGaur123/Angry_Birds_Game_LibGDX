🐦 Angry Birds-Inspired Game (APProject 3)
This is a 2D physics-based Angry Birds-style game developed using Java, LibGDX, and Box2D. The project demonstrates advanced game development concepts such as dynamic screen management, Box2D-based physics simulations, special bird abilities, serialization, and clean UI/UX handling across multiple game states.
________________________________________
🚀 How to Run the Project
1.	Configure Gradle in IntelliJ:
o	Ensure Gradle is properly set up under File > Project Structure > Gradle.
2.	Extract the ZIP file and open the APProject 3 folder in IntelliJ.
3.	Open the Launcher File:
o	Navigate to: lwjgl3/src/main/java/Lwjgl3Launcher.java
4.	Configure the Java SDK:
o	Go to File > Project Structure > SDKs and make sure the SDK is set correctly.
5.	Run the Game:
o	Press the green Run button on top of IntelliJ.
o	Make sure “Run Current File” is selected while launching.
________________________________________
📊 UML & Use Case Diagrams
•	UML Diagram: https://miro.com/app/board/uXjVLcYy270=/
•	Use Case Diagram: https://lucid.app/lucidchart/26c2325b-0426-43fe-a51d-82ab30227622/edit?invitationId=inv_7baea1e0-7adc-44b4-857d-9340e42c9d8b&page=.Q4MUjXso07N#
________________________________________
📂 Project Structure Overview
🎮 Main Game Engine (Main.java)
•	Manages screen transitions between:
o	StartScreen
o	HomeScreen
o	PlayScreen
o	PauseScreen
o	Level1 (and beyond)
•	Handles initialization and navigation to settings and other modules.
________________________________________
💻 Screens and Their Roles
1. StartScreen
•	Displays background, Start and Settings buttons.
•	Touch input detection is implemented.
2. HomeScreen
•	Main menu with navigation to:
o	Settings
o	PlayScreen
o	Back to StartScreen
3. PlayScreen
•	Allows the user to select between:
o	Level 1
o	Level 2
o	Level 3
4. PauseScreen
•	Displays when the game is paused.
•	Includes:
o	Resume button
o	Semi-transparent background
________________________________________
🌽 Gameplay Details
🌍 Level1 (and others)
•	Viewport: 1920×1080
•	Includes:
o	Birds: Red, Yellow, Blue (each with special powers)
o	Pigs: Normal and helmeted
o	Blocks: Destructible and non-destructible
o	TNT Explosions
🛆 Physics Engine: Box2D
•	All interactive objects are represented using Box2D bodies.
•	Physics properties such as restitution and friction are assigned.
•	GameContactListener handles collision detection and post-collision events (e.g., removing pigs/blocks).
________________________________________
🧬 Game Logic Highlights
•	Game world is simulated using Box2D physics.
•	Each sprite has a corresponding physics body.
•	Touch and keyboard input supported for various actions (pause, exit, special bird powers).
•	Dynamic scaling based on device screen size ensures consistent UI and gameplay experience.
•	Asset management ensures all textures/sprites are properly loaded and disposed.
________________________________________
🏁 Win/Lose Conditions
•	Game Win:
o	All pigs are destroyed before birds run out.
•	Game Lose:
o	Birds run out while pigs remain.
These checks are handled inside the render() method of each level.
________________________________________
🔐 Level Locking Mechanism
•	Players must complete Level 1 to unlock Level 2, and so on.
•	Prevents direct access to higher levels before earlier ones are cleared.
________________________________________
📀 Serialization & Testing
•	Game state serialization implemented for saving/loading progress.
•	JUnit tests written for core logic modules to ensure stability.
________________________________________
✨ Advanced Features
•	Bird Special Powers (based on type)
•	Trajectory prediction (for bird slingshot)
•	Explosion physics (TNT blocks)
•	Smooth screen transitions
•	Proper memory management and disposal
________________________________________
🔄 Basic Code Flow Outline
Start Screen
   └── Start Button
       └── Home Screen
           ├── Back
           ├── Settings
           └── Play
               └── Level Screen
                   ├── Level 1
                   ├── Level 2 (locked until Level 1 passed)
                   └── Level 3 (locked until Level 2 passed)
                       ├── Pause
                       └── Restart
Settings Menu also includes:
•	Pause game
•	Back
•	How to Play
________________________________________
📦 GitHub Repository
View the complete source code here:
🔗 GitHub - https://github.com/MadhavGaur123/Angry_Birds_Game_LibGDX
________________________________________
📣 Final Notes
This project reflects an in-depth implementation of a full-fledged physics-based 2D game using LibGDX and Box2D. It highlights good coding practices, modular screen design, effective input handling, collision management, dynamic rendering, and enhanced player experience with sound logic and serialization. The game depth and features will be best appreciated in the live demo.
