package com.adminsurfacetech.my_learn_hub.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.adminsurfacetech.my_learn_hub.ScoreDetail;
import com.adminsurfacetech.my_learn_hub.Activity.ScoreDetail;
import com.adminsurfacetech.my_learn_hub.ViewHolder.RankingViewHolder;
import com.adminsurfacetech.my_learn_hub.Interface.ItemClickListener;
import com.adminsurfacetech.my_learn_hub.Interface.RankingCallBack;
import com.adminsurfacetech.my_learn_hub.Models.QuestionScore;
import com.adminsurfacetech.my_learn_hub.Models.Ranking;
import com.adminsurfacetech.my_learn_hub.R;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class RankingFragment extends Fragment {
    View myFragment;

    RecyclerView rankingList;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Ranking,RankingViewHolder> adapter;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase database;
    DatabaseReference questionScore,rankingTbl;
    SpinKitView spinKitView;

    int sum=0;



    public static RankingFragment newInstance(){
        RankingFragment rankingFragment = new RankingFragment();

        return rankingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        questionScore = database.getReference("Question_Score");
        rankingTbl = database.getReference("Ranking");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myFragment = inflater.inflate(R.layout.fragment_ranking,container,false);

        // init views
        rankingList = (RecyclerView)myFragment.findViewById(R.id.rankingList);
        spinKitView = (SpinKitView)myFragment.findViewById(R.id.spin_kit);
        layoutManager = new LinearLayoutManager(getActivity());
        rankingList.setHasFixedSize(true);

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rankingList.setLayoutManager(layoutManager);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        // currentUser.getPhotoUrl();

        updateScore(currentUser.getDisplayName(), new RankingCallBack<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {
                // update the ranking here
                rankingTbl.child(ranking.getUsername())
                        .setValue(ranking);
                //    showRanking(); //After it uploads now it needs to show here

            }
        });
        // Set adapter
        spinKitView.setVisibility(View.VISIBLE);
        adapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(
                Ranking.class,
                R.layout.layout_ranking,
                RankingViewHolder.class,
                rankingTbl.orderByChild("score")
        ) {
            @Override
            protected void populateViewHolder(RankingViewHolder viewHolder, final Ranking model, int position) {
                viewHolder.txt_name.setText(model.getUsername());
                viewHolder.txt_score.setText(String.valueOf(model.getScore()));
              //  viewHolder.profilepic.setImageURI(currentUser.getPhotoUrl());
                // a place to display the picture in the ranking fragment
                Picasso.get()
                        .load(currentUser.getPhotoUrl())
                        .into(viewHolder.profilepic);
                spinKitView.setVisibility(View.INVISIBLE);
                // fixed issues when item is clicked
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent scoreDetail = new Intent(getActivity(), ScoreDetail.class);
                        scoreDetail.putExtra("viewUser",model.getUsername());
                        startActivity(scoreDetail);
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        rankingList.setAdapter(adapter);

        return myFragment;
    }



    // call back interface
    private void updateScore(final String username, final RankingCallBack<Ranking> callBack) {
        // questionScore.orderByChild("userimg").equalTo(userimg)
        questionScore.orderByChild("userId").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)  {
                        for (DataSnapshot data:dataSnapshot.getChildren())
                        {
                            QuestionScore ques = data.getValue(QuestionScore.class);
                            sum+=Integer.parseInt(ques.getScore());
                        }
                        // after everything has been added
                        Ranking ranking = new Ranking(username,sum);
                        callBack.callBack(ranking);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
}
