package com.example.michael.lab1;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    ListView chatChatMsgListView;
    ChatAdapter messageAdapter;
    EditText chatMsgEditTxt;
    Button chatSendBtn;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        chatChatMsgListView = (ListView) findViewById(R.id.chatChatMsgListView);
        chatMsgEditTxt = (EditText) findViewById(R.id.chatMsgEditTxt);
        chatSendBtn = (Button) findViewById(R.id.chatSendBtn);

        list = new ArrayList<String>();
        // “this” is ChatWindow, which is-a Context object ChatAdapter
        messageAdapter = new ChatAdapter(this);
        chatChatMsgListView.setAdapter (messageAdapter);

        chatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //list.add(new String(chatMsgEditTxt.getText().toString()));
                list.add(chatMsgEditTxt.getText().toString());
                // Restart process of getCount()/getView()
                messageAdapter.notifyDataSetChanged();
                // Clear Edit Text field so that it is ready for new message
                chatMsgEditTxt.setText("");
            }
        });
    }

    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return list.size();
        }

        public String getItem(int position) {
            return list.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;

            if (position%2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
                TextView outMessage = (TextView) result.findViewById(R.id.chatRowInTxtView);
                // get the string at position
                outMessage.setText(getItem(position));
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
                TextView inMessage = (TextView) result.findViewById(R.id.chatRowOutTxtView);
                // get the string at position
                inMessage.setText(getItem(position));
            }

            return result;
        }

    }

}
