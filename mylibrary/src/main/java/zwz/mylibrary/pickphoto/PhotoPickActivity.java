package zwz.mylibrary.pickphoto;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import zwz.mylibrary.R;

/**
 * Created by 朱伟志 on 2017/2/10 0010 13:42.
 */
public class PhotoPickActivity extends AppCompatActivity {

    private List<PhotoFile>mAllPhotos;
    private HashMap<String, List<PhotoFile>> mPhotoMap;

    private RecyclerView mRecyclerView;
    private PickPhotoAdapter mPhotoAdapter;
    private BottomSheetDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pick_photo);
        mRecyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager manager=new GridLayoutManager(this,4);
        mRecyclerView.setLayoutManager(manager);
        scanPhoto();
        findViewById(R.id.tvPathName).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });

    }


    private void scanPhoto() {

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //判断外部存储卡是否存在
            Toast.makeText(this, "外部存储卡不可用", Toast.LENGTH_LONG).show();
            return;
        }

        //开启线程扫描
        new Thread() {
            @Override
            public void run() {
                mAllPhotos=new ArrayList<PhotoFile>();
                mPhotoMap=new HashMap<>();
                //获取URI
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                ContentResolver contentResolver = PhotoPickActivity.this.getContentResolver();

                Cursor cursor = contentResolver.query(uri, null,//查询URI，所有列
                        MediaStore.Images.Media.MIME_TYPE + " = ? or " + MediaStore.Images.Media.MIME_TYPE + " = ? or " + MediaStore.Images.Media.MIME_TYPE + " = ? ", //过滤条件 只查jpeg,jpg,png
                        new String[]{"image/jpeg", "image/png", "image/jpg"}, //条件
                        MediaStore.Images.Media.DATE_MODIFIED +" desc" );//修改时间排序
                while (cursor != null && cursor.moveToNext()) {
                    //获取图片路径-列索引
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    String modifyDate = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));
                    Log.d("vivi","modifyDate="+modifyDate);
                    //获取父文件
                    File parentFile = new File(path).getParentFile();
                    //判断是否为NULL
                    if (parentFile == null) {
                        continue;
                    }
                    String name = parentFile.getName();
                    PhotoFile photoFile=new PhotoFile(path,name);
                    mAllPhotos.add(photoFile);
                    if (mPhotoMap.containsKey(name)) {

                        List<PhotoFile> folder = mPhotoMap.get(name);
                        if (folder == null) {
                            folder = new ArrayList<PhotoFile>();
                        }

                        folder.add(photoFile);
                        mPhotoMap.put(name, folder);
                    }else {
                        List<PhotoFile> folder = new ArrayList<PhotoFile>();
                        folder.add(photoFile);
                        mPhotoMap.put(name,folder);
                    }
                }
                cursor.close();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPhotoAdapter=new PickPhotoAdapter(mAllPhotos,PhotoPickActivity.this);
                        mRecyclerView.setAdapter(mPhotoAdapter);
                    }
                });

            }
        }.start();


    }

    private void showBottomSheetDialog(){
         dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog,null);

        handleList(view);

        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void handleList(View contentView){
        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        FileDirAdapter adapter=new FileDirAdapter(this,mPhotoMap);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new FileDirAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(List<PhotoFile> photoFiles) {
                mPhotoAdapter.clear();
                mPhotoAdapter.addNewData(photoFiles);
                mPhotoAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
    }




}
