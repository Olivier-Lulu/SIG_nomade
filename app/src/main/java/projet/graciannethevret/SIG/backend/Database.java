package projet.graciannethevret.SIG.backend;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    private static final String TABLE_MARKER = "table_marker";
    private static final String COL_ID = "id";
    private static final String COL_LON = "lon";
    private static final String COL_LAT = "lat";
    private static final String COL_NOM = "nom";
    private static final String COL_TAG = "tag";

    private static final String CREATE_BDD = "CREATE TABLE " + TABLE_MARKER + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_LON + " REAL NOT NULL, "
            + COL_LAT + " REAL NOT NULL, " + COL_NOM + " TEXT, " + COL_TAG + " TEXT );";

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //On peut faire ce qu'on veut ici, autant reinitialiser la table dans notre cas, ça ira
        db.execSQL("DROP TABLE " + TABLE_MARKER + ";");
        onCreate(db);
    }


}
