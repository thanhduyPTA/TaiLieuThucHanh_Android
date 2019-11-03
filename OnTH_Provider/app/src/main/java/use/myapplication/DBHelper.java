package use.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "accountsdb";
    private static int VERSION = 1;

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("Create table account(accountid nvarchar(50) primary key, credential varchar(30) not null," +
                " role varchar(20) not null default 'guest')");
        ContentValues values = new ContentValues();
        values.put("accountid", "thanhduy");
        values.put("credential", "123");
        values.put("role", "admin");
        sqLiteDatabase.insert("account", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("Drop table if exists account");
        onCreate(sqLiteDatabase);
    }
}
