package com.brainixdev.lokre.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainixdev.lokre.R;

public class AdaptateurDrapeau extends ArrayAdapter<Pays> {
        private final LayoutInflater mLayoutInflater;

        public AdaptateurDrapeau(Context context) {
            super(context, 0);
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.item_pays, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.mImageView = convertView.findViewById(R.id.drapeau_pays);
                viewHolder.mNameView = convertView.findViewById(R.id.nom_pays);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Pays pays = getItem(position);
            assert pays != null;
            viewHolder.mImageView.setImageResource(getFlagResource(pays));
            viewHolder.mNameView.setText(pays.getNom());
            return convertView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            /*Pays pays    = getItem(position);

            if (convertView == null) {
                convertView = new ImageView(getContext());
            }

            assert pays != null;
            ((ImageView) convertView).setImageResource(getFlagResource(pays));

            return convertView;*/
            return getDropDownView(position,convertView,parent);
        }

        private int getFlagResource(Pays pays) {
            return getContext().getResources().getIdentifier("country_" + pays.getIso().toLowerCase(), "drawable", getContext().getPackageName());
        }

        private static class ViewHolder {
            public ImageView mImageView;
            public TextView mNameView;
        }
}
