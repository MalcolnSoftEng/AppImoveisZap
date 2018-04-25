package malcoln.imobiliaria.model;

import java.io.Serializable;

/**
 * Created by Malcoln on 01/12/2017.
 */

public class Filtro implements Serializable{

    private int valor;
    private int quantDormitorios;
    private int quantSuites;
    private int quantVagas;


    public int getQuantVagas() {
        return quantVagas;
    }

    public void setQuantVagas(int quantVagas) {
        this.quantVagas = quantVagas;
    }


    public int getQuantSuites() {
        return quantSuites;
    }

    public void setQuantSuites(int quantSuites) {
        this.quantSuites = quantSuites;
    }

    public Filtro(int valor, int quantDormitorios, int quantSuites, int quantVagas) {
        this.valor = valor;
        this.quantDormitorios = quantDormitorios;
        this.quantSuites = quantSuites;
        this.quantVagas = quantVagas;
    }

    public int getQuantDormitorios() {

        return quantDormitorios;
    }

    public void setQuantDormitorios(int quantDormitorios) {
        this.quantDormitorios = quantDormitorios;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public Filtro() {

    }
}
