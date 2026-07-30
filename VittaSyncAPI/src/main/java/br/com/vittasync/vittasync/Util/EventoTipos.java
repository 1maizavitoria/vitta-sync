package br.com.vittasync.vittasync.Util;

public class EventoTipos {

    public static final String SINAIS_VITAIS_CRIADOS = "sinais_vitais_criados";
    public static final String SINAIS_VITAIS_EDITADOS = "sinais_vitais_editados";
    public static final String SINAIS_VITAIS_REMOVIDOS = "sinais_vitais_removidos";

    public static final String HABITOS_CRIADOS = "habito_registrado";
    public static final String HABITOS_EDITADOS = "habito_editado";
    public static final String HABITOS_REMOVIDOS = "habito_removido";

    public static final String SINTOMAS_CRIADOS = "sintoma_registrado";
    public static final String SINTOMAS_EDITADOS = "sintoma_editado";
    public static final String SINTOMAS_REMOVIDOS = "sintoma_removido";

    public static final String VINCULO_CRIADO = "vinculo_criado";
    public static final String VINCULO_REMOVIDO = "vinculo_removido";

    public static final String LEMBRETE_CRIADO = "lembrete_criado";
    public static final String LEMBRETE_ATUALIZADO = "lembrete_atualizado";

    public static final String PRESSAO_ANORMAL = "pressao_anormal";
    public static final String FEBRE_DETECTADA = "febre_detectada";
    public static final String SPO2_BAIXA = "spo2_baixa";
    public static final String SPO2_CRITICA = "spo2_critica";

    public static final String TAQUICARDIA_DETECTADA = "taquicardia_detectada";
    public static final String BRADICARDIA_DETECTADA = "bradicardia_detectada";

    public static final String TAQUIPNEIA_DETECTADA = "taquipneia_detectada";
    public static final String BRADIPNEIA_DETECTADA = "bradipneia_detectada";

    public static final String DOR_INTENSA_DETECTADA = "dor_intensa_detectada";
    public static final String DOR_CRITICA_DETECTADA = "dor_critica_detectada";

    public static final String SONO_CRITICO = "sono_critico";
    public static final String SEDENTARISMO_DETECTADO = "sedentarismo_detectado";

    public static final String DOCUMENTO_ENVIADO = "documento_enviado";

    public static final String DOCUMENTO_REMOVIDO = "documento_removido";

    private EventoTipos() {
    }

    public static String toFrontendCode(String tipoEvento) {
        if (tipoEvento == null) {
            return null;
        }

        return switch (tipoEvento) {
            case DOCUMENTO_ENVIADO -> "DOCUMENT_UPLOADED";
            case DOCUMENTO_REMOVIDO -> "DOCUMENT_REMOVED";
            case SINAIS_VITAIS_CRIADOS -> "VITAL_SIGNS_CREATED";
            case SINAIS_VITAIS_EDITADOS -> "VITAL_SIGNS_UPDATED";
            case SINAIS_VITAIS_REMOVIDOS -> "VITAL_SIGNS_REMOVED";
            case HABITOS_CRIADOS -> "HABITS_CREATED";
            case HABITOS_EDITADOS -> "HABITS_UPDATED";
            case HABITOS_REMOVIDOS -> "HABITS_REMOVED";
            case SINTOMAS_CRIADOS -> "SYMPTOMS_CREATED";
            case SINTOMAS_EDITADOS -> "SYMPTOMS_UPDATED";
            case SINTOMAS_REMOVIDOS -> "SYMPTOMS_REMOVED";
            case VINCULO_CRIADO -> "LINK_CREATED";
            case VINCULO_REMOVIDO -> "LINK_REMOVED";
            case LEMBRETE_CRIADO -> "REMINDER_CREATED";
            case LEMBRETE_ATUALIZADO -> "REMINDER_UPDATED";
            case PRESSAO_ANORMAL -> "HIGH_BLOOD_PRESSURE";
            case FEBRE_DETECTADA -> "FEVER_DETECTED";
            case SPO2_BAIXA -> "LOW_OXYGEN_SATURATION";
            case SPO2_CRITICA -> "CRITICAL_OXYGEN_SATURATION";
            case TAQUICARDIA_DETECTADA -> "HIGH_HEART_RATE";
            case BRADICARDIA_DETECTADA -> "LOW_HEART_RATE";
            case TAQUIPNEIA_DETECTADA -> "HIGH_RESPIRATORY_RATE";
            case BRADIPNEIA_DETECTADA -> "LOW_RESPIRATORY_RATE";
            case DOR_INTENSA_DETECTADA -> "INTENSE_PAIN_DETECTED";
            case DOR_CRITICA_DETECTADA -> "CRITICAL_PAIN_DETECTED";
            case SONO_CRITICO -> "CRITICAL_SLEEP";
            case SEDENTARISMO_DETECTADO -> "LOW_PHYSICAL_ACTIVITY";
            default -> tipoEvento;
        };
    }
}
