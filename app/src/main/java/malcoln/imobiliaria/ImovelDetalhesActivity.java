package malcoln.imobiliaria;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import malcoln.imobiliaria.controller.ImovelAdapter;
import malcoln.imobiliaria.model.Imoveis;
import malcoln.imobiliaria.model.Usuario;
import malcoln.imobiliaria.webservice.mensagemHttp;

public class ImovelDetalhesActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM = "imoveis";

    ////////////////////////////////////////////////////
    /////////////COMPONENTES E ATRIBUTOS REFERENTES A VIEWPAGER DA(S) IMAGENS WEBSERVICE
    private ViewPager viewPager;
    private ImovelAdapter mImovViewPagAdpter;
    private LinearLayout dotsLayout;
    private ImageButton btnGo, btnReturn;
    private TextView[] dots;
    int[] layout = new int[]{R.layout.activity_inicial,
            R.layout.activity_inicial2, R.layout.activity_inicial3,R.layout.activity_inicial4,R.layout.activity_inicial5};

    ///////////////////////////////////////////////////

    @BindView(R.id.mFabMensagem)
    FloatingActionButton mFabMensagem;

    @BindView(R.id.imgImovel)
    ImageView mImageImovel;

    @BindView(R.id.coordinator)
    CoordinatorLayout mCoordinator;


    AppBarLayout mAppBar;

    @BindView(R.id.collapseToolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.txtSubTipoImovelDet)
    TextView txtSubTipoImovel;

    @BindView(R.id.txtCodImovel)
    TextView txtCodImovel;

    @BindView(R.id.txtCodCliente)
    TextView txtCodCliente;

    @BindView(R.id.txtEnderecoDet)
    TextView txtEnderecol;

    @BindView(R.id.txtPrecoDet)
    TextView txtPreco;

    @BindView(R.id.txtDormitoriosDet)
    TextView txtDormitorios;

    @BindView(R.id.txtSuitesDet)
    TextView txtSuites;

    @BindView(R.id.txtVagasDet)
    TextView txtVagas;

    @BindView(R.id.txtAreaUtilDet)
    TextView txtAreaUtil;

    @BindView(R.id.btnMapa)
    ImageButton btnMapa;

    @BindView(R.id.btnRelativeLayout)
    RelativeLayout btnRelativeLayout;

    @BindView(R.id.btnContato)
    Button btnContato;

    @BindView(R.id.txtNomeFantasia)
    TextView txtNomeFantasia;

    private Usuario mUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imovel_detalhes);

        ButterKnife.bind(this);

        final Imoveis imoveis = (Imoveis)getIntent().getSerializableExtra(EXTRA_ITEM);

        preencheCampos(imoveis);

        configurarBarraDeTitulo(imoveis.getTipoImovel());

        carregarCapa(imoveis);
        //////////////////////////////////////////////////////////
        ///////// INSTANCIAS/OBJETOS REFERENTES A VIEWPAGER DAS IMAGENS DE WEBSERVICE
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnReturn = (ImageButton)findViewById(R.id.imageButtonLeft);
        btnGo = (ImageButton) findViewById(R.id.imageButtonRight);

        mImovViewPagAdpter = new ImovelAdapter(this,layout,imoveis.getFotos_imoveis());

        viewPager.setAdapter(mImovViewPagAdpter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int viewAtual = getItem(+1);
                int viewAnterior = getItem(-1);
                if (viewAtual < mImovViewPagAdpter.getCount()){
                    viewPager.setCurrentItem(viewAtual);
                } else {
                    viewPager.setCurrentItem(viewAnterior);
                }
            }
        });
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int viewAtual = getItem(+1);
                int viewAnterior = getItem(-1);
                if (viewAtual < mImovViewPagAdpter.getCount()){
                    viewPager.setCurrentItem(viewAtual);
                } else {
                    viewPager.setCurrentItem(viewAnterior);
                }
            }
        });

        addBottomDots(0);

        btnContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater layoutInflater = LayoutInflater.from(getBaseContext());
                final View msgView = layoutInflater.inflate(R.layout.msg_tela_layout,null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImovelDetalhesActivity.this);
                alertDialogBuilder.setView(msgView);
                alertDialogBuilder.setTitle("Enviar Interesse");
                alertDialogBuilder
                        .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                TextView msgNome=msgView.findViewById(R.id.txtNome);
                                TextView msgEmail=msgView.findViewById(R.id.txtEmail);
                                TextView msgTelefone=msgView.findViewById(R.id.txtTelefone);

                                String nome = msgNome.getText().toString();
                                String email = msgEmail.getText().toString();
                                String telefone = msgTelefone.getText().toString();
                                String codigoImovel = null;

                                if (!nome.isEmpty()&&!email.isEmpty()&&!telefone.isEmpty()){

                                    mUsuario = new Usuario(nome,email,telefone, codigoImovel);

                                    mUsuario.setNome(msgNome.getText().toString());
                                    mUsuario.setEmail(msgEmail.getText().toString());
                                    mUsuario.setTelefone(msgTelefone.getText().toString());

                                    //recuperando codigo Imovel from Intent
                                    mUsuario.setCodigoImovel(imoveis.getCodImovel());

                                    Toast.makeText(getBaseContext(),"Mensagem enviada com sucesso!",Toast.LENGTH_SHORT).show();

                                    new EnviarMSGTask().execute();

                                } else {
                                    Toast.makeText(getBaseContext(),"Favor preencher os campos para enviar", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        btnRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMapa = new Intent(Intent.ACTION_VIEW);
                intentMapa.setData(Uri.parse("geo:0,0?z=14&q="+ imoveis.getEnderecoFormatado())); //protocolo geo:0,0
                startActivity(intentMapa);
            }
        });
    }

    private void carregarCapa(Imoveis imoveis) { // carregar String de Json caminho Fotos

        Picasso.with(this)
                .load(imoveis.getUrlImagem())
                .into(mImageImovel);

        // carregar ARRAY de String de Json para popular ViewPager com FOTOS dos detalhes do Imovel
    }
    private void preencheCampos(Imoveis imoveis){

        txtSubTipoImovel.setText(imoveis.getSubtipoImovel());
        txtEnderecol.setText(imoveis.getEndereco());
        txtPreco.setText(imoveis.getPrecoVenda());
        txtDormitorios.setText(imoveis.getDormitorios());
        txtSuites.setText(imoveis.getSuites());
        txtVagas.setText(imoveis.getVagas());
        txtAreaUtil.setText(imoveis.getAreaUtil());
        txtCodImovel.setText(imoveis.getCodImovel());
        txtCodCliente.setText(imoveis.getCodCliente());
        txtNomeFantasia.setText(imoveis.getNomeFantasia());
    }

    private void configurarBarraDeTitulo(String nome) {
        setSupportActionBar(mToolbar);
       
        if (mAppBar != null){
            if (mAppBar.getLayoutParams() instanceof CoordinatorLayout.LayoutParams){
                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) mAppBar.getLayoutParams();
                lp.height = getResources().getDisplayMetrics().widthPixels;
            }
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (mCollapsingToolbarLayout!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(true);

            //getSupportActionBar().setIcon(R.drawable.ic_launcher);

            mCollapsingToolbarLayout.setTitle(nome);

        } else {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    //////////////////////////////////////////////////////////////////////
    //////////////// MÉTODOS E CLASSE REFERETES A VIEWPAGER DAS IMAGENS WEBSERVICE

    private void addBottomDots(int currentPage) {
        dots = new TextView[mImovViewPagAdpter.getCount()];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i){
        return  viewPager.getCurrentItem()+i;
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageSelected(int position) {

        }
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private class EnviarMSGTask extends AsyncTask<Void, Void, Usuario>{


        @Override
        protected Usuario doInBackground(Void... voids) {
            return mensagemHttp.enviarMsgServidor(mUsuario);
        }
    }

}
