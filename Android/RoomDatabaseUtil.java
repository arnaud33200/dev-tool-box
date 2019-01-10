public static void removeDatabaseFileAndReset() {
    if (sInstance != null) { sInstance.close(); }
    sInstance = null;
    Context context = // Get Context here
    String databaseName = // Get DatabaseName
    File databaseFile = context.getDatabasePath(databaseName).getAbsoluteFile();
    if (databaseFile.exists()) {
        FileManager.deleteRecursive(databaseFile);
    }
}
