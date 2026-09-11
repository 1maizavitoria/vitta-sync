package br.com.vittasync.vittasync.Service;

import br.com.vittasync.vittasync.DTO.LinhaTempoHabitosDTO;
import br.com.vittasync.vittasync.DTO.LinhaTempoItemDTO;
import br.com.vittasync.vittasync.DTO.LinhaTempoResponseDTO;
import br.com.vittasync.vittasync.DTO.LinhaTempoSinaisVitaisDTO;
import br.com.vittasync.vittasync.DTO.LinhaTempoSintomasDTO;
import br.com.vittasync.vittasync.Exception.AcessoNegadoException;
import br.com.vittasync.vittasync.Exception.DadosInvalidosException;
import br.com.vittasync.vittasync.Model.Habitos;
import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Repository.DiarioSintomasRepository;
import br.com.vittasync.vittasync.Repository.HabitosRepository;
import br.com.vittasync.vittasync.Repository.SinaisVitaisRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class LinhaTempoService {

    private final HabitosRepository habitosRepository;
    private final SinaisVitaisRepository sinaisVitaisRepository;
    private final DiarioSintomasRepository diarioSintomasRepository;
    private final PermissaoService permissaoService;

    public LinhaTempoService(
            HabitosRepository habitosRepository,
            SinaisVitaisRepository sinaisVitaisRepository,
            DiarioSintomasRepository diarioSintomasRepository,
            PermissaoService permissaoService
    ) {
        this.habitosRepository = habitosRepository;
        this.sinaisVitaisRepository = sinaisVitaisRepository;
        this.diarioSintomasRepository = diarioSintomasRepository;
        this.permissaoService = permissaoService;
    }

    public LinhaTempoResponseDTO consultarLinhaTempo(
            Integer usuarioLogadoId,
            Integer pacienteId,
            LocalDate inicioInformado,
            LocalDate fimInformado
    ) {
        if (!permissaoService.podeVisualizarPaciente(usuarioLogadoId, pacienteId)) {
            throw new AcessoNegadoException("Você não tem permissão para visualizar este paciente.");
        }

        LocalDate fim = fimInformado != null ? fimInformado : LocalDate.now();
        LocalDate inicio = inicioInformado != null ? inicioInformado : fim.minusDays(6);

        if (inicio.isAfter(fim)) {
            throw new DadosInvalidosException("A data inicial não pode ser posterior à data final");
        }

        List<SinaisVitais> sinaisEntidades = sinaisVitaisRepository
                .findByPacienteIdAndDataRegistroBetweenOrderByDataRegistroAsc(
                        pacienteId,
                        inicio.atStartOfDay(),
                        fim.atTime(LocalTime.MAX)
                );

        List<Habitos> habitosEntidades = habitosRepository
                .findByPacienteIdAndDataReferenciaBetweenOrderByDataReferenciaAsc(
                        pacienteId,
                        inicio,
                        fim
                );

        var sintomasEntidades = diarioSintomasRepository
                .findByPacienteIdAndDataReferenciaBetweenOrderByDataReferenciaAsc(
                        pacienteId,
                        inicio,
                        fim
                );

        List<LinhaTempoSinaisVitaisDTO> sinais = sinaisEntidades.stream()
                .map(sv -> new LinhaTempoSinaisVitaisDTO(
                        sv.getId(),
                        sv.getDataRegistro(),
                        sv.getPeso(),
                        sv.getFcBpm(),
                        sv.getFrRpm(),
                        sv.getPaSistolica(),
                        sv.getPaDiastolica(),
                        sv.getTempCelcius(),
                        sv.getSpo2Porcento()
                ))
                .toList();

        List<LinhaTempoHabitosDTO> habitos = habitosEntidades.stream()
                .map(h -> new LinhaTempoHabitosDTO(
                        h.getId(),
                        h.getDataRegistro(),
                        h.getHorasSono(),
                        h.getMinutosExercicio(),
                        h.getIndiceRepouso(),
                        h.getRepouso(),
                        h.getCanal(),
                        h.getDataReferencia()
                ))
                .toList();

        List<LinhaTempoSintomasDTO> sintomas = sintomasEntidades.stream()
                .map(s -> new LinhaTempoSintomasDTO(
                        s.getId(),
                        s.getDataRegistro(),
                        s.getSintoma(),
                        s.getIntensidadeDor(),
                        s.getDataReferencia()
                ))
                .toList();

        List<LinhaTempoItemDTO> itens = new ArrayList<>();

        for (LinhaTempoSinaisVitaisDTO sinal : sinais) {
            LocalDateTime dataRegistro = sinal.getDataHora();
            itens.add(new LinhaTempoItemDTO(
                    sinal.getId(),
                    "SINAL_VITAL",
                    dataRegistro.toLocalDate(),
                    dataRegistro,
                    sinal
            ));
        }

        for (LinhaTempoHabitosDTO habito : habitos) {
            itens.add(new LinhaTempoItemDTO(
                    habito.getId(),
                    "HABITO",
                    habito.getDataReferencia(),
                    habito.getDataHora(),
                    habito
            ));
        }

        for (LinhaTempoSintomasDTO sintoma : sintomas) {
            itens.add(new LinhaTempoItemDTO(
                    sintoma.getId(),
                    "SINTOMA",
                    sintoma.getDataReferencia(),
                    sintoma.getDataHora(),
                    sintoma
            ));
        }

        itens.sort(
                Comparator.comparing(
                        LinhaTempoItemDTO::getDataReferencia,
                        Comparator.nullsLast(Comparator.reverseOrder())
                ).thenComparing(
                        LinhaTempoItemDTO::getDataRegistro,
                        Comparator.nullsLast(Comparator.reverseOrder())
                )
        );

        return new LinhaTempoResponseDTO(sinais, habitos, sintomas, itens);
    }
}
