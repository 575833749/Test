package zwz.mylibrary.pickphoto;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zwz.mylibrary.R;

/**
 * Created by 朱伟志 on 2017/2/10 0010 16:17.
 */
public class FileDirAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private HashMap<String,List<PhotoFile>>mData;
    private List<String> keys;
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public FileDirAdapter(Context context, HashMap<String, List<PhotoFile>> data) {
        mData = data;
        mContext=context;
        keys=new ArrayList<>();
        if (mData!=null&&mData.size()!=0){
            for (Map.Entry<String, List<PhotoFile>> entries:mData.entrySet()) {
                String key = entries.getKey();
                keys.add(key);
            }
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popu_recyview, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder h = (ViewHolder) holder;
        List<PhotoFile> list = mData.get(keys.get(position));
        if (list!=null){
            PhotoFile photoFile = list.get(0);
            Glide.with(mContext).load(photoFile.path).into(h.ivItemIcon);
            h.tvDirName.setText(photoFile.parentDirName);
            h.tvItemCount.setText(String.valueOf(list.size()+"张"));
        }



    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder {
         TextView tvDirName;
         TextView tvItemCount;
         ImageView ivItemIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDirName = (TextView) itemView.findViewById(R.id.tvDirName);
            tvItemCount = (TextView) itemView.findViewById(R.id.tvItemCount);
            ivItemIcon = (ImageView) itemView.findViewById(R.id.ivItemIcon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mOnItemClickListener!=null){
                        mOnItemClickListener.onItemClick(mData.get(keys.get(getAdapterPosition())));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(List<PhotoFile>photoFiles);
    }

}
