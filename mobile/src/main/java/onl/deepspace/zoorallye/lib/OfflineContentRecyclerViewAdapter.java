package onl.deepspace.zoorallye.lib;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import onl.deepspace.zoorallye.R;
import onl.deepspace.zoorallye.fragments.OfflineItem;
import onl.deepspace.zoorallye.helper.Const;

/**
 */
public class OfflineContentRecyclerViewAdapter extends
        RecyclerView.Adapter<OfflineContentRecyclerViewAdapter.ViewHolder> {

    private final List<OfflineItem> mValues;
    private OfflineItemCommunication mListener;

    public OfflineContentRecyclerViewAdapter(List<OfflineItem> items,
                                             OfflineItemCommunication listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_offline_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).name);

        if (mValues.get(position).offline) {
            Log.d(Const.LOGTAG, "Offline: true");
            holder.mActionView.setImageResource(R.drawable.ic_offline_pin);
        } else {
            Log.d(Const.LOGTAG, "Offline: false");
            holder.mActionView.setImageResource(R.drawable.ic_download);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(holder.mItem);
            }
        });
        holder.mActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final ImageView mActionView;
        public OfflineItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.name);
            mActionView = (ImageView) view.findViewById(R.id.action);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }

    public interface OfflineItemCommunication {
        void onItemClick(OfflineItem item);
    }

}
