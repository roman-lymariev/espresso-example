package app.com.mobileassignment.views;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.Collections;
import java.util.List;

import app.com.mobileassignment.views.adapters.CityAdapter;
import app.com.mobileassignment.R;
import app.com.mobileassignment.model.City;
import app.com.mobileassignment.utils.JsonMapper;

public class MainActivity extends AppCompatActivity implements TextWatcher, AdapterView.OnItemClickListener {

    public static final String COORDINATES_LAT = "COORDINATES_LAT";
    public static final String COORDINATES_LON = "COORDINATES_LON";

    ListView citiesListView;
    EditText search;
    CityAdapter adapter;
    ProgressBar progressBar;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (LinearLayout) findViewById(R.id.results);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        citiesListView = (ListView) findViewById(R.id.citiesList);
        citiesListView.setOnItemClickListener(this);

        search = (EditText) findViewById(R.id.search);
        search.addTextChangedListener(this);

        new LoadCitiesTask().execute();

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //Nothing Here
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        adapter.getFilter().filter(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        //Nothing Here
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        City city = adapter.getItem(i);

        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(COORDINATES_LAT, city.getCoord().getLat());
        intent.putExtra(COORDINATES_LON, city.getCoord().getLon());
        startActivity(intent);
    }

    private class LoadCitiesTask extends AsyncTask<Void, Void, List<City>> {

        @Override
        protected void onPostExecute(List<City> cities) {
            super.onPostExecute(cities);
            Collections.sort(cities);

            adapter = new CityAdapter(MainActivity. this, cities);
            citiesListView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<City> doInBackground(Void... voids) {
            return new JsonMapper().getCityListFromRawFile(MainActivity.this, R.raw.cities);
        }
    }


}
