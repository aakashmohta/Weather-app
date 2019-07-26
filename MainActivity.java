package com.example.locs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
public class MainActivity extends AppCompatActivity implements LocationListener {
    ProgressDialog dialog;
    LocationManager locationManager;
    static Double lati, longi;
    AutoCompleteTextView actv;
    ImageView img,img2;
    Button b1;
    String[] cities = {"Kolkata","Ghana","New Delhi", "Mumbai", "Chennai", "New York", "New Jersey", "Toronto", "Bhubaneswar","Buenos Aires","Yerevan","Canberra","Vienna","Baku",
            "Kabul", "Tirana", "Algiers", "Andorra la Vella", "Luanda", "Saint John's", "Dhaka", "Thimphu", "Brasilia","Nassau","Manama","Bridgetown","Minsk",
            "Ottawa","Brussels","Belmopan","Porto-Novo","Thimphu","La Paz","Sucre","Sarajevo","Gaborone","Brasilia","Bandar Seri Begawan","Sofia","Ouagadougou",
            "Gitega","Bujumbura","Phnom Penh","Yaounde","Praia","Bangui","N'Djamena","Santiago","Beijing","Bogota","Brazzaville","Kinshasa","San Jose","Yamoussoukro","Abidjan"
    ,"Zagreb","Havana","Nicosia","Prague","Copenhagen","Djibouti","Roseau","Santo Domingo","Dili","Quito","Cairo","San Salvador","Malabo","Asmara","Tallinn","Addis Ababa",
    "Suva","Helsinki","Paris","Libreville","Banjul","Tbilisi","Berlin","Accra","Athens","Saint George's","Guatemala City","Conakry","Bissau","Georgetown","Port-au-Prince","Tegucigalpa",
    "Budapest","Reykjavik","Jakarta","Tehran","Baghdad","Dublin","Jerusalem","Rome","Kingston","Tokyo","Amman","Nairobi","Astana","Tarawa Atoll","Pyongyang",
    "Seoul","Pristina","Kuwait City","Bishkek","Vientiane","Riga","Beirut","Maseru","Monrovia","Tripoli","Vaduz","Vilnius","Luxembourg","Skopje","Antananarivo",
    "Lilongwe","Kuala Lumpur","Male","Bamako","Valleta","Majuro","Nouakchott","Port Louis","Mexico City","Palikir","Chisinau","Monaco","Ulaanbaatar","Podgorica",
    "Rabat","Maputo","Rangoon","Yangon","Naypyidaw","Windhoek","Kathmandu","Amsterdam","Wellington","Managua","Niamey","Abuja","Oslo","Muscat","Islamabad","Melekeok",
    "Panama City","Port Moresby","Asuncion","Lima","Manila","Warsaw","Lisbon","Doha","Moscow","Bucharest","Kigali","Basseterre","Kingstown","Castries","Apia",
    "San Marino","San Tome","Riyadh","Dakar","Victoria","Belgrade","Freetown","Singapore","Bratislava","Ljubljana","Honiara","Mogadishu","Juba","Pretoria","Cape Town",
    "Bloemfontein","Madrid","Colombo","Khartoum","Paramaribo","Mbabane","Bern","Stockholm","Damascus","Taipei","Dushanbe","Dar es Salaam","Vadodara","Meerut","Faridabad",
    "Dodoma","Bangkok","Lome","Nuku'alofa","Port-of-Spain","Tunis","Ankara","Ashgabat","Ashgabat","Kyiv","Ashgabat","London","Washington D.C.","Montevideo","Tashkent",
    "Port-Vila","Vatican City","Caracas","Hanoi","Sanaa","Lusaka","Harare","Jammu","Kanpur","Lucknow","Gangtok","Jaipur","Jamshedpur","Jaiselmer","Bikaner",
    "Chandigarh","Nagpur","Bhopal","Birmingham","Manchester","Belfast","Glasgow","Leeds","Liverpool","Newcastle","Sheffield","Bristol","Nottingham","Leicester",
    "Chicago","Houston","San Antonio","San Diego","Kanpur","Ranchi","Sydney","Melbourne","Canberra","Perth","Lyon","Nice","MARSEILLES","Nantes","Strasbourg",
    "Paro","Jakar","Calgary","Vancouver","Pune","Puri","Saint Petersburg","Kazan","Samara","Nizhny Novgorod","Yekaterinburg","Novosibirsk","Omsk","Sochi","Ufa",
    "Abidjan","Alexandria","East London","Durban","Johannesburg","Port Elizabeth","Cape Town","Sao Paolo","Buenos Aires","Varanasi","Kochi","Surat","Raipur","Coimbatore"
    ,"Hyderabad","Namur","Leh","Shillong","Guwahati","Itanagar","Dispur","Patna","Raipur","Panaji","Gandhinagar","Gurgaon","Nainital","Agra","Amritsar","Dwaraka",
    "Shimla","Thiruvananthpuram","Imphal","Aizwal","Kohima","Chandigarh","Agartala","Dehradun","Puducherry","Sweden","Amravati","Aurangabad","Udaipur","Alwar",
    "Cuttack","Dalhousie","Gaya","Indore","Manali","Ooty","Portblair","Pushkar","Srinagar","Bhilai","Rourkela","Ajmer","Siliguri","Jhansi","Tirupati","Kharagput"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
        b1=findViewById(R.id.button);
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) )
            buildAlertMessageNoGps();
        if(!isNetworkAvailable())
            buildAlertMessageNoInternet();
        actv = findViewById(R.id.autoCompleteTextView1);
        img=findViewById(R.id.imageView16);
        img2=findViewById(R.id.imageView17);
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1, cities);
        actv.setAdapter(adapter);
        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);
                getLocation();
                new NewWork().execute();
                actv.setText("");
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                if(!isNetworkAvailable())
                    buildAlertMessageNoInternet();
                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) )
                    buildAlertMessageNoGps();
                getLocation();
                new NewWork().execute();
                actv.setText("");
            }
        });
        findViewById(R.id.linearLayout).setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
                return true;
            }
        });
        ImageView queryButton = findViewById(R.id.queryButton);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        getLocation();
        new Work().execute();

        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNetworkAvailable())
                    buildAlertMessageNoInternet();
                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) )
                    buildAlertMessageNoGps();
                getLocation();
                new Work().execute();
            }
        });
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    private void buildAlertMessageNoInternet() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Internet seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    void getLocation() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching Data...");
        dialog.show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = LocationManager.NETWORK_PROVIDER;
        Location loc1 = locationManager.getLastKnownLocation(provider);
        lati = loc1.getLatitude();
        longi = loc1.getLongitude();
        try { locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        lati = location.getLatitude();
        longi = location.getLongitude();
    }
    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    @Override
    public void onProviderEnabled(String provider) {
    }
    private class Work extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat=" + lati + "&lon=" + longi + "&APPID=a0273aa74a1cc045d349502b47def3af");//, Double.toString(lat), Double.toString(lon));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    connection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }
        protected void onPostExecute(String response) {
            JSONObject one;
            Date currentTime = Calendar.getInstance().getTime();
            try {
                one = new JSONObject(response);
                JSONObject sys = one.getJSONObject("sys");
                JSONObject main = one.getJSONObject("main");
                JSONObject wind=one.getJSONObject("wind");
                JSONObject ll=one.getJSONObject("coord");
                JSONArray ja=one.getJSONArray("weather");
                JSONObject jas = ja.getJSONObject(0);
                String desc=jas.getString("main");
                String country = sys.getString("country");
                String weasd=jas.getString("description");
                Long sunr = sys.getLong("sunrise");
                sunr=sunr*1000;
                Time sunris=new Time(sunr);
                String sunrise=sunris.toString();
                Long suns = sys.getLong("sunset");
                suns=suns*1000;
                Time sunse=new Time(suns);
                String sunset=sunse.toString();
                int tem = main.getInt("temp");
                tem=tem-273;
                String name = one.getString("name");
                String humidity=main.getString("humidity");
                String ws=wind.getString("speed");
                Long dates=one.getLong("dt");
                dates=dates*1000;
                java.sql.Date da=new java.sql.Date(dates);
                String dated=da.toString();
                String lat=ll.getString("lat");
                String lon=ll.getString("lon");
                String p=main.getString("pressure");
                //if(currentTime.after(sunris)&&currentTime.before(sunse))
                  //  img2.setImageResource(R.drawable.day);
                //else
                  //  img2.setImageResource(R.drawable.night);
                doing(country,weasd,sunrise,sunset,tem,name,humidity,ws,dated,desc,lat,lon,p);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        void doing(String country,String weatherdesc,String sunrise,String sunset,int temp,String name,String humidity,String ws,String dated,String wea,String lat,String lon,String p) {
            ConstraintLayout layout=findViewById(R.id.linearLayout);
            String temp1= String.valueOf(temp)+(char) 0x00B0+"C";
            humidity=humidity+"%";
            if(wea.equalsIgnoreCase("Thunderstorm"))
                img.setImageResource(R.drawable.thunder);
            if(wea.equalsIgnoreCase("haze"))
                img.setImageResource(R.drawable.haze);
            if(wea.equalsIgnoreCase("Drizzle"))
                img.setImageResource(R.drawable.rain);
            if(wea.equalsIgnoreCase("Rain"))
                img.setImageResource(R.drawable.rain);
            if(wea.equalsIgnoreCase("Snow"))
                img.setImageResource(R.drawable.snow);
            if(wea.equalsIgnoreCase("Clear"))
                img.setImageResource(R.drawable.sunny);
            if(wea.equalsIgnoreCase("Clouds")&&weatherdesc.equalsIgnoreCase("few clouds"))
                img.setImageResource(R.drawable.cloudy);
            if(wea.equalsIgnoreCase("Clouds"))
                img.setImageResource(R.drawable.cloudy);
            if(wea.equalsIgnoreCase("Mist"))
                img.setImageResource(R.drawable.mist);
            if(wea.equalsIgnoreCase("Smoke"))
                img.setImageResource(R.drawable.haze);
            if(wea.equalsIgnoreCase("fog"))
                img.setImageResource(R.drawable.mist);
            if(wea.equalsIgnoreCase("dust"))
                img.setImageResource(R.drawable.haze);
            TextView t1=findViewById(R.id.temp2);
            TextView t2=findViewById(R.id.pressure2);
            TextView t3=findViewById(R.id.sunrise2);
            TextView t4=findViewById(R.id.sunset2);
            TextView t5=findViewById(R.id.date2);
            TextView t7=findViewById(R.id.humidity2);
            TextView t8=findViewById(R.id.city2);
            TextView t6=findViewById(R.id.weather);
            TextView t9=findViewById(R.id.country2);
            TextView t10=findViewById(R.id.latitude2);
            TextView t11=findViewById(R.id.longitude2);
            TextView t12=findViewById(R.id.windspeed2);
            String weas= wea.toUpperCase();
            t9.setText(String.format("(%s)", country));
            t3.setText(sunrise);
            t4.setText(sunset);
            t1.setText(temp1);
            t8.setText(name);
            t7.setText(humidity);
            t12.setText(ws);
            t10.setText(lat);
            t11.setText(lon);
            t6.setText(wea);
            t2.setText(p);
            t5.setText(dated);
            dialog.dismiss();
        }
    }
    private class NewWork extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(String... urls) {
            Editable city= actv.getText();
            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&APPID=a0273aa74a1cc045d349502b47def3af");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    connection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }
        protected void onPostExecute(String response) {
            JSONObject one;
            Date currentTime = Calendar.getInstance().getTime();
            try {
                one = new JSONObject(response);
                JSONObject sys = one.getJSONObject("sys");
                JSONObject main = one.getJSONObject("main");
                JSONObject wind=one.getJSONObject("wind");
                JSONObject ll=one.getJSONObject("coord");
                JSONArray ja=one.getJSONArray("weather");
                JSONObject jas = ja.getJSONObject(0);
                String desc=jas.getString("main");
                String weasd=jas.getString("description");
                String country = sys.getString("country");
                Long sunr = sys.getLong("sunrise");
                sunr=sunr*1000;
                Time sunris=new Time(sunr);
                String sunrise=sunris.toString();
                Long suns = sys.getLong("sunset");
                suns=suns*1000;
                Time sunse=new Time(suns);
                String sunset=sunse.toString();
                int tem = main.getInt("temp");
                tem=tem-273;
                String temp=Double.toString(tem);
                String name = one.getString("name");
                String humidity=main.getString("humidity");
                String ws=wind.getString("speed");
                Long dates=one.getLong("dt");
                dates=dates*1000;
                java.sql.Date da=new java.sql.Date(dates);
                String dated=da.toString();
                String lat=ll.getString("lat");
                String lon=ll.getString("lon");
                String p=main.getString("pressure");
                //if(currentTime.after(sunris)&&currentTime.before(sunse))
                    //img2.setImageResource(R.drawable.day);
                //else
                    //img2.setImageResource(R.drawable.night);
                doing(country,weasd,sunrise,sunset,temp,name,humidity,ws,dated,desc,lat,lon,p);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        void doing(String country,String weatherdesc,String sunrise,String sunset,String temp,String name,String humidity,String ws,String dated,String wea,String lat,String lon,String p) {
            ConstraintLayout layout=findViewById(R.id.linearLayout);
            String temp1= String.valueOf(temp)+(char) 0x00B0+"C";
            humidity=humidity+"%";
            if(wea.equalsIgnoreCase("Thunderstorm"))
                img.setImageResource(R.drawable.thunder);
            if(wea.equalsIgnoreCase("haze"))
                img.setImageResource(R.drawable.haze);
            if(wea.equalsIgnoreCase("Drizzle"))
                img.setImageResource(R.drawable.rain);
            if(wea.equalsIgnoreCase("Rain"))
                img.setImageResource(R.drawable.rain);
            if(wea.equalsIgnoreCase("Snow"))
                img.setImageResource(R.drawable.snow);
            if(wea.equalsIgnoreCase("Clear"))
            img.setImageResource(R.drawable.sunny);
            if(wea.equalsIgnoreCase("Clouds")&&weatherdesc.equalsIgnoreCase("few clouds"))
            img.setImageResource(R.drawable.cloudy);
            if(wea.equalsIgnoreCase("Clouds"))
                img.setImageResource(R.drawable.cloudy);
            if(wea.equalsIgnoreCase("Mist"))
                img.setImageResource(R.drawable.mist);
            if(wea.equalsIgnoreCase("Smoke"))
                img.setImageResource(R.drawable.haze);
            if(wea.equalsIgnoreCase("fog"))
                img.setImageResource(R.drawable.mist);
            if(wea.equalsIgnoreCase("dust"))
                img.setImageResource(R.drawable.haze);
            TextView t1=findViewById(R.id.temp2);
            TextView t6=findViewById(R.id.weather);
            TextView t2=findViewById(R.id.pressure2);
            TextView t3=findViewById(R.id.sunrise2);
            TextView t4=findViewById(R.id.sunset2);
            TextView t5=findViewById(R.id.date2);
            TextView t7=findViewById(R.id.humidity2);
            TextView t8=findViewById(R.id.city2);
            TextView t9=findViewById(R.id.country2);
            TextView t10=findViewById(R.id.latitude2);
            TextView t11=findViewById(R.id.longitude2);
            TextView t12=findViewById(R.id.windspeed2);
            String weas= wea.toUpperCase();
            t9.setText(String.format("(%s)", country));
            t3.setText(sunrise);
            t4.setText(sunset);
            t1.setText(temp1);
            t8.setText(name);
            t7.setText(humidity);
            t12.setText(ws);
            t10.setText(lat);
            t11.setText(lon);
            t6.setText(weas);
            t2.setText(p);
            t5.setText(dated);
            dialog.dismiss();
        }
    }
}