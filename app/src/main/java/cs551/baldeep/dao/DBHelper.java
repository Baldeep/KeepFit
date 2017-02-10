package cs551.baldeep.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import cs551.baldeep.keepfit.R;
import cs551.baldeep.models.Activity;
import cs551.baldeep.models.Goal;

/**
 * Created by balde on 10/02/2017.
 */

public class DBHelper extends OrmLiteSqliteOpenHelper {

    public static final String DATABASE_NAME = "KeepFit.db";
    public static final int DATABASE_VERSION = 1;

    private Dao<Goal, String> goalDAO;
    private Dao<Activity, String> activityDAO;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null,
                DATABASE_VERSION, R.raw.ormlite_config);

    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try{
            TableUtils.createTable(connectionSource, Goal.class);
            TableUtils.createTable(connectionSource, Activity.class);
        } catch (SQLException e) {
            Log.e("DBHelper", "Failed to initialise tables. " + e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Goal.class, false);
            TableUtils.dropTable(connectionSource, Activity.class, false);
            onCreate(database, connectionSource);

        } catch (SQLException e) {
            Log.e("DBHelper", "Failed to upgrade tables. " + e.toString());
        }
    }

    public Dao<Goal, String> getGoalDAO() throws SQLException{
        if(goalDAO == null){
            goalDAO = getDao(Goal.class);
        }
        return goalDAO;
    }

    public Dao<Activity, String> getActivityDAO() throws SQLException{
        if(activityDAO == null){
            activityDAO = getDao(Activity.class);
        }
        return activityDAO;
    }

   /* public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + GOAL_TABLE_NAME + " "
                        + GOAL_COLUMN_ID + " integer primary key, "
                        + GOAL_COULMN_NAME + " varchar(255) not null, "
                        + GOAL_COLUMN_GOALVAL + " real, "
                        + GOAL_COLUMN_GOALPROGRESS + " real, "
                        + GOAL_COLUMN_GOALUNITS + " varchar(255);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GOAL_TABLE_NAME);
        onCreate(db);
    }*/


}
