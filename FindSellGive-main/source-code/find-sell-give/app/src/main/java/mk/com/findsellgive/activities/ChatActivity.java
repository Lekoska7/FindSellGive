package mk.com.findsellgive.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mk.com.findsellgive.R;
import mk.com.findsellgive.adapters.ChatAdapter;
import mk.com.findsellgive.models.Chat;
import mk.com.findsellgive.models.Message;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView mChat;
    private ImageView mSend;
    private EditText mMessage;
    private List<Message> mMessages = new ArrayList<>();
    private ChatAdapter adapter;
    private FirebaseFirestore database;
    private String uid;
    private String friendId;
    private AppCompatTextView mWelcomeMessage;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        database = FirebaseFirestore.getInstance();
        initViews();
        initRecyclerView();
        friendId = getIntent() != null ? getIntent().getStringExtra("friendId") : null;
        getData();
        Log.e("friendId", friendId);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    public void sendMessage() {
        String msg = mMessage.getText().toString();
        Log.e("MEssage", msg);
//                        if(!msg.equals("")) {
        Message message = new Message(uid, friendId, msg, new Date());
        Chat chat = new Chat(friendId, message);
        database.collection("users").document(uid)
                .collection("chat").document(friendId)
                .set(chat.getChatMap(chat));
        database.collection("users").document(uid)
                .collection("chat").document(friendId).collection("messages")
                .document().set(message.getMap(message));
        Chat chat1 = new Chat(uid, message);
        database.collection("users").document(friendId)
                .collection("chat").document(uid)
                .set(chat1.getChatMap(chat1));
        database.collection("users").document(friendId)
                .collection("chat").document(uid).collection("messages")
                .document().set(message.getMap(message));

        mMessage.setText("");
    }

    private void getData() {
        if (uid != null && friendId != null) {
            DocumentReference chatDocument = database.collection("users").document(uid)
                    .collection("chat").document(friendId);
            chatDocument.collection("messages").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshots != null) {
                        for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                            Message message = documentChange.getDocument().toObject(Message.class);
                            switch (documentChange.getType()) {
                                case ADDED:
                                    if (!mMessages.contains(message)) {
                                        mMessages.add(message);
                                    }
                                    break;
                                case MODIFIED:
                                    break;
                                case REMOVED:
                                    break;
                            }
                        }
                        Log.e("size", mMessages.size() + "");
                        if (!mMessages.isEmpty()) {
                            mWelcomeMessage.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        } else {
                            mWelcomeMessage.setVisibility(View.VISIBLE);
                            mWelcomeMessage.setText("Say hi");
                        }
                    }
                }
            });
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        mChat.setLayoutManager(llm);
        adapter = new ChatAdapter(this, mMessages);
        mChat.setAdapter(adapter);
    }

    private void initViews() {
        mChat = findViewById(R.id.rv_messages);
        mMessage = findViewById(R.id.et_message);
        mSend = findViewById(R.id.ic_send);
        mWelcomeMessage = findViewById(R.id.tv_welcome_msg);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser != null ? firebaseUser.getUid() : null;
    }
}
