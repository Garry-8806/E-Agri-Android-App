package com.sourcey.tools;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    EditText Etname, Etemail, Etmno, Etcity, Etpassowrd;
    Button btnUpdate;
    JSONParser jsonParser = new JSONParser();
    ProgressDialog pDialog;
    CircleImageView profileImage;
    private static final int PICK_IMAGE = 1;
    private static final int PICK_Camera_IMAGE = 2;
    private Bitmap bitmap;
    Uri imageUri;
    public static final String PRODUCT_PHOTO = "photo";

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImage = (CircleImageView) view.findViewById(R.id.profileImage);
        Etpassowrd = (EditText) view.findViewById(R.id.etpassword);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertadd = new AlertDialog.Builder(ProfileFragment.this.getContext()).create();
                LayoutInflater factory = LayoutInflater.from(ProfileFragment.this.getContext());
                final View view = factory.inflate(R.layout.activity_selection_for_photo, null);
                alertadd.setView(view);
                ImageView imageGallery = (ImageView) view.findViewById(R.id.imageFromGallery);
                imageGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  Toast.makeText(ComplaintActivity.this, "gallery", Toast.LENGTH_SHORT).show();
                        try {
                            Intent gintent = new Intent();
                            gintent.setType("image/*");
                            gintent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(
                                    Intent.createChooser(gintent, "Select Picture"),
                                    PICK_IMAGE);
                            alertadd.dismiss();

                        } catch (Exception e) {
                            Toast.makeText(ProfileFragment.this.getContext(),
                                    e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            Log.e(e.getClass().getName(), e.getMessage(), e);
                        }
                    }
                });
                ImageView imageCamera = (ImageView) view.findViewById(R.id.imageFromCamera);
                imageCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  Toast.makeText(ComplaintActivity.this, "Camera", Toast.LENGTH_SHORT).show();
                        String fileName = "new-photo-name.jpg";
                        //create parameters for Intent with filename
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, fileName);
                        values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
                        //imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
                        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        //create new Intent
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                        startActivityForResult(intent, PICK_Camera_IMAGE);
                        alertadd.dismiss();

                    }
                });
                alertadd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertadd.show();
            }
        });

        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Etname.getText().toString().equals("") || Etemail.getText().toString().equals("") ||
                        Etcity.getText().toString().equals("") || Etmno.getText().toString().equals("") || Etpassowrd.getText().toString().equals("")) {
                    Toast.makeText(ProfileFragment.this.getContext(), "Fields should not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                new UpdateProfile().execute();
            }
        });
        Etcity = (EditText) view.findViewById(R.id.etcity);

        Etname = (EditText) view.findViewById(R.id.etname);
        Etemail = (EditText) view.findViewById(R.id.etemail);
        Etmno = (EditText) view.findViewById(R.id.etmno);
        new GetProfileInfo().execute();
        String str_bitmap = getDefaults(PRODUCT_PHOTO, ProfileFragment.this.getContext());
        if (!str_bitmap.equals("")) {
            try {

                bitmap = decodeBase64(str_bitmap);
                //----------- finally set the this image to the Imageview.
                profileImage.setImageBitmap(bitmap);
            } catch (Exception e) {
            }
        }

        return view;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImageUri = null;
        String filePath = null;
        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    selectedImageUri = data.getData();
                }
                break;
            case PICK_Camera_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    //use imageUri here to access the image
                    selectedImageUri = imageUri;
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(ProfileFragment.this.getContext(), "Picture was not taken", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileFragment.this.getContext(), "Picture was not taken", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        if (selectedImageUri != null) {
            try {
                // OI FILE Manager
                String filemanagerstring = selectedImageUri.getPath();

                // MEDIA GALLERY
                String selectedImagePath = getPath(selectedImageUri);

                if (selectedImagePath != null) {
                    filePath = selectedImagePath;
                } else if (filemanagerstring != null) {
                    filePath = filemanagerstring;
                } else {
                    Toast.makeText(ProfileFragment.this.getContext(), "Unknown path",
                            Toast.LENGTH_LONG).show();
                    Log.e("Bitmap", "Unknown path");
                }

                if (filePath != null) {
                    decodeFile(filePath);
                } else {
                    bitmap = null;
                }
            } catch (Exception e) {
                Toast.makeText(ProfileFragment.this.getContext(), "Internal error",
                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }

    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA.

            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public void decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);

        profileImage.setImageBitmap(bitmap);

        String str_bitmap = BitMapToString(bitmap);
//__________create two method setDefaults() andgetDefaults()
        setDefaults(PRODUCT_PHOTO, str_bitmap, ProfileFragment.this.getContext());

    }

    public static void setDefaults(String str_key, String value, Context context) {
        SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = shre.edit();
        edit.putString(str_key, value);
        edit.apply();
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, "");

    }

    public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] arr = baos.toByteArray();
        return Base64.encodeToString(arr, Base64.DEFAULT);
    }

    public class GetProfileInfo extends AsyncTask<String, String, String> {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        boolean isAvailable = false;
        String url = ServerUtility.url_get_user_profile();
        JSONObject jsonObject;
        String name, city, mobile, password;

        protected void onPreExecute() {
            params.add(new BasicNameValuePair("email", ServerUtility.txtEmail));
        }

        @Override
        protected String doInBackground(String... args) {
            jsonObject = jsonParser.makeHttpRequest(url, "GET", params);

            try {


                name = jsonObject.getString("username");
                mobile = jsonObject.getString("mobile");
                city = jsonObject.getString("address");
                password = jsonObject.getString("password");

            } catch (Exception e) {
                e.printStackTrace();
            }
            isAvailable = jsonObject.has(ServerUtility.TAG_SUCCESS);
            return null;
        }

        protected void onPostExecute(String srr) {
            if (isAvailable) {
                Etname.setText(name);
                Etemail.setText(ServerUtility.txtEmail);
                Etmno.setText(mobile);
                Etcity.setText(city);
                Etpassowrd.setText(password);
            }
        }
    }

    String ImageTag = "image_tag";
    String ImageName = "image_data";

    class UpdateProfile extends AsyncTask<String, String, String> {
        Boolean isregistered = false;
        String val = "";
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProfileFragment.this.getContext());
            pDialog.setMessage("Registering user details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            Bitmap bitmap = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
            String strBytes = BitMapToString(bitmap);

            params.add(new BasicNameValuePair("email", Etemail.getText().toString()));
            params.add(new BasicNameValuePair("uname", Etname.getText().toString()));
            params.add(new BasicNameValuePair("mno", Etmno.getText().toString()));
            params.add(new BasicNameValuePair("city", Etcity.getText().toString()));
            params.add(new BasicNameValuePair("password", Etpassowrd.getText().toString()));
          /*  params.add(new BasicNameValuePair(ImageTag, Etmno.getText().toString()));
            params.add(new BasicNameValuePair(ImageName, strBytes));*/
        }

        protected String doInBackground(String... args) {
            JSONObject json = jsonParser.makeHttpRequest(ServerUtility.url_update_user_profile(), "POST", params);
            Log.d("All Products: ", json.toString());
            isregistered = json.has("success");
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if (isregistered) {
                Toast.makeText(ProfileFragment.this.getContext(), "Updated successfully", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(ProfileFragment.this.getContext(), "Updation failed...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

