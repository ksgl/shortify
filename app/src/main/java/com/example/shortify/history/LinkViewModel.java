package com.example.shortify.history;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.os.AsyncTask;

import com.example.shortify.database.LinkDatabase;
import com.example.shortify.database.LinkModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LinkViewModel extends AndroidViewModel {

    ExecutorService service = Executors.newFixedThreadPool(1);

    private LiveData<List<LinkModel>> linkList;
    private LiveData<List<LinkModel>> favouritesList;
    private LinkDatabase linkDatabase;

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

    public void switchToFavorite() {
        toShow.removeSource(linkList);
        toShow.removeSource(favouritesList);
        toShow.addSource(favouritesList, linkModels -> toShow.postValue(linkModels));
    }

    public void removeAll() {
        new deleteAsyncTask(linkDatabase).execute(linkList.getValue());
    }

    private static class deleteAsyncTask extends AsyncTask<List<LinkModel>, Void, Void> {
        private LinkDatabase db;

        deleteAsyncTask(LinkDatabase linkDatabase) {
            db = linkDatabase;
        }

        @Override
        protected Void doInBackground(final List<LinkModel>... params) {
                db.linkModel().deleteAll();
            return null;
        }
    }

    public void add(final LinkModel link) {
        new addAsyncTask(linkDatabase).execute(link);
    }

    private static class addAsyncTask extends AsyncTask<LinkModel, Void, Void> {
        private LinkDatabase db;
        addAsyncTask(LinkDatabase linkDatabase) {
            db = linkDatabase;
        }

        @Override
        protected Void doInBackground(final LinkModel... params) {
            db.linkModel().addLink(params[0]);
            return null;
        }
    }


    public void changeStarred(final LinkModel link) {
        service.submit(() -> linkDatabase.linkModel().updateStarred(link));
//        new changeStarredAsyncTask(linkDatabase).execute(link);
    }
}