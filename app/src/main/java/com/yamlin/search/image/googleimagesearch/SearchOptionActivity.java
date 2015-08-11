package com.yamlin.search.image.googleimagesearch;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import java.io.BufferedInputStream;


public class SearchOptionActivity extends Activity implements View.OnClickListener {
    public static final String TAG = "SearchOptionActivity";

    private Spinner mSpinnerColor;
    private Spinner mSpinnerType;
    private Spinner mSpinnerSize;
    private Button mBtnSave;
    private Button mBtnCancel;
    private EditText mEditSite;
    private SharedPreferences mSettings;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_option);

        mSpinnerColor = (Spinner) findViewById(R.id.spannerColor);
        mSpinnerType = (Spinner) findViewById(R.id.spinnerType);
        mSpinnerSize = (Spinner) findViewById(R.id.spannerSize);
        mBtnSave = (Button) findViewById(R.id.btnSave);
        mBtnCancel = (Button)  findViewById(R.id.btnCancel);

        mBtnSave.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
        mEditSite = (EditText) findViewById(R.id.etSearchSite);

        setupSpanner();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void setupSpanner() {

        int index;
        mSettings = getSharedPreferences(MainActivity.PREFERENCE_NAME, MODE_PRIVATE);


        ArrayAdapter<CharSequence> adapterColor = ArrayAdapter.createFromResource(this,
                R.array.color_array, android.R.layout.simple_spinner_item);
        adapterColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerColor.setAdapter(adapterColor);
        index = adapterColor.getPosition(mSettings.getString("color", ""));
        if (index != -1) {
            mSpinnerColor.setSelection(index);
        }

        ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(this,
                R.array.type_array, android.R.layout.simple_spinner_item);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerType.setAdapter(adapterType);
        index = adapterType.getPosition(mSettings.getString("type", ""));
        if (index != -1) {
            mSpinnerType.setSelection(index);
        }


        ArrayAdapter<CharSequence> adapterSize = ArrayAdapter.createFromResource(this,
                R.array.size_array, android.R.layout.simple_spinner_item);
        adapterSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerSize.setAdapter(adapterSize);
        index = adapterSize.getPosition(mSettings.getString("size", ""));
        if (index != -1) {
            mSpinnerSize.setSelection(index);
        }


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
        case R.id.btnSave:
            // save the setting to preference
            SharedPreferences settings =
                    getSharedPreferences(MainActivity.PREFERENCE_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("imgcolor", mSpinnerColor.getSelectedItem().toString());
            editor.putString("imgsz", mSpinnerSize.getSelectedItem().toString());
            editor.putString("imgtype", mSpinnerType.getSelectedItem().toString());
            editor.putString("as_sitesearch", mEditSite.getText().toString());
            editor.commit();
            setResult(RESULT_OK);
            finish();
            break;
        case R.id.btnCancel:
            setResult(RESULT_CANCELED);
            finish();
            break;
        default:
        }
    }
}
