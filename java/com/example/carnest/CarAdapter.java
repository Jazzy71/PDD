package com.example.carnest;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private List<CarModel> carList;
    private Context context;

    public CarAdapter(List<CarModel> carList, Context context) {
        this.carList = carList;
        this.context = context;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        CarModel car = carList.get(position);

        holder.carImage.setImageResource(car.getImageResId());
        holder.carTitle.setText(car.getTitle());
        holder.carDetails.setText(car.getDetails());
        holder.carPrice.setText("₹" + car.getPrice());

        holder.buyNowBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, CarBookingActivity.class);
            intent.putExtra("car_title", car.getTitle());
            intent.putExtra("car_details", car.getDetails());
            intent.putExtra("car_price", car.getPrice());
            intent.putExtra("car_image", car.getImageResId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    static class CarViewHolder extends RecyclerView.ViewHolder {
        ImageView carImage;
        TextView carTitle, carDetails, carPrice;
        Button buyNowBtn;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            carImage = itemView.findViewById(R.id.img_car);
            carTitle = itemView.findViewById(R.id.tv_car_title);
            carDetails = itemView.findViewById(R.id.tv_car_details);
            carPrice = itemView.findViewById(R.id.tv_car_price);
            buyNowBtn = itemView.findViewById(R.id.btn_buy_now);

        }
    }
}
