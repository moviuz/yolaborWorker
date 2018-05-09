package mx.com.omnius.yolabor.Fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.Calendar;

import mx.com.omnius.yolabor.Adapter.PlaceAutocompleteAdapter;
import mx.com.omnius.yolabor.Model.PlaceInfo;
import mx.com.omnius.yolabor.R;
import mx.com.omnius.yolabor.YolaborApplication;
import mx.com.omnius.yolabor.parse.AsyncTaskCompleteListener;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by David on 04/05/18.
 */

public class ProFragment extends Fragment implements View.OnClickListener, AsyncTaskCompleteListener, Response.ErrorListener, GoogleApiClient.OnConnectionFailedListener  {
    private ViewPager view_pager;
    private TabLayout tab_layout;
    private View view;
    public final Calendar c = Calendar.getInstance();
    private static final String CERO = "0";
    private static final String BARRA = "/";
    private LocationManager locManager;
    private  int dia,mes,ano;


    //Variables para obtener la fecha
    //final int mes = c.get(Calendar.MONTH);
    //final int dia = c.get(Calendar.DAY_OF_MONTH);
    //final int anio = c.get(Calendar.YEAR);

    private TextView txt_result;
    private EditText firstname, lastename, email, password,phone;
    private RelativeLayout relativeLayout;
    private ImageButton btnCalendar;
    private RadioGroup radioGroup;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_proffile, container, false);
        super.onCreate(savedInstanceState);
        firstname = (EditText) view.findViewById(R.id.firstname_proff);
        lastename = (EditText) view.findViewById(R.id.lastname_proff) ;
        email = (EditText) view.findViewById(R.id.email_proff);
        password = (EditText) view.findViewById(R.id.password_proff);
        phone = (EditText) view.findViewById(R.id.phone_proff);
        btnCalendar = (ImageButton) view.findViewById(R.id.btn_date);
        txt_result = (TextView) view.findViewById(R.id.txt_result);
        radioGroup = (RadioGroup) view.findViewById(R.id.radio_proff);
        llenarcampos();
        //initComponent();

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFecha();
            }
        });

        return view;
    }


    private void llenarcampos(){
        String y = YolaborApplication.preferenceHelper.getGender();

        // final RelativeLayout relav = (RelativeLayout) findViewById(R.id.rla_profile) ;
        //elativeLayout = (RelativeLayout) findViewById(R.id.rla_profile);
        firstname.setText(YolaborApplication.preferenceHelper.getFirstName());
        lastename.setText(YolaborApplication.preferenceHelper.getLastName());
        email.setText(YolaborApplication.preferenceHelper.getEmail());
        password.setText((YolaborApplication.preferenceHelper.getPassword()));
        phone.setText(YolaborApplication.preferenceHelper.getPhone());
        txt_result.setText(YolaborApplication.preferenceHelper.getBirthdate());




        if (y =="M"){
            radioGroup.check(R.id.raBtnFemale);
        }else {

            radioGroup.check(R.id.raBtnMale);
        }


    }
    private void selectFecha(){
    final Calendar c= Calendar.getInstance();
    dia=c.get(Calendar.DAY_OF_MONTH);
    mes=c.get(Calendar.MONTH);
    ano=c.get(Calendar.YEAR);

    android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(getContext(), new android.app.DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            long date_ship_mill = c.getTimeInMillis();
            txt_result.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
            int mes = monthOfYear +1;
            String cumple = year+"-"+mes+"-"+dayOfMonth;

        }
    }
            ,dia,mes,ano);
        datePickerDialog.show();

}

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

    }
}
