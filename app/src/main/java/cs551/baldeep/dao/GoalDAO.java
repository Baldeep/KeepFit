package cs551.baldeep.dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs551.baldeep.models.Goal;

/**
 * Created by balde on 12/02/2017.
 */

public class GoalDAO {

    private Dao<Goal, String> goalDAO;

    public GoalDAO(Context context) throws SQLException {
        DBHelper db = new DBHelper(context);
        goalDAO = db.getGoalDAO();
    }

    public List<Goal> findAll(){
        try {
            return goalDAO.queryForAll();
        } catch (SQLException e) {
            Log.e("GoalDAO", "Failed to get all Goals.\n" + e.toString());
            return new ArrayList<Goal>();
        }
    }

    public boolean saveOrUpdate(Goal goal){
        try {
            goalDAO.create(goal);
            return true;
        } catch (SQLException e) {
            Log.e("GoalDAO", "Failed to save Goal.\n" + e.toString());
            return false;
        }
    }


}
