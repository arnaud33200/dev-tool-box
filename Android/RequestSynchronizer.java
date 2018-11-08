public class RequestSynchronizer {
    private Integer count = 0;
    private SyncFinishInterface callBackAfterSync;
    private boolean success = true;

    public interface SyncFinishInterface {
        void onSyncFinish(boolean success);
    }

    public RequestSynchronizer(SyncFinishInterface callBackAfterSync) {
        this.count = 0;
        this.callBackAfterSync = callBackAfterSync;
    }

    public void enter() {
        synchronized (count) {
            count++;
        }
    }

    public void leave() {
        leave(true);
    }

    public void leave(boolean success) {
        synchronized (count) {
            count--;
            this.success = this.success && success;
            if (count <= 0 && callBackAfterSync != null) {
                callBackAfterSync.onSyncFinish(this.success);
            }
        }
    }
}