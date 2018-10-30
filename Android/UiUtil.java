
/**
    * util for Android Json Object 
*/

public class UiUtil {

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// region Search Subview

    public interface SearchViewPredicate {
        boolean matchSearch(View view);
    }

    public static ArrayList<View> getAllSubViewByClassType(View rootView, Class classType) {
        return getAllSubViewByPredicate(rootView, view -> {
            if (view.getClass() == classType) {
                return true;
            }
            return false;
        });
    }

    public static ArrayList<View> getAllSubViewByTagName(View rootView, String tagName) {
        if (Strings.isNullOrEmpty(tagName)) {
            return new ArrayList<>();
        }
        return getAllSubViewByPredicate(rootView, view -> {
            Object tag = view.getTag();
            if (tag == null || tag instanceof String == false) {
                return false;
            }
            String tagString = (String) tag;
            return tagString.equals(tagName);
        });
    }

    public static ArrayList<View> getAllSubViewByPredicate(View rootView, SearchViewPredicate predicate) {
        ArrayList<View> viewArrayList = new ArrayList<>();
        if (rootView == null) {
            return viewArrayList;
        }

        if (rootView instanceof ViewGroup == false) {
            if (predicate.matchSearch(rootView)) {
                viewArrayList.add(rootView);
            }
            return viewArrayList;
        }


        ViewGroup viewGroup = (ViewGroup) rootView;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childView = viewGroup.getChildAt(i);
            if (predicate.matchSearch(childView)) {
                viewArrayList.add(childView);
            }
            // check subView
            if (childView instanceof ViewGroup) {
                viewArrayList.addAll(getAllSubViewByPredicate(childView, predicate));
            }
        }
        return viewArrayList;
    }

// endregion
   
}