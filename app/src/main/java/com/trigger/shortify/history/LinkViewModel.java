package com.trigger.shortify.history;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.trigger.shortify.database.LinkDatabase;
import com.trigger.shortify.database.LinkModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LinkViewModel extends AndroidViewModel {

    ExecutorService service = Executors.newFixedThreadPool(1);

    private LinkDatabase linkDatabase;

    private LiveData<List<LinkModel>> linkList;
    private LiveData<List<LinkModel>> favouritesList;

    public LinkViewModel(Application application) {
        super(application);

        linkDatabase = LinkDatabase.getDatabase(this.getApplication());
        linkList = linkDatabase.linkModel().getAll();
        favouritesList = linkDatabase.linkModel().getFavourites();
    }

    public LiveData<List<LinkModel>> getLinkList() {
        return this.linkList;
    }

    public LiveData<List<LinkModel>> getFavouritesList() {
        return this.favouritesList;
    }

    public void removeAll() {
        service.submit(() -> linkDatabase.linkModel().deleteAll());
    }

    public void add(final LinkModel link) {
        service.submit(() -> linkDatabase.linkModel().addLink(link));
    }

    public void changeStarred(final LinkModel link) {
        service.submit(() -> linkDatabase.linkModel().updateStarred(link));
    }
}