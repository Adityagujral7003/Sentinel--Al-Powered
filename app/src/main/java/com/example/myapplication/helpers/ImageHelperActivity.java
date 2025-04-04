package com.example.myapplication.helpers;

import static com.example.myapplication.R.*;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ImageHelperActivity extends AppCompatActivity {

    private int REQUEST_PICK_IMAGE =1001;
    private int REQUEST_CATURE_IMAGE =1002;
    private ImageView inputImageView;
    private TextView outputTextView;

    private File photoFile;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_image_helper);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        inputImageView=findViewById(R.id.imageViewInput);
        outputTextView=findViewById(id.textViewOutput);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        }



    }

    public void onStartCamera(View view) {

        photoFile =createPhotofile();
        Uri fileUri = FileProvider.getUriForFile(this,"com.iago.fileprovider", photoFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);

        startActivityForResult(intent,REQUEST_CATURE_IMAGE);


    }

    private File createPhotofile(){
        File photofileDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"IMAGE_HELPER");

        if(!photofileDir.exists()){
            photofileDir.mkdir();
        }

        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File file = new File(photofileDir.getPath()+File.separator +name);
        return file;

    }
    public void onPickImage(View view){
        Intent intent =new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if (requestCode ==REQUEST_PICK_IMAGE){
                assert data != null;
                Uri uri= data.getData();
                try {
                    Bitmap bitmap =loadFromUri(uri);
                    inputImageView.setImageBitmap(bitmap);
                    runClassification((bitmap));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (requestCode == REQUEST_CATURE_IMAGE) {
                Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                inputImageView.setImageBitmap(bitmap);
                runClassification(bitmap);

            }
        }
    }

    private  Bitmap loadFromUri(Uri uri) throws IOException {
        Bitmap bitmap =null;

        ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), uri);

        bitmap = ImageDecoder.decodeBitmap(source);

        return bitmap;
    }

    protected void runClassification(Bitmap bitmap){

    }

    protected TextView getOutputTextView(){
        return outputTextView;
    }

    protected ImageView getInputImageView(){
        return inputImageView;
    }
}