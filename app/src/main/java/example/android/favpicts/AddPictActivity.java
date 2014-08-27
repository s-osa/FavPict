package example.android.favpicts;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.InputStream;

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
                TextView textView = (TextView) findViewById(R.id.tv_uri);
                String uri = textView.getText().toString();

                EditText editText = (EditText) findViewById(R.id.et_description);
                String description = editText.getText().toString();

                FavPictsDBHelper helper = new FavPictsDBHelper(AddPictActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();

                db.execSQL("INSERT INTO picts(uri, description) values (?, ?);", new Object[] {uri, description});

                Toast.makeText(AddPictActivity.this, getCount(db) + " picts exists.", Toast.LENGTH_SHORT).show();
                finish();
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
                    imageView.setVisibility(View.VISIBLE);

                    TextView textView = (TextView) findViewById(R.id.tv_uri);
                    textView.setText(data.getData().toString());

                    Button saveButton = (Button) findViewById(R.id.bt_save);
                    saveButton.setEnabled(true);
                } catch(Exception e) {
                }
            }
        } else {
            Toast.makeText(AddPictActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
        }
    }
}
