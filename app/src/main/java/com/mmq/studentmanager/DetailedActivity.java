package com.mmq.studentmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Calendar;

public class DetailedActivity extends AppCompatActivity {

    ImageView imgBigAvatar;
    TextView txtBirthYear;
    TextView txtHome;
    TextView txtAge;
    TextView txtName;
    TextView txtPhone;

    //Education
    TextView txtListening;
    TextView txtSpeaking;
    TextView txtReading;
    TextView txtWriting;
    TextView txtCourse;
    TextView txtReserved;

    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        // Mapping
        imgBigAvatar = (ImageView)findViewById(R.id.imgBigAvatar);
        txtBirthYear = (TextView)findViewById(R.id.txtBirthYear);
        txtHome = (TextView)findViewById(R.id.txtHome);
        txtAge = (TextView)findViewById(R.id.txtAge);
        txtName = (TextView)findViewById(R.id.txtName);
        txtPhone = (TextView)findViewById(R.id.txtPhone);
        txtListening = (TextView)findViewById(R.id.txtListening);
        txtSpeaking = (TextView)findViewById(R.id.txtSpeaking);
        txtReading = (TextView)findViewById(R.id.txtReading);
        txtWriting = (TextView)findViewById(R.id.txtWriting);
        txtCourse = (TextView)findViewById(R.id.txtCourse);
        txtReserved = (TextView)findViewById(R.id.txtReserved);

        // Receive the student extra.
        Intent intent = getIntent();
        student = (Student)intent.getExtras().getSerializable("student");

        // Do load detailed information.
        loadDetailedInfo();
    }

    public void loadDetailedInfo() {
        // Set personal information
        if (URLUtil.isValidUrl(student.getAvatar()))
            Picasso.with(this)
                    .load(student.getAvatar()) // Add this
                    .into(imgBigAvatar);
        txtName.setText("Name:   " + student.getName() + ", " + student.stringGender());
        txtReserved.setText(student.getKey());
        txtBirthYear.setText("Birth year:   " + student.getYear());
        txtHome.setText(("Birthplace:   " + student.getHome() + ", " + student.getCountry()));

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (student.getYear() != 0)
            txtAge.setText(("Current age:   " + (currentYear - student.getYear()) + " Years Old"));
        else
            txtAge.setText(("Current age: 0 Years Old"));
        txtPhone.setText("Phone number:   " + student.getPhone());

        // Set education results
        DecimalFormat df = new DecimalFormat("0.0");
        txtListening.setText("Listening:  " + df.format(student.getListeningPts()));
        txtSpeaking.setText("Speaking:  " + df.format(student.getSpeakingPts()));
        txtReading.setText("Reading:  " + df.format(student.getReadingPts()));
        txtWriting.setText("Writing:  " + df.format(student.getWritingPts()));
        txtCourse.setText("Attended year:  " + student.getCourseDate());
        txtReserved.setText("Average:   " + df.format(student.averageScore()));
    }

    // In case the EditingActivity send back an edited Student object
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                student = (Student)data.getExtras().getSerializable("student");
                loadDetailedInfo();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }

    // Menu inits
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detailed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.m_detailed_Edit:
                // Open edit Intent
                Intent editIntent = new Intent(DetailedActivity.this, EditingActivity.class);

                // Send current Student obj to DetailedActivity
                editIntent.putExtra("student", student);
                startActivityForResult(editIntent, 1);
                return true;
            case R.id.m_detailed_Remove:
                // Do remove student
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do remove student.
                        APP.removeStudent(DetailedActivity.this, student);
                        dialog.dismiss();
                        finish();
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
                return super.onOptionsItemSelected(item);
        }
    }
}
