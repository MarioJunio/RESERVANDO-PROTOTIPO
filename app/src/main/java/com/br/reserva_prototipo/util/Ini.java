package com.br.reserva_prototipo.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.br.reserva_prototipo.R;
import com.br.reserva_prototipo.model.Estabelecimento;
import com.br.reserva_prototipo.model.filter.FiltroEstabelecimentos;

import java.math.BigDecimal;

/**
 * Created by MarioJ on 19/05/16.
 */
public class Ini {

    public static void salvarPreferenciasFiltroEstabelecimentos(Context context, FiltroEstabelecimentos filtro) {

        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_filtro_estabelecimentos), Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();

        edit.putFloat(context.getString(R.string.distancia), (float) filtro.getDistancia());
        edit.putString(context.getString(R.string.cidade), filtro.getCidade());

        if (filtro.getHorario() != null)
            edit.putString(context.getString(R.string.horario), filtro.getHorario().name());

        if (filtro.getCategoria() != null)
            edit.putString(context.getString(R.string.categoria), filtro.getCategoria().name());

        if (filtro.getPrecoMin() != null)
            edit.putFloat(context.getString(R.string.preco_min), filtro.getPrecoMin().floatValue());

        if (filtro.getPrecoMax() != null)
            edit.putFloat(context.getString(R.string.preco_max), filtro.getPrecoMax().floatValue());

        edit.commit();
    }

    public static FiltroEstabelecimentos carregarPreferenciasFiltroEstabelecimentos(Context context) {

        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_filtro_estabelecimentos), Context.MODE_PRIVATE);

        float distancia = preferences.getFloat(context.getString(R.string.distancia), 0);
        String cidade = preferences.getString(context.getString(R.string.cidade), null);
        float minPreco = preferences.getFloat(context.getString(R.string.preco_min), 0);
        float maxPreco = preferences.getFloat(context.getString(R.string.preco_max), 0);
        String horario = preferences.getString(context.getString(R.string.horario), null);
        String categoria = preferences.getString(context.getString(R.string.categoria), null);

        FiltroEstabelecimentos filtroEstabelecimentos = new FiltroEstabelecimentos();
        filtroEstabelecimentos.setDistancia(distancia);
        filtroEstabelecimentos.setPrecoMin(BigDecimal.valueOf(minPreco));
        filtroEstabelecimentos.setPrecoMax(BigDecimal.valueOf(maxPreco));

        if (cidade != null)
            filtroEstabelecimentos.setCidade(cidade);

        if (horario != null)
            filtroEstabelecimentos.setHorario(Estabelecimento.Horario.valueOf(horario));

        if (categoria != null)
            filtroEstabelecimentos.setCategoria(Estabelecimento.Categoria.valueOf(categoria));

        return filtroEstabelecimentos;
    }

}
