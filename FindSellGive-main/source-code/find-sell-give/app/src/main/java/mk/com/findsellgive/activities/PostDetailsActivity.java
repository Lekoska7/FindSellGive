package mk.com.findsellgive.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import mk.com.findsellgive.R;
import mk.com.findsellgive.adapters.CommentsRecyclerViewAdapter;
import mk.com.findsellgive.models.Comment;
import mk.com.findsellgive.models.User;
import mk.com.findsellgive.models.WishListPost;

public class PostDetailsActivity extends AppCompatActivity {
    private CircleImageView ivAvatar;
    private TextView tvUserName;
    private TextView tvPostQuestion;
    private RecyclerView rvComments;
    private EditText etComment;
    private ImageView ivSend;
    private ImageView ivAttach;
    private FirebaseFirestore database;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private List<Comment> comments = new ArrayList<>();
    private CommentsRecyclerViewAdapter adapter;
    private String postId;
    private String uid;
    private String imageUrl = "";
    private WishListPost post;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        initViews();
        database = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        LinearLayoutManager llm = new LinearLayoutManager(this);
        adapter = new CommentsRecyclerViewAdapter(this, comments);
        rvComments.setLayoutManager(llm);
        rvComments.setAdapter(adapter);
        loadData();
        initListeners();
    }

    private void initListeners() {
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postId != null) {
                    String text = etComment.getText().toString();
                    Date datePosted = new Date();
                    Comment comment = new Comment(uid, text, imageUrl, datePosted);
                    if (!imageUrl.equals("")) {
                        //snimi ja slikata pa posle komentarot
                        try {
                            saveComment(Uri.parse(imageUrl), comment);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //snimi komentar bez slika
                        database.collection("wishlist").document(postId)
                                .collection("comments").document().set(Comment.getCommentMap(comment))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(PostDetailsActivity.this, "Comment added successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(PostDetailsActivity.this, "Something went wrong. Please try again later", Toast.LENGTH_SHORT).show();
                                        Log.e("PostDetails comment", e.getMessage());
                                    }
                                });
                        database.collection("wishlist").document(postId).update("commentsCount",post.getCommentsCount()+1);
                    }
                }
                etComment.setText("");
                imageUrl = "";
            }
        });
        ivAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(PostDetailsActivity.this);
            }
        });
    }

    private void saveComment(Uri uri, final Comment comment) throws FileNotFoundException {
        String imageName = UUID.randomUUID().toString();
        storageReference = storageReference.child("attachments/" + imageName);
        ivAvatar.setDrawingCacheEnabled(true);
        ivAvatar.buildDrawingCache();
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
                    Log.e("ImageUrl", downloadUri.toString());//log za testiranje
                    comment.setImageUrl(downloadUri.toString());//linkot do slikata
                    database.collection("wishlist").document(postId)
                            .collection("comments").document().set(Comment.getCommentMap(comment))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(PostDetailsActivity.this, "Comment added successfully!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(PostDetailsActivity.this, "Something went wrong. Please try again later", Toast.LENGTH_SHORT).show();
                                    Log.e("PostDetails comment", e.getMessage());
                                }
                            });
                    database.collection("wishlist").document(postId).update("commentsCount",post.getCommentsCount()+1);
                } else {
                    Toast.makeText(PostDetailsActivity.this, "Upload failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadData() {
        if (getIntent() != null) {
            postId = getIntent().getStringExtra("post_id");
            if (postId != null) {
                database.collection("wishlist")
                        .document(postId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot != null) {
                            post = documentSnapshot.toObject(WishListPost.class);
                            if (post != null) {
                                uid = post.getUid();
                                FirebaseFirestore.getInstance().collection("users")
                                        .document(post.getUid()).get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (documentSnapshot != null) {
                                                    User user = documentSnapshot.toObject(User.class);
                                                    //kastiranje na korisnik
                                                    if (user != null) {
                                                        if (!user.getProfileImage().equals("")) {
                                                            Picasso.get()
                                                                    .load(user.getProfileImage())
                                                                    .error(R.drawable.ic_home)
                                                                    .placeholder(R.drawable.progress_loader)
                                                                    .fit()
                                                                    .into(ivAvatar);
                                                        } else {
                                                            Picasso.get()
                                                                    .load(R.drawable.ic_profile)
                                                                    .error(R.drawable.ic_home)
                                                                    .placeholder(R.drawable.progress_loader)
                                                                    .fit()
                                                                    .into(ivAvatar);
                                                        }
                                                        tvUserName.setText(user.getFullName());
                                                    }
                                                    tvPostQuestion.setText(post.getQuestion());

                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
                database.collection("wishlist")
                        .document(postId).collection("comments")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                if (queryDocumentSnapshots != null && queryDocumentSnapshots.getDocumentChanges().size() > 0) {
                                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                                        switch (documentChange.getType()) {
                                            case ADDED:
                                                Comment comment = documentChange.getDocument().toObject(Comment.class);
                                                if (!comments.contains(comment)) {
                                                    comments.add(comment);
                                                }
                                        }
                                    }
                                    if (!comments.isEmpty()) {
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        //TODO: prikazi ekran deka nema komentari
                                    }
                                }
                            }
                        });
            }
        }
    }

    private void initViews() {
        ivAvatar = findViewById(R.id.iv_user_avatar);
        tvUserName = findViewById(R.id.tv_user_name);
        tvPostQuestion = findViewById(R.id.tv_post_question);
        rvComments = findViewById(R.id.rv_comments);
        etComment = findViewById(R.id.et_comment);
        ivSend = findViewById(R.id.ic_send);
        ivAttach = findViewById(R.id.ic_attach);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                if (resultUri != null) {
                    imageUrl = resultUri.toString();
                    Log.i("Image url", imageUrl);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
