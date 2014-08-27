package example.android.favpicts;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class PictListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pict_list);
    }

    @Override
    protected void onResume() {
        super.onResume();

        FavPictsDBHelper helper = new FavPictsDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("picts", new String[]{"_id", "uri", "description"}, null, null, null, null, "_id");

        String[] cols = new String[]{"uri", "description"};
        int[] viewIds = new int[]{R.id.iv_image, R.id.description};

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.pict_row, cursor, cols, viewIds, 0);

        ListView listView = (ListView) findViewById(R.id.picts);
        listView.setEmptyView(findViewById(R.id.empty));
        listView.setAdapter(cursorAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pict_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.add_pict) {
            Intent intent = new Intent(this, AddPictActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
