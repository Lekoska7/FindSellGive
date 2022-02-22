package mk.com.findsellgive.holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import mk.com.findsellgive.R;
import mk.com.findsellgive.listeners.OnChatClickedListener;
import mk.com.findsellgive.models.Chat;
import mk.com.findsellgive.models.Message;
import mk.com.findsellgive.models.User;
import mk.com.findsellgive.utills.UtilitiesHelper;


public class InboxItemViewHolder extends RecyclerView.ViewHolder {
    private AppCompatTextView mMessageDate;
    private AppCompatTextView mMessageSender;
    private AppCompatTextView mMessageText;

    public InboxItemViewHolder(@NonNull View itemView) {
        super(itemView);
        mMessageDate = itemView.findViewById(R.id.tv_date);
        mMessageSender = itemView.findViewById(R.id.tv_user);
        mMessageText = itemView.findViewById(R.id.tv_message);
    }

    public void bind(final Chat chat, final OnChatClickedListener listener) {
        if (chat != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onChatClicked(chat);
                }
            });
            Message lastMessage = chat.getLastMessage();
            mMessageDate.setText(UtilitiesHelper.convertDateToString(lastMessage.getDate()));
            mMessageText.setText(lastMessage.getMessage());
            FirebaseFirestore.getInstance().collection("users").document(chat.getChatName()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot != null) {
                        User user = documentSnapshot.toObject(User.class);
                        mMessageSender.setText(user.getFullName());
                    }
                }
            });
        }
    }
}
