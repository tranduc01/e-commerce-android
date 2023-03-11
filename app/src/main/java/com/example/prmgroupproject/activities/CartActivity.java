package com.example.prmgroupproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prmgroupproject.R;
import com.example.prmgroupproject.adapter.CartAdapter;
import com.example.prmgroupproject.constants.AppExecutors;
import com.example.prmgroupproject.dao.AppDatabase;
import com.example.prmgroupproject.dao.ProductDAO;
import com.example.prmgroupproject.model.Product;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView textViewTotal;
    AppDatabase mdb;
    CartAdapter adapter;
    Button btnCheckOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ActionBar actionBar = getSupportActionBar();
        // Customize the back button
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios);
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        textViewTotal=findViewById(R.id.textViewTotal);
        btnCheckOut=findViewById(R.id.btnCheckOut);
        getRoomData();

        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Check it out!!!!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getRoomData(){

        mdb= Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"cart-db").allowMainThreadQueries().build();
        ProductDAO productDAO=mdb.productDAO();
        recyclerView=findViewById(R.id.listViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Product> products=productDAO.getAll();
        adapter=new CartAdapter(CartActivity.this,textViewTotal);
        recyclerView.setAdapter(adapter);
        double sum=0;
        for (int i = 0; i <products.size() ; i++) {
            sum=sum+(products.get(i).getPrice()*products.get(i).getQuantity());
        }
        textViewTotal.setText("Total: $"+sum);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        retrieveTasks();
        super.onResume();
    }
    private void retrieveTasks(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Product> products=mdb.productDAO().getAll();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setTasks(products);
                    }
                });
            }
        });
    }
}