package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.Model.LinhaBase;
import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Repository.LinhaBaseRepository;
import br.com.vittasync.vittasync.Repository.SinaisVitaisRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class LinhaBaseService {

    private final LinhaBaseRepository linhaBaseRepository;
    private final SinaisVitaisRepository sinaisVitaisRepository;

    public static final List<String> SINAIS = List.of(
            "peso",
            "frequencia_cardiaca",
            "frequencia_respiratoria",
            "pressao_sistolica",
            "pressao_diastolica",
            "temperatura",
            "saturacao"
    );

    public static final int diasNecessarios = 14;
    private static final int desviosPadraoFaixa = 2;

    public LinhaBaseService(
            LinhaBaseRepository linhaBaseRepository,
            SinaisVitaisRepository sinaisVitaisRepository
    ) {
        this.linhaBaseRepository = linhaBaseRepository;
        this.sinaisVitaisRepository = sinaisVitaisRepository;
    }


    public void atualizarLinhasBase(Integer pacienteId) {
        List<SinaisVitais> sinais = sinaisVitaisRepository.findByPacienteIdOrderByDataRegistroAsc(pacienteId);

        for (String sinal : SINAIS) {
            if (linhaBaseRepository.existsByPacienteIdAndSinal(pacienteId, sinal)) continue;

            TreeMap<LocalDate, Double> mediasDiarias = calcularMediasDiarias(sinais, sinal);
            List<LocalDate> periodo = buscarPrimeiraSequenciaCompleta(mediasDiarias);
            if (periodo != null) {
                formarLinhaBase(pacienteId, sinal, periodo, mediasDiarias);
            }
        }
    }

    public int calcularProgresso(Integer pacienteId, String sinal) {
        List<SinaisVitais> sinais = sinaisVitaisRepository.findByPacienteIdOrderByDataRegistroAsc(pacienteId);
        TreeMap<LocalDate, Double> mediasDiarias = calcularMediasDiarias(sinais, sinal);

        LocalDate hoje = LocalDate.now();
        LocalDate dia = mediasDiarias.containsKey(hoje) ? hoje : hoje.minusDays(1);

        int dias = 0;
        while (mediasDiarias.containsKey(dia) && dias < diasNecessarios) {
            dias++;
            dia = dia.minusDays(1);
        }
        return dias;
    }

    public String comparar(LinhaBase linhaBase, Number valor) {
        if (linhaBase == null || valor == null
                || linhaBase.getLimiteInferior() == null || linhaBase.getLimiteSuperior() == null) {
            return null;
        }

        double numero = valor.doubleValue();
        if (numero < linhaBase.getLimiteInferior()) return "abaixo";
        if (numero > linhaBase.getLimiteSuperior()) return "acima";
        return "dentro";
    }

    public Number valorSinal(SinaisVitais sinais, String sinal) {
        return switch (sinal) {
            case "peso" -> sinais.getPeso();
            case "frequencia_cardiaca" -> sinais.getFcBpm();
            case "frequencia_respiratoria" -> sinais.getFrRpm();
            case "pressao_sistolica" -> sinais.getPaSistolica();
            case "pressao_diastolica" -> sinais.getPaDiastolica();
            case "temperatura" -> sinais.getTempCelcius();
            case "saturacao" -> sinais.getSpo2Porcento();
            default -> null;
        };
    }

    private TreeMap<LocalDate, Double> calcularMediasDiarias(List<SinaisVitais> sinais, String sinal) {
        Map<LocalDate, List<Double>> valoresPorDia = new HashMap<>();

        for (SinaisVitais registro : sinais) {
            Number valor = valorSinal(registro, sinal);
            if (valor == null || registro.getDataRegistro() == null) continue;

            valoresPorDia
                    .computeIfAbsent(registro.getDataRegistro().toLocalDate(), k -> new ArrayList<>())
                    .add(valor.doubleValue());
        }

        TreeMap<LocalDate, Double> mediasDiarias = new TreeMap<>();
        for (var entry : valoresPorDia.entrySet()) {
            mediasDiarias.put(entry.getKey(), calcularMedia(entry.getValue()));
        }
        return mediasDiarias;
    }

    private List<LocalDate> buscarPrimeiraSequenciaCompleta(TreeMap<LocalDate, Double> mediasDiarias) {
        List<LocalDate> sequencia = new ArrayList<>();

        for (LocalDate dia : mediasDiarias.keySet()) {
            if (!sequencia.isEmpty() && !dia.equals(sequencia.get(sequencia.size() - 1).plusDays(1))) {
                sequencia.clear();
            }
            sequencia.add(dia);

            if (sequencia.size() == diasNecessarios) return sequencia;
        }
        return null;
    }

    private void formarLinhaBase(Integer pacienteId,
                                 String sinal,
                                 List<LocalDate> periodo,
                                 TreeMap<LocalDate, Double> mediasDiarias) {
        List<Double> valores = periodo.stream().map(mediasDiarias::get).toList();

        double media = calcularMedia(valores);
        double desvioPadrao = calcularDesvioPadraoAmostral(valores, media);

        LinhaBase linhaBase = new LinhaBase();
        linhaBase.setPacienteId(pacienteId);
        linhaBase.setSinal(sinal);
        linhaBase.setDataInicio(periodo.get(0));
        linhaBase.setDataFim(periodo.get(periodo.size() - 1));
        linhaBase.setMedia(arredondar(media));
        linhaBase.setDesvioPadrao(arredondar(desvioPadrao));

        if (desvioPadrao > 0) {
            linhaBase.setLimiteInferior(arredondar(media - desviosPadraoFaixa * desvioPadrao));
            linhaBase.setLimiteSuperior(arredondar(media + desviosPadraoFaixa * desvioPadrao));
        }

        linhaBase.setDataFormacao(LocalDateTime.now());
        linhaBaseRepository.save(linhaBase);
    }

    private double calcularMedia(List<Double> valores) {
        return valores.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    private double calcularDesvioPadraoAmostral(List<Double> valores, double media) {
        double somaQuadrados = valores.stream().mapToDouble(v -> Math.pow(v - media, 2)).sum();
        return Math.sqrt(somaQuadrados / (valores.size() - 1));
    }

    private double arredondar(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
