package cs551.baldeep.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by balde on 07/02/2017.
 */

public class Subject {

    private List<Observer> observers = new ArrayList<Observer>();

    public void notifyAllObservers(){
        for(Observer o : observers){
            o.update();
        }
    }

    public void attach(Observer observer){
        observers.add(observer);
    }
}
