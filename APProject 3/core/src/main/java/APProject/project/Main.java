package APProject.project;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen; // Added missing import for Screen
import com.badlogic.gdx.Screen;

public class Main extends Game {
    public StartScreen StartScreen;
    public HomeScreen HomeScreen;
    public PlayScreen PlayScreen;
    public PauseScreen PauseScreen;
    public Level1 Level1;
    public PauseScreen2 PauseScreen2;
    public Lose lose;
    public Lose2 lose2;
    public Lose3 lose3;
    public PauseScreen3 PauseScreen3;
    public Win Win;
    public Win3 Win3;
    public Win2 Win2;
    public Level2 Level2;
    public Level3 Level3;
    public int wins;
    public int music;
    @Override
    public void create() {
        StartScreen = new StartScreen(this);
        HomeScreen = new HomeScreen(this);
        PlayScreen = new PlayScreen(this);
        PauseScreen = new PauseScreen(this);
        PauseScreen2 = new PauseScreen2(this);
        PauseScreen3 = new PauseScreen3(this);
        Level1 = new Level1(this);
        Level2=new Level2(this);
        Level3=new Level3(this);
        lose= new Lose(this);
        lose2= new Lose2(this);
        lose3= new Lose3(this);
        Win = new Win(this);
        Win2=new Win2(this);
        Win3=new Win3(this);
        setScreen(StartScreen);
        wins=0;
        music=0;
    }

    @Override
    public void dispose() {
        super.dispose();
        StartScreen.dispose();
        HomeScreen.dispose();
        PlayScreen.dispose();
        PauseScreen.dispose();
        Level1.dispose();
        lose.dispose();
        Win.dispose();
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void goToSettings(Screen previousScreen) {
        setScreen(new SettingsClass(this, previousScreen));
    }
}

