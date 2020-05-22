package com.jtmcompany.smartadvertisingboard.VideoEdit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jtmcompany.smartadvertisingboard.VideoEdit.Music.MusicList;
import com.jtmcompany.smartadvertisingboard.R;
import com.jtmcompany.smartadvertisingboard.StickerView.StickerImageView;
import com.jtmcompany.smartadvertisingboard.StickerView.StickerTextView;
import com.jtmcompany.smartadvertisingboard.StickerView.StickerView;
import com.jtmcompany.smartadvertisingboard.VideoEdit.Adapter.VideoEdit_RecyclerAdapter;
import com.jtmcompany.smartadvertisingboard.VideoEdit.Adapter.VideoEdit_StickerBottomsheet_RecyclerAdapter;
import com.jtmcompany.smartadvertisingboard.VideoEdit.Adapter.VideoEdit_TextBottomsheet_RecyclerAdapter;
import com.waynell.videorangeslider.RangeSlider;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class VideoEditAtivity extends AppCompatActivity implements VideoEdit_RecyclerAdapter.OnClickEditor_ModelListener, VideoEdit_TextBottomsheet_RecyclerAdapter.textClickListener, VideoEdit_StickerBottomsheet_RecyclerAdapter.stickerClickListener, View.OnClickListener {

    List<Editor_Model> list=new ArrayList<>();
    FrameLayout videoView_container;
    ImageView textInsert_bt;
    ImageView textexit_bt;
    ImageView stickerInsert_bt;
    ImageView stickerExit_bt;
    ImageView video_play_bt;
    ImageView video_stop_bt;
    BottomSheetDialog text_bottomsheet;
    BottomSheetDialog sticker_bottomsheet;

    VideoView videoView;

    //현재 프레임레이아웃에 삽입된 스티커뷰
    List<StickerView> insertView=new ArrayList<>();
    private int REQUEST_CODE=100;

    Uri select_Video_Uri;



    private int mDuration;

    static int trim_start;
    static int trim_end;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_edit_ativity);

        video_play_bt=findViewById(R.id.video_play);
        video_stop_bt=findViewById(R.id.video_stop);
        video_play_bt.setOnClickListener(this);
        video_stop_bt.setOnClickListener(this);

        videoView_container=findViewById(R.id.video_eidt_container);
        videoView_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int position=0; position<insertView.size(); position++){
                    insertView.get(position).hide_View();
                }

            }
        });

        videoView=findViewById(R.id.video_eidt_video);
        select_Video_Uri=getIntent().getParcelableExtra("selectUri");
        videoView.setVideoURI(select_Video_Uri);


        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {


                mDuration=videoView.getDuration()/1000;




            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                video_play_bt.setVisibility(View.VISIBLE);
                video_stop_bt.setVisibility(View.GONE);
            }
        });




        RecyclerView recyclerView=findViewById(R.id.video_edit_recycler);

        list.add(new Editor_Model(getResources().getDrawable(R.drawable.trim),"잘라내기"));
        list.add(new Editor_Model(getResources().getDrawable(R.drawable.text),"텍스트"));
        list.add(new Editor_Model(getResources().getDrawable(R.drawable.sticker),"스티커"));
        list.add(new Editor_Model(getResources().getDrawable(R.drawable.music),"음악"));
        list.add(new Editor_Model(getResources().getDrawable(R.drawable.gallery),"사진"));
        VideoEdit_RecyclerAdapter recyclerAdapter=new VideoEdit_RecyclerAdapter(list);
        recyclerAdapter.setOnClickedListener(this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);

        //텍스트 바텀시트
        text_bottomsheet=new BottomSheetDialog(this);

        //스티커 바텀시트
        sticker_bottomsheet=new BottomSheetDialog(this);



    }


    @Override
    public void OnClickedEditor_Model(int position) {
        Log.d("tak4",""+position);

            //잘라내기
        if(position==0){

            //trim됫다면
            if(trim_start!=0 && trim_end!=0)
            mDuration=trim_end-trim_start;

            VideoTrimFragment videoTrimFragment= new VideoTrimFragment(mDuration,videoView,select_Video_Uri);
            videoTrimFragment.show(getSupportFragmentManager(),"hi");




            //텍스트
        }else if(position==1){
            List<Drawable> list=new ArrayList<>();
            View view=getLayoutInflater().inflate(R.layout.text_bottomsheet,null);
            RecyclerView recyclerView=view.findViewById(R.id.text_bottomsheet_recycler);
            textInsert_bt=view.findViewById(R.id.text_check);
            textexit_bt=view.findViewById(R.id.text_exit);

            textexit_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    text_bottomsheet.cancel();
                }
            });


            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);

            list.add(getResources().getDrawable(R.drawable.text1));
            list.add(getResources().getDrawable(R.drawable.text2));
            list.add(getResources().getDrawable(R.drawable.text3));
            VideoEdit_TextBottomsheet_RecyclerAdapter recyclerAdapter=new VideoEdit_TextBottomsheet_RecyclerAdapter(list);
            recyclerAdapter.setOnTextListener(this);
            recyclerView.setAdapter(recyclerAdapter);



            text_bottomsheet.setContentView(view);
            text_bottomsheet.show();


            //스티커
        }else if(position==2){
            List<Drawable> list = new ArrayList<>();
            View view=getLayoutInflater().inflate(R.layout.sticker_bottomsteet,null);
            RecyclerView recyclerView=view.findViewById(R.id.sticker_bottomsheet_recycler);
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);

            stickerInsert_bt=view.findViewById(R.id.sticker_insert);
            stickerExit_bt=view.findViewById(R.id.sticker_exit);
            stickerExit_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sticker_bottomsheet.cancel();
                }
            });

            list.add(getResources().getDrawable(R.drawable.icon));
            list.add(getResources().getDrawable(R.drawable.advertise));
            VideoEdit_StickerBottomsheet_RecyclerAdapter recyclerAdapter=new VideoEdit_StickerBottomsheet_RecyclerAdapter(list);
            recyclerAdapter.setOnStickerListener(this);
            recyclerView.setAdapter(recyclerAdapter);



            sticker_bottomsheet.setContentView(view);
            sticker_bottomsheet.show();


            //음악
        }else if(position==3){
            Intent intent =new Intent(this, MusicList.class);
            startActivity(intent);



            //사진
        }else if(position==4){
            Intent intent=new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,REQUEST_CODE);

        }
    }



    StickerTextView prieveTextView;
    StickerTextView curTextView;
    @Override
    public void textOnClick(int position) {
        Log.d("tak3",""+position);

        //삭제하는뷰가 삽입해져있는뷰랑 같다면 flag=true로 놓아 삭제를방지
        Boolean flag=false;
        for(int i=0; i<insertView.size(); i++) {
            if (insertView.get(i) == curTextView) {
                flag = true;
                break;
            }
        }
        if(prieveTextView!=null && flag!=true)
        videoView_container.removeView(prieveTextView);

        final StickerTextView stickerTextView=new StickerTextView(this);
        stickerTextView.setText("Hello");
        curTextView=stickerTextView;



        if(position==0)
            stickerTextView.tv_main.setTextColor(Color.WHITE);
        else if(position==1)
            stickerTextView.tv_main.setTextColor(Color.BLACK);
        else if(position==2)
            stickerTextView.tv_main.setTextColor(Color.BLUE);

        videoView_container.addView(stickerTextView);

        // V버튼
        textInsert_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tak3","클릭");
                text_bottomsheet.dismiss();
                insertView.add(curTextView);
            }
        });



        //외부배경 눌렀을때 이벤트발생 (API17이상)
        text_bottomsheet.setOnCancelListener(new DialogInterface.OnCancelListener() {
            Boolean flag=false;
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d("tak3","취소");
                for(int i=0; i<insertView.size(); i++) {
                    if (insertView.get(i) == curTextView) {
                        flag = true;
                        break;
                    }
                }
                if(flag!=true)
               videoView_container.removeView(curTextView);
            }
        });

        prieveTextView=curTextView;
    }


    StickerImageView prieveStickerView;
    StickerImageView curStickerView;
    @Override
    public void stickerOnClick(int position, Drawable drawable) {
        final StickerImageView stickerImageView=new StickerImageView(VideoEditAtivity.this);
        stickerImageView.setImageDrawable(drawable);

        Boolean flag=false;
        for(int i=0; i<insertView.size(); i++) {
            if (insertView.get(i) == curStickerView) {
                flag = true;
                break;
            }
        }
        if(prieveStickerView!=null && flag!=true)
            videoView_container.removeView(prieveStickerView);

        curStickerView=stickerImageView;

        videoView_container.addView(curStickerView);

        prieveStickerView=curStickerView;

        // V 버튼
        stickerInsert_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sticker_bottomsheet.dismiss();
                insertView.add(stickerImageView);
            }
        });




        //외부배경 눌렀을때 이벤트발생 (API17이상)
        sticker_bottomsheet.setOnCancelListener(new DialogInterface.OnCancelListener() {
            Boolean flag=false;
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d("tak3","취소");
                for(int i=0; i<insertView.size(); i++) {
                    if (insertView.get(i) == curStickerView) {
                        flag = true;
                        break;
                    }
                }
                if(flag!=true)
                    videoView_container.removeView(curStickerView);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE){
            if(resultCode==RESULT_OK){
                try{
                    InputStream in=getContentResolver().openInputStream(data.getData());
                    Bitmap img= BitmapFactory.decodeStream(in);
                    in.close();
                    StickerImageView stickerImageView=new StickerImageView(this);

                    stickerImageView.setImageBitmap(img);
                    videoView_container.addView(stickerImageView);
                    insertView.add(stickerImageView);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getTime(int seconds) {
        int hr = seconds / 3600;
        int rem = seconds % 3600;
        int mn = rem / 60;
        int sec = rem % 60;
        return String.format("%02d", hr) + ":" + String.format("%02d", mn) + ":" + String.format("%02d", sec);
    }

    @Override
    public void onClick(View view) {
        if(view==video_play_bt){
            videoView.start();
            video_stop_bt.setVisibility(View.VISIBLE);
            video_play_bt.setVisibility(VideoView.GONE);

        }else if(view==video_stop_bt){
            videoView.pause();
            video_play_bt.setVisibility(View.VISIBLE);
            video_stop_bt.setVisibility(View.GONE);

        }
    }
}
