package es.victorcuena.queleregalo.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.victorcuena.queleregalo.R;
import es.victorcuena.queleregalo.models.AmazonProduct;


public class AmazonProductsAdapter extends RecyclerView.Adapter<AmazonProductsAdapter.MyViewHolder> {

    private List<AmazonProduct> products;
    private Context context;

    private Typeface roboto;

    public AmazonProductsAdapter(List<AmazonProduct> products, Context context) {
        this.products = products;
        this.context = context;
        roboto = Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf");

    }


    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.amazon_card_view, parent, false);

        return new MyViewHolder(view);


    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        final AmazonProduct p = products.get(position);

        holder.itemView.setTag(p.getHyperlink());

        holder.name.setTypeface(roboto);
        holder.price.setTypeface(roboto);

        holder.name.setText(p.getTitle());
        holder.price.setText(p.getFormatedPrice());

        //Load the image
        Picasso.with(context).load(p.getUrlImage()).into(holder.background);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
        holder.itemView.startAnimation(animation);


    }


    class MyViewHolder extends RecyclerView.ViewHolder {


        TextView name;
        TextView price;
        ImageView background;

        MyViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Starting the detail activity
                    Uri uri = Uri.parse((String) itemView.getTag());
                    context.startActivity(new Intent(Intent.ACTION_VIEW, uri));

                }
            });

            name = (TextView) itemView.findViewById(R.id.amazon_name);
            price = (TextView) itemView.findViewById(R.id.amazon_price);
            background = (ImageView) itemView.findViewById(R.id.amazon_pic);


        }
    }
}