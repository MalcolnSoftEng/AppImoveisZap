package malcoln.imobiliaria;

import android.Manifest;
import android.animation.Animator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import malcoln.imobiliaria.controller.ListaAdapter;
import malcoln.imobiliaria.model.Filtro;
import malcoln.imobiliaria.model.Imoveis;

import malcoln.imobiliaria.util.IsOnline;
import malcoln.imobiliaria.webservice.GeocodeHttp;
import malcoln.imobiliaria.webservice.ImoveisHttp;

import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by Malcoln on 28/11/2017.
 */

public class ListWebServiceFragment extends Fragment implements ListaAdapter.AoClicarNoImovelListener{

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout mSwipe;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.txtQTDE)
    TextView txtQTDE;

    @BindView(R.id.txtLat)
    TextView txtLat;

    @BindView(R.id.btnFiltro)
    FloatingActionButton btnFiltro;

    public String qtdeFiltro;
    public String qtdeTotal;

    Unbinder unbinder;

    List<Imoveis> mImoveis;
    ImoveisDownloadTask mTask;
    GeoTask mGeoTask;

    public boolean filtro = false;

    public Filtro mFiltro;

    private ListaAdapter adapter;

    private Imoveis Geoimoveis;


    private static final String TAG = ListWebServiceFragment.class.getSimpleName();

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * Provides the entry point to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;

    public static double mLatitudeLabel;
    public static double mLongitudeLabel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.recyler_lista_imoveis,container,false);

        ButterKnife.bind(this,view);

        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mTask = new ImoveisDownloadTask();
                mTask.execute();

            }
        });

        mRecyclerView.setTag("web");
        mRecyclerView.setHasFixedSize(true);
        //if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //} else {
       // mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        // }
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        btnFiltro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                getViewFiltro();
            }
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //chamar unbind da view
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<Imoveis> imoveisList = mImoveis;
        if (mImoveis == null){
            if (mTask == null){
                mTask = new ImoveisDownloadTask();
               mTask.execute();
                //new GeoTask().execute();
            } else if (mTask.getStatus() == AsyncTask.Status.RUNNING){
                exibirProgresso();
                new IsOnline(getContext()).isOn();

            }
        } else {
           // mTask.executeOnExecutor(ImoveisDownloadTask.SERIAL_EXECUTOR,GeocodeHttp.obterLatLong(imoveisList));
            //new GeoTask().execute();
            atualizarLista();
        }
    }

    @Override
    public void aoClicarNoItem(View view, int position, Imoveis imoveis) {

        Intent it = new Intent(getActivity(), ImovelDetalhesActivity.class);
        it.putExtra(ImovelDetalhesActivity.EXTRA_ITEM, imoveis);
        startActivity(it);
    }


    private void atualizarQtdeItens() {
        qtdeTotal = String.valueOf(ImoveisHttp.retornaQTDETotal());
        qtdeFiltro = String.valueOf(ImoveisHttp.retornaQTDEfILTRO());
        if (qtdeFiltro.equals("0")|| filtro==false){
            txtQTDE.setText(" "+ qtdeTotal + " de " + qtdeTotal + " Imoveis Encontrados");
        }else{
            txtQTDE.setText(" "+ qtdeFiltro + " de " + qtdeTotal + " Imoveis Encontrados");
        }

        //MOCKANDO GEOLOCALIZAÇÃO


        String txtLatitude = String.valueOf(mLatitudeLabel);
        String txtLongitude = String.valueOf(mLongitudeLabel);



        txtLat.setText(txtLatitude+" : quantidade de enderecos "+mImoveis.size()+"\n" );

    }

    private void atualizarLista() {

        adapter = new ListaAdapter(getActivity(), mImoveis);
        adapter.setAoClicarNoItemListener(this);
        mRecyclerView.setAdapter(adapter);

        atualizarQtdeItens();
        new IsOnline(getContext()).isOn();

    }

    public void exibirProgresso(){
        mSwipe.post(new Runnable() {
            @Override
            public void run() {
                mSwipe.setRefreshing(true);
            }
        });
    }

    private class ImoveisDownloadTask extends AsyncTask<List, Void, List<Imoveis>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected List<Imoveis> doInBackground(List... voids) {
            List valor = null;
            if (!filtro){
                valor = ImoveisHttp.obterImoveisDoServidor();
            }else{
                valor = ImoveisHttp.obterImoveisPorPreco(mFiltro);
            }
            return valor;
        }
        protected void onPostExecute(List<Imoveis> imoveis) {
            super.onPostExecute(imoveis);
            mSwipe.setRefreshing(false);
            if (imoveis!=null){
                mImoveis= imoveis;
                new GeoTask().execute();

                atualizarLista();
            }
        }
    }


    private class GeoTask extends AsyncTask<List, Void, List<Imoveis>>{

        @Override
        protected List<Imoveis> doInBackground(List... voids) {

            return GeocodeHttp.obterLatLong(mImoveis);
        }

        @Override
        protected void onPostExecute(List<Imoveis> imoveises) {
            super.onPostExecute(imoveises);

            atualizarLista();
        }
    }


    private void getViewFiltro() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View msgView = layoutInflater.inflate(R.layout.busca_tela_layout,null);

        final View myView = msgView.findViewById(R.id.reveal);
        //The runnable will run after the view's creation
        myView.post(new Runnable() {
            @Override
            public void run() {
                // get the center for the clipping circle
                int cx = (myView.getLeft() + myView.getRight()) / 2;
                int cy = (myView.getTop() + myView.getBottom()) / 2;

                // get the final radius for the clipping circle
                int finalRadius = Math.max(myView.getWidth(), myView.getHeight());

                Animator animator =
                        ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
                myView.setVisibility(View.VISIBLE);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(1000);
                animator.start();
            }
        });


        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(msgView);
        alertDialogBuilder.setTitle("Filtre por:");

        final LinearLayout txtLinearLayout1_1 = msgView.findViewById(R.id.txtLinearLayout1_1);
        final LinearLayout txtLinearLayout2_1 = msgView.findViewById(R.id.txtLinearLayout2_1);
        final LinearLayout txtLinearLayout3_1 = msgView.findViewById(R.id.txtLinearLayout3_1);
        final LinearLayout txtLinearLayout4_1 = msgView.findViewById(R.id.txtLinearLayout4_1);

        boolean click=false;
        final TextView txtVInserido = msgView.findViewById(R.id.txtValorInserido);
        txtVInserido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txtLinearLayout1_1.getVisibility()!=View.VISIBLE) {
                    txtLinearLayout1_1.setVisibility(View.VISIBLE);

                }else{
                    txtLinearLayout1_1.setVisibility(View.GONE);
                }
            }
        });

        final TextView txtQTDEDorm = msgView.findViewById(R.id.txtQTDEDorm);
        txtQTDEDorm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtLinearLayout2_1.getVisibility()!=View.VISIBLE) {
                    txtLinearLayout2_1.setVisibility(View.VISIBLE);
                }else{
                    txtLinearLayout2_1.setVisibility(View.GONE);
                }
            }
        });

        final TextView txtQTDESuite = msgView.findViewById(R.id.txtQTDESuite);
        txtQTDESuite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtLinearLayout3_1.getVisibility()!=View.VISIBLE){
                    txtLinearLayout3_1.setVisibility(View.VISIBLE);
                }else{
                    txtLinearLayout3_1.setVisibility(View.GONE);
                }
            }
        });
        final TextView txtQTDEVagas = msgView.findViewById(R.id.txtQTDEVagas);
        txtQTDEVagas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtLinearLayout4_1.getVisibility()!=View.VISIBLE) {
                    txtLinearLayout4_1.setVisibility(View.VISIBLE);
                }else {
                    txtLinearLayout4_1.setVisibility(View.GONE);
                }
            }
        });

        final SeekBar seekBar = msgView.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtVInserido.setText("ATÉ "+String.valueOf(seekBar.getProgress()+" REAIS"));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        final SeekBar seekBarDorm = msgView.findViewById(R.id.seekBarDorm);
        seekBarDorm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtQTDEDorm.setText(String.valueOf(seekBarDorm.getProgress())+" QUARTOS");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        final SeekBar seekBarSuite = msgView.findViewById(R.id.seekBarSuite);
        seekBarSuite.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtQTDESuite.setText(String.valueOf(seekBarSuite.getProgress())+" SUÍTES");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        final SeekBar seekBarVagas = msgView.findViewById(R.id.seekBarVagas);
        seekBarVagas.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtQTDEVagas.setText(String.valueOf(seekBarVagas.getProgress())+" VAGAS");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Toast.makeText(getActivity(), "Valor R$" + seekBar.getProgress()+"\n"
                                        + " Dormitórios:"+ seekBarDorm.getProgress()+"\n"
                                        + " Suítes:"+ seekBarSuite.getProgress()+"\n"
                                        + " Vagas:"+ seekBarVagas.getProgress(),
                                Toast.LENGTH_LONG).show();
                        mFiltro = new Filtro(seekBar.getProgress(),seekBarDorm.getProgress(),
                                seekBarSuite.getProgress(),seekBarVagas.getProgress());
                        mFiltro.setValor(seekBar.getProgress()+seekBarDorm.getProgress()
                                +seekBarSuite.getProgress()+seekBarVagas.getProgress());
                        filtro = true;
                        new ImoveisDownloadTask().execute();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLastLocation();
        }
    }
    @SuppressWarnings("MissingPermission")
    private void getLastLocation(){
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                            mLatitudeLabel = mLastLocation.getLatitude();
                            mLongitudeLabel = mLastLocation.getLongitude();

                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());

                        }
                    }
                });
    }
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");



        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }
}
