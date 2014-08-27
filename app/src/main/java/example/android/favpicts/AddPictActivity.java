package example.android.favpicts;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import example.android.favpicts.R;

public class AddPictActivity extends Activity {
    private static final int REQUEST_PICK_PICT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pict);

        Button selectPictButton = (Button) findViewById(R.id.bt_select_pict);
        selectPictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_PICK_PICT);
            }
        });

        Button savePictButton = (Button) findViewById(R.id.bt_save);
        savePictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView imageView = (ImageView) findViewById(R.id.iv_selected_image);
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap image = bitmapDrawable.getBitmap();

                EditText editText = (EditText) findViewById(R.id.et_description);
                String description = editText.getText().toString();

                FavPictsDBHelper helper = new FavPictsDBHelper(AddPictActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();

                String sql = "INSERT INTO picts(image, description) values (?, ?);";
                Object[] elements = new Object[] {image, description};
                db.execSQL(sql, elements);

                int records = getCount(db);

                Toast.makeText(AddPictActivity.this, records + " picts exists.", Toast.LENGTH_SHORT).show();
            }

            private int getCount(SQLiteDatabase db) {
                String sql = "select count(*) from picts;";
                Cursor c = db.rawQuery(sql, null);
                c.moveToLast();
                int count = c.getInt(0);
                c.close();
                return count;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_PICT) {
                try {
                    InputStream stream = getContentResolver().openInputStream(data.getData());
                    Bitmap image = BitmapFactory.decodeStream(stream);
                    stream.close();

                    ImageView imageView = (ImageView) findViewById(R.id.iv_selected_image);
                    imageView.setImageBitmap(image);
                } catch(Exception e) {
                }
            }
        } else {
            Toast.makeText(AddPictActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
        }
    }
}
