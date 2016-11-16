package com.example.michael.lab1;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.example.michael.lab1.dummy.DummyContent;
import java.util.ArrayList;
import java.util.List;
import static android.R.id.list;
import static android.R.id.message;
import static com.example.michael.lab1.ChatDatabaseHelper.TABLE_NAME;
import static com.example.michael.lab1.R.id.chatChatMsgListView;
import static com.example.michael.lab1.R.id.chatMsgEditTxt;
import static com.example.michael.lab1.R.id.chatSendBtn;
import static com.example.michael.lab1.R.id.toolbar;
import android.widget.ArrayAdapter;

/**
 * An activity representing a list of Messages. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MessageDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MessageListActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "ChatWindow";
    ListView chatChatMsgListView;
    ChatAdapter messageAdapter;
    EditText chatMsgEditTxt;
    Button chatSendBtn;
    ArrayList<String> list;
    ChatDatabaseHelper dbHelper;
    SQLiteDatabase db;
    // Whether or not activity is in 2-pane mode (ie running on tablet)
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        // setContentView(R.layout.activity_chat_window);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // View recyclerView = findViewById(R.id.message_list);
        // assert recyclerView != null;
        // setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.message_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        // following code copied from ChatWindow.java

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

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = MessageListActivity.this.getLayoutInflater();
            View result = null;
            final String messageText = getItem(position);

            if (position%2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
                TextView outMessage = (TextView) result.findViewById(R.id.chatRowInTxtView);
                // get the string at position
                outMessage.setText(messageText);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
                TextView inMessage = (TextView) result.findViewById(R.id.chatRowOutTxtView);
                // get the string at position
                inMessage.setText(messageText);
            }

            result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(MessageDetailFragment.ARG_ITEM_ID, messageText);
                        MessageDetailFragment fragment = new MessageDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.message_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MessageDetailActivity.class);
                        intent.putExtra(MessageDetailFragment.ARG_ITEM_ID, messageText);

                        context.startActivity(intent);
                    }
                }
            });

            return result;
        }

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(MessageDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        MessageDetailFragment fragment = new MessageDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.message_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MessageDetailActivity.class);
                        intent.putExtra(MessageDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
