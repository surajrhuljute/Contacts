package nareshit.com.contactsprovider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import static android.support.v4.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER;

public class MainActivity extends AppCompatActivity {

    ListView contactsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactsList = (ListView) findViewById(R.id.contactsList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkContactsPermission();
    }

    private void checkContactsPermission() {
        String[] permissions = {Manifest.permission.READ_CONTACTS};

        if(ActivityCompat.checkSelfPermission(this, permissions[0])!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, permissions, 1234);
        }else{
            retrieveContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
            retrieveContacts();
        }else{
            Toast.makeText(this, "Please enable permission", Toast.LENGTH_SHORT).show();
        }
    }

    private void retrieveContacts() {
        Uri contactsUri = ContactsContract.Contacts.CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(contactsUri, null, null, null, null);
        String[] from = {ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME};
        int[] to = {R.id.textId, R.id.textName};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor, from, to, FLAG_REGISTER_CONTENT_OBSERVER);
        contactsList.setAdapter(adapter);
    }
}
