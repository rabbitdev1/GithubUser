import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "UserDatabase.db"
        const val TABLE_NAME = "user"
        const val COLUMN_ID = "id"
        const val COLUMN_LOGIN = "login"
        const val COLUMN_AVATAR_URL = "avatar_url"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        try {
            val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY, " +
                    "$COLUMN_LOGIN TEXT, " +
                    "$COLUMN_AVATAR_URL TEXT)"
            db?.execSQL(CREATE_TABLE)
        } catch (e: Exception) {
            Log.e("DBHelper", "Error creating table", e)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        try {
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        } catch (e: Exception) {
            Log.e("DBHelper", "Error upgrading table", e)
        }
    }
}
