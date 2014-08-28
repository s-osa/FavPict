package example.android.favpicts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

        Button saveButton = (Button) findViewById(R.id.bt_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editedText = (EditText) findViewById(R.id.et_description);
                String newDescription = editedText.getText().toString();

                TextView pictIdText = (TextView) findViewById(R.id.pict_id);
                String pictId = pictIdText.getText().toString();

                ContentValues contentValues = new ContentValues();
                contentValues.put("description", newDescription);

                FavPictsDBHelper helper = new FavPictsDBHelper(EditPictActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();

                db.update("picts", contentValues, "_id = ?", new String[]{pictId});

                Toast.makeText(EditPictActivity.this, "Saved", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(EditPictActivity.this, PictListActivity.class);
                startActivity(intent);
            }
        });

        Button deleteButton = (Button) findViewById(R.id.bt_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(EditPictActivity.this);

                TextView pictIdText = (TextView) findViewById(R.id.pict_id);
                final String pictId = pictIdText.getText().toString();

                dialog.setTitle("Confirmation");
                dialog.setMessage("Are you sure to delete this pict?");

                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FavPictsDBHelper helper = new FavPictsDBHelper(EditPictActivity.this);
                        SQLiteDatabase db = helper.getWritableDatabase();

                        db.delete("picts", "_id = ?", new String[]{pictId});

                        Toast.makeText(EditPictActivity.this, "Deleted", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(EditPictActivity.this, PictListActivity.class);
                        startActivity(intent);
                    }
                });

                dialog.setNegativeButton("Do not delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                dialog.show();
            }
        });
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
