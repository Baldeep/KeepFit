package cs551.baldeep.observer;

/**
 * Created by balde on 07/02/2017.
 */

public abstract class Observer {

    protected Subject subject;

    public abstract void update();
}
