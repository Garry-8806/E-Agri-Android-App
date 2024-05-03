package com.sourcey.tools.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.sourcey.tools.JSONParser;
import com.sourcey.tools.R;
import com.sourcey.tools.ServerUtility;
import com.sourcey.tools.model.Tools;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SPToolsAdapter extends RecyclerView.Adapter<SPToolsAdapter.ToolsViewHolder> implements Filterable {
    private List<Tools> bookList;
    private List<Tools> filteredBookList;
    private Context context;

    JSONParser jsonParser = new JSONParser();

    public SPToolsAdapter(List<Tools> bookList, Context context) {
        this.bookList = bookList;
        this.context = context;
        this.filteredBookList = bookList;
    }

    @Override
    public ToolsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_sp_tools, parent, false);
        return new ToolsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ToolsViewHolder holder, int position) {
        final Tools tools = filteredBookList.get(position);
        holder.txtTitle.setText(tools.getTitle());
        holder.txtDescription.setText(tools.getDescription());
        holder.txtSPName.setText(tools.getAdded_on());
        holder.txtStatus.setText(tools.getStatus());
        holder.txtCost.setText(tools.getCost());

        Picasso.with(context)
                .load(ServerUtility.Server_URL + "posts/" + tools.getImage_name())
                .into(holder.imgLogo);
    }

    @Override
    public int getItemCount() {
        return filteredBookList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredBookList = bookList;
                } else {
                    List<Tools> filteredList = new ArrayList<>();
                    for (Tools club : bookList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name
                        if (club.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(club);
                        }
                    }

                    filteredBookList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredBookList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredBookList = (ArrayList<Tools>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    public class ToolsViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle, txtDescription, txtCost, txtStatus, txtSPName;
        public CircleImageView imgLogo;



        public ToolsViewHolder(View view) {
            super(view);

            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            txtDescription = (TextView) view.findViewById(R.id.txtDescription);
            txtCost = (TextView) view.findViewById(R.id.txtCost);
            txtStatus = (TextView) view.findViewById(R.id.txtStatus);
            txtSPName = (TextView) view.findViewById(R.id.txtSPName);
            imgLogo = (CircleImageView) view.findViewById(R.id.toolImage);
        }
    }


}
