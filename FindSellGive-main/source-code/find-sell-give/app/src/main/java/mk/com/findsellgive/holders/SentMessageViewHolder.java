package mk.com.findsellgive.holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import mk.com.findsellgive.R;
import mk.com.findsellgive.models.Message;
import mk.com.findsellgive.utills.UtilitiesHelper;


public class SentMessageViewHolder extends RecyclerView.ViewHolder {
    private TextView messageBody;
    private TextView messageTime;

    public SentMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        messageBody = itemView.findViewById(R.id.text_message_body);
        messageTime = itemView.findViewById(R.id.text_message_time);
    }

    public void bind(Message message) {
        if (message != null) {
            messageBody.setText(message.getMessage());
            messageTime.setText(UtilitiesHelper.convertDateToString(message.getDate()));
        }
    }
}
