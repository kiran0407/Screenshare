package com.enchcorp.screenshare;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.turkialkhateeb.materialcolorpicker.ColorChooserDialog;
import com.turkialkhateeb.materialcolorpicker.ColorListener;

import java.io.ByteArrayOutputStream;

public class WhiteboardActivity extends NDNChronoSyncActivity {
    private DrawingView drawingView_canvas; // Reference to the associated DrawingView

    // View references
    private ImageButton button_color;

    private String whiteboard;
    private String prefix;
    Handler mHandler = new Handler();      // To handle view access from other threads
    ProgressDialog progressDialog = null;  // Progress dialog for initial setup
    // Helper for TTS

    private String TAG = WhiteboardActivity.class.getSimpleName();  // TAG for logging
    protected static final int RESULT_SPEECH = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whiteboard);
        Intent introIntent = getIntent();
        this.username = introIntent.getExtras().getString("name");  // from NDNChronoSyncActivity
        this.whiteboard = introIntent.getExtras().getString("whiteboard").replaceAll("\\s", "");
        this.prefix = introIntent.getExtras().getString("prefix");
        Log.d(TAG, "username: " + this.username);
        Log.d(TAG, "whiteboard: " + this.whiteboard);
        Log.d(TAG, "prefix: " + this.prefix);
        Toast.makeText(getApplicationContext(), "Welcome " + this.username, Toast.LENGTH_SHORT)
                .show();
        applicationNamePrefix = prefix + "/" + whiteboard + "/" + username;
        applicationBroadcastPrefix = "/ndn/broadcast/whiteboard/" + whiteboard;
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        // Get relevant View references
        drawingView_canvas = (DrawingView) findViewById(R.id.drawingview_canvas);
        ImageButton button_pencil = (ImageButton) findViewById(R.id.button_pencil);
        ImageButton button_eraser = (ImageButton) findViewById(R.id.button_eraser);
       // ImageButton button_change = (ImageButton) findViewById(R.id.button_change);
        button_color = (ImageButton) findViewById(R.id.button_color);
        // ImageButton button_save = (ImageButton) findViewById(R.id.button_save);
        ImageButton button_undo = (ImageButton) findViewById(R.id.button_undo);
        ImageButton button_clear = (ImageButton) findViewById(R.id.button_clear);

        // Set link to this activity in the DrawingView class
        drawingView_canvas.setWhiteboardActivity(this);




        // Set click listeners for the buttons
        button_pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView_canvas.setPencil();
            }
        });
        button_eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView_canvas.setEraser();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorChooserDialog dialog = new ColorChooserDialog(WhiteboardActivity.this);
                dialog.setTitle("Choose color");
                dialog.setColorListener(new ColorListener() {
                    @Override
                    public void OnColorClick(View v, int color) {
//                        //do whatever you want to with the values
//                        String col=Integer.toString(color);
//                        Toast.makeText(getApplicationContext(),Integer.toString(color),Toast.LENGTH_LONG).show();
                        drawingView_canvas.setBackgroundColor(color);

                    }
                });
                //customize the dialog however you want
                dialog.show();


            }
        });
        button_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // drawingView_canvas.incrementColor();

                drawingView_canvas.incrementColor();

            }
        });
//        button_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                confirmSave();
//            }
//        });

        button_undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView_canvas.undo();
            }
        });
        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmErase();
            }
        });

        // Initialize TTS Helper


        // Show progress dialog for setup
        progressDialog = ProgressDialog.show(this, "Initializing", "Performing ping", true);

        // Ping -> RegisterPrefix -> ChronoSyncRegistration
        initialize();

        Log.d(TAG, "Finished onCreate");

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Set the boolean flag that stops all long running loops
        stop();


        Log.d(TAG, "Finished onDestroy");
    }
    public void callback(String jsonData) {
        dataHistory.add(jsonData);  // Add action to history
        increaseSequenceNos();
        Log.d(TAG, "Stroke generated: " + jsonData);
    }

    /**
     * Change color of the color button based the currently active color
     *
     * @param color the color to paint the button
     */
    public void setButtonColor(int color) {
        button_color.setBackgroundColor(color);
    }

    /**
     * Function to confirm that user want's the canves to be erased
     */
    private void confirmErase() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirm erase")
                .setMessage("Are you sure you want to erase the canvas?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        drawingView_canvas.clear();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    /**
     * Function to confirm that the user want's to save the current whiteboard state as a image in
     * phone gallery
     */
    private void confirmSave() {
        drawingView_canvas.setDrawingCacheEnabled(true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        drawingView_canvas.getDrawingCache().compress(Bitmap.CompressFormat.PNG, 100, baos);
        Utils.saveWhiteboardImage(this, baos);
        drawingView_canvas.destroyDrawingCache();
    }
    @Override
    public Handler getHandler() {
        return mHandler;
    }

    /**
     * @return the progress dialog for initial NDN setup
     */
    @Override
    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    @Override
    public void handleDataReceived(String data) {
        drawingView_canvas.callback(data);
    }

    //*********************************************

}
