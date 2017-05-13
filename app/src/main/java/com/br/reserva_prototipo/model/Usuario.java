package com.br.reserva_prototipo.model;

import com.br.reserva_prototipo.util.App;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MarioJ on 10/03/16.
 */
public class Usuario implements Serializable {

    public static final String EXTRA_KEY = "obj_usuario";

    @Expose
    private Long id;

    @Expose
    private String nome;

    @Expose
    private String email;

    @Expose
    private String senha;

    @Expose
    private String cpf;

    @Expose
    private Date dataNascimento;

    private byte[] foto;
    private InputStream fotoPerfil;

    public Usuario(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public Usuario() {
    }

    public enum Fields {
        id, nome, foto, email, senha, cpf, dataNascimento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void codificarSenha() {
        setSenha(App.SHA256Hex(senha.getBytes()));
    }

    public String getDataNascimentoFormatadaParaEnvio() {
        return new SimpleDateFormat("yyyy-MM-dd").format(getDataNascimento());
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public InputStream getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(InputStream fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usuario usuario = (Usuario) o;

        return !(id != null ? !id.equals(usuario.id) : usuario.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public String toJson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("yyyy-MM-dd").create().toJson(this);
    }
}
