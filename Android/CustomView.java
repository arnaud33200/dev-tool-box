public class CustomView extends RelativeLayout {

    public ConsoleLogView(Context context) {
        super(context);
        init(context);
    }

    public ConsoleLogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
    // setup View from layout
        int layoutRes = 0; // TODO: set layout res
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(layoutRes, null, false);
        addView(rootView);
        
    // setup view component
        // TODO
    }
}
