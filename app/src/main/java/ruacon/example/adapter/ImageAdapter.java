package ruacon.example.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ruacon.example.R;
import ruacon.example.entity.ImageData;

/**
 * Created by Nguyen Tien Hoang on 12/13/2016.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private List<ImageData> mListItem;

    public ImageAdapter(List<ImageData> listImageData) {
        mListItem = listImageData;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_image, parent, false);
        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        ImageData item = mListItem.get(position);
        holder.setData(item);
    }

    @Override
    public int getItemCount() {
        return mListItem.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImgThumb;

        ImageViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        void initView(View itemView) {
            mImgThumb = itemView.findViewById(R.id.img_thumb);
        }

        void setData(ImageData item) {
            mImgThumb.setImageResource(item.getImageResource());
        }
    }
}