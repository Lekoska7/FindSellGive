package mk.com.findsellgive.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import mk.com.findsellgive.R;
import mk.com.findsellgive.activities.ChatActivity;
import mk.com.findsellgive.adapters.InboxRecyclerViewAdapter;
import mk.com.findsellgive.listeners.OnChatClickedListener;
import mk.com.findsellgive.models.Chat;


public class InboxFragment extends ProfileBaseFragment implements OnChatClickedListener {
    private RecyclerView rvMessages;
    private InboxRecyclerViewAdapter adapter;
    private ProgressBar progressBar;
    private TextView noData;
    private FirebaseFirestore database;
    private List<Chat> chats = new ArrayList<>();
    private String uid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = getFragmentView(inflater, container, savedInstanceState);
        rvMessages = fragmentView.findViewById(R.id.rv_items);
        progressBar = fragmentView.findViewById(R.id.progress_bar);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        adapter = new InboxRecyclerViewAdapter(chats, getContext(), this);
        initRecyclerView(rvMessages, adapter);//povikuvanje na metodot od abstractnata klasa
        noData = fragmentView.findViewById(R.id.tv_no_data);
        showNoData(true, rvMessages, noData, progressBar);
        database = FirebaseFirestore.getInstance();
        getInbox();
        return fragmentView;
    }

    public void initRecyclerView(RecyclerView recyclerView, InboxRecyclerViewAdapter adapter) {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
    }

    public static InboxFragment newInstance() {
        return new InboxFragment();
    }

    /**
     * metod sto gi vrakja site poraki na korisnikot
     */
    @Override
    void getInbox() {
        database.collection("users").document(uid).collection("chat")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (documentSnapshots != null) {
                            for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                                QueryDocumentSnapshot documentSnapshot = documentChange.getDocument();
                                switch (documentChange.getType()) {
                                    case ADDED:
                                        if (!chats.contains(documentSnapshot.toObject(Chat.class))) {
                                            chats.add(documentSnapshot.toObject(Chat.class));
                                        }
                                        break;
                                    case REMOVED:
                                        chats.remove(documentSnapshot.toObject(Chat.class));
                                        adapter.notifyDataSetChanged();
                                        break;
                                    case MODIFIED:
                                        adapter.notifyDataSetChanged();
                                }
                            }
                            if (!chats.isEmpty()) {
                                progressBar.setVisibility(View.GONE);
                                noData.setVisibility(View.GONE);
                                rvMessages.setVisibility(View.VISIBLE);
                                // showNoData(false, rvMessages, noData, progressBar);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    /**
     * metod sto gi vrakja site postovi na korisnikot
     */
    @Override
    void getMyPosts() {
        //not used
    }

    @Override
    public void onChatClicked(Chat chat) {
        if (chat != null) {
            String friendId = chat.getChatName();
            Intent iChat = new Intent(getContext(), ChatActivity.class);
            iChat.putExtra("friendId", friendId);
            startActivity(iChat);
        }
    }
}
