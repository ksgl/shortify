import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {LinkModel.class}, version = 1)
public abstract class LinkDatabase extends RoomDatabase {

    private static LinkDatabase INSTANCE;

    public static LinkDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), LinkDatabase.class, "link_db")
                            .build();
        }
        return INSTANCE;
    }

    public abstract LinkDAO linkModel();
}