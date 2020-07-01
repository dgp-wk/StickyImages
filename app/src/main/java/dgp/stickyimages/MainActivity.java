package dgp.stickyimages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;

    private boolean isFullScreen = false;
    private boolean goingFullScreen = false;

    private int FULLSCREEN = View.SYSTEM_UI_FLAG_LOW_PROFILE
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart()
    {
        super.onStart();



        if (imageView == null) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 0);
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                DoSetup();
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(getApplicationContext(), "You haven't picked an image",Toast.LENGTH_LONG).show();
        }
    }

    private void DoSetup() {

        imageView = new ImageView(getApplicationContext());
        imageView.setOnSystemUiVisibilityChangeListener(
                new View.OnSystemUiVisibilityChangeListener()
                {
                    public void onSystemUiVisibilityChange(int visibility)
                    {
                        /*
                        //Toast.makeText(getApplicationContext(), "Visibility Change", Toast.LENGTH_SHORT).show();
                        if (isFullScreen && !goingFullScreen)
                            imageView.setSystemUiVisibility(0);

                        if (!goingFullScreen) {

                            goingFullScreen = true;

                            final Handler handler = new Handler();
                            handler.postDelayed(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            GoFullScreen();
                                        }
                                    }, 100);
                        }
                        */
                    }
                });
        addContentView(imageView,new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        getSupportActionBar().hide();
        GoFullScreen();
    }

    public void onWindowFocusChanged(boolean hasFocus)
    {
        //Toast.makeText(getApplicationContext(), "Window Focus Change", Toast.LENGTH_LONG).show();
        if (imageView == null)
            return;

        if (!hasFocus)
            imageView.setSystemUiVisibility(0);
        else
            imageView.setSystemUiVisibility(FULLSCREEN);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if (imageView == null)
            return;

        imageView.setSystemUiVisibility(0);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (imageView == null)
            return;

        imageView.setSystemUiVisibility(FULLSCREEN);
    }

    public void GoFullScreen()
    {
        imageView.setSystemUiVisibility(FULLSCREEN);
        goingFullScreen = false;
        isFullScreen = true;
    }
}
