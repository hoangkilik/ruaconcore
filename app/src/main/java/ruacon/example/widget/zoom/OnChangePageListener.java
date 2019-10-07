package ruacon.example.widget.zoom;

/**
 * Created by Bui Xuan VU on 1/19/2017.
 * Listener for scroll view when update page position
 */

public interface OnChangePageListener {
    void onChangePage(int page);

    float getPageWidth();

    float getPageHeight();

    int getTotalPage();
}
