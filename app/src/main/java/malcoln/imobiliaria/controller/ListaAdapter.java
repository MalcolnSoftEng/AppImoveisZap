package malcoln.imobiliaria.controller;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import malcoln.imobiliaria.R;
import malcoln.imobiliaria.model.Imoveis;

/**
 * Created by Malcoln on 28/11/2017.
 */

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ImoveisViewHolder> implements Filterable{

    private Context mContext;
    private List<Imoveis> mImoveis;
    private List<Imoveis> mImoveisFilter;
    private AoClicarNoImovelListener mListener;

    public ListaAdapter(Context ctx, List<Imoveis> imoveis){
        mContext = ctx;
        mImoveis = imoveis;
        mImoveisFilter = imoveis;
    }

    public void setAoClicarNoItemListener(AoClicarNoImovelListener clickDoImovel){
        mListener = clickDoImovel;
        notifyDataSetChanged();
    }

    @Override
    public ImoveisViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemViewImovel = LayoutInflater.from(mContext).inflate(R.layout.imoveis_lista,parent,false);

        ImoveisViewHolder imoveisViewHolder = new ImoveisViewHolder(itemViewImovel); //instancia chamando item da lista

        itemViewImovel.setTag(imoveisViewHolder); // chave do item

        itemViewImovel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null){
                    ImoveisViewHolder imoveisViewHolder = (ImoveisViewHolder)view.getTag();
                    int position = imoveisViewHolder.getAdapterPosition();
                    mListener.aoClicarNoItem(view, position, mImoveis.get(position));
                }
            }
        });
        return imoveisViewHolder;
    }

    @Override
    public void onBindViewHolder(ImoveisViewHolder holder, int position) {
        Imoveis imoveis = mImoveis.get(position);
        Picasso.with(mContext).load(imoveis.getUrlImagem()).into(holder.imgImovel);
        holder.txtTipoImovel.setText(imoveis.getSubtipoImovel());
        holder.txtEndereco.setText(imoveis.getEndereco());
        holder.txtPreco.setText(imoveis.getPrecoVenda());
        holder.txtDormitorios.setText(imoveis.getDormitorios()+"dorm.(s)");
        holder.txtSuites.setText(imoveis.getSuites()+"suíte(s)");
        holder.txtVagas.setText(imoveis.getVagas()+"vaga(s)");
        holder.txtAreaUtil.setText(" área util:"+imoveis.getAreaUtil()+"m2");
        holder.txtAreaTotal.setText(" área total:"+imoveis.getAreaTotal()+"m2");

        //MOCKANDO LAT E LONG
        holder.txtDistLat.setText(imoveis.getLatModel());
        holder.txtDistLong.setText(imoveis.getLongModel());

        holder.txtDistancia.setText(imoveis.getDistancia()+"Km");

        //FILTRANDO POR STATUS E TAGEANDO SAIDA COM COR
        if (imoveis.getSubTipoOferta().equals("Destaque")){
            holder.txtSubTipoOferta.setText(imoveis.getSubTipoOferta());
            holder.txtSubTipoOferta.setTextColor(Color.WHITE);
            holder.txtSubTipoOferta.setBackgroundColor(Color.RED);
        }
        if (imoveis.getSubTipoOferta().equals("Normal")){
            holder.txtSubTipoOferta.setText(imoveis.getSubTipoOferta());
            holder.txtSubTipoOferta.setTextColor(Color.WHITE);
            holder.txtSubTipoOferta.setBackgroundColor(Color.BLUE);
        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mImoveisFilter = (List<Imoveis>) results.values;
                ListaAdapter.this.notifyDataSetChanged();
            }
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Imoveis> filtradoResultados = null;
                if (charSequence.length() == 0){
                    filtradoResultados = mImoveis;
                }else{
                    filtradoResultados = getFiltradoResultados(charSequence.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filtradoResultados;

                return  results;
            }
        };
    }


    protected List<Imoveis> getFiltradoResultados(String char_sequence){
        List<Imoveis> results = new ArrayList<>();

        for (Imoveis imoveis : mImoveis){
            if (imoveis.getEndereco().toLowerCase().contains(char_sequence)){
                results.add(imoveis);
            }
        }
        return results;
    }


    public interface AoClicarNoImovelListener{
        void aoClicarNoItem(View view, int position, Imoveis imoveis);
    }

    @Override
    public int getItemCount() {
        return mImoveis != null? mImoveis.size() : 0;
    }

    public static class ImoveisViewHolder  extends RecyclerView.ViewHolder{

        @BindView(R.id.imgImovel)
        public ImageView imgImovel;

        @BindView(R.id.txtTipoImovel)
        public TextView txtTipoImovel;

        //@Nullable
        @BindView(R.id.txtPreco)
        public TextView txtPreco;

        @BindView(R.id.txtEndereco)
        public TextView txtEndereco;

        @BindView(R.id.txtDormitorios)
        public TextView txtDormitorios;

        @BindView(R.id.txtSuites)
        public TextView txtSuites;

        @BindView(R.id.txtVagas)
        public TextView txtVagas;

        @BindView(R.id.txtAreaUtil)
        public TextView txtAreaUtil;

        @BindView(R.id.txtAreaTotal)
        public TextView txtAreaTotal;

        @BindView(R.id.txtSubTipoOferta)
        public TextView txtSubTipoOferta;

        @BindView(R.id.txtDistLat)
        public TextView txtDistLat;

        @BindView(R.id.txtDistLong)
        public TextView txtDistLong;

        @BindView(R.id.txtDistancia)
        TextView txtDistancia;

        public ImoveisViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }

}
