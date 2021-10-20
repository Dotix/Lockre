package com.brainixdev.lokre.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.brainixdev.lokre.R;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class AdaptateurSlides extends PagerAdapter {

    private Context mContext;
    ArrayList<Slides> slides ;

    public AdaptateurSlides(Context context, ArrayList<Slides> slide){
        this.mContext = context;
        this.slides = slide;
    }

    @Override
    public int getCount() {
        return slides.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.slides, container, false);

        Slides item = slides.get(position);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_image_slide);
        imageView.setImageResource(item.getIdImage());

        TextView tv_title=(TextView)itemView.findViewById(R.id.tv_titre_slide);
        tv_title.setText(item.getTitre());

        TextView tv_content=(TextView)itemView.findViewById(R.id.tv_desc_slide);
        tv_content.setText(item.getDescription());

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((ConstraintLayout) object);
    }

}
