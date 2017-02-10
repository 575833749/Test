package zwz.mylibrary.pickphoto;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import zwz.mylibrary.R;

/**
 * Created by 朱伟志 on 2017/2/10 0010 15:15.
 */
public class PickPhotoAdapter extends RecyclerView.Adapter {

    private List<PhotoFile>photos;
    private Context mContext;
    private LayoutInflater mInflater;


    public PickPhotoAdapter(List<PhotoFile> photos, Context context) {
        this.photos = photos;
        mContext = context;
        mInflater=LayoutInflater.from(mContext);
    }

    public void clear(){
        if (photos!=null){
            photos.clear();
        }
    }

    public void addNewData(List<PhotoFile>photoFiles){
        clear();
        photos.addAll(photoFiles);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHodler(mInflater.inflate(R.layout.adapter_photo,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHodler h = (ViewHodler) holder;
        Glide.with(mContext).load(photos.get(position).path).into(h.mImageView);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }


    class ViewHodler extends RecyclerView.ViewHolder{

        ImageView mImageView;
        public ViewHodler(View itemView) {
            super(itemView);
            mImageView= (ImageView) itemView.findViewById(R.id.iamge);
        }
    }



}
