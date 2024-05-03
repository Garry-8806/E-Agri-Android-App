package com.sourcey.tools;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sourcey.tools.model.ToolRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Dinesh on 4/13/2018.
 */

public class BookingAdapter extends ArrayAdapter {
    List<ToolRequest> list = new ArrayList<>();
    JSONParser jsonParser = new JSONParser();
    Context ctx;
    private int mYear, mMonth, mDay, mHour, mMinute;


    public BookingAdapter(@NonNull Context context, int resource) {

        super(context, resource);
        ctx = context;
    }

    public void add(@Nullable ToolRequest object) {
        list.add(object);
    }

    public BookingAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        final BookingHolder bookingHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_booking, parent, false);
            bookingHolder = new BookingHolder();
            bookingHolder.txtCost = (TextView) row.findViewById(R.id.txtCost);
            bookingHolder.txtName = (TextView) row.findViewById(R.id.txtSPName);
            bookingHolder.txtStatus = (TextView) row.findViewById(R.id.txtStatus);
            bookingHolder.txtToolName = (TextView) row.findViewById(R.id.txtToolName);
            bookingHolder.txtRequestBack = (TextView) row.findViewById(R.id.txtReturnRequest);
            row.setTag(bookingHolder);
        } else {
            bookingHolder = (BookingHolder) row.getTag();
        }
        try {
            final ToolRequest toolRequest = (ToolRequest) getItem(position);
            bookingHolder.txtToolName.setText(toolRequest.getTool_name());
            bookingHolder.txtName.setText(toolRequest.getSp_name());
            bookingHolder.txtStatus.setText(toolRequest.getRequest_status());
            bookingHolder.txtCost.setText("" + toolRequest.getCost());
            bookingHolder.txtRequestBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (toolRequest.getRequest_status().equals("Not Viewed")) {
                        Toast.makeText(getContext(), "Your request not accepted yet...", Toast.LENGTH_SHORT).show();
                    }
                    if (toolRequest.getRequest_status().equals("Accepted")) {
                        Toast.makeText(getContext(), "Tool Not Deliver to you.", Toast.LENGTH_SHORT).show();
                    }
                    if (toolRequest.getRequest_status().equals("Delivered")) {
                        //new ChangeStatus(bookingInfo.getId(),"Return Request");
                        final Calendar c = Calendar.getInstance();
                        mHour = c.get(Calendar.HOUR_OF_DAY);
                        mMinute = c.get(Calendar.MINUTE);

                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(ctx,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {

                                        bookingHolder.returnTime = getTime(hourOfDay, minute);
                                        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(ctx);
                                        alert.setMessage("Are you sure?");
                                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if (bookingHolder.returnTime.equals("")) {
                                                } else {
                                                    new ChangeStatus(toolRequest.getId(), "Return Requested", bookingHolder.returnTime).execute();

                                                }
                                            }
                                        });
                                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {


                                            }
                                        });
                                        alert.setTitle("Confirmation");
                                        alert.show();

                                    }
                                }, mHour, mMinute + 5, false);
                        timePickerDialog.show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return row;
    }

    private String getTime(int hr, int min) {
        Time tme = new Time(hr, min, 0);//seconds by default set to zero
        Format formatter;
        formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(tme);
    }

    public static class BookingHolder {
        TextView txtCost, txtName, txtStatus, txtToolName, txtRequestBack;
        String returnTime = "";
    }

    public class ChangeStatus extends AsyncTask<String, String, String> {
        List<NameValuePair> params = new ArrayList<>();
        boolean flag = false;
        String id, status, returntime;

        public ChangeStatus(String id, String status, String returntime) {
            this.id = id;
            this.status = status;
            this.returntime = returntime;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            params.add(new BasicNameValuePair("id", id));
            params.add(new BasicNameValuePair("status", status));
            params.add(new BasicNameValuePair("returntime", returntime));
        }

        @Override
        protected String doInBackground(String... strings) {
            JSONObject object = jsonParser.makeHttpRequest(ServerUtility.url_change_request_status(), "GET", params);
            try {
                flag = object.has(ServerUtility.TAG_SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (flag) {
                Toast.makeText(ctx, "Request for tool return placed successfully..", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ctx, CustomerHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ctx.startActivity(intent);
            } else {
                Toast.makeText(ctx, "There is problem while changing status", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
