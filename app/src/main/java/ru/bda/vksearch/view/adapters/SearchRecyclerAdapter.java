package ru.bda.vksearch.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.bda.vksearch.R;
import ru.bda.vksearch.model.SearchItem;

/**
 * email: denbelobaba@gmail.com
 *
 * @author Belobaba Denis
 */

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<SearchItem> items;

    public SearchRecyclerAdapter (Context context, List<SearchItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SearchItem item = items.get(position);
        boolean online = item.getOnline() == 1;
        int color = context.getResources().getColor(online ? R.color.colorOnline : R.color.colorOffline);
        Picasso.with(context)
                .load(item.getPhoto200())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into(holder.avatarImage);
        String name = item.getFirstName() + " " + item.getLastName();
        String city = item.getCity() != null ? item.getCity().getTitle() + ", " : "";
        String country = item.getCountry() != null ? item.getCountry().getTitle() : "";
        String connect = online ? context.getString(R.string.online) : context.getString(R.string.offline);
        holder.tvName.setText(name);
        holder.tvCity.setText(city + country);
        holder.tvConnect.setText(connect);
        holder.tvConnect.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setSearchList(List<SearchItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImage;
        TextView tvName;
        TextView tvCity;
        TextView tvConnect;
        public ViewHolder(View itemView) {
            super(itemView);
            avatarImage = (ImageView) itemView.findViewById(R.id.avatar_image);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvCity = (TextView) itemView.findViewById(R.id.tv_city);
            tvConnect = (TextView) itemView.findViewById(R.id.tv_connect);
        }
    }
}
