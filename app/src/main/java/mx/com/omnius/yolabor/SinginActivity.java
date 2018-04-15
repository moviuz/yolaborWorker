package mx.com.omnius.yolabor;

import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v13.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import mx.com.omnius.yolabor.Adapter.PlaceAutocompleteAdapter;
import mx.com.omnius.yolabor.Model.CompanyModel;
import mx.com.omnius.yolabor.Model.LanguajesModel;
import mx.com.omnius.yolabor.Model.PlaceInfo;
import mx.com.omnius.yolabor.parse.AsyncTaskCompleteListener;
import mx.com.omnius.yolabor.parse.VolleyHttpRequest;
import mx.com.omnius.yolabor.utils.AppLog;
import mx.com.omnius.yolabor.utils.Constants;

import static mx.com.omnius.yolabor.YolaborApplication.requestQueue;

/**
 * Created by omnius on 23/02/18.
 */

public class SinginActivity extends AppCompatActivity implements AsyncTaskCompleteListener,Response.ErrorListener, GoogleApiClient.OnConnectionFailedListener {

    public final Calendar c = Calendar.getInstance();
    private static final String CERO = "0";
    private static final String BARRA = "/";
    private LocationManager locManager;
    private Spinner compSpiner;
    private Spinner spinner_service, spinner_mother_language,spinnerRadioAttend;
    String[] serrviceArray, attend ;
    private AutoCompleteTextView autComAttend,mSearchAttend;
    private GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));
    private PlaceInfo mPlace;
    private Switch available;

    String[] languajes = {"Spanish", "French", "Italian"};



    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);


    String gender;
    EditText txtName,txtLastName,txtEmail, textPhone, textPassword, textItin, txtPasword;
    TextView textBirtdate;

    ImageButton dateBirth;
    RadioGroup rgroupGender;
    String latitud,longitud;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_singin);

        txtName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        textPhone =(EditText) findViewById(R.id.txtPhone);
        textPassword = (EditText) findViewById(R.id.txtPassword);
        textBirtdate = (TextView) findViewById(R.id.txtBirth);
       // textItin = (EditText) findViewById(R.id.citin);
        dateBirth = (ImageButton) findViewById(R.id.cbtn_date);
        rgroupGender =(RadioGroup) findViewById(R.id.crgroup);

        compSpiner = (Spinner) findViewById(R.id.spinner_company);
        serrviceArray = getResources().getStringArray(R.array.services);
        spinner_service = (Spinner) findViewById(R.id.spinner_service);
        spinnerService();
        spinner_mother_language = (Spinner) findViewById(R.id.spinner_mother_language);
        motherLanguage();
        autComAttend = (AutoCompleteTextView) findViewById(R.id.autComAttend);
        mSearchAttend = (AutoCompleteTextView) findViewById(R.id.autComAttend);
        spinnerRadioAttend = (Spinner) findViewById(R.id.spinnerRadioAttend);
        available = (Switch) findViewById(R.id.switchAvailable);
        attend = getResources().getStringArray(R.array.radioAttend);
        spinnerAttend();

         Button button = (Button) findViewById(R.id.singinButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar();

            }
        });

        dateBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerFecha();
            }
        });

