public class CustomView extends ANYTHING_VIEW {

    public CustomView(Context context) {
        super(context);
        init(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
    // setup View from layout
        int layoutRes = 0; // TODO: set layout res
        // use <merge ...> for the root of the layout
        
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(layoutRes, this, true);
        
    // setup view component
        // TODO
    }
}
