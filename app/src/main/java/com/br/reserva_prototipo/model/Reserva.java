package com.br.reserva_prototipo.model;

import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by MarioJ on 10/03/16.
 */
public class Reserva implements Serializable {

    private Long id;
    private Usuario usuario;
    private Estabelecimento estabelecimento;
    private Date dataReserva;
    private int quantidadePessoas;
    private Date data;
    private TipoReserva tipoReserva;
    private BigDecimal total;
    private StatusReserva statusReserva;

    public enum StatusReserva {
        PENDENTE("Pendente"), REJEITADO("Rejeitado"), CONFIRMADO("Confirmado");

        private String nome;

        StatusReserva(String nome) {
            this.nome = nome;
        }

        public String getNome() {
            return nome;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public TipoReserva setCliente(Usuario usuario) {
        this.usuario = usuario;
        return null;
    }

    public Estabelecimento getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(Estabelecimento estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    public Date getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(Date dataReserva) {
        this.dataReserva = dataReserva;
    }

    public int getQuantidadePessoas() {
        return quantidadePessoas;
    }

    public void setQuantidadePessoas(int quantidadePessoas) {
        this.quantidadePessoas = quantidadePessoas;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public TipoReserva getTipoReserva() {
        return tipoReserva;
    }

    public void setTipoReserva(TipoReserva tipoReserva) {
        this.tipoReserva = tipoReserva;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public StatusReserva getStatusReserva() {
        return statusReserva;
    }

    public void setStatusReserva(StatusReserva statusReserva) {
        this.statusReserva = statusReserva;
    }

    public String toJson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create().toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reserva reserva = (Reserva) o;

        return !(id != null ? !id.equals(reserva.id) : reserva.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", usuario=" + usuario.getId() +
                ", estabelecimento=" + estabelecimento.getId() +
                ", dataReserva=" + dataReserva +
                ", quantidadePessoas=" + quantidadePessoas +
                ", data=" + data +
                ", tipoReserva=" + tipoReserva.getId() +
                ", total=" + total +
                ", statusReserva=" + statusReserva.nome +
                '}';
    }
}
