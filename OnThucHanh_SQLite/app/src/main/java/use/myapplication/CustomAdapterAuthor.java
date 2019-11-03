package use.myapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapterAuthor extends ArrayAdapter<Author> {

    Activity context;
    int resource;
    ArrayList<Author> authors = null;

    public CustomAdapterAuthor(Activity context, int resource, ArrayList<Author> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.authors = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(resource, null);

        Author author = authors.get(position);

        if (authors.size() > 0 && position >= 0) {
            TextView id = convertView.findViewById(R.id.txtID);
            CheckBox checked = convertView.findViewById(R.id.checked);

            id.setText(author.toString());
        }

        return convertView;
    }
}
