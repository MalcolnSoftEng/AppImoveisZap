package malcoln.imobiliaria.webservice;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import malcoln.imobiliaria.ListWebServiceFragment;
import malcoln.imobiliaria.controller.ListaAdapter;
import malcoln.imobiliaria.model.Imoveis;
import malcoln.imobiliaria.util.MalcolnGeoPoint;

/**
 * Created by Malcoln on 05/12/2017.
 */

public class GeocodeHttp {
    public static String writeConsult;
    public static String lat,lng,localName;
    static Context ctx;
    static ListaAdapter adapter;

    public static final String BASE_URL ="http://maps.googleapis.com/maps/api/geocode/json?address=";

        public static List<Imoveis> obterLatLong(List<Imoveis> imoveisList){

            Log.e("SAIDA quantidade:", String.valueOf(imoveisList.size()));
            for (int i =0; i< 10 ;i++) {
                Imoveis imoveis = imoveisList.get(i);

                writeConsult = imoveis.getEndereco();
                writeConsult = writeConsult.replace(" ","+");
                writeConsult = writeConsult.replace(",","+");
                writeConsult = writeConsult.replace("Várias+Unidades","+");
                writeConsult = writeConsult.replace("Sob+Consulta","+");

                Log.e("SAIDA ENDERECOS", writeConsult);

                try {
                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(5, TimeUnit.SECONDS);
                    client.setConnectTimeout(10, TimeUnit.SECONDS);
                    Request request = new Request.Builder()
                            .url(BASE_URL + writeConsult)
                            .build();
                    Response response = null;
                    response = client.newCall(request).execute();
                    String jsonStr = response.body().string();

                    Log.e("GEO", "Resposta da URL GEO: " + jsonStr);
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray jsonObject = jsonObj.getJSONArray("results");

                    for (int j= 0; j < jsonObject.length(); j++) {

                        JSONObject jsonFormAddress = jsonObject.getJSONObject(j);


                        //JSONObject jsonObject1 = jsonObject.getJSONObject("formatted_address");
                        JSONObject jsonObject2 = jsonFormAddress.getJSONObject("geometry");
                        JSONObject jsonLocation = jsonObject2.getJSONObject("location");
                        //JSONObject jsonLocation2 = jsonLocation.getJSONObject("location");
                        lat = jsonLocation.getString("lat");
                        lng = jsonLocation.getString("lng");

                    Log.e("Lista:", "Lista: " + lat + "long: " + lng );

                    }
                    imoveis.setLatModel(lat);
                    imoveis.setLongModel(lng);

                    MalcolnGeoPoint imoveisGeoPoint = new MalcolnGeoPoint(Double.parseDouble(lat),Double.parseDouble(lng));
                    Double usuarioLat = ListWebServiceFragment.mLatitudeLabel;
                    Double usuarioLong = ListWebServiceFragment.mLongitudeLabel;
                    Double userCalcLocation = imoveisGeoPoint.distanceInKilometersTo(new MalcolnGeoPoint(usuarioLat,usuarioLong));
                    Double userFinalLocation = (double) Math.round(userCalcLocation * 10)/10;
                    imoveis.setDistancia(String.valueOf(userFinalLocation));

                    imoveisList.add(imoveis);


                } catch (Exception e) {

                }

            }
            return imoveisList;
        }

}
