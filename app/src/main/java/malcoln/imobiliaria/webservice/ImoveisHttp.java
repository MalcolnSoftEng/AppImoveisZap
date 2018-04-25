package malcoln.imobiliaria.webservice;


import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import malcoln.imobiliaria.model.Filtro;
import malcoln.imobiliaria.model.Imoveis;

/**
 * Created by Malcoln on 28/11/2017.
 */

public class ImoveisHttp {
    public static final String BASE_URL ="http://demo4573903.mockable.io/imoveis";


    private static int QTDE_IMOV_FILTRADOS = 0;

    private static int QTDE_IMV_TOTAL = 0;

    public static int[] filtroPreco = new int[0];


    public static List<Imoveis> obterImoveisDoServidor(){
        List<Imoveis> listaDeImoveis = new ArrayList<>();

        try {
            JSONArray imoveisArray = getJsonArray();

            // looping through All Imoveis
            for (int i = 0; i < imoveisArray.length(); i++) {

                JSONObject jsonImoveis = imoveisArray.getJSONObject(i);

                Imoveis imoveis = new Imoveis();
                getAtributosJson(imoveis, jsonImoveis);

                listaDeImoveis.add(imoveis);
                Log.e("SAIDA", "Lista: " + listaDeImoveis);
                QTDE_IMV_TOTAL = imoveisArray.length();


            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return listaDeImoveis;
    }

    public static List<Imoveis> obterImoveisPorPreco(Filtro filtro){
        List<Imoveis> listaDeImoveis = new ArrayList<>();

        String teste = "";
        int qtdeImovFiltro = 0;
        try {
            JSONArray imoveisArray = getJsonArray();
            int acum = 0;
            JSONObject jsonImoveis = null;

            for (int i = 0; i < imoveisArray.length(); i++) {

                jsonImoveis = imoveisArray.getJSONObject(i);
                Imoveis imoveis = new Imoveis();
                int preco = Integer.valueOf(jsonImoveis.getString("PrecoVenda"));
                int dorm = Integer.valueOf(jsonImoveis.getString("Dormitorios"));
                int suites = Integer.valueOf(jsonImoveis.getString("Suites"));
                int vagas = Integer.valueOf(jsonImoveis.getString("Vagas"));

                if (preco<=filtro.getValor() || (dorm==filtro.getQuantDormitorios()
                        && suites==filtro.getQuantSuites() && vagas==filtro.getQuantVagas()) )
                {
                    getAtributosJson(imoveis, jsonImoveis);
                    listaDeImoveis.add(imoveis);
                }
                Log.e("SAIDA", "Lista: " + listaDeImoveis);
                QTDE_IMOV_FILTRADOS = imoveisArray.length();
            }

            QTDE_IMOV_FILTRADOS = listaDeImoveis.size();
            Log.e("SAIDA", "Lista: " + listaDeImoveis);
            Log.e("TESTE FILTRO"," valor :" + filtro.getValor());
            
        } catch (Exception e){
            e.printStackTrace();
        }

        return listaDeImoveis;
    }

    private static void getAtributosJson(Imoveis imoveis, JSONObject jsonImoveis) throws JSONException {

        imoveis.setCodImovel(jsonImoveis.getString("CodImovel"));
        imoveis.setTipoImovel(jsonImoveis.getString("TipoImovel"));
        imoveis.setPrecoVenda(jsonImoveis.getString("PrecoVenda"));
        imoveis.setDormitorios(jsonImoveis.getString("Dormitorios"));
        imoveis.setSuites(jsonImoveis.getString("Suites"));
        imoveis.setVagas(jsonImoveis.getString("Vagas"));
        imoveis.setAreaUtil(jsonImoveis.getString("AreaUtil"));
        imoveis.setAreaTotal(jsonImoveis.getString("AreaTotal"));
        imoveis.setSubtipoImovel(jsonImoveis.getString("SubtipoImovel"));

        //Pega Objeto "Cliente" e a String/Atributos
        JSONObject jsonObjCliente = jsonImoveis.getJSONObject("Cliente");
        imoveis.setNomeFantasia(jsonObjCliente.getString("NomeFantasia"));
        imoveis.setCodCliente(jsonObjCliente.getString("CodCliente"));

        //Pega Objeto "Endereco" e valida quantidade de atributos do objeto, pois o primeiro item tem 6 atributos
        JSONObject jsonObjEndereco = jsonImoveis.getJSONObject("Endereco");
        if (jsonObjEndereco.length() < 8) {
            imoveis.setEndereco(jsonObjEndereco.getString("Numero") + ", Cep: " + jsonObjEndereco.getString("CEP")
                    + ", Bairro: " + jsonObjEndereco.getString("Bairro") + ", Cidade: " + jsonObjEndereco.getString("Cidade"));

            imoveis.setEnderecoFormatado(jsonObjEndereco.getString("Numero") + ", Cep: " + jsonObjEndereco.getString("CEP")
                    + ", Bairro: " + jsonObjEndereco.getString("Bairro") + ", Cidade: " + jsonObjEndereco.getString("Cidade"));
        } else {
            imoveis.setEndereco(jsonObjEndereco.getString("Logradouro") + "," + jsonObjEndereco.getString("Numero")
                    + " - " + jsonObjEndereco.getString("Complemento") + ", " + jsonObjEndereco.getString("Bairro") + ", " + jsonObjEndereco.getString("Cidade"));

            imoveis.setEnderecoFormatado(jsonObjEndereco.getString("Logradouro") + "," +jsonObjEndereco.getString("Numero") + ", Cep: " + jsonObjEndereco.getString("CEP")
                    + ", Bairro: " + jsonObjEndereco.getString("Bairro") + ", Cidade: " + jsonObjEndereco.getString("Cidade"));
        }

        imoveis.setUrlImagem(jsonImoveis.getString("UrlImagem"));
        imoveis.setSubTipoOferta(jsonImoveis.getString("SubTipoOferta"));

        //MOCKANDO VIEW PAGER DE IMAGENS DE IMOVEIS
        String linkCapa = jsonImoveis.getString("UrlImagem");
        String linkZap = "https://i.ytimg.com/vi/sUhq5aZlX40/maxresdefault.jpg";
        String link2Zap = "http://revistaimoveis.zap.com.br/imoveis/2012/05/iPhone_.png";
        String[] arrayFotos = new String[]{linkCapa,linkZap,link2Zap,linkCapa,link2Zap};
        imoveis.setFotos_imoveis(arrayFotos);
    }


    private static JSONArray getJsonArray() throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        client.setReadTimeout(5, TimeUnit.SECONDS);
        client.setConnectTimeout(10, TimeUnit.SECONDS);
        Request request = new Request.Builder()
                .url(BASE_URL)
                .build();
        Response response = client.newCall(request).execute();
        String jsonStr = response.body().string();

        Log.e("SAIDA", "Resposta da URL: " + jsonStr);

        JSONObject jsonObj = new JSONObject(jsonStr);

        // Getting JSON Array node
        return jsonObj.getJSONArray("Imoveis");
    }


    public static int retornaQTDEfILTRO(){

        return QTDE_IMOV_FILTRADOS;
    }
    public static int retornaQTDETotal(){

        return QTDE_IMV_TOTAL;
    }


}

