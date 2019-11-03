package use.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String  DB_NAME = "quanly_tacgia";
    private static final String TABLE_AUTHOR = "author";
    private static final String TABLE_BOOK = "book";

    private static int VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table author(id integer primary key autoincrement, firstname text, lastname text)");
        sqLiteDatabase.execSQL("create table book(id integer primary key, title text, dateadded text," +
                "authorid integer not null constraint authorid references auhtor(id) on delete cascade)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("Drop table if exists author");
        sqLiteDatabase.execSQL("Drop table if exists book");     // xóa bảng chứa khóa ngoại trước
        onCreate(sqLiteDatabase);
    }

    public boolean insertAuthor(Author author) {
        if (author == null)
            return false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id", author.getId());
        cv.put("firstname", author.getFirstname());
        cv.put("lastname", author.getLastname());

        db.insert(TABLE_AUTHOR, null, cv);
        return true;
    }

    public boolean updateAuthor(int id, String firstname, String lastname) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("firstname", firstname);
        cv.put("lastname", lastname);
        db.update("author", cv, "id = " + id, null);
        return true;
    }

    public ArrayList<Author> getAllAuthors() {
        ArrayList<Author> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("Select * from author", null);
        if (cursor != null)
            cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            list.add(new Author(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return list;
    }

    public Author getAuthor(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from author where id = " + id, null);
        if (cursor != null)
            cursor.moveToFirst();
        Author author = new Author(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
        cursor.close();
        db.close();
        return author;
    }

    public boolean deleteAuthor(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Delete from author where id = " + id, null);
        if (cursor == null)
            return false;
        cursor.moveToFirst();
        cursor.close();
        return true;
    }


    // ------------------------------- Quản lý BOOKS -------------------------------//
    public boolean insertBook (Book b) {
        if (b == null)
            return false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("id", b.getId());
        cv.put("title", b.getTitle());
        cv.put("dateadded", b.getDateadded().toString());
        cv.put("authorid", b.getAuthorid());

        db.insert(TABLE_BOOK, null, cv);
        return true;
    }

    public boolean updateBook (int id, String title, String date, int authorid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("title", title);
        cv.put("dateadded", date);
        cv.put("authorid", authorid);
        db.update(TABLE_BOOK, cv, "id = " + id, null);
        return true;
    }

    public ArrayList<Book> getAllBooks() {
        ArrayList<Book> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("Select * from book", null);
        if (cur != null)
            cur.moveToFirst();
        while (cur.isAfterLast() == false) {
            list.add(new Book(cur.getInt(0), cur.getString(1), cur.getString(2), cur.getInt(3)));
            cur.moveToNext();
        }
        cur.close();
        db.close();
        return list;
    }

    public ArrayList<Book> getBooksByAuthor(int authorid) {
        ArrayList<Book> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("Select * from Book Where authorid = " + authorid, null);
        if (cur != null)
            cur.moveToFirst();
        while (cur.isAfterLast() == false) {
            list.add(new Book(cur.getInt(0), cur.getString(1), cur.getString(2), cur.getInt(3)));
            cur.moveToNext();
        }
        cur.close();
        db.close();
        return list;
    }

    public boolean deleteBook (int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("Delete from book where id = " + id, null);
        if (cur == null)
            return false;
        cur.moveToNext();
        cur.close();
        return true;
    }
}
