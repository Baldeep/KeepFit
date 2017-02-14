package cs551.baldeep.dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.TableUtils;

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
            goalDAO.createOrUpdate(goal);
            return true;
        } catch (SQLException e) {
            Log.e("GoalDAO", "Failed to save Goal.\n" + e.toString());
            return false;
        }
    }

    public Goal findById(String goalUUID){
        try {
            return goalDAO.queryForId(goalUUID);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteAll(){
        try {
            TableUtils.clearTable(goalDAO.getConnectionSource(), Goal.class);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Goal> findAllNotCurrentNotFinished(){
        try{
            QueryBuilder<Goal, String> queryBuilder = goalDAO.queryBuilder();

            Where where = queryBuilder.where();
            where.ne(Goal.CURRENT_GOAL, true);
            where.and();
            where.ne(Goal.GOAL_DONE, true);

            PreparedQuery<Goal> query = queryBuilder.prepare();
            return goalDAO.query(query);

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<Goal>();
        }
    }

    public Goal findCurrentGoal(){
        try {
            QueryBuilder<Goal, String> queryBuilder = goalDAO.queryBuilder();

            Where<Goal, String> where = queryBuilder.where();
            where.eq(Goal.CURRENT_GOAL, true);

            PreparedQuery<Goal> query = queryBuilder.prepare();
            List<Goal> goals = goalDAO.query(query);
            if(goals.size() > 1){
                Log.i("GoalDAO", "Multiple Current Goals found, returning newest, resetting others.");
                Goal newest = goals.get(0);

                for(int i = 1; i < goals.size(); i++){
                    if(goals.get(i).getDateOfGoal().before(newest.getDateOfGoal())){
                        newest = goals.get(i);
                        Log.d("GoalDAO", "Newest is: " + newest.getName() + " on " + newest.getDateOfGoal().toString());
                    }
                }

                goals.remove(newest);
                for(int i = 0; i < goals.size(); i++){
                    goals.get(i).setCurrentGoal(false);
                    saveOrUpdate(goals.get(i));
                }

                return newest;
            } else if(goals.size() == 1){
                return goals.get(0);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public boolean deleteById(String goalUUID) {
        try {
            goalDAO.deleteById(goalUUID);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Goal> findAllFinished() {
        try{
            QueryBuilder<Goal, String> queryBuilder = goalDAO.queryBuilder();

            Where where = queryBuilder.where();
            where.eq(Goal.GOAL_DONE, true);

            PreparedQuery<Goal> query = queryBuilder.prepare();
            return goalDAO.query(query);

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<Goal>();
        }
    }
}
