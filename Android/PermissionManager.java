import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import java.util.ArrayList;

public class PermissionManager {

    public static final int ALL_PERMISSIONS = 11;
    public static final int PHONE_PERMISSIONS = 12;
    public static final int READ_WRITE_PERMISSIONS = 13;
    public static final int READ_WRITE_CAMERA_PERMISSIONS = 14;
    public static final int FILE_RECORD_PERMISSIONS = 15;

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// region METHOD TO COMPLETE

    private static Context getContext() {

        // TODO

        return null;
    }

    private static void displayMessagePermissionPermanentlyDenied(ArrayList<PermissionType> permissionPermanentlyDeniedArray, Activity activity) {
        if (permissionPermanentlyDeniedArray.size() == 0) {
            return;
        }

        SpannableStringBuilder message = new SpannableStringBuilder();
        String globalMessage = "<TO DO: tell that app won't work properly because some permission are denied>";
        message.append(new SpannableString(globalMessage + "\n\n"));

        int leadingMargin = 30;
        for (int i = 0; i < permissionPermanentlyDeniedArray.size(); i++) {
            PermissionType type = permissionPermanentlyDeniedArray.get(i);
            CharSequence line = type.getShortName() + ": " + type.getMessageString() + "\n";
            Spannable spannable = new SpannableString(line);
            spannable.setSpan(new BulletSpan(leadingMargin), 0, spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            message.append(spannable);
        }

        // TODO: display dialog here
    }

// endregion



    public interface PermissionTextInterface {
        String getText();
    }

    public enum PermissionType {
        Sip(1, Manifest.permission.USE_SIP, new PermissionTextInterface() {
            @Override public String getText() {
                return "<TO DO>";
            }
        }),
        RecordAudio(2, Manifest.permission.RECORD_AUDIO, new PermissionTextInterface() {
            @Override public String getText() {
                return "<TO DO>";
            }
        }),
        Camera(5, Manifest.permission.CAMERA, new PermissionTextInterface() {
            @Override public String getText() {
                return "<TO DO>";
            }
        }),
        ReadStorage(3, Manifest.permission.READ_EXTERNAL_STORAGE, new PermissionTextInterface() {
            @Override public String getText() {
                return "<TO DO>";
            }
        }),
        WriteStorage(4, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionTextInterface() {
            @Override public String getText() {
                return "<TO DO>";
            }
        }),
        PhoneContact(0, Manifest.permission.READ_CONTACTS, new PermissionTextInterface() {
            @Override public String getText() {
                return "<TO DO>";
            }
        }),
        ReadPhoneState(6, Manifest.permission.READ_PHONE_STATE, new PermissionTextInterface() {
            @Override public String getText() {
                return "<TO DO>";
            }
        });

        public final int requestCode;
        public final PermissionTextInterface textInterface;
        public final String permissionString;

        PermissionType(int requestCode, String permissionString, PermissionTextInterface textInterface) {
            this.requestCode = requestCode;
            this.textInterface = textInterface;
            this.permissionString = permissionString;
        }

        public CharSequence getShortName() {
            PackageManager packageManager = getContext().getPackageManager();
            PermissionInfo permissionInfo = null;
            try {
                permissionInfo = packageManager.getPermissionInfo(permissionString, 0);
                PermissionGroupInfo permissionGroupInfo = packageManager.getPermissionGroupInfo(permissionInfo.group, 0);
                return permissionGroupInfo.loadLabel(packageManager);
            } catch (Exception whatEver) {
                return "";
            }
        }

        public static PermissionType fromRequestCode(int code) {
            for (PermissionType type : PermissionType.values()) {
                if (code == type.requestCode) {
                    return type;
                }
            }
            return Sip;
        }

        public static PermissionType fromPermissionString(String permissionString) {
            for (PermissionType type : PermissionType.values()) {
                if (type.permissionString.equals(permissionString)) {
                    return type;
                }
            }
            return Sip;
        }

        public String getMessageString() {
            return textInterface.getText();
        }
    }

    private static ArrayList<PermissionRequestWithCodeListener> permissionRequestListenerArrayList;

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// region MULTIPLE PERMISSION CHECK

    public static boolean hasWriteSettingsPermission(Activity activity) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.System.canWrite(activity);
    }

    private static ArrayList<PermissionType> getPhonePermissionArray() {
        ArrayList<PermissionType> permissionTypes = new ArrayList<>();
        permissionTypes.add(PermissionType.Sip);
        permissionTypes.add(PermissionType.RecordAudio);
        permissionTypes.add(PermissionType.ReadPhoneState);
        return permissionTypes;
    }

    public static boolean hasPhonePermissions() {
        ArrayList<PermissionType> permissionTypes = getPhonePermissionArray();
        return PermissionManager.hasGrantedPermission(permissionTypes);
    }

    public static boolean displayPhonePermissionRequestIfNotGranted(Activity activity, PermissionRequestListener listener) {
        ArrayList<PermissionType> permissionTypes = getPhonePermissionArray();
        return displayPermissionRequestIfNotGranted(activity, permissionTypes, PHONE_PERMISSIONS, listener);
    }

    public static ArrayList<PermissionType> getReadWritePermissionArray() {
        ArrayList<PermissionType> permissionTypes = new ArrayList<>();
        permissionTypes.add(PermissionType.ReadStorage);
        permissionTypes.add(PermissionType.WriteStorage);
        return permissionTypes;
    }

    public static ArrayList<PermissionType> getReadWriteCameraPermissionArray() {
        ArrayList<PermissionType> permissionTypes = new ArrayList<>();
        permissionTypes.add(PermissionType.ReadStorage);
        permissionTypes.add(PermissionType.WriteStorage);
        permissionTypes.add(PermissionType.Camera);
        return permissionTypes;
    }

