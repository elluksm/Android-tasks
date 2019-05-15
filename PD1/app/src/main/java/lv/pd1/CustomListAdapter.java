package lv.pd1;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter {

    private Activity context;
    private Integer image =  R.drawable.folder;
   ArrayList<String> nameArray = new ArrayList<String>();

    public CustomListAdapter(Activity context, ArrayList<String> nameArrayParam){

        super(context,R.layout.listview_row , nameArrayParam);

        this.context=context;
        this.nameArray = nameArrayParam;
    }

    public void addName(String newName) {
        nameArray.add(newName);
    }


    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_row, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView nameTextField = (TextView) rowView.findViewById(R.id.nameTextViewID);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1ID);

        //this code sets the values of the objects to values from the arrays
        nameTextField.setText(nameArray.get(position));
        imageView.setImageResource(image);

        return rowView;

    };
}