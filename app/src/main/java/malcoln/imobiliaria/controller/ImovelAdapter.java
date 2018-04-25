package malcoln.imobiliaria.controller;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import malcoln.imobiliaria.R;
import malcoln.imobiliaria.model.Imoveis;

/**
 * Created by Malcoln on 30/11/2017.
 */

public class ImovelAdapter extends PagerAdapter {

    private Context mContext;

    private LayoutInflater layoutInflater; //intancia que será carregada/inflada nas Views de Array de Views -> int[] layout
    int[] layout = new int[]{R.layout.activity_inicial,
            R.layout.activity_inicial2, R.layout.activity_inicial3, R.layout.activity_inicial4,R.layout.activity_inicial5};
    private String[] arrayFotos;

    public ImovelAdapter(Context mContext, int[] layout, String[] arrayFotos ){ //método construtor assinado para parametros do contexto a ser
        // chamado e objeto/componente que será carregado
        this.mContext = mContext;
        this.layout = layout;
        this.arrayFotos = arrayFotos;

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Imoveis imoveis = new Imoveis();
        layoutInflater= LayoutInflater.from(mContext);
        ViewGroup view = (ViewGroup) layoutInflater.inflate(layout[position], container,false);
        container.addView(view);
        String fotoCarregada = arrayFotos[position];
        ImageView imageUpLoadWS = (ImageView) view.findViewById(R.id.imageUpLoadWS);

        Picasso.with(mContext).load(fotoCarregada).into(imageUpLoadWS);

        return view;
    }
    @Override
    public int getCount() {
        return layout.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View)object;
        container.removeView(view);
    }
}
