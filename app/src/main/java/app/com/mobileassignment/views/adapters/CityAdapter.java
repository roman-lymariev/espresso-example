package app.com.mobileassignment.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.com.mobileassignment.R;
import app.com.mobileassignment.model.City;

public class CityAdapter extends ArrayAdapter<City> {

    private List<City> cityList;
    private List<City>filteredCityListData;


    public CityAdapter(Context context, List<City> cityList) {
        super(context, 0, cityList);
        this.cityList = cityList;
        this.filteredCityListData = cityList;
    }

    @Override
    public int getCount() {
        return filteredCityListData.size();
    }

    @Override
    public City getItem(int i) {
        return filteredCityListData.get(i) ;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        City city = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.city_row_layout, parent, false);
            holder = new ViewHolder();
            holder.cityNameTextView = ((TextView) convertView.findViewById(R.id.cityName));
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.cityNameTextView.setText(city.getName()+", "+city.getCountry());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String filterString = charSequence.toString().toLowerCase();

                FilterResults results = new FilterResults();

                int count = cityList.size();
                final List<City> resultList = new ArrayList<City>(count);

                for(City filterableCity: cityList){
                    if (filterableCity.getName().toLowerCase().startsWith(filterString)) {
                        resultList.add(filterableCity);
                    }
                }

                results.values = resultList;
                results.count = resultList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredCityListData = (List<City>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public List<City> getCityList() {
        return filteredCityListData;
    }

    private static class ViewHolder {
        TextView cityNameTextView;
    }
}
