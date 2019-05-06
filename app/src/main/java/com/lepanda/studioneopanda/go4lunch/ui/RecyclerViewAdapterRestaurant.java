package com.lepanda.studioneopanda.go4lunch.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.lepanda.studioneopanda.go4lunch.R;
import com.lepanda.studioneopanda.go4lunch.events.NavToDetailEvent;
import com.lepanda.studioneopanda.go4lunch.models.Restaurant;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapterRestaurant extends RecyclerView.Adapter<RecyclerViewAdapterRestaurant.MyViewHolder> {

    private static final String TAG = "RVAdapter: ";
    private Context mContext;
    private List<Restaurant> mDataRestaurant;

    //CONSTRUCTOR
    public RecyclerViewAdapterRestaurant(Context mContext, List<Restaurant> mDataRest) {
        this.mContext = mContext;
        this.mDataRestaurant = mDataRest;
    }

    //VIEWHOLDER
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        //We use this layout to display items inside
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_view_items, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Restaurant r = mDataRestaurant.get(position);
        //NAME OK
        holder.restaurantName.setText(r.getName());

        //ADDRESS OK
        holder.restaurantAddress.setText(r.getAddress());

        //IMG OK
//        if (isImageActive) {
        Glide.with(mContext).asBitmap()
                .load(r.getPhotos())
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        // resource is your loaded Bitmap
                        holder.restaurantPhoto.setImageBitmap(resource);
                        return true;
                    }
                }).submit();
