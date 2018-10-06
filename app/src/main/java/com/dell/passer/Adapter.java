package com.dell.passer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Adapter extends ArrayAdapter<BaiViet> {
    private Context context;
    private int resource;
    private List<BaiViet> dsbaiViets;

    public Adapter(@NonNull Context context, int resource, @NonNull List<BaiViet> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.dsbaiViets=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       ViewHolder viewHolder;
       if (convertView==null){
           viewHolder=new ViewHolder();
            convertView=LayoutInflater.from(context).inflate(R.layout.custum_list,parent,false);
           viewHolder.hinhaidien = (ImageView) convertView.findViewById(R.id.hinhaidien);
           viewHolder.txttintuc = (TextView)  convertView.findViewById(R.id.txttintuc);
           viewHolder.txtngaydang = (TextView)  convertView.findViewById(R.id.txtngaydang);
           viewHolder.txtnoidung = (TextView)  convertView.findViewById(R.id.txtnoidung);

           convertView.setTag(viewHolder);
       }
       else {
           viewHolder= (ViewHolder) convertView.getTag();
       }

       viewHolder.txttintuc.setText(dsbaiViets.get(position).getTitle());
        viewHolder.txtngaydang.setText(dsbaiViets.get(position).getPubdate());
        viewHolder.txtnoidung.setText(dsbaiViets.get(position).getDescription());

        String duongdan=dsbaiViets.get(position).getImage();
        Taihinhanh taihinhanh=new Taihinhanh();
        taihinhanh.execute(duongdan);
        try {
            viewHolder.hinhaidien.setImageBitmap(taihinhanh.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public class ViewHolder{
        private ImageView hinhaidien;
        private TextView txttintuc;
        private TextView txtngaydang;
        private TextView txtnoidung;

    }
    public class Taihinhanh extends AsyncTask<String,Void,Bitmap> {

        Bitmap hinhanh;

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                hinhanh = BitmapFactory.decodeStream(inputStream);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return hinhanh;
        }
    }

}
