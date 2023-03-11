package com.example.prmgroupproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prmgroupproject.MainActivity;
import com.example.prmgroupproject.R;
import com.example.prmgroupproject.dao.AppDatabase;
import com.example.prmgroupproject.dao.ProductDAO;
import com.example.prmgroupproject.model.Product;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ProductDetailActivity extends AppCompatActivity {
    private TextView textViewName;
    private TextView textViewPrice;
    private TextView textViewDescription;
    private ImageView imageView;
    private Button btnAddToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ActionBar actionBar = getSupportActionBar();
        // Customize the back button
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios);
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);


        Bundle bundle=getIntent().getExtras();
        if(bundle==null){
            return;
        }
        Product product= (Product) bundle.get("product");
        textViewName=findViewById(R.id.textViewName);
        textViewPrice=findViewById(R.id.textViewPrice);
        textViewDescription=findViewById(R.id.textViewDescription);
        imageView=findViewById(R.id.imageViewProduct);
        btnAddToCart=findViewById(R.id.btnAddToCart);

        textViewName.setText(product.getName());
        textViewPrice.setText("$"+product.getPrice().toString());
        textViewDescription.setText(product.getDescription());


        URL url = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            url = new URL(product.getImage());
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            imageView.setImageBitmap(bmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailActivity.this);
                builder.setMessage("Are you sure you want to add this item to your cart?")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AppDatabase db= Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"cart-db").allowMainThreadQueries().build();
                                ProductDAO productDAO=db.productDAO();
                                boolean check=productDAO.is_exist(product.getName());
                                if(check==false){
                                    productDAO.addToCart(new Product(product.getId(),product.getName(),product.getPrice(),product.getImage(),1));
                                    Toast.makeText(getApplicationContext(), "Added "+product.getName(), Toast.LENGTH_SHORT).show();
                                }else{
                                    Product product1=productDAO.getByName(product.getName());
                                    productDAO.updateQuantity(product.getName(), product1.getQuantity()+1);
                                    Toast.makeText(getApplicationContext(), "Added "+product.getName(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });


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
}