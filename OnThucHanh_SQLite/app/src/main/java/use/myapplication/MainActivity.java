package use.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // khai báo biến cho tác giả
    EditText ed_authorID, ed_authorFName, ed_authorLName;
    Button btn_authorSelect, btn_authorSave, btn_authorDelete, btn_authorUpdate;
    ArrayList<Author> authors;
    CustomAdapterAuthor customAuthors;
    ListView list_Authors;

    // khai báo biến cho sách
    EditText ed_bID, ed_bTitle, ed_bDate, ed_bAuthorId;
    Button btnbSelect, btnbSave, btnbUpdate, btnbDelete;
    Spinner spinBooks;
    ArrayAdapter<String> adater_Spiner;
    ArrayList<String> book_authors;
    GridView gridBooks;
    ArrayList<String> array_BookToString;       // chuyển đổi đối tượng thành string để lưu đưa và apdapter gridview
    ArrayAdapter<String> adapter_books;
    ArrayList<Book> list_Books;

    DatabaseHelper database = new DatabaseHelper(this);;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.qlAuthors:
                showDialogAuthor();
                return true;
            case R.id.qlBooks:
                showDialogBook();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initEventAuthor(Dialog dialog) {
        ed_authorID = dialog.findViewById(R.id.ed_authorID);
        ed_authorFName = dialog.findViewById(R.id.ed_authorFName);
        ed_authorLName = dialog.findViewById(R.id.ed_authorLName);

        btn_authorSelect = dialog.findViewById(R.id.btn_authorSelect);
        btn_authorSave = dialog.findViewById(R.id.btn_authorSave);
        btn_authorDelete = dialog.findViewById(R.id.btn_authorDelete);
        btn_authorUpdate = dialog.findViewById(R.id.btn_authorUpdate);

        list_Authors = dialog.findViewById(R.id.list_Authors);
    }

    public void setAdapterAuthors() {
        if (customAuthors == null) {
            customAuthors = new CustomAdapterAuthor(this, R.layout.listview_authors, authors);
            list_Authors.setAdapter(customAuthors);
        }
        else
            customAuthors.notifyDataSetChanged();
    }

    public void showDialogAuthor() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Light);
        dialog.setTitle("Quản lý tác giả");
        dialog.setContentView(R.layout.dialog_author);

        initEventAuthor(dialog);

        authors = new ArrayList<>();

        eventHandelforDiagloAuhtor();

        dialog.show();
    }

    public void eventHandelforDiagloAuhtor() {
        btn_authorSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Author author = new Author(Integer.parseInt(ed_authorID.getText().toString()),
                                            ed_authorFName.getText().toString(),
                                            ed_authorLName.getText().toString());
                if (authors.contains(author))
                    Toast.makeText(MainActivity.this, "Trùng mã", Toast.LENGTH_SHORT).show();
                else
                    if (database.insertAuthor(author)) {
                        authors.add(author);
                        Toast.makeText(MainActivity.this, "Thêm tác giả thành công", Toast.LENGTH_SHORT).show();
                    }
            }
        });
        btn_authorSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAdapterAuthors();
            }
        });
        btn_authorDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = list_Authors.getChildCount() - 1; i >= 0; i--) {
                    // lấy view của listview_authors
                    View customView = list_Authors.getChildAt(i);
                    CheckBox ckb = customView.findViewById(R.id.checked);
                    if (ckb.isChecked()) {
                        int id = authors.get(i).getId();       // lấy ra mã tác giả
                        database.deleteAuthor(id);
                        authors.remove(i);
                        setAdapterAuthors();
                    }
                }
            }
        });
        btn_authorUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                for (int i = list_Authors.getChildCount() - 1; i >= 0; i--) {
//                    View customView = list_Authors.getChildAt(i);
//                    CheckBox ckb = customView.findViewById(R.id.checked);
//                    if (ckb.isChecked()) {
//                        int id = authors.get(i).getId();
//                        String firstName = ed_authorFName.getText().toString();
//                        String lastName = ed_authorLName.getText().toString();
//                        if (database.updateAuthor(id, firstName, lastName))
//                            Toast.makeText(MainActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
//                    }
//                }
                int id = Integer.parseInt(ed_authorID.getText().toString());
                String firstName = ed_authorFName.getText().toString();
                String lastName = ed_authorLName.getText().toString();
                if (database.updateAuthor(id, firstName, lastName))
                Toast.makeText(MainActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            }
        });
        list_Authors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, authors.get(i).toString(), Toast.LENGTH_SHORT).show();
                Author aut = authors.get(i);
                ed_authorID.setText(aut.getId() + "");
                ed_authorFName.setText(aut.getFirstname());
                ed_authorLName.setText(aut.getLastname());
            }
        });
    }
    // --------------------------------------------- Dialog Book --------------------------------------------- //

    /**
     * Nếu gặp lỗi này
     * 'Attempt to invoke virtual method 'void android.widget.Spinner.setAdapter(android.widget.SpinnerAdapter)' on a null object reference'
     * trong khi thực hiện với dialog thì do bạn quên khai báo findViewById của dialog tên function chưa truyền giá trị Dialog public void initEventBooks ()
     */

    public void initEventBooks (Dialog dialog) {
 //       ed_bID = findViewById(R.id.ed_bID);         // khai báo sai không truyền qua layout của dialog
        ed_bID = dialog.findViewById(R.id.ed_bID);
        ed_bTitle = dialog.findViewById(R.id.ed_bTitle);
        ed_bDate = dialog.findViewById(R.id.ed_bDate);

        spinBooks = dialog.findViewById(R.id.spi_bAuthorId);

        btnbSelect = dialog.findViewById(R.id.btnbSelect);
        btnbSave = dialog.findViewById(R.id.btnbSave);
        btnbUpdate = dialog.findViewById(R.id.btnbUpdate);
        btnbDelete = dialog.findViewById(R.id.btnbDelete);

        gridBooks = dialog.findViewById(R.id.list_books);
    }
    public void showDialogBook () {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Light);
        dialog.setTitle("Quản lý sách");
        dialog.setContentView(R.layout.dialog_book);

        initEventBooks(dialog);

        // truyền spiner
        authors = database.getAllAuthors();
        book_authors = new ArrayList<>();
        for (Author a : authors)
            book_authors.add(a.getId() + " - " + a.getFirstname() + " " + a.getLastname());

        adater_Spiner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, book_authors);
        adater_Spiner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinBooks.setAdapter(adater_Spiner);

        // truyền gridview
        array_BookToString = new ArrayList<>();

        list_Books = database.getAllBooks();

        // chuyển thành string để đưa vào array_BookToString
        for (Book b : list_Books) {
            array_BookToString.add(b.getId() + "");
            array_BookToString.add(b.getTitle());
            array_BookToString.add(b.getDateadded());
            array_BookToString.add(b.getAuthorid() + "");
        }

        eventHandelforDiagloBook();

        dialog.show();
    }

    public void setAdapter_books() {
        if (adapter_books == null) {
            adapter_books = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, array_BookToString);
            gridBooks.setAdapter(adapter_books);
        }
        else
            adapter_books.notifyDataSetChanged();
    }

    public void eventHandelforDiagloBook() {
        btnbSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAdapter_books();
            }
        });
        btnbSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String author = spinBooks.getSelectedItem().toString();
                String[] strs = author.split(" - ");            // cắt từng phần tử trong chuỗi thành mảng
                Book book = new Book(Integer.parseInt(ed_bID.getText().toString()), ed_bTitle.getText().toString(),
                                        ed_bDate.getText().toString(), Integer.parseInt(strs[0]));
                if (list_Books.contains(book))
                    Toast.makeText(MainActivity.this, "Trùng mã", Toast.LENGTH_SHORT).show();
                else if (database.insertBook(book)) {
                    list_Books.add(book);
                    array_BookToString.add(book.getId() + "");
                    array_BookToString.add(book.getTitle());
                    array_BookToString.add(book.getDateadded());
                    array_BookToString.add(book.getAuthorid() + "");
                    Toast.makeText(MainActivity.this, "Thêm tác giả thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnbDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_bID.getText().toString().equals(""))
                    Toast.makeText(MainActivity.this, "Nhập id sách để xóa", Toast.LENGTH_SHORT).show();
                else {
                    int id = Integer.parseInt(ed_bID.getText().toString());       // lấy ra mã tác giả
                    database.deleteBook(id);
                    list_Books = database.getAllBooks();
                    for (Book b : list_Books) {
                        array_BookToString.add(b.getId() + "");
                        array_BookToString.add(b.getTitle());
                        array_BookToString.add(b.getDateadded());
                        array_BookToString.add(b.getAuthorid() + "");
                    }
                    adapter_books.notifyDataSetChanged();
                }
            }
        });
        btnbUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = Integer.parseInt(ed_bID.getText().toString());
                String title = ed_bTitle.getText().toString();
                String date = ed_bDate.getText().toString();
                String author = spinBooks.getSelectedItem().toString();
                String[] strs = author.split(" - ");            // cắt từng phần tử trong chuỗi thành mảng
                if (database.updateBook(id, title, date, Integer.parseInt(strs[0])))
                    Toast.makeText(MainActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
