package br.com.vittasync.vittasync.Util;

import br.com.vittasync.vittasync.DTO.RelatorioPreviewDTO;
import java.nio.charset.StandardCharsets;

public class RelatorioCSVGenerator {
    public byte[] gerarRelatorio(RelatorioPreviewDTO preview) {
        // BOM identifica UTF-8 no Excel. Uma única tabela facilita importação estatística.
        StringBuilder csv = new StringBuilder("\uFEFF");
        linha(csv, "categoria", "id_registro", "paciente", "data_registro", "data_referencia",
                "peso_kg", "fc_bpm", "fr_rpm", "pa_sistolica_mmHg", "pa_diastolica_mmHg",
                "temperatura_C", "spo2_percentual", "sono_horas", "exercicio_minutos",
                "indice_repouso", "repouso", "canal", "sintoma", "intensidade_dor");
        if (preview.getCategorias().contains("SINAIS")) {
            preview.getSinaisVitais().forEach(s -> linha(csv,
                    "SINAIS", s.getId(), preview.getPaciente().getNome(), s.getDataHora(), null,
                    s.getPeso(), s.getFcBpm(), s.getFrRpm(), s.getPaSistolica(), s.getPaDiastolica(),
                    s.getTempCelcius(), s.getSpo2Porcento(), null, null, null, null, null, null, null));
        }
        if (preview.getCategorias().contains("HABITOS")) {
            preview.getHabitos().forEach(h -> linha(csv,
                    "HABITOS", h.getId(), preview.getPaciente().getNome(), h.getDataHora(), h.getDataReferencia(),
                    null, null, null, null, null, null, null, h.getHorasSono(), h.getMinutosExercicio(),
                    h.getIndiceRepouso(), h.getRepouso(), h.getCanal(), null, null));
        }
        if (preview.getCategorias().contains("SINTOMAS")) {
            preview.getSintomas().forEach(s -> linha(csv,
                    "SINTOMAS", s.getId(), preview.getPaciente().getNome(), s.getDataHora(), s.getDataReferencia(),
                    null, null, null, null, null, null, null, null, null, null, null, null,
                    s.getSintoma(), s.getIntensidadeDor()));
        }
        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private void linha(StringBuilder csv, Object... valores) {
        for (int i = 0; i < valores.length; i++) {
            if (i > 0) csv.append(';');
            Object valor = valores[i];
            if (valor == null) continue;
            String texto = valor.toString();
            // Textos livres não devem ser interpretados como fórmulas pela planilha.
            String inicio = texto.stripLeading();
            if (valor instanceof String && !texto.isEmpty()
                    && ((!inicio.isEmpty() && "=+-@".indexOf(inicio.charAt(0)) >= 0)
                        || "\t\r\n".indexOf(texto.charAt(0)) >= 0)) {
                texto = "'" + texto;
            }
            csv.append('"').append(texto.replace("\"", "\"\"")).append('"');
        }
        csv.append("\r\n");
    }
}
