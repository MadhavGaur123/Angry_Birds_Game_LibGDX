package APProject.project;
import com.badlogic.gdx.math.Vector2;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
public class DataofLevel2 implements Serializable {
    public List<Vector2> birdPositionsred = new ArrayList<>();
    public List<Vector2> birdPositionsblue = new ArrayList<>();
    public List<Vector2> pigPositions = new ArrayList<>();
    public List<Vector2> blockPositions = new ArrayList<>();
    public List<Boolean> birdstatusred = new ArrayList<>();
    public List<Boolean> birdstatusblue = new ArrayList<>();
    public List<String> texture = new ArrayList<>();

}
