package com.br.reserva_prototipo.model;

import java.io.Serializable;

/**
 * Created by MarioJ on 10/03/16.
 */
public class Atrativo implements Serializable {

    private Long id;
    private Estabelecimento estabelecimento;
    private TipoAtrativo tipoAtrativo;
    private String descricao;

    public enum TipoAtrativo {

        WIFI("Wi-fi"), SHOW_AO_VIVO("Show ao Vivo"), AMBIENTE_ILUMINADO("Iluminação"), PROXIMO_AO_CENTRO("Próximo ao Centro");

        private String nome;

        TipoAtrativo(String nome) {
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

    public Estabelecimento getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(Estabelecimento estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    public TipoAtrativo getTipoAtrativo() {
        return tipoAtrativo;
    }

    public void setTipoAtrativo(TipoAtrativo tipoAtrativo) {
        this.tipoAtrativo = tipoAtrativo;
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

        Atrativo atrativo = (Atrativo) o;

        return !(id != null ? !id.equals(atrativo.id) : atrativo.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
