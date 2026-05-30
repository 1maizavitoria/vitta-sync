package br.com.vittasync.vittasync.Util;

import java.util.Set;

public class FuncoesSaude {

    public static final String MEDICO_PRINCIPAL = "medico_principal";

    public static final String ESPECIALISTA = "especialista";

    public static final String CONSULTOR = "consultor";

    public static final String ACOMPANHAMENTO_CLINICO = "acompanhamento_clinico";

    public static final String EQUIPE_ASSISTENCIAL = "equipe_assistencial";

    public static final Set<String> VALIDAS = Set.of(MEDICO_PRINCIPAL, ESPECIALISTA, CONSULTOR, ACOMPANHAMENTO_CLINICO, EQUIPE_ASSISTENCIAL);
}
