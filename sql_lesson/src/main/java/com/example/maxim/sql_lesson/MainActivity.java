package com.example.maxim.sql_lesson;

//package com.example.anton.lesson_itea_adventure_sql;

        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.app.DialogFragment;
        import android.app.FragmentManager;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;

public class MainActivity extends AppCompatActivity {

    String APP_LOG = "SQL_LOG";
    static SQLiteDatabase mDB;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sql_study_layout);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bOpenDB:
                openDBTest();
                break;
            case R.id.bCreateTable:
                createTableTest();
                break;
            case R.id.bDropTable:
                dropTableTest();
                break;
            case R.id.bInsert:
                insertTest();
                break;
            case R.id.bDelete:
                deleteTest();
                break;
            case R.id.bUpdate:
                updateTest();
                break;
            case R.id.bSelect:
                selectTest();
                break;
        }
    }

    private void openDBTest() {

        FragmentManager fm = getFragmentManager();

        StandartDialogFragment sdf = new StandartDialogFragment();

        sdf.show(fm, "dialog");
    }

    private void createTableTest() {
        String createTablePersonsStmt = "CREATE TABLE IF NOT EXISTS persons " +
                "( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "_last_name TEXT NOT NULL, " +
                "_bdate INTEGER " +
                ");";

        String createTableCitiesStmt = "CREATE TABLE IF NOT EXISTS cities " +
                "( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "_name TEXT NOT NULL, " +
                "_is_capital INTEGER " +
                ");";

        Log.d("q", createTablePersonsStmt);

        mDB.execSQL(createTablePersonsStmt);
        mDB.execSQL(createTableCitiesStmt);
    }

    private void dropTableTest() {
        String dropTablePersonStmt = "DROP TABLE persons;";
        String dropTableCityStmt = "DROP TABLE cities;";

        mDB.execSQL(dropTablePersonStmt);
        mDB.execSQL(dropTableCityStmt);
    }

    private void insertTest() {

        String insertPersonsStmt = "INSERT INTO persons(_last_name, _bdate) VALUES ('Fargo', 8888);";
        String insertCitiesStmt = "INSERT INTO cities(_name, _is_capital) VALUES ('Lester', 7777);";

        mDB.execSQL(insertPersonsStmt);
        mDB.execSQL(insertCitiesStmt);
    }

    private void deleteTest() {

        String deletePersonsStmt = "DELETE FROM persons WHERE _last_name = 'Ivan' AND _bdate = 3333;";
        String deleteCitiesStmt = "DELETE FROM cities WHERE _name = 'Petr' AND _is_capital = 5555;";

        mDB.execSQL(deletePersonsStmt);
        mDB.execSQL(deleteCitiesStmt);
    }

    private void updateTest() {
        String updatePersonsStmt = "UPDATE persons SET _last_name = 'Ivan', _bdate = 3333;";
        String updateCitiesStmt = "UPDATE cities SET _name = 'Petr', _is_capital = 5555;";

        mDB.execSQL(updatePersonsStmt);
        mDB.execSQL(updateCitiesStmt);
    }

    private void selectTest() {

        String selectStmt = "SELECT * FROM persons;";

        Cursor c = mDB.rawQuery(selectStmt, null);

        int columns = c.getColumnCount();
        int count = c.getCount();
        String[] columnNames = c.getColumnNames();

        StringBuilder sb = new StringBuilder(/* selectStmt + ":\r\n" */);
        sb.append("Записей : " + count + ";\n");
        sb.append("Колонок : " + columns + ";\n");
        sb.append("Колонки :");

        for (String colName : columnNames)
            sb.append(" | " + colName);
        sb.append(" |\n");

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {

                for (int i = 0; i < columns; i++) {
                    sb.append(c.getString(i) + "  :  ");
                }

                sb.append("\n");

                c.moveToNext();
            }
        }
        c.close();

        Log.d(APP_LOG, sb.toString());
    }

    public static class StandartDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

        final CharSequence[] items = {" SQLiteOpenHelper "," openOrCreate "};
        AlertDialog alert;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            alert = new AlertDialog.Builder(getActivity())
                    .setTitle("Выберите способ открытия базы данных:")
                    .setSingleChoiceItems(items, -1, this)
                    .create();
            return alert;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {

            switch(which){
                case 0:
                    mDB = MyDBHelper.getInstance(getActivity().getApplicationContext(),MyDBHelper.DB_NAME).getWritableDatabase();
                    break;
                case 1:
                    mDB = getActivity().openOrCreateDatabase(MyDBHelper.DB_NAME, MODE_PRIVATE, null);
                    break;
            }
            alert.dismiss();
        }
    }

    public static class MyDBHelper extends SQLiteOpenHelper {
        public static final String DB_NAME = "sql_test.db";

        private static MyDBHelper myDBHelper;

        public static MyDBHelper getInstance(Context context, String dbName){
            if(myDBHelper == null)
                myDBHelper = new MyDBHelper(context, dbName);

            return myDBHelper;
        }

        public static MyDBHelper getInstance(Context context){
            return getInstance(context, DB_NAME);
        }

        private MyDBHelper(Context context) {
            super(context, DB_NAME, null, 1);
        }

        private MyDBHelper(Context context, String dbName) {
            super(context, dbName, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {}

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}
    }
}
