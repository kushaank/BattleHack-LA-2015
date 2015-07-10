package com.portier.portier;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.layer.MainChatActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class AnswersActivity extends Activity {

    private ListView mAnswers;
    private AnswersListAdapter mAdapter;
    private List<AnswerMessage> messageList;
    private static Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);
        mAnswers = (ListView) findViewById(R.id.answersListView);
        //initializes list
        messageList = new ArrayList<AnswerMessage>();

        //Set ListAdapater
        mAdapter = new AnswersListAdapter(this, messageList);
        mAnswers.setAdapter(mAdapter);
        mHandler = new Handler();
        mHandler.postDelayed(runnable, 1000);
    }

    // Defines a runnable which is run every 10000ms
    private Runnable runnable = new Runnable() {
        @Override
        public void run()
        {
            ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.ANSWER);
            query.setLimit(Constants.MAX_MESSAGES);
            query.orderByDescending("createdAt");
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> messages, ParseException e) {
                    if(e == null)
                    {
                        if(messageList.size() == messages.size())
                        {
                            return;
                        }
                        else
                        {
                            List<AnswerMessage> newMessages = new ArrayList<AnswerMessage>();
                            int i = messages.size() - 1;
                            for(; i >= 0; i--)
                            {
                                final AnswerMessage message = new AnswerMessage();
                                String mNameKey = messages.get(i).getString("email_expert");
                                try
                                {
                                    List<ParseObject> possibleNames = ParseQuery.getQuery("Person").find();
                                    for (ParseObject p : possibleNames)
                                    {
                                        if (p.getString("email").equals(mNameKey))
                                        {
                                            message.name = p.getString("name");
                                        }
                                    }
                                }
                                catch (ParseException e1)
                                {
                                    e1.printStackTrace();
                                }
                                message.message = messages.get(i).getString("text");
                                newMessages.add(message);
                            }
                            messageList.clear();
                            messageList.addAll(newMessages);
                            mAdapter.notifyDataSetChanged();
                            mAnswers.invalidate();
                        }
                    }
                }
            });
            mHandler.postDelayed(this, 10000);
        }
    };

    //Select this answer as the best answer && venmo the reward
    private View.OnLongClickListener longclickanswerlistener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v)
        {
            Toast.makeText(getApplicationContext(), "Selected Best Response", Toast.LENGTH_SHORT).show();
            return false;
        }
    };

    private View.OnClickListener shortanswerlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "Initiating Chat", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainChatActivity.class);
            startActivity(intent);
        }
    };

    private class AnswersListAdapter extends ArrayAdapter
    {
        private List<AnswerMessage> msgList;
        private Context mContext = null;
        private LayoutInflater inflater = null;

        private class ViewHolder
        {
            ImageView profilePicture;
            TextView responderName;
            TextView responderPreview;
        }

        public AnswersListAdapter(Context context, List<AnswerMessage> items)
        {
            super(context, R.layout.answers_list_item, items);
            this.msgList = items;
            this.mContext = context;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount()
        {
            return msgList.size();
        }

        @Override
        public AnswerMessage getItem(int item)
        {
            return msgList.get(item);
        }

        @Override
        public long getItemId(int id)
        {
            return id;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent)
        {
            ViewHolder viewholder = new ViewHolder();
            view = inflater.inflate(R.layout.answers_list_item, parent, false);

            AnswerMessage mMsg = messageList.get(position);
            viewholder.responderName = (TextView) view.findViewById(R.id.responderName);
            viewholder.responderPreview = (TextView) view.findViewById(R.id.response_preview);
            viewholder.profilePicture = (ImageView) view.findViewById(R.id.userIcon);
            viewholder.profilePicture.setOnLongClickListener(longclickanswerlistener);
            viewholder.profilePicture.setOnClickListener(shortanswerlistener);
            viewholder.responderName.setText(mMsg.name);
            viewholder.responderPreview.setText(mMsg.message);
            view.setTag(viewholder);
            return view;
        }

    }
}
