package edu.southhills.storyland;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShareActivity extends AppCompatActivity {

    private static final int RESULT_PICK_CONTACT = 1;
    private static final int REQUEST_TAKE_PHOTO = 2;
    private static final int REQUEST_SEND_EMAIL = 3;
    private String mCurrentPhotoPath;
    private Uri photoAttachment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_share);

        Button btnTo = findViewById(R.id.btnTo);
        EditText etMessage = findViewById(R.id.etMessage);
        etMessage.setText("Having a blast at StoryLand,\nwish you were here!\n" +
                "Check it out on the web at https://www.storylandnh.com/");

        // logo and home button fragments
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        LogoFragment lf = new LogoFragment();
        final ButtonFragment bf = new ButtonFragment();
        ft.add(R.id.shareContainer, lf);
        ft.add(R.id.shareContainer, bf);
        ft.commit();

        btnTo.setFocusableInTouchMode(true);
        btnTo.requestFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK || requestCode == REQUEST_SEND_EMAIL) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    Cursor cursor = null;
                    try {
                        String email = null;
                        String name = null;

                        Uri uri = data.getData();
                        cursor = getContentResolver().query(uri, null, null, null, null);
                        cursor.moveToFirst();
                        int  emailIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
                        int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        email = cursor.getString(emailIndex);
                        name = cursor.getString(nameIndex);

                        EditText editText = findViewById(R.id.etEmail);
                        editText.setText(email);
                        Log.e("Name and Contact number is",name+"," + email);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case REQUEST_TAKE_PHOTO:
                    System.out.println("Completed and Saved");
                    galleryAddPic();
                    break;
                case REQUEST_SEND_EMAIL:
                    // return to the main activity when the app restarts
                    Intent homeIntent = new Intent(this, MainActivity.class);
                    homeIntent.putExtra("activityReturn", "");
                    startActivity(homeIntent);
                    break;
            }
        } else {
            Log.e("Failed", "Request Failed");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if(requestCode == REQUEST_TAKE_PHOTO){
            // received permission result for writing to storage

            // check if the only required permission has been granted
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // write to storage permission has been granted, we can save the image
                takePhoto();
            } else {
                // write to storage permission was denied, can't do it
                Toast.makeText(this,"Permission was denied",Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void backButton(View v){
        Intent back = new Intent(this, AboutActivity.class);
        startActivity(back);
    }

    public void toButton(View v){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Email.CONTENT_TYPE);
        startActivityForResult(intent, RESULT_PICK_CONTACT);

    }

    public void sendButton(View v){
        String subject = "A Message from StoryLand";
        EditText etTo = findViewById(R.id.etEmail);
        EditText etMessage = findViewById(R.id.etMessage);

        String[] recipients = etTo.getText().toString().split(",");
        String message = etMessage.getText().toString();

        sendEmail(recipients, subject, message, photoAttachment);
    }

    public void addPhoto(View v){
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Gallery Permission Already Granted",
                    Toast.LENGTH_LONG).show();

            takePhoto();

        } else {
            if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(this,
                        "Permission Required to Save Image to Gallery",
                        Toast.LENGTH_LONG).show();
            }

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_TAKE_PHOTO);
        }
    }

    // send email using implicit intent
    private void sendEmail(String[] toField, String subject, String message, Uri fileAttachment){
        Intent mailIntent = new Intent(Intent.ACTION_SEND);

        mailIntent.setType("plain/text");
        mailIntent.putExtra(Intent.EXTRA_EMAIL, toField);
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        mailIntent.putExtra(Intent.EXTRA_TEXT, message);
        if(fileAttachment != null) {
            mailIntent.putExtra(Intent.EXTRA_STREAM, fileAttachment);
        }

        Intent chooser = Intent.createChooser(mailIntent, "Send Email");

        try{
            setResult(RESULT_OK, chooser);
            startActivityForResult(chooser, REQUEST_SEND_EMAIL);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "eMail Client not Found", Toast.LENGTH_SHORT).show();
        }

    }

    private void takePhoto(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            // create the file where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            }catch (IOException ex){
                ex.printStackTrace();
            }
            // continue only if the file was successfully created
            if(photoFile != null){
                Uri photoURI = FileProvider.getUriForFile(this,
                        "edu.southhills.storyland",
                        photoFile);

                photoAttachment = photoURI;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException{
        // create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "StoryLand_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName, // name
                ".jpg", // extension
                storageDir   // directory
        );

        // save a file path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic(){
        try {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);

            ImageView mImage = findViewById(R.id.ivStoryLandPic);
            Bitmap bitmap = null;

            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentUri);
            mImage.setImageBitmap(bitmap);
            mImage.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
