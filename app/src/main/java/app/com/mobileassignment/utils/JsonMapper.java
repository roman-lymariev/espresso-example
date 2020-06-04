package app.com.mobileassignment.utils;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import app.com.mobileassignment.model.City;

public class JsonMapper {


    public List<City> getCityListFromRawFile(Context context, int rawFileId){
        List<City> cityList = null;

        ObjectMapper mapper = new ObjectMapper();
        InputStream is = context.getResources().openRawResource(rawFileId);
        try {
            byte[] buffer = new byte[is.available()];
            while (is.read(buffer) != -1);
            String jsontext = new String(buffer);
            cityList = mapper.readValue(jsontext, mapper.getTypeFactory().constructCollectionType(List.class, City.class));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return cityList;
    }
}