    public static ArrayList<PermissionType> getFileRecordPermissionArray() {
        ArrayList<PermissionType> permissionTypes = new ArrayList<>();
        permissionTypes.add(PermissionType.ReadStorage);
        permissionTypes.add(PermissionType.WriteStorage);
        permissionTypes.add(PermissionType.RecordAudio);
        return permissionTypes;
    }


// endregion


// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// region CHECK GRANTED

    public static boolean hasGrantedPermission(PermissionType permissionType) {
        ArrayList<PermissionType> permissionTypes = new ArrayList<>();
        permissionTypes.add(permissionType);
        return hasGrantedPermission(permissionTypes);
    }

    public static boolean hasGrantedPermission(ArrayList<PermissionType> permissionTypes) {
        for (PermissionType type : permissionTypes) {
            String permissionString = type.permissionString;
            int status = ContextCompat.checkSelfPermission(getContext(), permissionString);
            if (status != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

// endregion

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// region DISPLAY PERMISSION

    public static boolean alreadyRequestOneTime(final Activity activity, PermissionType permissionType) {
        if (activity == null) {
            return false;
        }
        String permissionString = permissionType.permissionString;
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionString);
    }

    public static boolean displayPermissionRequestIfNotGranted(final Activity activity, PermissionType permissionType) {
        return displayPermissionRequestIfNotGranted(activity, permissionType, null);
    }

    /**
     * check if permission is granted, display the prompt if denied
     * @param activity the activity to display the permission
     * @param permissionType type of permission to display, see Enum
     * @param listener a callback when permission has been granted or not
     * @return true if permission granted
     */
    public static boolean displayPermissionRequestIfNotGranted(final Activity activity, final PermissionType permissionType, final PermissionRequestListener listener) {
        ArrayList<PermissionType> permissionTypes = new ArrayList<>();
        permissionTypes.add(permissionType);
        return displayPermissionRequestIfNotGranted(activity, permissionTypes, -1, listener);
    }

    public static boolean displayPermissionRequestIfNotGranted(Activity activity, ArrayList<PermissionType> permissionTypes, int code, final PermissionRequestListener listener) {

        int requestCode = code;
        ArrayList<String> listPermissionsNeeded = new ArrayList<>();
        // store denied permissions
        for (PermissionType type : permissionTypes) {
            if (requestCode < 0) { requestCode = type.requestCode; }
            if (hasGrantedPermission(type) == false) {
                listPermissionsNeeded.add(type.permissionString);
            }
        }

    // Everything already granted
        if (listPermissionsNeeded.isEmpty()) {
            if (listener != null) {
                listener.onRequestPermissionResult(true, code);
            }
            return true;
        }

        addPermissionListener(listener, requestCode);

    // needed if only one permission
        // avoid this error: "Can reqeust only one set of permissions at a time"
        if (permissionTypes.size() == 1) {
            PermissionType permissionType = permissionTypes.get(0);
            ActivityCompat.requestPermissions(activity, new String[]{permissionType.permissionString}, permissionType.requestCode);
        }
        else {
            ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), requestCode);
        }
        
        return false;
    }

// endregion

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// region CALL BACK REQUEST RESULT

    public interface PermissionRequestListener {
        void onRequestPermissionResult(boolean granted, int requestCode);
    }

    public interface PermissionRequestWithCodeListener extends PermissionRequestListener {
        int getRequestCode();
    }

    private static void addPermissionListener(final PermissionRequestListener listener, final int requestCode) {
        if (listener == null) {
            return;
        }
        if (permissionRequestListenerArrayList == null) {
            permissionRequestListenerArrayList = new ArrayList<>();
        }
        PermissionRequestWithCodeListener codeListener = new PermissionRequestWithCodeListener() {
            @Override
            public int getRequestCode() {
                return requestCode;
            }

            @Override
            public void onRequestPermissionResult(boolean granted, int requestCode) {
                listener.onRequestPermissionResult(granted, requestCode);
            }
        };
        permissionRequestListenerArrayList.add(codeListener);
    }

// endregion

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// region PERMISSION RESULT


    public static void onRequestPermissionsResult(final Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int result = grantResults != null && grantResults.length > 0 ? grantResults[0] : -1;
        boolean granted = result == PackageManager.PERMISSION_GRANTED;

    // check if the user check "Don't ask again"
        ArrayList<PermissionType> permissionPermanentlyDeniedArray = new ArrayList<>();
        for (int i = 0, len = permissions.length; i < len; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                boolean showRationale = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    showRationale = activity.shouldShowRequestPermissionRationale(permission);
                }
                // user ask to not see the notification again
                if (!showRationale) {
                    PermissionType type = PermissionType.fromPermissionString(permission);
                    permissionPermanentlyDeniedArray.add(type);
                }
            }
        }

        if (requestCode != ALL_PERMISSIONS) {
            displayMessagePermissionPermanentlyDenied(permissionPermanentlyDeniedArray, activity);
        }

        if (permissionRequestListenerArrayList == null) {
            return; // no listener
        }
        ArrayList<PermissionRequestWithCodeListener> listenerArray = (ArrayList<PermissionRequestWithCodeListener>) permissionRequestListenerArrayList.clone();
        for (PermissionRequestWithCodeListener listener : listenerArray) {
            if (listener.getRequestCode() == requestCode) {
                listener.onRequestPermissionResult(granted, requestCode);
                permissionRequestListenerArrayList.remove(listener);
            }
        }
    }

// endregion

// *********************************************************************************************
// region Overlay permission

    public static boolean checkOverlayPermission() {
        // Create floating view if valid android version and checking permission is on
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getContext())) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getContext().getPackageName()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
            return false;
        }
        return true;
    }

// endregion

}
