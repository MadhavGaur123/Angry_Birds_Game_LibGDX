2023303_2023393_APPROJECT

AP PROJECT 3 - FULLY FUNCTIONING ANGR BIRDS Members - Madhav Gaur , Pratham Nasir

How To Run -

    Configure Gradel on your IntelliJ
    Extract the zip file and open the APProject 3 folder on Intellij
    Open the Launcher File (lwjgl3 -> src -> main -> java -> Lwjgl3Launcher)
    Configure SDK
    Click on the run button at the top of IntelliJ please ensure you press the run current file option while running

UML Link - https://miro.com/welcomeonboard/VU81YUhCZmlkMVE1aUUzZWpVTWJiTU9VTGlBM25OUmFDWVh4SUJMdm52dDZKdTdBbEZoNFBKZ3FDRUxFZElFQ3wzNDU4NzY0NTYyNTcyNTkwMzg2fDI=?share_link_id=672945920038

USE CASE Link - https://lucid.app/lucidchart/26c2325b-0426-43fe-a51d-82ab30227622/edit?viewport_loc=180%2C2864%2C4312%2C1581%2C.Q4MUjXso07N&invitationId=inv_7baea1e0-7adc-44b4-857d-9340e42c9d8b

GitHub Link - https://github.com/MadhavGaur123/2023303_2023393_APPROJECT Code Explanation - The main game class (Main.java) manages different screens like StartScreen, HomeScreen, PlayScreen, PauseScreen, and Level1, and transitions between them. It also provides a method to navigate to the settings screen.

Each screen serves a different purpose: the StartScreen features a background, start button (leading to HomeScreen), and settings button, and handles input for touch interactions. The HomeScreen is the main menu, which allows the user to access settings, start the game, or return to the previous screen. The next screen is play screen where you can select the level you want to play. The PauseScreen provides an option to pause the game, with a resume button and a subtle background overlay.

In terms of gameplay, Level1 is the first playable level, featuring a game world of dimensions 1920x1080 with destructible and non-destructible blocks, pigs (regular and helmeted), and different bird types (Red, Yellow, Blue). Viewport management ensures proper camera control, while screen scaling is adjusted dynamically for various screen sizes. Game objects like birds and pigs are defined through base classes (Bird.java, Pig.java), with specific types inheriting and adding custom behaviors.

Input handling includes touch detection for buttons and objects, with keyboard input for pausing or exiting the game.The above is handeleld by input module and use of vectors. Resources such as textures and sprites are managed efficiently with proper disposal, and screens transition smoothly with cleanup mechanisms. Dynamic sprite scaling and positioning ensure consistency across different devices, and all assets (backgrounds, birds, pigs) are scaled based on the viewport size to maintain proportional spacing and proper display.

Box2D has been used to simulate real world like physics. Apart from the screen word created by libGDX box2d has an inbuilt physics world. That has been initialised and for every corresponding sprite(that have to be moved in the real angry bids game not like pause sprite start sprite) you create its respective world body. The great thing about box2D is that it will simulate the physics by itself on the bodies in the world. You can assign shapes and different features such as coefficient of restitution and friction. For collisions we have used a thing called game contact listener. It is used to detect collisions and each world can have its own contact listener. This is used to removed block and pigs on collisions and everything.

Finally for the finishing of the game i have implements a game win and game loose logic. If number of bird is zero and number of pigs left is non 0 then game lost screen. If number of birds non zero or zero and number of pigs left is 0 the game won screen. The function which does it will be called in the redner function of the level code.

Also the user wont be able to play level2 unless level1 is passed and same with level3. There is also game serilization and Juint for the necessary functions. There is also special power for birds and trajectory printing.

Basic Code Outline - Start Screen -> Start Button -> Home Screen (Back , Settings , Play) -> Play Screen (Level 1 , Level 2 , Level 3) -> Level Screen (Pause, Restart). Settings will also have pause game , back and how to play option. This is a basic outline of the code. There are more features like special bird powers and tnt Explosions. Serilization and Juint has also been done and the project depth will be more visible during the demo
