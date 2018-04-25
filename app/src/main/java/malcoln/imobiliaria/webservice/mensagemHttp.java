package malcoln.imobiliaria.webservice;

import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.util.concurrent.TimeUnit;

import malcoln.imobiliaria.model.Usuario;

/**
 * Created by Malcoln on 28/11/2017.
 */

public class mensagemHttp {
    public static final String BASE_URL ="http://demo4573903.mockable.io/imoveis/contato";


    public static Usuario enviarMsgServidor(Usuario usuario){
        try {
            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(5, TimeUnit.SECONDS);
            client.setConnectTimeout(10, TimeUnit.SECONDS);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            String jsonPOST = "{'Nome':'"+usuario.getNome()+"','e-mail':'"+usuario.getEmail()+"'," +
                    "'telefone':'"+usuario.getTelefone()+"','código do anúncio':'"+usuario.getCodigoImovel()+"'}";
            RequestBody body = RequestBody.create(JSON,jsonPOST);

            Request postRequest = new Request.Builder()
                    .url(BASE_URL)
                    .post(body)
                    .build();
            Response postResponse = client.newCall(postRequest).execute();
            String jsonStr = postResponse.body().string();

            Log.e("SAIDA", "Resposta da URL: " + jsonStr);
            Log.e("POST", "Json POST MSG: " + jsonPOST);

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

