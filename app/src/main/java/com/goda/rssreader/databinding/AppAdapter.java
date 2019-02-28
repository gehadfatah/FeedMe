package com.goda.rssreader.databinding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;

public class AppAdapter<T extends AppAdapterViewModel> extends ListAdapter<T, AppAdapter.ViewHolder> {

    private ClickCallback clickCallback;

    public AppAdapter(ClickCallback clickCallback, @NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
        this.clickCallback = clickCallback;
    }


    public static class ViewHolder<V extends ViewDataBinding> extends RecyclerView.ViewHolder {
        private V v;

        ViewHolder(V v) {
            super(v.getRoot());
            this.v = v;
        }

        public V getBinding() {
            return v;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).layoutId();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding bind = DataBindingUtil.bind(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
        return new ViewHolder<>(bind);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final T model = getItem(position);
        holder.getBinding().setVariable(BR.model, model);
        holder.getBinding().setVariable(BR.callback, clickCallback);
        holder.getBinding().executePendingBindings();
    }
}