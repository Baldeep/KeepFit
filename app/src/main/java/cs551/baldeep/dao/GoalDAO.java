package cs551.baldeep.dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
            queryBuilder.orderBy(Goal.GOAL_DATE, false);

            PreparedQuery<Goal> query = queryBuilder.prepare();
            List<Goal> goals = goalDAO.query(query);
            if(goals.size() >= 1){
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

            queryBuilder.orderBy(Goal.GOAL_DATE, false);

            PreparedQuery<Goal> query = queryBuilder.prepare();
            return goalDAO.query(query);

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<Goal>();
        }
    }

    public Goal findFinishedForDate(Date date){
        try{
            QueryBuilder<Goal, String> queryBuilder = goalDAO.queryBuilder();

            Where where = queryBuilder.where();
            where.eq(Goal.GOAL_DONE, true);
            where.and();
            where.eq(Goal.GOAL_DATE, date);

            queryBuilder.orderBy(Goal.GOAL_DATE, false);

            PreparedQuery<Goal> query = queryBuilder.prepare();

            List<Goal> results = goalDAO.query(query);
            if(results.size() > 0){
                return results.get(0);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteAllFinished() {
        try{
            DeleteBuilder<Goal, String> deleteBuilder = goalDAO.deleteBuilder();

            Where where = deleteBuilder.where();
            where.eq(Goal.GOAL_DONE, true);


            PreparedDelete<Goal> query = deleteBuilder.prepare();

            goalDAO.delete(query);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Goal> findAllFinishedByFilters(Date startDate, Date endDate, int startPercentage, int endPecentage){
        try{
            QueryBuilder<Goal, String> queryBuilder = goalDAO.queryBuilder();

            Where where = queryBuilder.where();
            where.eq(Goal.GOAL_DONE, true);
            where.and();

            if(startDate != null) {
                where.ge(Goal.GOAL_DATE, startDate);
                where.and();
            }
            if(endDate != null) {
                where.le(Goal.GOAL_DATE, endDate);
                where.and();
            }

            where.ge(Goal.PERCENTAGE_COMPLETED, startPercentage);
            if(endPecentage < 100) {
                where.and();
                where.le(Goal.PERCENTAGE_COMPLETED, endPecentage);
            }

            queryBuilder.orderBy(Goal.GOAL_DATE, false);

            PreparedQuery<Goal> query = queryBuilder.prepare();

            return goalDAO.query(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<Goal>();
        }
    }

}
