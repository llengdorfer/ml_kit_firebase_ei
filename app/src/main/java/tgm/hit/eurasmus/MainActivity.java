package tgm.hit.eurasmus;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import java.util.List;

/**
 * Main Activity Class to take a photo and process it with googles firebase ml kit api
 * @author Luca Lengdorfer
 * @author Benny Stark
 * @version 17-05-2021
 */
public class MainActivity extends AppCompatActivity {
    private Bitmap picture;
    private ImageView imageView;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);   //Get the ImageView from the activity_main.xml
        textView = findViewById(R.id.TextView);     //Get the TextView from the activity_main.xml
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;

    /**
     * Uses the Camera to take a photo
     * @param v
     */
    public void chooseImage(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }
    @Override
    /**
     * When the picture is chosen copy it onto the ImageView and saves it into picture
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Log.d("datavalue at onActivityResult", String.valueOf(data));
            picture = (Bitmap) extras.get("data");
            imageView.setImageBitmap(picture);
        }
    }

    /**
     * Main Class to analyze the faces previously taken
     * @param v
     */
    public void analyzeFaces(View v) {
        FirebaseVisionFaceDetectorOptions highAccuracyOpts =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                        .build();
        FirebaseVisionImage image = null;
        try {
            Context context = null;
            image = FirebaseVisionImage.fromBitmap(picture);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance()
                .getVisionFaceDetector(highAccuracyOpts);
        Log.d("Image Value at analyzeFaces()", String.valueOf(image));
        Task<List<FirebaseVisionFace>> result =
                detector.detectInImage(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionFace>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionFace> faces) {
                                        textView.setText("Probability of Smiling: "+faces.get(0).getSmilingProbability()+"\n Probability of Left Eye being open: "+faces.get(0).getLeftEyeOpenProbability()+
                                                "\n Probability of Right Eye being open"+faces.get(0).getRightEyeOpenProbability());    //Output of the Values into the TextView
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        textView.setText("Error");  //Error received
                                    }
                                });
    }
}
