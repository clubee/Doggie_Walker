package com.clubee.doggywalker;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by barcat on 6/26/15.
 */
public class DAOPostmon {
    @JsonIgnoreProperties(ignoreUnknown = true)

    private String cep;
    private String bairro;
    private String cidade;
    private String estado;
    private String complemento;
    private String unidade;
    private String cliente;
    private String endereco;
    private String logradouro;


    public String getBairro() {
        return this.bairro;
    }

    public String getCidade() {
        return this.cidade;
    }

    @JsonIgnore
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

    public String getCliente() {
        return this.cliente;
    }
}