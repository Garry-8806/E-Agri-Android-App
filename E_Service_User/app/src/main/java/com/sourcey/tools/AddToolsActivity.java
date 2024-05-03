package com.sourcey.tools;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddToolsActivity extends AppCompatActivity {
    private ImageView imageView;
    private Button btnCamera, btnGallery, btnAdd;
    public static final int REQUEST_IMAGE = 100;
    public static final int REQUEST_PERMISSION = 200;
    private String imageFilePath = "";
    private static final int PHOTO_REQUEST_GALLERY = 2;
    JSONParser jsonParser = new JSONParser();
    private static final int INPUT_SIZE = 224;
    private EditText etTitle, etPrice, etDescription;
    private Bitmap mDiagnosisBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tools);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etPrice = (EditText) findViewById(R.id.etCost);
        etDescription = (EditText) findViewById(R.id.etDescription);
        btnCamera = (Button) findViewById(R.id.btnCapture);
        btnGallery = (Button) findViewById(R.id.btnSelect);
        btnAdd = (Button) findViewById(R.id.btnAddTool);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etTitle.getText().toString().equals("") || etPrice.getText().toString().equals("") || etDescription.getText().toString().equals("")) {
                    Toast.makeText(AddToolsActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                } else {
                    new UploadToolDetails().execute();
                }
            }
        });
        imageView = (ImageView) findViewById(R.id.image);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraIntent();
            }
        });
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gallery(view);

            }
        });
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollview);
        scrollView.smoothScrollTo(0, 0);
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        btnCamera.requestFocus();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Uri photoUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(pictureIntent, REQUEST_IMAGE);
        }
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();

        return image;
    }

    //call gallery for image selection
    private void gallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thanks for granting Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                imageView.setImageURI(Uri.parse(imageFilePath));
                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                mDiagnosisBitmap = scaleImage(bitmap);
                imageView.setImageBitmap(mDiagnosisBitmap);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You cancelled the operation", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                Uri uri = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    mDiagnosisBitmap = scaleImage(bitmap);
                    imageView.setImageBitmap(mDiagnosisBitmap);
//                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }

    }

    public class UploadToolDetails extends AsyncTask<String, String, String> {
        List<NameValuePair> params = new ArrayList<>();
        boolean flag = false;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AddToolsActivity.this);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.setIndeterminate(false);
            dialog.show();
            params.add(new BasicNameValuePair("id", ServerUtility.txtEmail));
            params.add(new BasicNameValuePair("title", etTitle.getText().toString()));
            params.add(new BasicNameValuePair("cost", etPrice.getText().toString()));
            params.add(new BasicNameValuePair("description", etDescription.getText().toString()));
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            String str_bitmap = BitMapToString(bitmap);
            params.add(new BasicNameValuePair("imageData", str_bitmap));
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject object = jsonParser.makeHttpRequest(ServerUtility.url_add_tool_details(), "POST", params);
                flag = object.has(ServerUtility.TAG_SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if (flag) {
                Toast.makeText(AddToolsActivity.this, "Tool Details Added Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AddToolsActivity.this, "Details not added ", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }


    public Bitmap scaleImage(Bitmap bitmap) {
        int orignalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        float scaleWidth = ((float) INPUT_SIZE) / orignalWidth;
        float scaleHeight = ((float) INPUT_SIZE) / originalHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, orignalWidth, originalHeight, matrix, true);
        return scaledBitmap;
    }
    public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] arr = baos.toByteArray();
        return Base64.encodeToString(arr, Base64.DEFAULT);
    }

}
