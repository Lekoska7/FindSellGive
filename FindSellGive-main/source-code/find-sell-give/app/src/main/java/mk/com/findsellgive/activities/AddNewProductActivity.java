package mk.com.findsellgive.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
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
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import mk.com.findsellgive.R;
import mk.com.findsellgive.models.Product;
import mk.com.findsellgive.utills.SharedPreferencesManager;

public class AddNewProductActivity extends AppCompatActivity {
    private CircleImageView productAvatar;
    private AppCompatButton btAddNewAvatar;
    private AppCompatSpinner spProductCategory;
    private AppCompatEditText etLocation;
    private RadioGroup rbProductCondition;
    private AppCompatEditText etProductDescription;
    private AppCompatEditText etProductName;
    private AppCompatEditText etProductPrice;
    private AppCompatSpinner spPurpose;
    private FirebaseFirestore firebaseFirestoreDatabase;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private int category = 0;
    private int purpose = 0;
    private boolean condition;
    private Uri avatarUri = null;
    private String downloadUrl = null;
    private double price = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestoreDatabase = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();//nasiot korisnik
        storageReference = firebaseStorage.getReference();
        initViews();
        initListeners();
    }

    private void initListeners() {
        btAddNewAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //otvori dijalog za menuvanje na slika
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(AddNewProductActivity.this);
            }
        });
        spProductCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spPurpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                purpose = position;
                etProductPrice.setEnabled(position != 1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        rbProductCondition.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_new:
                        condition = true;
                        break;
                    case R.id.rb_used:
                        condition = false;
                        break;
                }
            }
        });
    }

    private void initViews() {
        productAvatar = findViewById(R.id.iv_product_image);
        btAddNewAvatar = findViewById(R.id.bt_add_avatar);
        spProductCategory = findViewById(R.id.sp_product_category);
        etLocation = findViewById(R.id.et_product_location);
        rbProductCondition = findViewById(R.id.rg_condition);
        etProductDescription = findViewById(R.id.et_product_desc);
        etProductName = findViewById(R.id.et_product_name);
        etProductPrice = findViewById(R.id.et_product_price);
        spPurpose = findViewById(R.id.sp_product_purpose);
        initSpinners(spProductCategory, spPurpose);
    }

    private void initSpinners(AppCompatSpinner spProductCategory, AppCompatSpinner spPurpose) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.product_categories));
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.product_purpose));
        spProductCategory.setAdapter(adapter);
        spPurpose.setAdapter(adapter1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_product, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            try {
                saveAvatarToStorage(avatarUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void saveProductToDb(String url) {
        String id = firebaseFirestoreDatabase.collection("products").document().getId();
        String name = etProductName.getText().toString();
        String description = etProductDescription.getText().toString();
        String city = etLocation.getText().toString();
        if (!TextUtils.isEmpty(etProductPrice.getText().toString())) {
            price = Double.parseDouble(etProductPrice.getText().toString());
        }
        double lat = SharedPreferencesManager.getInstance(this).getLatitude();
        double lon = SharedPreferencesManager.getInstance(this).getLongitude();
        GeoPoint location = new GeoPoint(lat,lon);
        Product product = new Product(id, url,
                name, description, condition, category, purpose,
                city, price, firebaseUser.getUid(),location);
        firebaseFirestoreDatabase.collection("products").document(id)
                .set(product.getProductMap(product)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        });
    }

    private void saveAvatarToStorage(Uri uri) throws FileNotFoundException {
        String imageName = UUID.randomUUID().toString();
        storageReference = storageReference.child("product_images/" + imageName);
        productAvatar.setDrawingCacheEnabled(true);
        productAvatar.buildDrawingCache();
        InputStream imageStream = getContentResolver().openInputStream(uri);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(imageStream);
        Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageReference.putBytes(data);
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
                    if (downloadUri != null) {
                        saveProductToDb(downloadUri.toString());
                    }
                } else {
                    Toast.makeText(AddNewProductActivity.this, "Upload failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                if (resultUri != null) {
                    Picasso.get().load(resultUri)
                            .error(R.drawable.ic_cart)
                            .placeholder(R.drawable.ic_loader)
                            .into(productAvatar);
                    avatarUri = resultUri;
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

