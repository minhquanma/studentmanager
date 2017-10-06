package com.mmq.studentmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;

import com.firebase.client.Firebase;
import com.firebase.client.core.view.View;


/**
 * Created by Jon on 4/5/2017.
 */

public class APP {
    public static String FIREBASE_SERVER = "https://students-4fa29.firebaseio.com/";
    public static String FIREBASE_CHILD = "Student";

    public static void removeStudent(final Context context, final Student student) {
        // Inits Firebase
        Firebase.setAndroidContext(context);
        Firebase ref = new Firebase(FIREBASE_SERVER).child(FIREBASE_CHILD);
        // Delete data
        ref.child(student.getKey()).removeValue();
    }

    public static void editStudent(Context context, Student student) {

    }
}
