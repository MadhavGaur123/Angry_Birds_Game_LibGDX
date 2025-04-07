package APProject.project;
import com.badlogic.gdx.math.Vector2;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class DataofLevel3 implements Serializable {
    public List<Vector2> birdPositionsred = new ArrayList<>();
    public List<Vector2> birdPositionsblack = new ArrayList<>();
    public List<Vector2> birdPositionsyellow = new ArrayList<>();
    public List<Vector2> pigPositions = new ArrayList<>();
    public List<Vector2> blockPositions = new ArrayList<>();
    public List<Vector2> tntpositions = new ArrayList<>();
    public List<Boolean> birdstatusred = new ArrayList<>();
    public List<Boolean> birdstatusyellow = new ArrayList<>();
    public List<Boolean> birdstatusblack = new ArrayList<>();

}
