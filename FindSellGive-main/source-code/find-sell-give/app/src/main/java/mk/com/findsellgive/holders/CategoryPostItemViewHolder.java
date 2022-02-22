package mk.com.findsellgive.holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import mk.com.findsellgive.R;
import mk.com.findsellgive.listeners.OnItemClickListener;
import mk.com.findsellgive.models.User;
import mk.com.findsellgive.models.WishListPost;
import mk.com.findsellgive.utills.UtilitiesHelper;


public class CategoryPostItemViewHolder extends RecyclerView.ViewHolder {
    private CircleImageView ivUserAvatar;
    private AppCompatTextView tvUserName;
    private AppCompatTextView tvDate;
    private AppCompatTextView tvQuestion;
    private AppCompatTextView tvCommentsCount;

    public CategoryPostItemViewHolder(@NonNull final View itemView) {
        super(itemView);
        ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
        tvUserName = itemView.findViewById(R.id.tv_user_name);
        tvDate = itemView.findViewById(R.id.tv_date);
        tvQuestion = itemView.findViewById(R.id.tv_question);
        tvCommentsCount = itemView.findViewById(R.id.tv_comments_count);
    }

    public void bind(final WishListPost wishListPost, final OnItemClickListener listener) {
        if (wishListPost != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(wishListPost);
                }
            });
            FirebaseFirestore.getInstance().collection("users")
                    .document(wishListPost.getUid()).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
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
                                tvQuestion.setText(wishListPost.getQuestion());
                                tvDate.setText(UtilitiesHelper.convertDateToString(wishListPost.getDatePosted()));
                                tvCommentsCount.setText(String.valueOf(wishListPost.getCommentsCount()));
                            }
                        }
                    });
        }
    }
}
