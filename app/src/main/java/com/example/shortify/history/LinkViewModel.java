package com.example.shortify.history;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.shortify.database.LinkDatabase;
import com.example.shortify.database.LinkModel;

import java.util.List;

public class LinkViewModel extends AndroidViewModel {

    private LiveData<List<LinkModel>> linkList;
    private LinkDatabase linkDatabase;

    public LinkViewModel(Application application) {
        super(application);

        linkDatabase = LinkDatabase.getDatabase(this.getApplication());
        linkList = linkDatabase.linkModel().getAll();
    }

    public LiveData<List<LinkModel>> getLinkList() {
        return linkList;
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
        new changeStarredAsyncTask(linkDatabase).execute(link);
    }


    private static class changeStarredAsyncTask extends AsyncTask<LinkModel, Void, Void> {
        private LinkDatabase db;
        changeStarredAsyncTask(LinkDatabase linkDatabase) {
            db = linkDatabase;
        }

        @Override
        protected Void doInBackground(final LinkModel... params) {
            db.linkModel().updateStarred(params[0]);
            return null;
        }
    }
}