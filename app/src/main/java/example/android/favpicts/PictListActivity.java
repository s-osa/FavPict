package example.android.favpicts;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

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

        String[] cols = new String[]{"_id", "uri", "description"};
        int[] viewIds = new int[]{R.id.pict_id, R.id.iv_image, R.id.description};

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.pict_row, cursor, cols, viewIds, 0);

        ListView listView = (ListView) findViewById(R.id.picts);
        listView.setEmptyView(findViewById(R.id.empty));
        listView.setAdapter(cursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView pictIdView = (TextView) view.findViewById(R.id.pict_id);
                String pictId = pictIdView.getText().toString();

                Intent intent = new Intent(PictListActivity.this, EditPictActivity.class);
                intent.putExtra("pictId", pictId);
                startActivity(intent);
            }
        });
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

    public void editPict(View view) {
        Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show();
    }
}
