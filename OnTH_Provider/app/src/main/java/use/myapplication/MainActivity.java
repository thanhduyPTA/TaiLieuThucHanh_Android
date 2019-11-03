package use.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText ed_ID, ed_pass, ed_role;
    Button btnSave, btnSelect;
    ListView listView;
    ArrayAdapter<Account> adapters;
    ArrayList<Account> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed_ID = findViewById(R.id.ed_ID);
        ed_pass = findViewById(R.id.ed_pass);
        ed_role = findViewById(R.id.ed_role);

        btnSave = findViewById(R.id.btnSave);
        btnSelect = findViewById(R.id.btnSelect);
        listView = findViewById(R.id.listView);

        list = selectAll();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Account account = new Account(ed_ID.getText().toString(), ed_pass.getText().toString(), ed_role.getText().toString());
                insert(account);

                // thực hiện xóa
//               delelteByAccountID(ed_ID.getText().toString());

//                updateAccount(account);
            }
        });
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAdapters();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Account account = list.get(i);
                ed_ID.setText(account.getAccountid());
                ed_pass.setText(account.getCredential());
                ed_role.setText(account.getRole());
            }
        });
    }

    public void setAdapters() {
        if (adapters == null) {
            adapters = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            listView.setAdapter(adapters);
        }
        else
            adapters.notifyDataSetChanged();
    }

    public void insert(Account account){

        if (list.contains(account))
            Toast.makeText(MainActivity.this, "Trùng mã", Toast.LENGTH_SHORT).show();
        else {
            Uri uri = AccountProvider.CONTENT_URI;
            ContentValues values = new ContentValues();

            values.put("accountid", account.getAccountid());
            values.put("credential", account.getCredential());
            values.put("role", account.getRole());

            getContentResolver().insert(uri, values);

            list.add(account);
            Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<Account> selectAll() {
        ArrayList<Account> ds = new ArrayList<>();

        Uri uri = AccountProvider.CONTENT_URI;
        Cursor cur = getContentResolver().query(uri, null, "Select * from account", null, null);

        if (cur != null)
            cur.moveToFirst();

        while (cur.isAfterLast() == false) {
            ds.add(new Account(cur.getString(0), cur.getString(1), cur.getString(2)));
            cur.moveToNext();
        }
        cur.close();
        return ds;
    }

    public void delelteByAccountID(String accountid) {
        if (accountid.equals(""))
            Toast.makeText(this, "AccountID không được rỗng", Toast.LENGTH_SHORT).show();
        else {
            Uri uri = AccountProvider.CONTENT_URI;
            int xoa = getContentResolver().delete(uri, accountid, null);
            Log.d("vị trí: ", "" + xoa);
            if (xoa > 0) {
                Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                Account acc = new Account(accountid);
                list.remove(acc);

                setAdapters();
            }
            else
                Toast.makeText(this, "Không có account này", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateAccount(Account account) {

        Uri uri = AccountProvider.CONTENT_URI;
        ContentValues contentValues = new ContentValues();

        contentValues.put("credential", account.getCredential());
        contentValues.put("role", account.getRole());

        int kt = getContentResolver().update(uri, contentValues, account.getAccountid(), null);
        if (kt > 0) {
            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();

            int vt = list.indexOf(account);
            list.get(vt).setCredential(account.getCredential());
            list.get(vt).setRole(account.getRole());

            setAdapters();
        }
        else
            Toast.makeText(this, "Không có account này", Toast.LENGTH_SHORT).show();
    }
}