package APProject.project;
import com.badlogic.gdx.math.Vector2;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class DataofLevel1 implements Serializable {
    public List<Vector2> birdPositions = new ArrayList<>();
    public List<Vector2> pigPositions = new ArrayList<>();
    public List<Vector2> blockPositions = new ArrayList<>();
    public List<Boolean> birdstatus = new ArrayList<>();
    public List<Float> pighealth = new ArrayList<>();
}
