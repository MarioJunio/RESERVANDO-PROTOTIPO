package com.br.reserva_prototipo.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by MarioJ on 10/03/16.
 */
public class Estabelecimento implements Serializable {

    private Long id;
    private String nomeFantasia;
    private byte[] fotoPerfil;
    private Categoria categoria;
    private long rateTotal;
    private long rates;
    private Calendar horarioAbrir;
    private Calendar horarioFechar;
    private int acomodacao;
    private Endereco endereco;
    private String descricao;

    public enum Categoria {

        RESTAURANTE("Restaurante"), LANCHONETE("Lanchonete"), CASA_NOTURNA("Casa Noturna");

        private String nome;

        Categoria(String nome) {
            this.nome = nome;
        }

        public String getNome() {
            return nome;
        }
    }

    public enum Horario {
        Aberto, Fechado
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(byte[] fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public long getRateTotal() {
        return rateTotal;
    }

    public void setRateTotal(long rateTotal) {
        this.rateTotal = rateTotal;
    }

    public long getRates() {
        return rates;
    }

    public void setRates(long rates) {
        this.rates = rates;
    }

    public Calendar getHorarioAbrir() {
        return horarioAbrir;
    }

    public void setHorarioAbrir(Calendar horarioAbrir) {
        this.horarioAbrir = horarioAbrir;
    }

    public Calendar getHorarioFechar() {
        return horarioFechar;
    }

    public void setHorarioFechar(Calendar horarioFechar) {
        this.horarioFechar = horarioFechar;
    }

    public int getAcomodacao() {
        return acomodacao;
    }

    public void setAcomodacao(int acomodacao) {
        this.acomodacao = acomodacao;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Estabelecimento that = (Estabelecimento) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
