package br.com.vittasync.vittasync.Util;

import java.util.Set;

public class FuncoesResponsavel {

    public static final String CUIDADOR = "cuidador";
    public static final String RESPONSAVEL_LEGAL = "responsavel_legal";
    public static final String ACOMPANHANTE = "acompanhante";
    public static final String CONTATO_EMERGENCIA = "contato_emergencia";
    public static final String TUTOR = "tutor";

    public static final Set<String> VALIDAS = Set.of(
            CUIDADOR,
            RESPONSAVEL_LEGAL,
            ACOMPANHANTE,
            CONTATO_EMERGENCIA,
            TUTOR
    );
}