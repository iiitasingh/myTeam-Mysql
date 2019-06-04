package com.ashish.myteam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import de.hdodenhof.circleimageview.CircleImageView;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.acl.Owner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class resume_Page extends AppCompatActivity {

    User resumeMail;
    ImageView resumeWallpaper, resumeImgEdit, editAboutImg, resumeEdit;
    TextView resumeName, resumeDesignation, textVwabout, resumeHobbies, resumeAbout, resumeEmail;
    CircleImageView circleResumeImageView;
    final int REQUEST_CODE_GALLERY = 888;
    Dialog dialog, updateDialog, aboutPopup, hobbiesPopup;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    ArrayList<User> owner;
    String newImg;
    int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume__page);
        resumeMail = list_test.profileUser.get(0);
        resumeWallpaper = (ImageView) findViewById(R.id.resumeWallpaper);
        resumeImgEdit = (ImageView) findViewById(R.id.resumeImgEdit);
        editAboutImg = (ImageView) findViewById(R.id.editAboutImg);
        resumeEdit = (ImageView) findViewById(R.id.resumeEdit);
        resumeName = (TextView) findViewById(R.id.resumeName);
        resumeDesignation = (TextView) findViewById(R.id.resumeDesignation);
        textVwabout = (TextView) findViewById(R.id.textVwabout);
        resumeHobbies = (TextView) findViewById(R.id.resumeHobbies);
        resumeAbout = (TextView) findViewById(R.id.resumeAbout);
        resumeEmail = (TextView) findViewById(R.id.resumeEmail);
        circleResumeImageView = (CircleImageView) findViewById(R.id.circleResumeImageView);
        dialog = new Dialog(this);
        updateDialog = new Dialog(this);
        aboutPopup = new Dialog(this);
        hobbiesPopup = new Dialog(this);
        owner = new ArrayList<>();

        setResumeData(resumeMail);
        resumeImgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogUpdateImg(resume_Page.this, resumeMail.email);
            }
        });
        resumeAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutPopup(resume_Page.this, resumeMail);
            }
        });
        editAboutImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutEditPopup(resume_Page.this, resumeMail);
            }
        });
        resumeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutYourselfPopup(resume_Page.this, resumeMail);
            }
        });

    }

    public void updateUserData(String email) {
        HashMap<String, String> getUser = new HashMap<>();
        getUser.put("email", email);
        owner.clear();
        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(this, getUser, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                owner = new JsonConverter<User>().toArrayList(s, User.class);

                if (owner.size() > 0) {
                    setResumeData(owner.get(0));
                    resumeMail = owner.get(0);
                }
            }
        });
        taskRead.setExceptionHandler(new ExceptionHandler() {
            @Override
            public void handleException(Exception e) {
                if (e != null && e.getMessage() != null) {
                    Toast.makeText(getApplicationContext(),
                            "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        taskRead.execute("https://voteforashish.000webhostapp.com/myTeam/getUserData.php");

    }

    public void setResumeData(User ower) {

        byte[] usrerImg = org.apache.commons.codec.binary.Base64.decodeBase64(ower.image);
        Bitmap bitmap = BitmapFactory.decodeByteArray(usrerImg, 0, usrerImg.length);
        circleResumeImageView.setImageBitmap(bitmap);
        resumeName.setText(ower.name);
        resumeDesignation.setText(ower.designation);
        resumeHobbies.setText(ower.hobbies);
        textVwabout.setText(ower.about);
        resumeEmail.setText(ower.email);
    }

    ImageView uploadImg;
    Button uploadBtn;

    private void showDialogUpdateImg(Activity activity, final String mail) {

        dialog.setContentView(R.layout.upload_image);
        uploadImg = (ImageView) dialog.findViewById(R.id.UpimageView);
        uploadBtn = (Button) dialog.findViewById(R.id.uploadbutton);

        int width1 = (int) (activity.getResources().getDisplayMetrics().widthPixels * .67);
        int height1 = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.57);
        dialog.getWindow().setLayout(width1, height1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        resume_Page.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    newImg = imageToString(uploadImg);
                    HashMap<String, String> updateImg = new HashMap<>();
                    updateImg.put("email", mail);
                    updateImg.put("img", newImg);
                    PostResponseAsyncTask taskRead = new PostResponseAsyncTask(activity, updateImg, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            if (s.contains("Success")) {
                                dialog.dismiss();
                                updateUserData(mail);
                                Toast.makeText(getApplicationContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    taskRead.execute("https://voteforashish.000webhostapp.com/myTeam/updateImg.php");
                } catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            CropImage.activity(uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .setRequestedSize(512, 512, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(resultUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    uploadImg.setImageBitmap(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    ImageView Usimgpop2;
    TextView MemNamepop2, MemEmailpop2, MemTeampop2, MemDobpop2, MemFoodpop2, MemMobpop2, MemBgppop2, MemNknampop2, MemDesig;
    byte[] userimgpop2;

    private void aboutPopup(Activity popActivity, final User userAbout) {

        aboutPopup.setContentView(R.layout.home_header);

        int width1 = (int) (popActivity.getResources().getDisplayMetrics().widthPixels * .82);
        int height1 = (int) (popActivity.getResources().getDisplayMetrics().heightPixels * 0.75);
        aboutPopup.getWindow().setLayout(width1, height1);
        aboutPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        aboutPopup.show();

        Usimgpop2 = (ImageView) aboutPopup.findViewById(R.id.Uimg);
        MemNknampop2 = (TextView) aboutPopup.findViewById(R.id.MemNknam);
        MemNamepop2 = (TextView) aboutPopup.findViewById(R.id.MemName);
        MemEmailpop2 = (TextView) aboutPopup.findViewById(R.id.MemEmail);
        MemTeampop2 = (TextView) aboutPopup.findViewById(R.id.MemTeam);
        MemDobpop2 = (TextView) aboutPopup.findViewById(R.id.MemDob);
        MemFoodpop2 = (TextView) aboutPopup.findViewById(R.id.MemFood);
        MemMobpop2 = (TextView) aboutPopup.findViewById(R.id.MemMob);
        MemBgppop2 = (TextView) aboutPopup.findViewById(R.id.MemBgp);
        MemDesig = (TextView) aboutPopup.findViewById(R.id.MemDesig);

        MemEmailpop2.setText("Email: " + userAbout.email);
        MemNamepop2.setText(userAbout.name);
        MemNknampop2.setText(userAbout.nickname);
        MemDesig.setText(userAbout.designation);
        MemTeampop2.setText("Team: " + userAbout.team);
        MemFoodpop2.setText("Food: " + userAbout.food);
        MemBgppop2.setText("BGrp: " + userAbout.bl_grp);
        MemMobpop2.setText("Mob: " + userAbout.mobile);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date data = null;
        try {
            data = sdf.parse(userAbout.dob);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        MemDobpop2.setText("DOB: " + DateFormat.format("dd MMMM", data));
        userimgpop2 = org.apache.commons.codec.binary.Base64.decodeBase64(userAbout.image);
        Bitmap bitmap = BitmapFactory.decodeByteArray(userimgpop2, 0, userimgpop2.length);
        Usimgpop2.setImageBitmap(bitmap);
    }

    TextInputEditText editNknm, editDob, editMob, editBgrp, editFood, editPan, editAadhar, editDesig;
    Button updatebtn;

    private void aboutEditPopup(Activity activity, User resumeUsr) {

        updateDialog.setContentView(R.layout.label_text_view);
        editNknm = (TextInputEditText) updateDialog.findViewById(R.id.name_edit_text1);
        editDob = (TextInputEditText) updateDialog.findViewById(R.id.name_edit_text2);
        editMob = (TextInputEditText) updateDialog.findViewById(R.id.name_edit_text3);
        editBgrp = (TextInputEditText) updateDialog.findViewById(R.id.name_edit_text4);
        editFood = (TextInputEditText) updateDialog.findViewById(R.id.name_edit_text5);
        editPan = (TextInputEditText) updateDialog.findViewById(R.id.name_edit_text6);
        editAadhar = (TextInputEditText) updateDialog.findViewById(R.id.name_edit_text7);
        editDesig = (TextInputEditText) updateDialog.findViewById(R.id.name_edit_text8);
        updatebtn = (Button) updateDialog.findViewById(R.id.ProfileUpdatebtn);

        int width1 = (int) (activity.getResources().getDisplayMetrics().widthPixels * .83);
        int height1 = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.85);
        updateDialog.getWindow().setLayout(width1, height1);
        updateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        updateDialog.show();

        editNknm.setText(resumeUsr.nickname);
        editDob.setText(resumeUsr.dob);
        editFood.setText(resumeUsr.food);
        editMob.setText(resumeUsr.mobile);
        editBgrp.setText(resumeUsr.bl_grp);
        editPan.setText(resumeUsr.pan_no);
        editAadhar.setText(resumeUsr.aadhaar);
        editDesig.setText(resumeUsr.designation);

        editDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dobPicker = new DatePickerDialog(
                        resume_Page.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dobPicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dobPicker.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date;
                String dayS = String.valueOf(day);
                String monthS = String.valueOf(month);
                if (month < 10) {
                    monthS = "0" + monthS;
                }
                if (day < 10) {
                    dayS = "0" + dayS;
                }

                date = year + "-" + monthS + "-" + dayS;
                editDob.setText(date);
            }
        };

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserDetails(resumeUsr.email);
            }
        });


    }

    public void updateUserDetails(String mailid) {
        String nkname = editNknm.getText().toString().trim();
        String dob = editDob.getText().toString().trim();
        String mob = editMob.getText().toString().trim();
        String blgp = editBgrp.getText().toString().trim();
        String food = editFood.getText().toString().trim();
        String pan = editPan.getText().toString().trim();
        String aadhar = editAadhar.getText().toString().trim();
        String designation = editDesig.getText().toString().trim();

        if (nkname.length() >= 3 && dob.length() != 0 && mob.length() == 10 && blgp.length() >= 2 && food.length() >= 3 && pan.length() == 10 && aadhar.length() == 12 && designation.length() > 5) {
            HashMap<String, String> updateUser = new HashMap<>();
            updateUser.put("email", mailid);
            updateUser.put("nkname", nkname);
            updateUser.put("dob", dob);
            updateUser.put("mob", mob);
            updateUser.put("blgrp", blgp);
            updateUser.put("food", food);
            updateUser.put("pan", pan);
            updateUser.put("aadhaar", aadhar);
            updateUser.put("desig", designation);

            PostResponseAsyncTask taskupdate = new PostResponseAsyncTask(this, updateUser, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    if (s.contains("Success")) {
                        updateDialog.dismiss();
                        updateUserData(mailid);
                        Toast.makeText(getApplicationContext(), "Updated successfully!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            taskupdate.setExceptionHandler(new ExceptionHandler() {
                @Override
                public void handleException(Exception e) {
                    if (e != null && e.getMessage() != null) {
                        Toast.makeText(getApplicationContext(),
                                "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            taskupdate.execute("https://voteforashish.000webhostapp.com/myTeam/updateUser.php");
        } else if (nkname.length() < 3) {
            Toast.makeText(resume_Page.this, "Nick name should have atleast 3 characters", Toast.LENGTH_SHORT).show();
        } else if (dob.length() == 0) {
            Toast.makeText(resume_Page.this, "DOB should not be null", Toast.LENGTH_SHORT).show();
        } else if (mob.length() < 10) {
            Toast.makeText(resume_Page.this, "Mobile no should contain 10 digits", Toast.LENGTH_SHORT).show();
        } else if (blgp.length() < 2) {
            Toast.makeText(resume_Page.this, "Blood Grp needs atleast 2 characters", Toast.LENGTH_SHORT).show();
        } else if (food.length() < 3) {
            Toast.makeText(resume_Page.this, "Min 3 characters (Veg/Nonveg)", Toast.LENGTH_SHORT).show();
        } else if (pan.length() < 10) {
            Toast.makeText(resume_Page.this, "Pan contains 10 characters", Toast.LENGTH_SHORT).show();
        } else if (designation.length() < 5) {
            Toast.makeText(resume_Page.this, "fill your designation", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(resume_Page.this, "Aadhaar contains 12 digits", Toast.LENGTH_SHORT).show();
        }
    }

    TextInputEditText yourself, hobby;
    Button updateAbt;

    private void aboutYourselfPopup(Activity activity, final User resumeUsr) {

        hobbiesPopup.setContentView(R.layout.edit_about);
        yourself = (TextInputEditText) hobbiesPopup.findViewById(R.id.yourself);
        hobby = (TextInputEditText) hobbiesPopup.findViewById(R.id.hobby);
        updateAbt = (Button) hobbiesPopup.findViewById(R.id.AbtUpdatebtn);

        int width1 = (int) (activity.getResources().getDisplayMetrics().widthPixels * .8);
        int height1 = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.6);
        hobbiesPopup.getWindow().setLayout(width1, height1);
        hobbiesPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        hobbiesPopup.show();

        yourself.setText(resumeUsr.about);
        hobby.setText(resumeUsr.hobbies);

        updateAbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserAbout(resumeUsr.email);
            }
        });
    }

    public void updateUserAbout(String mailid) {

        String abt = yourself.getText().toString().trim();
        String hob = hobby.getText().toString().trim();

        if (abt.length() >= 10 && hob.length() >= 10) {

            HashMap<String, String> updateabout = new HashMap<>();
            updateabout.put("email", mailid);
            updateabout.put("about", abt);
            updateabout.put("hobby", Signup.capitailizeWord(hob));

            PostResponseAsyncTask taskRead = new PostResponseAsyncTask(this, updateabout, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    if (s.contains("Success")) {
                        hobbiesPopup.dismiss();
                        updateUserData(mailid);
                        Toast.makeText(getApplicationContext(), "Updated successfully!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            taskRead.execute("https://voteforashish.000webhostapp.com/myTeam/updateUserAbout.php");
        } else if (abt.length() < 10) {
            Toast.makeText(resume_Page.this, "minimum 10 characters in about", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(resume_Page.this, "minimum 10 characters in hoobies", Toast.LENGTH_SHORT).show();
        }
    }

    public String imageToString(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String encodedImage = android.util.Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encodedImage;
    }
}
