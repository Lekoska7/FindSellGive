package mk.com.findsellgive.holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import mk.com.findsellgive.R;
import mk.com.findsellgive.models.Comment;
import mk.com.findsellgive.models.User;
import mk.com.findsellgive.utills.UtilitiesHelper;


public class CommentViewHolder extends RecyclerView.ViewHolder {
    private CircleImageView ivUserAvatar;
    private AppCompatTextView tvUserName;
    private AppCompatTextView tvCommentText;
    private AppCompatImageView ivCommentImage;
    private AppCompatTextView tvCommentDate;

    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
        tvUserName = itemView.findViewById(R.id.tv_user_name);
        tvCommentText = itemView.findViewById(R.id.tv_comment_text);
        ivCommentImage = itemView.findViewById(R.id.iv_comment_image);
        tvCommentDate = itemView.findViewById(R.id.tv_date);
    }

    public void bind(final Comment comment) {
        if (comment != null) {
            FirebaseFirestore.getInstance().collection("users").document(comment.getUid())
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null) {
                        User user = documentSnapshot.toObject(User.class);
                        //kastiranje na korisnik
                        if (user != null) {
                            if(!user.getProfileImage().equals("")){
                                Picasso.get()
                                        .load(user.getProfileImage())
                                        .error(R.drawable.ic_home)
                                        .placeholder(R.drawable.progress_loader)
                                        .fit()
                                        .into(ivUserAvatar);
                            }else{
                                Picasso.get()
                                        .load(R.drawable.ic_profile)
                                        .error(R.drawable.ic_home)
                                        .placeholder(R.drawable.progress_loader)
                                        .fit()
                                        .into(ivUserAvatar);
                            }
                            tvUserName.setText(user.getFullName());
                        }
                        if(comment.getImageUrl() != null){
                            if(!comment.getImageUrl().equals("")){
                                ivCommentImage.setVisibility(View.VISIBLE);
                                Picasso.get()
                                        .load(comment.getImageUrl())
                                        .error(R.drawable.ic_home)
                                        .placeholder(R.drawable.progress_loader)
                                        .fit()
                                        .into(ivCommentImage);
                            }else{
                                ivCommentImage.setVisibility(View.GONE);
                            }
                        }
                        tvCommentText.setText(comment.getText());
                        tvCommentDate.setText(UtilitiesHelper.convertDateToString(comment.getDatePosted()));
                    }
                }
            });
        }
    }
}
