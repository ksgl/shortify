import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class LinkViewModel extends AndroidViewModel {

    private final LiveData<List<LinkModel>> linkList;

    private LinkDatabase linkDatabase;

    public LinkViewModel(Application application) {
        super(application);

        linkDatabase = LinkDatabase.getDatabase(this.getApplication());

        linkList = linkDatabase.linkModel().getAll();
    }


    public LiveData<List<LinkModel>> getLinkList() {
        return linkList;
    }

    public void removeLink(LinkModel link) {
        new deleteAsyncTask(linkDatabase).execute(link);
    }

    private static class deleteAsyncTask extends AsyncTask<LinkModel, Void, Void> {

        private LinkDatabase db;

        deleteAsyncTask(LinkDatabase linkDatabase) {
            db = linkDatabase;
        }

        @Override
        protected Void doInBackground(final LinkModel... params) {
            db.linkModel().deleteLink(params[0]);
            return null;
        }

    }
}