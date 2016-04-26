package onl.deepspace.zoorallye.lib;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import onl.deepspace.zoorallye.R;
import onl.deepspace.zoorallye.fragments.OfflineItem;

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
        holder.mActionView.setBackgroundResource((mValues.get(position).offline) ?
                R.drawable.ic_offline : R.drawable.ic_download);

        holder.mView.setOnClickListener(new View.OnClickListener() {
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
        public final ImageButton mActionView;
        public OfflineItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.name);
            mActionView = (ImageButton) view.findViewById(R.id.action);
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
