package malcoln.imobiliaria.model;

import java.io.Serializable;

/**
 * Created by Malcoln on 28/11/2017.
 */

public class Imoveis implements Serializable{

    private String CodImovel;
    private String TipoImovel;
    private String Endereco;
    private String PrecoVenda;
    private String Dormitorios;
    private String Suites;
    private String Vagas;
    private String AreaUtil;
    private String AreaTotal;
    private String DataAtualizacao;
    private String NomeFantasia;

    public String latModel;
    public String longModel;

    private String distancia;

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public String getLatModel() {
        return latModel;
    }

    public void setLatModel(String latModel) {
        this.latModel = latModel;
    }

    public String getLongModel() {
        return longModel;
    }

    public void setLongModel(String longModel) {
        this.longModel = longModel;
    }

    //MOCKANDO FOTOS IMOVEIS
    private String[] fotos_imoveis;

    public String[] getFotos_imoveis() {
        return fotos_imoveis;
    }

    public void setFotos_imoveis(String[] fotos_imoveis) {
        this.fotos_imoveis = fotos_imoveis;
    }

    public String getEnderecoFormatado() {
        return EnderecoFormatado;
    }

    public void setEnderecoFormatado(String enderecoFormatado) {
        EnderecoFormatado = enderecoFormatado;
    }

    private String EnderecoFormatado;


    public String getCodCliente() {
        return CodCliente;
    }

    public void setCodCliente(String codCliente) {
        CodCliente = codCliente;
    }

    private String CodCliente;
    private String UrlImagem;
    private String SubTipoOferta;
    private String SubtipoImovel;


    public String getCodImovel() {
        return CodImovel;
    }

    public void setCodImovel(String codImovel) {
        CodImovel = codImovel;
    }

    public String getTipoImovel() {
        return TipoImovel;
    }

    public void setTipoImovel(String tipoImovel) {
        TipoImovel = tipoImovel;
    }

    public String getEndereco() {
        return Endereco;
    }

    public void setEndereco(String endereco) {
        Endereco = endereco;
    }

    public String getPrecoVenda() {
        return PrecoVenda;
    }

    public void setPrecoVenda(String precoVenda) {
        PrecoVenda = precoVenda;
    }

    public String getDormitorios() {
        return Dormitorios;
    }

    public void setDormitorios(String dormitorios) {
        Dormitorios = dormitorios;
    }

    public String getSuites() {
        return Suites;
    }

    public void setSuites(String suites) {
        Suites = suites;
    }

    public String getVagas() {
        return Vagas;
    }

    public void setVagas(String vagas) {
        Vagas = vagas;
    }

    public String getAreaUtil() {
        return AreaUtil;
    }

    public void setAreaUtil(String areaUtil) {
        AreaUtil = areaUtil;
    }

    public String getAreaTotal() {
        return AreaTotal;
    }

    public void setAreaTotal(String areaTotal) {
        AreaTotal = areaTotal;
    }

    public String getDataAtualizacao() {
        return DataAtualizacao;
    }

    public void setDataAtualizacao(String dataAtualizacao) {
        DataAtualizacao = dataAtualizacao;
    }

    public String getNomeFantasia() {
        return NomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        NomeFantasia = nomeFantasia;
    }

    public String getUrlImagem() {
        return UrlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        UrlImagem = urlImagem;
    }

    public String getSubTipoOferta() {
        return SubTipoOferta;
    }

    public void setSubTipoOferta(String subTipoOferta) {
        SubTipoOferta = subTipoOferta;
    }

    public String getSubtipoImovel() {
        return SubtipoImovel;
    }

    public void setSubtipoImovel(String subtipoImovel) {
        SubtipoImovel = subtipoImovel;
    }

    public Imoveis() {
    }

    @Override
    public String toString() {
        return TipoImovel;
    }
}
