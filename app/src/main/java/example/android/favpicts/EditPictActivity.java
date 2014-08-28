package example.android.favpicts;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EditPictActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pict);

        Intent intent = getIntent();
        String pictId = intent.getStringExtra("pictId");

        FavPictsDBHelper helper = new FavPictsDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(
                "picts",
                new String[]{"_id", "uri", "description"},
                "_id = ?",
                new String[]{pictId},
                null,
                null,
                null
        );
        cursor.moveToFirst();

        Uri uri = Uri.parse(cursor.getString(1));
        String description = cursor.getString(2);

        TextView pictIdView = (TextView) findViewById(R.id.pict_id);
        pictIdView.setText(pictId);

        ImageView imageView = (ImageView) findViewById(R.id.iv_image);
        imageView.setImageURI(uri);

        EditText editText = (EditText) findViewById(R.id.et_description);
        editText.setText(description);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_pict, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
