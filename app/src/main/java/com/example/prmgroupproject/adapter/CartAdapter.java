package com.example.prmgroupproject.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.prmgroupproject.R;
import com.example.prmgroupproject.dao.AppDatabase;
import com.example.prmgroupproject.dao.ProductDAO;
import com.example.prmgroupproject.model.Product;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Product> products;
    private Context mContext;
    private TextView textViewTotal;

    public CartAdapter(Context mContext, TextView textViewTotal) {
        this.mContext = mContext;
        this.textViewTotal = textViewTotal;
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from((parent.getContext())).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
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
        holder.textViewPrice.setText("$" + product.getPrice().toString());
        holder.textViewQuantity.setText("x" + product.getQuantity());
        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AppDatabase db = Room.databaseBuilder(mContext, AppDatabase.class, "cart-db").allowMainThreadQueries().build();
                                ProductDAO productDAO = db.productDAO();
                                productDAO.deleteById(product.getId());
                                products.remove(product);
                                Toast.makeText(mContext, "Removed Successfully!", Toast.LENGTH_SHORT).show();
                                updatePriceTotal();
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
    }

    private void updatePriceTotal() {
        double sum = 0;
        for (int i = 0; i < products.size(); i++) {
            sum = sum + (products.get(i).getPrice() * products.get(i).getQuantity());
        }
        textViewTotal.setText("Total: $" + sum);
    }

    public void setTasks(List<Product> productList) {
        products = productList;
        notifyDataSetChanged();
    }

    public List<Product> getTasks() {
        return products;
    }

    @Override
    public int getItemCount() {
        if (products == null) {
            return 0;
        }
        return products.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPrice, textViewQuantity;
        ImageView imageViewProduct;
        ImageView imageViewDelete;


        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
            imageViewDelete = itemView.findViewById(R.id.imageViewDelete);
        }
    }
}
