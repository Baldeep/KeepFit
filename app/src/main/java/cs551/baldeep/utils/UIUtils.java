package cs551.baldeep.utils;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by balde on 16/03/2017.
 */

public class UIUtils {

    /*
     * From:
     * http://stackoverflow.com/questions/1778485/android-listview-display-all-available-items-without-scroll-with-static-header
     */
    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();

                if(itemPos == numberOfItems - 1){
                    totalItemsHeight += item.getMeasuredHeight()/2;
                }
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            Log.i("UIUTILS", "Updated List Lenght: " + (totalDividersHeight + totalDividersHeight)+ "dp");
            return true;

        } else {
            return false;
        }
    }
}
