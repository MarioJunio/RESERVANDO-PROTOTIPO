package com.br.reserva_prototipo.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by MarioJ on 10/03/16.
 */
public class PromocaoReserva implements Serializable {

    private Long id;
    private TipoReserva tipoReserva;
    private Date dataInicio;
    private Date fataFim;
    private float desconto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoReserva getTipoReserva() {
        return tipoReserva;
    }

    public void setTipoReserva(TipoReserva tipoReserva) {
        this.tipoReserva = tipoReserva;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getFataFim() {
        return fataFim;
    }

    public void setFataFim(Date fataFim) {
        this.fataFim = fataFim;
    }

    public float getDesconto() {
        return desconto;
    }

    public void setDesconto(float desconto) {
        this.desconto = desconto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PromocaoReserva that = (PromocaoReserva) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
