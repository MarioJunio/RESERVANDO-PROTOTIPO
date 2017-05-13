package com.br.reserva_prototipo.model.filter;

import com.br.reserva_prototipo.model.Estabelecimento;
import com.google.gson.GsonBuilder;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by MarioJ on 27/04/16.
 */
public class FiltroEstabelecimentos {

    // atributos filtraveis
    private double distancia;
    private Coordenada coordenada;
    private String cidade;
    private BigDecimal precoMin;
    private BigDecimal precoMax;
    private Estabelecimento.Horario horario;
    private Date data;
    private Estabelecimento.Categoria categoria;

    // atributos ordenaveis
    private Estabelecimento.SortField ordenarPor;
    private boolean ordenacaoAsc;

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public Coordenada getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(Coordenada coordenada) {
        this.coordenada = coordenada;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public BigDecimal getPrecoMin() {
        return precoMin;
    }

    public void setPrecoMin(BigDecimal precoMin) {
        this.precoMin = precoMin;
    }

    public BigDecimal getPrecoMax() {
        return precoMax;
    }

    public void setPrecoMax(BigDecimal precoMax) {
        this.precoMax = precoMax;
    }

    public Estabelecimento.Horario getHorario() {
        return horario;
    }

    public void setHorario(Estabelecimento.Horario horario) {
        this.horario = horario;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Estabelecimento.Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Estabelecimento.Categoria categoria) {
        this.categoria = categoria;
    }

    public Estabelecimento.SortField getOrdenarPor() {
        return ordenarPor;
    }

    public void setOrdenarPor(Estabelecimento.SortField ordenarPor) {
        this.ordenarPor = ordenarPor;
    }

    public boolean isOrdenacaoAsc() {
        return ordenacaoAsc;
    }

    public void setOrdenacaoAsc(boolean ordenacaoAsc) {
        this.ordenacaoAsc = ordenacaoAsc;
    }

    public String toJson() {
        return new GsonBuilder().setDateFormat("HH:mm:ss zzz").create().toJson(this);
    }

    @Override
    public String toString() {
        return "FiltroEstabelecimentos{" +
                "distancia=" + distancia +
                ", coordenada=" + coordenada +
                ", cidade='" + cidade + '\'' +
                ", precoMin=" + precoMin +
                ", precoMax=" + precoMax +
                ", horario=" + horario +
                ", data=" + data +
                ", categoria=" + categoria +
                ", ordenarPor=" + ordenarPor +
                ", ordenacaoAsc=" + ordenacaoAsc +
                '}';
    }
}
