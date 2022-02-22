package mk.com.findsellgive.holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import mk.com.findsellgive.R;
import mk.com.findsellgive.models.Message;
import mk.com.findsellgive.models.User;
import mk.com.findsellgive.utills.UtilitiesHelper;


public class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
    private TextView messageText;
    private TextView timeText;
    private TextView nameText;
    private CircleImageView profileImage;

    public ReceivedMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        profileImage = itemView.findViewById(R.id.image_message_profile);
        nameText = itemView.findViewById(R.id.text_message_name);
        messageText = itemView.findViewById(R.id.text_message_body);
        timeText = itemView.findViewById(R.id.text_message_time);
    }

    public void bind(Message message) {
        messageText.setText(message.getMessage());
        timeText.setText(UtilitiesHelper.convertDateToString(message.getDate()));
        FirebaseFirestore.getInstance().collection("users").document(message.getFrom())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    User user = task.getResult().toObject(User.class);
                    if(user!=null){
                        nameText.setText(user.getFullName());
                        if(!user.getProfileImage().equals("")){
                            Picasso.get()
                                    .load(user.getProfileImage())
                                    .error(R.drawable.ic_home)
                                    .placeholder(R.drawable.progress_loader)
                                    .fit()
                                    .into(profileImage);
                        }else{
                            Picasso.get()
                                    .load(R.drawable.ic_profile)
                                    .error(R.drawable.ic_home)
                                    .placeholder(R.drawable.progress_loader)
                                    .fit()
                                    .into(profileImage);
                        }
                    }
                }
            }
        });
    }
}
