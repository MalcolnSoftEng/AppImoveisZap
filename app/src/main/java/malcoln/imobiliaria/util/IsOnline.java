package malcoln.imobiliaria.util;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

/**
 * Created by Malcoln on 03/12/2017.
 */

public class IsOnline extends Application{

    Context context;

    public IsOnline(Context context) {
        this.context = context;
    }

    public boolean statusConexao(){
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return manager.getActiveNetworkInfo() != null &&
                manager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
    public void isOn(){
        if (!statusConexao()){
            Toast.makeText(context,"Sem conexão",Toast.LENGTH_LONG).show();
        }

    }


}
