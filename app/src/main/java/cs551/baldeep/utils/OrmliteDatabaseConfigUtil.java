package cs551.baldeep.utils;

import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import cs551.baldeep.models.Goal;

/**
 * Created by balde on 12/02/2017.
 */

public class OrmliteDatabaseConfigUtil extends OrmLiteConfigUtil {

    private static final Class<?>[] classes = new Class[] {
            Goal.class
    };

    public static void main(String[] args) {

        String configPath = "/app/src/main/res/raw/ormlite_config.txt";

        String projectDir = System.getProperty("user.dir");

        String fullPath = projectDir + configPath;

        File configFile = new File(fullPath);

        try {

            if(configFile.exists()){
                configFile.delete();
                configFile = new File(fullPath);
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();
            } else {
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();
            }

            System.out.println("About to write config file for db");

            writeConfigFile(configFile, classes);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("OrmLiteDBConfig", "Failed to create SQL for db" + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("OrmLiteDBConfig", "Failed to write to file " + configFile.getPath() + ".\n" + e.toString());
        }

    }

}
