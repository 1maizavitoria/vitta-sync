package br.com.vittasync.vittasync.Util;


import br.com.vittasync.vittasync.DTO.RelatorioPreviewDTO;
import java.nio.charset.StandardCharsets;


public class RelatorioCSVGenerator {

    public byte[] gerarRelatorio(RelatorioPreviewDTO preview) {
        StringBuilder sb = new StringBuilder();

        sb.append("Paciente;").append(preview.getPaciente().getNome()).append("\n");
        sb.append("CPF;").append(preview.getPaciente().getCpf()).append("\n");
        sb.append("Altura;").append(preview.getPaciente().getAltura()).append("\n");
        sb.append("Peso Inicial;").append(preview.getPaciente().getPesoInicial()).append("\n");

        sb.append("\nSinais Vitais\n");
        sb.append("Data;Peso;FC;FR;PA Sistólica;PA Diastólica;Temp;SpO2\n");
        preview.getSinaisVitais().forEach(s -> sb.append(
                s.getDataHora() + ";" +
                        s.getPeso() + ";" +
                        s.getFcBpm() + ";" +
                        s.getFrRpm() + ";" +
                        s.getPaSistolica() + ";" +
                        s.getPaDiastolica() + ";" +
                        s.getTempCelcius() + ";" +
                        s.getSpo2Porcento() + "\n"
        ));

        sb.append("\nHábitos\n");
        sb.append("Data;Sono;Exercício;Índice Repouso;Repouso;Canal;Data Referência\n");
        preview.getHabitos().forEach(h -> sb.append(
                h.getDataHora() + ";" +
                        h.getHorasSono() + ";" +
                        h.getMinutosExercicio() + ";" +
                        h.getIndiceRepouso() + ";" +
                        h.getRepouso() + ";" +
                        h.getCanal() + ";" +
                        h.getDataReferencia() + "\n"
        ));

        sb.append("\nSintomas\n");
        sb.append("Data;Sintoma;Intensidade;Data Referência\n");
        preview.getSintomas().forEach(s -> sb.append(
                s.getDataHora() + ";" +
                        s.getSintoma() + ";" +
                        s.getIntensidadeDor() + ";" +
                        s.getDataReferencia() + "\n"
        ));

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}
