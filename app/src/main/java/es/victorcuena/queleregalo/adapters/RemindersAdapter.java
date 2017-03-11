package es.victorcuena.queleregalo.adapters;

import android.content.Context;
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
import es.victorcuena.queleregalo.models.Present;
import es.victorcuena.queleregalo.models.Reminder;


public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.MyViewHolder> {

    private List<Reminder> reminders;
    private Context context;


    public RemindersAdapter(List<Reminder> reminders, Context context) {
        this.reminders = reminders;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminder_card_view, parent, false);

        return new MyViewHolder(view);


    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Reminder r = reminders.get(position);

        // holder.name.setTypeface(Typeface.createFromAsset(context.getAssets(),"Roboto-Thin.ttf"));

        holder.name.setText(r.getPresent().getName());
        holder.whom.setText(r.getToWhom());

        String priceText = context.getString(R.string.price_from) + " "
                + Present.Price.getStringValueFromPrice(context, r.getPresent().getFromPrice()) + " "
                + context.getString(R.string.price_coin);

        holder.reason.setText(r.getReason());

        //Setting reminder as tag
        holder.itemView.setTag(r);

        //Load the image
        Picasso.with(context).load(r.getPresent().getUrl()).into(holder.background);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
        holder.itemView.startAnimation(animation);

    }

    public void clear() {
        reminders.clear();
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView whom;
        TextView reason;
        ImageView background;

        MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.present_name);
            whom = (TextView) itemView.findViewById(R.id.to_whom);
            reason = (TextView) itemView.findViewById(R.id.present_reason);
            background = (ImageView) itemView.findViewById(R.id.reminder_present_backround);


        }
    }
}