package com.goda.rssreader.rss.view.ui;


import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.goda.rssreader.MyApplication;
import com.goda.rssreader.R;
import com.goda.rssreader.dagger.providers.DaggerProvidersComponent;
import com.goda.rssreader.dagger.providers.ProvidersComponent;
import com.goda.rssreader.dagger.providers.ProvidersModule;
import com.goda.rssreader.databinding.AppAdapter;
import com.goda.rssreader.databinding.DialogAddProvidersBinding;
import com.goda.rssreader.databinding.FragmentProvidersBinding;
import com.goda.rssreader.rss.model.entities.Provider;
import com.goda.rssreader.rss.view.callback.Navigator;
import com.goda.rssreader.rss.view.callback.ProvidersCallback;
import com.goda.rssreader.rss.viewmodel.ProvidersViewModel;
import com.goda.rssreader.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class ProvidersFragment extends BaseFragment<FragmentProvidersBinding> implements ProvidersCallback {

    @Inject
    ProvidersViewModel viewModel;
    @Inject
    Gson gson;

    private ArrayList<Provider> providerList;
    private AppAdapter<Provider> mAdapter;
    private Navigator navigator;
    private AlertDialog addDialog;

    public ProvidersFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        observeLoading();
        observeErrorMessage();
        getProviders();
    }

    private void observeErrorMessage() {
        viewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String message) {
                if (!TextUtils.isEmpty(message)) {
                    showSnakeBar(message);
                }
            }
        });
    }

    private void observeLoading() {
        viewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isLoading) {
                if (isLoading != null && isLoading) {
                    showLoading();
                } else {
                    hideLoading();
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_providers;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Navigator) {
            navigator = (Navigator) context;
            ProvidersComponent component = DaggerProvidersComponent.builder()
                    .providersModule(new ProvidersModule((MainActivity) context))
                    .applicationComponent(MyApplication.get(context).getComponent())
                    .build();
            component.inject(this);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AuthNavigator");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().setView(this);
        setUpRecyclerView();
        setupAddRssDialog();
    }

    private DiffUtil.ItemCallback<Provider> diffCallback = new DiffUtil.ItemCallback<Provider>() {
        @Override
        public boolean areItemsTheSame(@NonNull Provider oldProvider, @NonNull Provider newProvider) {
            return oldProvider.getRssLink().equals(newProvider.getRssLink());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Provider oldProvider, @NonNull Provider newProvider) {
            return oldProvider.getRssLink().equals(newProvider.getRssLink())
                    && oldProvider.getName().equals(newProvider.getName());
        }
    };

    private void setUpRecyclerView() {
        RecyclerView recyclerView = getViewDataBinding().recyclerViewProviders;
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.setHasFixedSize(true);
        providerList = new ArrayList<>();
        mAdapter = new AppAdapter<>(this, diffCallback);
        recyclerView.setAdapter(mAdapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int pos = viewHolder.getAdapterPosition();
                viewModel.deleteProvider(providerList.get(pos));

            }

        }).attachToRecyclerView(recyclerView);

        getViewDataBinding().swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProviders();
            }
        });
    }

    private void setupAddRssDialog() {
        if (getContext() != null && isAdded()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_add_providers, null);
            DialogAddProvidersBinding addProvidersBinding = DialogAddProvidersBinding.bind(dialogView);
            builder.setView(dialogView);

            final EditText etName = addProvidersBinding.etName;
            final EditText etLink = addProvidersBinding.etLink;
            final Button btnAdd = addProvidersBinding.btnAdd;

            btnAdd.setOnClickListener(view -> {
                String name = etName.getText().toString();
                String link = etLink.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    etName.setError(getString(R.string.required));
                    etName.requestFocus();
                    return;
                }
                if (name.length() < 3) {
                    etName.setError(getString(R.string.invalid));
                    etName.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(link)) {
                    etLink.setError(getString(R.string.required));
                    etLink.requestFocus();
                    return;
                }
                if (!Patterns.WEB_URL.matcher(link).matches()) {
                    etLink.setError(getString(R.string.invalid));
                    etLink.requestFocus();
                    return;
                }
                if (!link.matches("^(http|https)://.*$")) {
                    etLink.setError(getString(R.string._no_http));
                    etLink.requestFocus();
                    return;
                }
                addDialog.dismiss();
                viewModel.addProvider(new Provider(etLink.getText().toString(), etName.getText().toString()));
                etName.setText("");
                etLink.setText("");
                etName.requestFocus();
            });

            addDialog = builder.create();
        }
    }

    Observer<List<Provider>> providersObserver = providers -> {
        if (providers != null) {
            if (getViewDataBinding().swipe.isRefreshing()) {
                getViewDataBinding().swipe.setRefreshing(false);
            }
            providerList.clear();
            providerList.addAll(providers);
            mAdapter.submitList(providers);
        }
    };


    private void getProviders() {
        viewModel.getProviders().observe(this, providersObserver);
    }

    @Override
    public void onDeleteProviderClicked(Provider provider) {
        viewModel.deleteProvider(provider);
    }

    @Override
    public void onProviderClicked(Provider provider) {
        navigator.openArticlesFragment(provider);
    }

    public void addRss() {
        addDialog.show();
    }
}
