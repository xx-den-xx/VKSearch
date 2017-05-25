package ru.bda.vksearch.view.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.api.VKError;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.bda.vksearch.R;
import ru.bda.vksearch.model.SearchAnswer;
import ru.bda.vksearch.model.SearchItem;
import ru.bda.vksearch.model.listeners.EndlessScrollListener;
import ru.bda.vksearch.presenter.MainActivityPresenter;
import ru.bda.vksearch.view.App;
import ru.bda.vksearch.view.MainActivityView;
import ru.bda.vksearch.view.adapters.SearchRecyclerAdapter;

public class MainActivity extends AppCompatActivity implements MainActivityView{

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    private MainActivityPresenter presenter;
    private SearchRecyclerAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<SearchItem> searchItems = new ArrayList<>();
    private String search = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        setupRecyclerView();
        presenter = new MainActivityPresenter(this);
        presenter.startAuthorized(this);
    }

    private void setupRecyclerView() {
        adapter = new SearchRecyclerAdapter(this, searchItems);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                presenter.vkRequest(search, false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchItems = new ArrayList<>();
                search = newText;
                adapter.setSearchList(searchItems);
                presenter.vkRequestFirst(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public void authComplete(VKAccessToken res) {
        App.saveToken(res.accessToken);
        presenter.vkRequestFirst("");
    }

    @Override
    public void authError(VKError error) {
        Snackbar.make(toolbar, error.errorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void startProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void loadRecyclerData(SearchAnswer answer, boolean isLoadNew) {
        if (isLoadNew) {
            searchItems = answer.getResponse().getItems();
        } else {
            searchItems.addAll(answer.getResponse().getItems());
        }
        if(searchItems != null) adapter.setSearchList(searchItems);
    }
}
