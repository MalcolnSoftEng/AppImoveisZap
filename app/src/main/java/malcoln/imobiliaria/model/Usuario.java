package malcoln.imobiliaria.model;

import java.io.Serializable;

/**
 * Created by Malcoln on 30/11/2017.
 */

public class Usuario implements Serializable{
    public String nome;
    public String email;
    public String telefone;
    public String codigoImovel;


    public Usuario() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCodigoImovel() {
        return codigoImovel;
    }

    public void setCodigoImovel(String codigoImovel) {
        this.codigoImovel = codigoImovel;
    }

    public Usuario(String nome, String email, String telefone, String codigoImovel) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.codigoImovel = codigoImovel;
    }
}
