package com.mmq.studentmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;


public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    CustomAdapter adapter = null;
    ArrayList<Student> list;
    ListView listView;
    TextView textView;
    Firebase ref;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Inits
        Firebase.setAndroidContext(this);
        ref = new Firebase(APP.FIREBASE_SERVER);

        // mapping
        textView = (TextView)findViewById(R.id.textView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(ListActivity.this);

        // Load item list
        progressBar.setVisibility(View.VISIBLE);
        ref.child(APP.FIREBASE_CHILD).addValueEventListener(firebaseLoadListEvent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Hide the reset button on destroyed
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Hide the reset button on destroyed
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Start new Activity
        Intent intent = new Intent(view.getContext(), DetailedActivity.class);

        // Send an Student obj to DetailedActivity
        intent.putExtra("student", list.get(position));
        view.getContext().startActivity(intent);
    }

    // Menu inits
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.m_add_new:
                // Start new Activity
                Intent intent = new Intent(ListActivity.this, EditingActivity.class);
                // In case "Add new": Send an empty Student obj to EditingActivity
                intent.putExtra("student", new Student());
                startActivity(intent);
                return true;
            case R.id.m_Reset:
                // Reset to the default list.
                ref.child(APP.FIREBASE_CHILD).addValueEventListener(firebaseLoadListEvent());
                return true;
            case R.id.m_sort_by_name:
                ref.child(APP.FIREBASE_CHILD).orderByChild("name").addValueEventListener(firebaseLoadListEvent());
                return true;
            case R.id.m_sort_by_score_o:
                ref.child(APP.FIREBASE_CHILD).orderByChild("averageScore").startAt(5).addValueEventListener(firebaseLoadListEvent());
                return true;
            case R.id.m_sort_by_score_u:
                ref.child(APP.FIREBASE_CHILD).orderByChild("averageScore").endAt(4.99).addValueEventListener(firebaseLoadListEvent());
                return true;
            case R.id.m_highest_score:
                ref.child(APP.FIREBASE_CHILD).orderByChild("averageScore").limitToLast(1).addValueEventListener(firebaseLoadListEvent());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Load item list function.
    public ValueEventListener firebaseLoadListEvent() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Create new list.
                int count = 0;
                list = new ArrayList<>();
                progressBar.setVisibility(View.VISIBLE);

                // Get each data from snapshot
                for (DataSnapshot db : dataSnapshot.getChildren()) {
                    // Get each Student object
                    Student IDOL = db.getValue(Student.class);
                    // Set key
                    IDOL.setKey(db.getKey());
                    // Add student into list
                    list.add(IDOL);
                    count++;
                }
                // Hide the progressBar
                progressBar.setVisibility(View.GONE);

                // Set adapter.
                adapter = new CustomAdapter(ListActivity.this, R.layout.item_list, list);
                listView.setAdapter(adapter);
                textView.setText("Students: " + count);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        return postListener;
    }
}