//        compSpiner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languajes));

        rgroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.raMale){
                    gender = "M";
                }else if(i == R.id.raFemale){
                    gender = "F";
                }

            }
        });
        init();


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {

        }



    }

    private void allCompany() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Constants.URL, Constants.ServiceType.ALLCOMPANY);


      requestQueue.add(new VolleyHttpRequest(Request.Method.GET, map,
                Constants.ServiceCode.ALLCOMPANY, this, this));
        Toast.makeText(this, "COMPANY", Toast.LENGTH_SHORT).show();



    }

    private void isAvailable(){
        if (available.isChecked()){
            YolaborApplication.preferenceHelper.putAvailable(Constants.ISAVAILABLE);
        }else{
            YolaborApplication.preferenceHelper.putAvailable(Constants.NOTAVAILABLE);
        }
    }

    private void init(){
        Log.d("", "init: initializing");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mSearchAttend.setOnItemClickListener(mAutocompleteClickListener);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);

        mSearchAttend.setAdapter(mPlaceAutocompleteAdapter);

        mSearchAttend.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    geoLocate();
                }

                return false;
            }
        });

    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideSoftKeyboard();

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                Log.d("", "onResult: Place query did not complete successfully: " + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);

            try{
                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                Log.d("", "onResult: name: " + place.getName());
                mPlace.setAddress(place.getAddress().toString());
                Log.d("", "onResult: address: " + place.getAddress());
//                mPlace.setAttributions(place.getAttributions().toString());
//                Log.d(TAG, "onResult: attributions: " + place.getAttributions());
                mPlace.setId(place.getId());
                Log.d("", "onResult: id:" + place.getId());

                mPlace.setLatlng(place.getLatLng());
                AppLog.Log("","corrdenadas "+ place.getLatLng());
                YolaborApplication.preferenceHelper.putLongitudeAttend(String.valueOf(place.getLatLng().longitude));
                YolaborApplication.preferenceHelper.putLatitudeAttend(String.valueOf(place.getLatLng().latitude));
                mPlace.setRating(place.getRating());
                Log.d("", "onResult: rating: " + place.getRating());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                Log.d("", "onResult: phone number: " + place.getPhoneNumber());
                mPlace.setWebsiteUri(place.getWebsiteUri());
                Log.d("", "onResult: website uri: " + place.getWebsiteUri());

                Log.d("", "onResult: place: " + mPlace.toString());
            }catch (NullPointerException e){
                Log.e("", "onResult: NullPointerException: " + e.getMessage() );
            }

            //moveCamera(new LatLng(place.getViewport().getCenter().latitude,
            //      place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace);

            places.release();
        }
    };
    private void geoLocate(){
        Log.d("", "geoLocate: geolocating");

        String searchString = mSearchAttend.getText().toString();

        Geocoder geocoder = new Geocoder(SinginActivity.this);
        List<Address> list = new ArrayList<>();



        try{
            list = geocoder.getFromLocationName(searchString, 1);
            AppLog.Log("","Lista de direcciones " + list);
        }catch (IOException e){
            Log.e("", "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);

            Log.d("", "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            //  moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
            //        address.getAddressLine(0));
        }
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case Constants.ServiceCode.ALLCOMPANY:
                Gson gns = new Gson();
                ArrayList<String> listCompany = new ArrayList<String>();
                CompanyModel[] listaComp = gns.fromJson(response, CompanyModel[].class);
                if (listaComp != null){
                    for (int i = 0; i < listaComp.length; i++) {
                        String datos = listaComp[i].getName();
                        AppLog.Log("Profile","Estados parse: " + datos);
                        listCompany.add(datos);
                }
                    compSpiner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCompany));
                    compSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            Toast.makeText(adapterView.getContext(), (String) adapterView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }else{
                    //vacio
                }
                break;

            case Constants.ServiceCode.CATALOG_MOTHER_LANGUAGE:
                Gson gsoLangugage = new Gson();
                ArrayList<String> listMotherLanguage = new ArrayList<String>();
                final ArrayList<String>listIdMotherLangugage = new ArrayList<>();
                LanguajesModel[] listaMotherLanguege = gsoLangugage.fromJson(response, LanguajesModel[].class);
                if (listaMotherLanguege != null) {
                    for (int i = 0; i < listaMotherLanguege.length; i++) {
                        String datos = listaMotherLanguege[i].getName();
                        String ids = listaMotherLanguege[i].getName() + "/"+ listaMotherLanguege[i].getIdCatalog();
                        AppLog.Log("Profile","Estados parse: " + datos);
                        listMotherLanguage.add(datos);
                        listIdMotherLangugage.add(ids);
                    }
                    spinner_mother_language.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listMotherLanguage));
                    spinner_mother_language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            String typeMotherLanguage = (String) adapterView.getItemAtPosition(position);
                            String motherLanguage = (String) adapterView.getItemAtPosition(position);
                            for (int i = 0; i < listIdMotherLangugage.size();i++){
                                String tmporal = listIdMotherLangugage.get(i);
                                String[] parts = tmporal.split("/");
                                String partNombre = parts[0];
                                String partId = parts[1];
                                if (partNombre.equals(motherLanguage)){
                                    String idTemporal = partId;
                                    AppLog.Log("","id langugage "+ idTemporal);
                                    YolaborApplication.preferenceHelper.putIdMotherLangugage(idTemporal);
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // vacio
                        }
                    });

                }
                break;
        }

    }
        private void singin(){
            latitud = YolaborApplication.preferenceHelper.getLongitudeC();
            longitud = YolaborApplication.preferenceHelper.getLatitudeC();
            Log.e("test1",txtName.getText().toString());
            Log.e("test1",txtLastName.getText().toString());
            Log.e("test1",txtEmail.getText().toString());
            Log.e("test1",txtPasword.getText().toString());
            Log.e("test1",textPhone.getText().toString());
            Log.e("test1",textItin.getText().toString());
            Log.e("test1",textBirtdate.getText().toString());
            Log.e("test1",gender);
            Log.e("test1latitud",latitud);
            Log.e("test1LONGITUD",longitud);

            /*HashMap<String, String> map = new HashMap<String, String>();
            map.put(Constants.URL, Constants.ServiceType.SINGIN

                    + Constants.Params.FIRSTNAME + "="
                    + txtName.getText().toString() + "&"
                    + Constants.Params.LASTNAME + "="
                    + txtLastName.getText().toString() + "&"
                    + Constants.Params.EMAIL + "="
                    + txtEmail.getText().toString() + "&"
                    + Constants.Params.PASSWORD + "="
                    + txtPasword.getText().toString() + "&"
                    + Constants.Params.PHONE + "="
                    + textPhone.getText().toString() + "&"
                    + Constants.Params.ITIN + "="
                    + textItin.getText().toString() + "&" );


            requestQueue.add(new VolleyHttpRequest(Request.Method.GET, map,
                    Constants.ServiceCode.NEW_CLIENT, this, this)); */
    }
    private void registrar() {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Constants.URL,Constants.ServiceType.SINGIN

                + Constants.Params.FIRSTNAME + "="
                + txtName.getText().toString() + "&"
                + Constants.Params.LASTNAME + "="
                + txtLastName.getText().toString() + "&"
                + Constants.Params.EMAIL + "="
                + txtEmail.getText().toString() + "&"
                + Constants.Params.PHONE + "="
                + textPassword.getText().toString() + "&"
                + Constants.Params.BIRTHDATE + "="
                + textBirtdate.getText().toString() + "&"
                + Constants.Params.GENDER + "="
                + gender.toString() + "&"
                + Constants.Params.PASSWORD + "="
                + textPassword.getText().toString() + "&"
                + Constants.Params.MOTHERLENGUAGE +"="
                + "1" + "&"
                + Constants.Params.AVIABILITY + "="
                + "Looking for job" + "&"
                + Constants.Params.EXPERIENCE + "="
                + "5" + "&"
                + Constants.Params.RESUME + "="
                + "LOREM IMPUTS ASPA ASKID ASDI" + "&"
                + Constants.Params.LATITUDATEND + "="
                + YolaborApplication.preferenceHelper.getLongitudeC() + "&"
                + Constants.Params.LONGITUATTEND + "="
                + YolaborApplication.preferenceHelper.getLatitudeC() + "&"
                + Constants.Params.RADIOATEEND + "="
                + "0" + "&"
                + Constants.Params.LATITUDE + "="
                + YolaborApplication.preferenceHelper.getLongitudeC() + "&"
                + Constants.Params.LONGITUDE + "="
                + YolaborApplication.preferenceHelper.getLatitudeC() + "&"
                + Constants.Params.LOGINMETHOD + "="
                + "N" + "&"
                + Constants.Params.ADDRESATEND + "="
                + "a"

        );

        requestQueue.add(new VolleyHttpRequest(Request.Method.GET, map,
                Constants.ServiceCode.NEW_CLIENT, this, this));
        Toast.makeText(this, "REGISTREAR", Toast.LENGTH_SHORT).show();

    }
    private void obtenerFecha(){
            DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                    final int mesActual = month + 1;
                    //Formateo el día obtenido: antepone el 0 si son menores de 10
                    String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                    //Formateo el mes obtenido: antepone el 0 si son menores de 10
                    String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                    //Muestro la fecha con el formato deseado
                   // textBirtdate.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
                    textBirtdate.setText(year + BARRA + mesFormateado + BARRA + diaFormateado);

                }
                //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
                /**
                 *También puede cargar los valores que usted desee
                 */
            },anio, mes, dia);
            //Muestro el widget
            recogerFecha.show();

        }

    private void  spinnerService(){

        spinner_service.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, serrviceArray));
        spinner_service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (adapterView.getItemAtPosition(position).equals(Constants.CONSTRUCTION)){
                    YolaborApplication.preferenceHelper.putServiceType(Constants.CONSTRUCTION);
                }else if (adapterView.getItemAtPosition(position).equals(Constants.RESTAURANTS)){
                    YolaborApplication.preferenceHelper.putServiceType(Constants.RESTAURANTS);
                }else if (adapterView.getItemAtPosition(position).equals(Constants.CLEANING)){
                    YolaborApplication.preferenceHelper.putServiceType(Constants.CLEANING);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // vacio
            }
        });
    }
    private void spinnerAttend(){
        spinnerRadioAttend.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, attend));
        spinnerRadioAttend.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String company = (String) adapterView.getItemAtPosition(position);
                String typeRadioAttend = (String) adapterView.getItemAtPosition(position);
                YolaborApplication.preferenceHelper.putRadioAttend(typeRadioAttend);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // vacio
            }
        });

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
        Toast.makeText(this, "Error al entrar en contacto con el servidor "+ error, Toast.LENGTH_SHORT).show();
    }

    private void motherLanguage(){
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Constants.URL, Constants.ServiceType.CATALOG_MOTHER_LANGUAGE
                + Constants.Params.ID_CATEGORY + "="
                + Constants.idCatrgory_language_mother);
        requestQueue.add(new VolleyHttpRequest(Request.Method.GET, map,
                Constants.ServiceCode.CATALOG_MOTHER_LANGUAGE, this, this));

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
