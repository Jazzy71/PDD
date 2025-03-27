package com.example.carnest;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CarSelectingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<CarModel> carList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_selecting);

        recyclerView = findViewById(R.id.recycler_cars);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        carList = new ArrayList<>();

        carList.add(new CarModel("Tata", "Nexon", "Manual", "Diesel", 6.75, 28000, 4.6, false, R.drawable.car_nexon));
        carList.add(new CarModel("Hyundai", "i20", "Automatic", "Petrol", 7.9, 18000, 4.4, false, R.drawable.car_i20));
        carList.add(new CarModel("Honda", "City", "Manual", "Petrol", 8.5, 32000, 4.3, false, R.drawable.car_city));


        CarAdapter adapter = new CarAdapter(carList, this);
        recyclerView.setAdapter(adapter);
    }
}
