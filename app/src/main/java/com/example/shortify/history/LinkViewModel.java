package com.example.shortify.history;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.example.shortify.database.LinkDatabase;
import com.example.shortify.database.LinkModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LinkViewModel extends AndroidViewModel {

    ExecutorService service = Executors.newFixedThreadPool(1);

    private LinkDatabase linkDatabase;

    private LiveData<List<LinkModel>> linkList;
    private LiveData<List<LinkModel>> favouritesList;

    public MediatorLiveData<List<LinkModel>> toShow;

    public LinkViewModel(Application application) {
        super(application);

        linkDatabase = LinkDatabase.getDatabase(this.getApplication());
        linkList = linkDatabase.linkModel().getAll();
        favouritesList = linkDatabase.linkModel().getFavourites();
        toShow = new MediatorLiveData<>();
        toShow.addSource(linkList, linkModels -> toShow.postValue(linkModels));
        toShow.setValue(new ArrayList<>());
    }

    public LiveData<List<LinkModel>> getLinkList() {
        return this.linkList;
    }

    public LiveData<List<LinkModel>> getFavouritesList() {
        return this.favouritesList;
    }

    public LiveData<List<LinkModel>> getToShow() {
        return toShow;
    }

    public void switchToFavorites() {
        toShow.removeSource(linkList);
        toShow.removeSource(favouritesList);
        toShow.addSource(favouritesList, linkModels -> toShow.postValue(linkModels));
    }

    public void switchToAll() {
        toShow.removeSource(linkList);
        toShow.removeSource(favouritesList);
        toShow.addSource(linkList, linkModels -> toShow.postValue(linkModels));
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