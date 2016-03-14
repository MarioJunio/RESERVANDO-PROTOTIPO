package com.br.reserva_prototipo.model;

import java.io.Serializable;

/**
 * Created by MarioJ on 10/03/16.
 */
public class Estado implements Serializable {

    private Long id;
    private String uf;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Estado estado = (Estado) o;

        return !(id != null ? !id.equals(estado.id) : estado.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
