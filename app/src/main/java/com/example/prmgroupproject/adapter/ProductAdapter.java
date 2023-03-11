package com.example.prmgroupproject.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.prmgroupproject.R;
import com.example.prmgroupproject.activities.ProductDetailActivity;
import com.example.prmgroupproject.dao.AppDatabase;
import com.example.prmgroupproject.dao.ProductDAO;
import com.example.prmgroupproject.model.Product;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import retrofit2.Callback;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> products;
    private Context mContext;

    public ProductAdapter(Context context,List<Product> products) {
        this.mContext=context;
        this.products = products;

    }


    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from((parent.getContext())).inflate(R.layout.item_list, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        Product product = products.get(position);
        URL url = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            url = new URL(product.getImage());
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            holder.imageViewProduct.setImageBitmap(bmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.textViewName.setText(product.getName());
        holder.textViewPrice.setText("$"+product.getPrice().toString());
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnClickDetail(product);
            }
        });

        holder.imageViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Are you sure you want to add this item to your cart?")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AppDatabase db= Room.databaseBuilder(mContext,AppDatabase.class,"cart-db").allowMainThreadQueries().build();
                                ProductDAO productDAO=db.productDAO();
                                boolean check=productDAO.is_exist(product.getName());
                                if(check==false){
                                    productDAO.addToCart(new Product(product.getId(),product.getName(),product.getPrice(),product.getImage(),1));
                                    Toast.makeText(mContext, "Added "+product.getName(), Toast.LENGTH_SHORT).show();
                                }else{
                                    Product product1=productDAO.getByName(product.getName());
                                    productDAO.updateQuantity(product.getName(), product1.getQuantity()+1);
                                    Toast.makeText(mContext, "Added "+product.getName(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
    }

    private void OnClickDetail(Product product){
        Intent i=new Intent(mContext,ProductDetailActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("product",product);
        i.putExtras(bundle);
        mContext.startActivity(i);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout layoutItem;
        ImageView imageViewProduct;
        TextView textViewName;
        TextView textViewPrice;
        ImageView imageViewCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutItem=itemView.findViewById(R.id.item_list);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            imageViewCart = itemView.findViewById(R.id.imageViewCart);
        }
    }
}
