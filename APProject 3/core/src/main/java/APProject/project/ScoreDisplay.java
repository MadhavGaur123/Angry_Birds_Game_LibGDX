package APProject.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;

public class ScoreDisplay {
    private BitmapFont font;
    public  int score;

    public ScoreDisplay(int score) {
        // Initialize the font
        font = new BitmapFont();
        font.setColor(Color.BLACK); // Set font color
        font.getData().setScale(2.0f); // Scale font size (adjust as needed)
        // Initial score
    }

    public void updateScore(int newScore) {
        this.score = newScore;
    }

    public void render(SpriteBatch batch) {
        // Draw the score at the top-right corner
        font.draw(batch, "Score: " + score, Gdx.graphics.getWidth() +100, Gdx.graphics.getHeight() - 20);
    }
    public void dispose() {
        font.dispose();
    }
}