//        }
        //OPENINGHOURS OK

        List<String> workingTime = r.getOpeningHours();

        String monday = "lundi";
        String tuesday = "mardi";
        String wednesday = "mercredi";
        String thursday = "jeudi";
        String friday = "vendredi";
        String saturday = "samedi";
        String sunday = "dimanche";

        String pattern = "EEEE";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("fr", "fr"));

        String date = simpleDateFormat.format(new Date());
        Log.i(TAG, "onBindViewHolder: " + date);

        ////////////////////////MONDAY
        if (workingTime != null && date.equals(monday) && Arrays.toString(workingTime.toArray())
                .substring(Arrays.toString(workingTime.toArray())
                        .indexOf("Monday"), Arrays.toString(workingTime.toArray())
                        .indexOf(", Tuesday:"))
                .contains("Monday")) {

            String currentDayWorkingTimeFull = Arrays.toString(workingTime.toArray())
                    .substring(Arrays.toString(workingTime.toArray())
                            .indexOf("Monday") + 8, Arrays.toString(workingTime.toArray())
                            .indexOf(", Tuesday:"));

            if (currentDayWorkingTimeFull.contains("Closed")) {
                holder.restaurantWorkingTime.setText(R.string.closed_sample);
                holder.restaurantWorkingTime.setTextColor(Color.parseColor("#DB0B0B"));
            } else if (!currentDayWorkingTimeFull.contains("Closed")) {
                holder.restaurantWorkingTime.setText(currentDayWorkingTimeFull);
                holder.restaurantWorkingTime.setTextColor(Color.parseColor("#06CC06"));
                Log.i(TAG, currentDayWorkingTimeFull);
            }

            ////////////////////////TUESDAY
        } else if (workingTime != null && date.equals(tuesday) && Arrays.toString(workingTime.toArray())
                .substring(Arrays.toString(workingTime.toArray())
                        .indexOf(", Tuesday:"), Arrays.toString(workingTime.toArray())
                        .indexOf(", Wednesday:"))
                .contains(", Tuesday:")) {

            String currentDayWorkingTimeFull = Arrays.toString(workingTime.toArray())
                    .substring(Arrays.toString(workingTime.toArray())
                            .indexOf(", Tuesday:") + 10, Arrays.toString(workingTime.toArray())
                            .indexOf(", Wednesday:"));

            if (currentDayWorkingTimeFull.contains("Closed")) {
                holder.restaurantWorkingTime.setText(R.string.closed_sample);
                holder.restaurantWorkingTime.setTextColor(Color.parseColor("#DB0B0B"));
            } else if (!currentDayWorkingTimeFull.contains("Closed")) {
                holder.restaurantWorkingTime.setText(currentDayWorkingTimeFull);
                holder.restaurantWorkingTime.setTextColor(Color.parseColor("#06CC06"));
                Log.i(TAG, currentDayWorkingTimeFull);
            }

            ////////////////////////WEDNESDAY
        } else if (workingTime != null && date.equals(wednesday) && Arrays.toString(workingTime.toArray())
                .substring(Arrays.toString(workingTime.toArray())
                        .indexOf("Wednesday"), Arrays.toString(workingTime.toArray())
                        .indexOf(", Thursday:"))
                .contains("Wednesday")) {

            String currentDayWorkingTimeFull = Arrays.toString(workingTime.toArray())
                    .substring(Arrays.toString(workingTime.toArray())
                            .indexOf("Wednesday") + 11, Arrays.toString(workingTime.toArray())
                            .indexOf(", Thursday:"));

            if (currentDayWorkingTimeFull.contains("Closed")) {
                holder.restaurantWorkingTime.setText(R.string.closed_sample);
                holder.restaurantWorkingTime.setTextColor(Color.parseColor("#DB0B0B"));
            } else if (!currentDayWorkingTimeFull.contains("Closed")) {
                holder.restaurantWorkingTime.setText(currentDayWorkingTimeFull);
                holder.restaurantWorkingTime.setTextColor(Color.parseColor("#06CC06"));
                Log.i(TAG, currentDayWorkingTimeFull);
            }

            ////////////////////////THURSDAY
        } else if (workingTime != null && date.equals(thursday) && Arrays.toString(workingTime.toArray())
                .substring(Arrays.toString(workingTime.toArray())
                        .indexOf("Thursday"), Arrays.toString(workingTime.toArray())
                        .indexOf(", Friday:"))
                .contains("Thursday")) {

            String currentDayWorkingTimeFull = Arrays.toString(workingTime.toArray())
                    .substring(Arrays.toString(workingTime.toArray())
                            .indexOf("Thursday") + 10, Arrays.toString(workingTime.toArray())
                            .indexOf(", Friday:"));

            if (currentDayWorkingTimeFull.contains("Closed")) {
                holder.restaurantWorkingTime.setText(R.string.closed_sample);
                holder.restaurantWorkingTime.setTextColor(Color.parseColor("#DB0B0B"));
            } else if (!currentDayWorkingTimeFull.contains("Closed")) {
                holder.restaurantWorkingTime.setText(currentDayWorkingTimeFull);
                holder.restaurantWorkingTime.setTextColor(Color.parseColor("#06CC06"));
                Log.i(TAG, currentDayWorkingTimeFull);
            }

            ////////////////////////FRIDAY
        } else if (workingTime != null && date.equals(friday) && Arrays.toString(workingTime.toArray())
                .substring(Arrays.toString(workingTime.toArray())
                        .indexOf("Friday"), Arrays.toString(workingTime.toArray())
                        .indexOf(", Saturday:"))
                .contains("Friday")) {

            String currentDayWorkingTimeFull = Arrays.toString(workingTime.toArray())
                    .substring(Arrays.toString(workingTime.toArray())
                            .indexOf("Friday") + 8, Arrays.toString(workingTime.toArray())
                            .indexOf(", Saturday:"));

            if (currentDayWorkingTimeFull.contains("Closed")) {
                holder.restaurantWorkingTime.setText(R.string.closed_sample);
                holder.restaurantWorkingTime.setTextColor(Color.parseColor("#DB0B0B"));
            } else if (!currentDayWorkingTimeFull.contains("Closed")) {
                holder.restaurantWorkingTime.setText(currentDayWorkingTimeFull);
                holder.restaurantWorkingTime.setTextColor(Color.parseColor("#06CC06"));
                Log.i(TAG, currentDayWorkingTimeFull);
            }

            ////////////////////////SATURDAY
        } else if (workingTime != null && date.equals(saturday) && Arrays.toString(workingTime.toArray())
                .substring(Arrays.toString(workingTime.toArray())
                        .indexOf("Saturday"), Arrays.toString(workingTime.toArray())
                        .indexOf(", Sunday:"))
                .contains("Saturday")) {

            String currentDayWorkingTimeFull = Arrays.toString(workingTime.toArray())
                    .substring(Arrays.toString(workingTime.toArray())
                            .indexOf("Saturday") + 10, Arrays.toString(workingTime.toArray())
                            .indexOf(", Sunday:"));

            if (currentDayWorkingTimeFull.contains("Closed")) {
                holder.restaurantWorkingTime.setText(R.string.closed_sample);
                holder.restaurantWorkingTime.setTextColor(Color.parseColor("#DB0B0B"));
            } else if (!currentDayWorkingTimeFull.contains("Closed")) {
                holder.restaurantWorkingTime.setText(currentDayWorkingTimeFull);
                holder.restaurantWorkingTime.setTextColor(Color.parseColor("#06CC06"));
                Log.i(TAG, currentDayWorkingTimeFull);
            }
            ////////////////////////SUNDAY
        } else if (workingTime != null && date.equals(sunday) && Arrays.toString(workingTime.toArray())
                .substring(Arrays.toString(workingTime.toArray())
                        .indexOf("Sunday"), Arrays.toString(workingTime.toArray())
                        .indexOf("]"))
                .contains("Sunday")) {

            String currentDayWorkingTimeFull = Arrays.toString(workingTime.toArray())
                    .substring(Arrays.toString(workingTime.toArray())
                            .indexOf("Sunday") + 8, Arrays.toString(workingTime.toArray())
                            .indexOf("]"));

            if (currentDayWorkingTimeFull.contains("Closed")) {
                holder.restaurantWorkingTime.setText(R.string.closed_sample);
                holder.restaurantWorkingTime.setTextColor(Color.parseColor("#DB0B0B"));
            } else if (!currentDayWorkingTimeFull.contains("Closed")) {

                holder.restaurantWorkingTime.setText(currentDayWorkingTimeFull);
                holder.restaurantWorkingTime.setTextColor(Color.parseColor("#06CC06"));
                Log.i(TAG, currentDayWorkingTimeFull);
            }
            ////////////////////////ELSE EXCEPTIONS
        } else {
            holder.restaurantWorkingTime.setText(R.string.opening_hours_unavailable);
            holder.restaurantWorkingTime.setTextColor(Color.parseColor("#BA7104"));
        }
        Boolean isReceivedFromList = true;

        //VIEW CLICK OK
        holder.restaurantContainer.setOnClickListener(v -> {
            EventBus.getDefault().post(new NavToDetailEvent(mDataRestaurant.get(position).getName(), isReceivedFromList));
        });

        //RATINGS OK
        Double rating = r.getRating();
        Log.i(TAG, "onBindViewHolder: the current RRR is : " + rating + "for " + r.getName());

        if (rating != null && rating >= 1.5 && rating < 3.5) {
            holder.oneStar.setVisibility(View.VISIBLE);
        } else if (rating != null && rating >= 3.5 && rating < 4.5) {
            holder.oneStar.setVisibility(View.VISIBLE);
            holder.twoStar.setVisibility(View.VISIBLE);
        } else if (rating != null && rating >= 4.5) {
            holder.oneStar.setVisibility(View.VISIBLE);
            holder.twoStar.setVisibility(View.VISIBLE);
            holder.threeStar.setVisibility(View.VISIBLE);
        }

        //DISTANCE TO PLACE OK
        holder.restaurantDistanceFromUser.setText(Math.round(r.getDistance()) + " m");
    }

    // we return the size of the article list
    @Override
    public int getItemCount() {
        return mDataRestaurant.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        // declaration of UI Elements
        private RelativeLayout restaurantContainer;
        private TextView restaurantName;
        private TextView restaurantAddress;
        private TextView restaurantDistanceFromUser;
        private TextView restaurantWorkingTime;
        private ImageView restaurantPhoto;
        private ImageView oneStar;
        private ImageView twoStar;
        private ImageView threeStar;
        private Button btnImg;


        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // we assign a view to each UI Element
            restaurantContainer = itemView.findViewById(R.id.list_view_items_container_parent);
            restaurantName = itemView.findViewById(R.id.list_view_place_name);
            restaurantAddress = itemView.findViewById(R.id.list_view_place_address);
            restaurantPhoto = itemView.findViewById(R.id.list_view_place_image);
            restaurantDistanceFromUser = itemView.findViewById(R.id.list_view_place_distance);
            restaurantWorkingTime = itemView.findViewById(R.id.list_view_place_schedule);
            oneStar = itemView.findViewById(R.id.star_rating1);
            twoStar = itemView.findViewById(R.id.star_rating2);
            threeStar = itemView.findViewById(R.id.star_rating3);
            btnImg = itemView.findViewById(R.id.btnImg);
        }
    }
}