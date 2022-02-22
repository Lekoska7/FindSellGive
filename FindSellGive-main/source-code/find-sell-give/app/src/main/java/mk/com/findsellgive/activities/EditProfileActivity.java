package mk.com.findsellgive.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import mk.com.findsellgive.R;
import mk.com.findsellgive.models.User;
import mk.com.findsellgive.utills.UtilitiesHelper;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private CircleImageView ivAvatar;
    private ProgressBar progressBar;
    private AppCompatEditText etFullName;
    private AppCompatEditText etEmail;
    private TextView tvChangePassword;
    private MaterialRatingBar rating;
    private AppCompatButton btSave;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestoreDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private User myUser;
    private boolean saveChanges = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestoreDatabase = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();//nasiot korisnik
        storageReference = firebaseStorage.getReference();
        initViews();
        initListeners();
        initUserInfo();
    }

    private void initUserInfo() {
        firebaseFirestoreDatabase.collection("users")
                .document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isComplete()) {
                    myUser = task.getResult().toObject(User.class);
                    if (myUser != null) {
                        displayUserInfo(myUser);
                    }
                }
            }
        });
    }

    private void displayUserInfo(User user) {
        if (user != null) {
            if(!user.getProfileImage().equals("")){
                Picasso.get().load(user.getProfileImage())
                        .error(R.drawable.ic_home)
                        .placeholder(R.drawable.ic_loader)
                        .into(ivAvatar);
            }else{
                Picasso.get().load(R.drawable.ic_profile)
                        .error(R.drawable.ic_home)
                        .placeholder(R.drawable.ic_loader)
                        .into(ivAvatar);
            }
            etFullName.setText(user.getFullName());
            etEmail.setText(user.getEmail());
            UtilitiesHelper.setRating(user, rating);
        }
    }

    private void initListeners() {
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //otvori dijalog za menuvanje na slika
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(EditProfileActivity.this);
            }
        });
        etFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveChanges = true;
                btSave.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveChanges = true;
                btSave.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btSave.setOnClickListener(this);
    }

    private void initViews() {
        ivAvatar = findViewById(R.id.iv_avatar);
        progressBar = findViewById(R.id.progress_bar);
        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        tvChangePassword = findViewById(R.id.tv_password);
        rating = findViewById(R.id.rating);
        btSave = findViewById(R.id.bt_save);
        btSave.setVisibility(View.GONE);
    }

    private void showLoader(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        ivAvatar.setVisibility(show ? View.GONE : View.VISIBLE);
        etEmail.setVisibility(show ? View.GONE : View.VISIBLE);
        etFullName.setVisibility(show ? View.GONE : View.VISIBLE);
        rating.setVisibility(show ? View.GONE : View.VISIBLE);
        tvChangePassword.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                if (resultUri != null) {
                    Picasso.get().load(resultUri)
                            .error(R.drawable.ic_cart)
                            .placeholder(R.drawable.ic_loader)
                            .into(ivAvatar);
                    try {
                        saveAvatarToStorage(resultUri);
                    } catch (FileNotFoundException e) {
                        Toast.makeText(this, "Fajlot ne e najden", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveAvatarToStorage(Uri uri) throws FileNotFoundException {
        String imageName = UUID.randomUUID().toString();
        storageReference = storageReference.child("images/" + imageName);
        ivAvatar.setDrawingCacheEnabled(true);
        ivAvatar.buildDrawingCache();
        InputStream imageStream = getContentResolver().openInputStream(uri);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(imageStream);
        Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageReference.putBytes(data);
        showLoader(true);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.e("ImageUrl", downloadUri.toString());//log za testiranje
                    Map<String, Object> userUpdates = new HashMap<>();
                    userUpdates.put("profileImage", downloadUri.toString());
                    firebaseFirestoreDatabase.collection("users")
                            .document(firebaseUser.getUid())
                            .update(userUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            showLoader(false);
                        }
                    });
                } else {
                    Toast.makeText(EditProfileActivity.this, "Upload failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                String newName = etFullName.getText().toString();
                String newEmail = etEmail.getText().toString();
                saveChangesToDatabase(newName, newEmail);
                break;
        }
    }

    private void saveChangesToDatabase(String name, String email) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("fullName", name);
        userMap.put("email", email);
        userMap.put("uid", firebaseUser.getUid());
        userMap.put("profileImage", myUser.getProfileImage());
        userMap.put("rating", myUser.getRating());
        showLoader(true);
        btSave.setVisibility(View.GONE);
        firebaseFirestoreDatabase.collection("users")
                .document(firebaseUser.getUid()).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                saveChanges = false;
                btSave.setVisibility(View.GONE);
                showLoader(false);
            }
        });
    }
}
