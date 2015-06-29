package com.clubee.doggywalker;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by barcat on 6/26/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DAOPostmon {

    private String cep;
    private String bairro;
    private String cidade;
    private String estado;
    private String complemento;
    private String unidade;
    private String cliente;
    private String endereco;
    private String logradouro;
    private String estado_info;
    private String cidade_info;



    public String getBairro() {
        return this.bairro;
    }

    public String getCidade() {
        return this.cidade;
    }

    public String getEndereco() {
        return this.endereco;
    }

    public String getLogradouro() {
        return this.logradouro;
    }

    public String getCep() {
        return this.cep;
    }

    public String getEstado() {
        return this.estado;
    }

}