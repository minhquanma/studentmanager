package com.mmq.studentmanager;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Jon on 3/15/2017.
 */

public class CustomAdapter extends ArrayAdapter<Student> {

    private Activity activity;
    private int id_layout;
    private ArrayList<Student> list;

    public CustomAdapter(Activity activity, int id_layout, ArrayList<Student> list) {
        super(activity, id_layout, list);

        // assigning
        this.activity = activity;
        this.id_layout = id_layout;
        this.list = list;
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        final LayoutInflater inflater = activity.getLayoutInflater();
        convertView = inflater.inflate(id_layout, null);

        // Fill the view.
        ImageView imgAvatar = (ImageView)convertView.findViewById(R.id.imgAvatar);
        TextView txtName = (TextView)convertView.findViewById(R.id.txtName);
        TextView txtYear = (TextView)convertView.findViewById(R.id.txtYear);
        final Button btEdit = (Button)convertView.findViewById(R.id.btEdit);
        final Student student = list.get(position);

        // Set name
        txtName.setText(student.getName());

        // Set info
        txtYear.setText(student.getHome() + ", " + student.stringGender() + ", " + new DecimalFormat("0.0").format(student.getAverageScore()));

        // Set profile (avatar) picture
        if (URLUtil.isValidUrl(student.getAvatar()))
            Picasso.with(getContext())
                    .load(student.getAvatar())
                    .into(imgAvatar);

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(getContext(), btEdit);
                popup.getMenuInflater().inflate(R.menu.menu_popup, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.m_popup_edit:
                                // Open edit Intent
                                Intent editIntent = new Intent(getContext(), EditingActivity.class);
                                // Send current Student obj to DetailedActivity
                                editIntent.putExtra("student", student);
                                getContext().startActivity(editIntent);

                                return true;
                            case R.id.m_popup_remove:
                                // When "remove" option clicked
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("You are about to delete: ''" + student.getName() +"'' from database. Are you sure?");
                                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Do remove student.
                                        APP.removeStudent(getContext(),student);
                                        dialog.dismiss();
                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Do nothing
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                                return true;
                            default:
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        return convertView;
    }
}
