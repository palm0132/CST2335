package com.example.michael.lab1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

import static com.example.michael.lab1.ChatDatabaseHelper.TABLE_NAME;

public class ChatWindow extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "ChatWindow";

    ListView chatChatMsgListView;
    ChatAdapter messageAdapter;
    EditText chatMsgEditTxt;
    Button chatSendBtn;
    ArrayList<String> list;
    ChatDatabaseHelper dbHelper;
    SQLiteDatabase db;

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

        dbHelper = new ChatDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        // String query = "SELECT * FROM " + TABLE_NAME + " WHERE 1";// why not leave out the WHERE clause?
        String query = "SELECT * FROM " + TABLE_NAME;
        //Cursor points to a location in your results
        Cursor cursor = db.rawQuery(query, null);
        //Move to the first row in your results
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            list.add(cursor.getString(cursor.getColumnIndex(dbHelper.KEY_MESSAGE)));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(
                    cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            cursor.moveToNext();
        }
        for (int columnIndex = 0; columnIndex < cursor.getColumnCount(); columnIndex++) {
            cursor.getColumnName(columnIndex);
            Log.i(ACTIVITY_NAME, "Cursor's column count =" + cursor.getColumnCount());
        }

        chatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //list.add(new String(chatMsgEditTxt.getText().toString()));
                list.add(chatMsgEditTxt.getText().toString());
                // Insert message into database
                ContentValues values = new ContentValues();
                values.put(dbHelper.KEY_MESSAGE, chatMsgEditTxt.getText().toString());
                db.insert(dbHelper.TABLE_NAME, null, values);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
