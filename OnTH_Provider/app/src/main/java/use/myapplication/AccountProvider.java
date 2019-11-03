package use.myapplication;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AccountProvider extends ContentProvider {

    static final String TAG = "AccountProvider";
    static final String AUTHORITY = "content://use.myapplication.AccountProvider/account";
    static final Uri CONTENT_URI = Uri.parse(AUTHORITY);

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DBHelper helper = new DBHelper(context);
        db = helper.getWritableDatabase();
        if (db!=null) return true;
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cur = db.rawQuery(selection, null);
        return cur;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        String ret = getContext().getContentResolver().getType(Settings.System.CONTENT_URI);
        Log.d(TAG,"getType returning: "+ret);
        return ret;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long rowID = db.insert("account", null, contentValues);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            Log.d("Thêm thành công", "------");
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String value, @Nullable String[] selectAgrs) {

//        Cursor cursor = db.rawQuery("Delete from account where accountid = " + Integer.parseInt(value), null);
//        if (cursor == null)
//            return -1;
//        cursor.moveToFirst();
//        cursor.close();
//        return cursor.getColumnCount();
        int count = 0;
        count = db.delete("account", "accountid = '" + value + "'", selectAgrs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String ma, @Nullable String[] selectionArgs) {
        int count = 0;
        count = db.update("account", contentValues, "accountid = '" + ma + "'", null);
        getContext().getContentResolver().notifyChange(uri, null);
        Log.d("Cập nhật thành công", "------");
        return count;
    }
}
