package com.mmq.studentmanager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;

public class EditingActivity extends AppCompatActivity {

    EditText editTextName;
    EditText editTextHome;
    EditText editTextCountry;
    EditText editTextYear;
    EditText editTextPhone;
    EditText editTextAvatar;
    EditText editTextListening;
    EditText editTextSpeaking;
    EditText editTextReading;
    EditText editTextWriting;
    EditText editTextCourse;
    ImageView imgEditAvatar;
    Button buttonSave;
    Button buttonCreate;
    Button buttonSelect;
    TextView textViewDate;
    RadioGroup radioGroup;
    RadioButton radioMale;
    RadioButton radioFemale;

    private String selectedImagePath;
    private String filemanagerstring;

    Student student;
    Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing);

        // Init
        Firebase.setAndroidContext(EditingActivity.this);

        // Mapping
        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextHome = (EditText)findViewById(R.id.editTextHome);
        editTextCountry = (EditText)findViewById(R.id.editTextCountry);
        editTextYear = (EditText)findViewById(R.id.editTextYear);
        editTextPhone = (EditText)findViewById(R.id.editTextPhone);
        editTextAvatar = (EditText)findViewById(R.id.editTextAvatar);
        editTextListening = (EditText)findViewById(R.id.editTextListening);
        editTextSpeaking = (EditText)findViewById(R.id.editTextSpeaking);
        editTextReading = (EditText)findViewById(R.id.editTextReading);
        editTextWriting = (EditText)findViewById(R.id.editTextWriting);
        editTextCourse = (EditText)findViewById(R.id.editTextCourse);
        imgEditAvatar = (ImageView)findViewById(R.id.imgEditAvatar);
        buttonSave = (Button)findViewById(R.id.buttonSave);
        buttonCreate = (Button)findViewById(R.id.buttonCreate);
        buttonSelect = (Button)findViewById(R.id.buttonSelect);
        textViewDate = (TextView)findViewById(R.id.textViewDate) ;
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        radioMale = (RadioButton)findViewById(R.id.radioMale);
        radioFemale = (RadioButton)findViewById(R.id.radioFemale);

        // Get the student extra.
        Intent intent = getIntent();
        student = (Student)intent.getExtras().getSerializable("student");

        // Check if open for editing or creating
        if (student.getKey() == null) {
            buttonSave.setVisibility(View.GONE);
            setTitle("Create new");
        }
        else {
            // In case of "Editing", hide the "Create" button.
            buttonCreate.setVisibility(View.GONE);
            setStudentData();
        }

        // Save student
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Config Student obj
                final Student editedStudent = getStudentData();
                // Send a null key
                editedStudent.setKey(null);

                // Save new data
                ref = new Firebase(APP.FIREBASE_SERVER).child(APP.FIREBASE_CHILD);
                ref.child(student.getKey()).setValue(editedStudent);

                // Back to DetailedActivity when succeeded.
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Start new Activity
                        Intent intent = new Intent(EditingActivity.this, DetailedActivity.class);

                        // Send an Student obj to DetailedActivity
                        intent.putExtra("student", getStudentData());
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {}
                });
            }
        });

        // Create new Student
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create new student object based on input data.
                final Student new_student = getStudentData();
                new_student.setKey(null);

                // Store value to database, this code will generate new key value.
                ref = new Firebase(APP.FIREBASE_SERVER);
                ref.child(APP.FIREBASE_CHILD).push().setValue(new_student);

                // Back to DetailedActivity when succeeded.
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Start new Activity
                        Intent intent = new Intent(EditingActivity.this, DetailedActivity.class);

                        // Send an Student obj to DetailedActivity
                        intent.putExtra("student", new_student);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {}
                });
            }
        });

        // Select an image from local storage.
        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // select a file
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), 1);
            }
        });
    }
    // Open image gallery
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri selectedImageUri = data.getData();

                //OI FILE Manager
                filemanagerstring = selectedImageUri.getPath();

                //MEDIA GALLERY
                selectedImagePath = getPath(selectedImageUri);

                //DEBUG PURPOSE - you can delete this if you want
                if(selectedImagePath!=null)
                    System.out.println(selectedImagePath);
                else System.out.println("selectedImagePath is null");
                if(filemanagerstring!=null)
                    System.out.println(filemanagerstring);
                else System.out.println("filemanagerstring is null");

                //NOW WE HAVE OUR WANTED STRING
                if(selectedImagePath!=null) {
                    // Set new URL
                    editTextAvatar.setText(selectedImagePath);
                    if (URLUtil.isValidUrl(selectedImagePath))
                        Picasso.with(this)
                                .load(selectedImagePath)
                                .into(imgEditAvatar);
                }
                else
                    System.out.println("filemanagerstring is the right one for you!");
            }
        }
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null)
        {
            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return "file://" + cursor.getString(column_index);
        }
        else return null;
    }

    public void setStudentData() {
        DecimalFormat df = new DecimalFormat("0.0");

        // Information
        setTitle("Edit: " + student.getKey());
        editTextName.setText(student.getName());
        editTextHome.setText(student.getHome());
        editTextCountry.setText(student.getCountry());
        editTextYear.setText(student.getYear() + "");
        editTextPhone.setText(student.getPhone() + "");
        editTextAvatar.setText(student.getAvatar());
        textViewDate.setText("Last edited date: " + student.getCreatedDate());
        // Gender
        if (student.isGender())
            radioMale.setChecked(true);
        else
            radioFemale.setChecked(true);
        // Avatar
        if (URLUtil.isValidUrl(student.getAvatar()))
            Picasso.with(this)
                    .load(student.getAvatar())
                    .into(imgEditAvatar);
        else
            Picasso.with(this)
                    .load("@drawable/avatar")
                    .into(imgEditAvatar);

        // Education
        editTextListening.setText(df.format(student.getListeningPts()) + "");
        editTextSpeaking.setText(df.format(student.getSpeakingPts()) + "");
        editTextReading.setText(df.format(student.getReadingPts()) + "");
        editTextWriting.setText(df.format(student.getWritingPts()) + "");
        editTextCourse.setText(student.getCourseDate() + "");
    }

    public Student getStudentData() {
        // Config  Student obj
        final Student student = new Student();
        String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());

        // Information
        student.setKey(this.student.getKey());
        student.setName(editTextName.getText().toString());
        student.setHome(editTextHome.getText().toString());
        student.setCountry(editTextCountry.getText().toString());
        student.setCreatedDate(currentDateTime);
        student.setPhone(editTextPhone.getText().toString());
        student.setAvatar(editTextAvatar.getText().toString());
        if (radioGroup.getCheckedRadioButtonId() == radioMale.getId())
            student.setGender(true);
        else
            student.setGender(false);

        // Education
        try {
            student.setYear(Integer.parseInt(editTextYear.getText().toString()));
            student.setListeningPts(Float.parseFloat(editTextListening.getText().toString()));
            student.setSpeakingPts(Float.parseFloat(editTextSpeaking.getText().toString()));
            student.setReadingPts(Float.parseFloat(editTextReading.getText().toString()));
            student.setWritingPts(Float.parseFloat(editTextWriting.getText().toString()));
            student.setCourseDate(Integer.parseInt(editTextCourse.getText().toString()));
            student.setAverageScore(student.averageScore());
        } catch (NumberFormatException ex) {

        }
        return student;
    }
}
