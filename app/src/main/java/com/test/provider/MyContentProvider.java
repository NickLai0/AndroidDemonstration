package com.test.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/3/23<br>
 * Time: 11:59<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 *
 * Implement this to initialize your content provider on startup.
 * This method is called for all registered content providers on the application main thread at application launch time.
 * It must not perform lengthy operations, or application startup will be delayed.
 *
 */
public class MyContentProvider extends ContentProvider {

    private static final String TAG = MyContentProvider.class.getSimpleName();

    @Override
    public boolean onCreate() {
        Log.i(TAG, "onCreate -> the content provider initialized!");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
