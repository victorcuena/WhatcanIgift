package es.victorcuena.queleregalo.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import es.victorcuena.queleregalo.R;
import es.victorcuena.queleregalo.models.Present;


public class PresentsAdapter extends RecyclerView.Adapter<PresentsAdapter.MyViewHolder> {

    private List<Present> presents;
    private Context context;

    private View.OnClickListener onClickCardListener;
    private Typeface roboto;

    public PresentsAdapter(List<Present> presents, Context context, View.OnClickListener onClickCardListener) {
        this.presents = presents;
        this.context = context;
        roboto = Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf");
        this.onClickCardListener = onClickCardListener;
    }


    @Override
    public int getItemCount() {
        return presents.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.present_card_view, parent, false);

        return new MyViewHolder(view);


    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final ImageView background = holder.background;
        final ProgressBar loading = holder.loading;

        final Present p = presents.get(position);


        holder.itemView.setTag(p.getId());

        holder.name.setTypeface(roboto);
        holder.price.setTypeface(roboto);
        holder.name.setText(p.getName());


        String priceText = Present.Price.getStringValueFromPrice(context, p.getFromPrice());
        if(p.getFromPrice() != 0)
            priceText = context.getString(R.string.price_from) + " "
                + priceText + " "
                + context.getString(R.string.price_coin);

        holder.price.setText(priceText);
        holder.price.setTag(p.getFromPrice());


        //Load the image
        Picasso.with(context).load(p.getUrl()).into(background, new Callback() {
            @Override
            public void onSuccess() {
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

                //Show default error
                loading.setVisibility(View.GONE);
            }
        });


        Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
        holder.itemView.startAnimation(animation);


    }


    public void clear() {
        presents.clear();
        notifyDataSetChanged();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {


        TextView name;
        TextView price;
        ImageView background;
        ProgressBar loading;

        MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(onClickCardListener);

            name = (TextView) itemView.findViewById(R.id.present_name);
            price = (TextView) itemView.findViewById(R.id.present_price);
            background = (ImageView) itemView.findViewById(R.id.present_pic);
            loading = (ProgressBar) itemView.findViewById(R.id.loadingPanelCardView);


        }
    }
}