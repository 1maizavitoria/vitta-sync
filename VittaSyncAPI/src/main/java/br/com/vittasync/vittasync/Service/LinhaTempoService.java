package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.DTO.LinhaTempoSinaisVitaisDTO;
import br.com.vittasync.vittasync.DTO.LinhaTempoHabitosDTO;
import br.com.vittasync.vittasync.DTO.LinhaTempoSintomasDTO;
import br.com.vittasync.vittasync.Repository.HabitosRepository;
import br.com.vittasync.vittasync.Repository.SinaisVitaisRepository;
import br.com.vittasync.vittasync.Repository.DiarioSintomasRepository;
import br.com.vittasync.vittasync.Exception.AcessoNegadoException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LinhaTempoService {

    private final HabitosRepository habitosRepository;
    private final SinaisVitaisRepository sinaisVitaisRepository;
    private final DiarioSintomasRepository diarioSintomasRepository;
    private final PermissaoService permissaoService;

    public LinhaTempoService(HabitosRepository habitosRepository,
                             SinaisVitaisRepository sinaisVitaisRepository,
                             DiarioSintomasRepository diarioSintomasRepository,
                             PermissaoService permissaoService) {
        this.habitosRepository = habitosRepository;
        this.sinaisVitaisRepository = sinaisVitaisRepository;
        this.diarioSintomasRepository = diarioSintomasRepository;
        this.permissaoService = permissaoService;
    }

    public List<LinhaTempoSinaisVitaisDTO> consultarSinaisVitais(Integer usuarioLogadoId,
                                                                 Integer pacienteId,
                                                                 LocalDate inicio,
                                                                 LocalDate fim) {
        if (!permissaoService.podeVisualizarPaciente(usuarioLogadoId, pacienteId)) {
            throw new AcessoNegadoException("Você não tem permissão para visualizar este paciente.");
        }

        return sinaisVitaisRepository.findByPacienteIdOrderByDataRegistroAsc(pacienteId)
                .stream()
                .filter(sv -> (inicio == null || !sv.getDataRegistro().toLocalDate().isBefore(inicio)) &&
                        (fim == null || !sv.getDataRegistro().toLocalDate().isAfter(fim)))
                .map(sv -> new LinhaTempoSinaisVitaisDTO(
                        sv.getDataRegistro(),
                        sv.getPeso(),
                        sv.getFcBpm(),
                        sv.getFrRpm(),
                        sv.getPaSistolica(),
                        sv.getPaDiastolica(),
                        sv.getTempCelcius(),
                        sv.getSpo2Porcento()
                ))
                .collect(Collectors.toList());
    }

    public List<LinhaTempoHabitosDTO> consultarHabitos(Integer usuarioLogadoId,
                                                       Integer pacienteId,
                                                       LocalDate inicio,
                                                       LocalDate fim) {
        if (!permissaoService.podeVisualizarPaciente(usuarioLogadoId, pacienteId)) {
            throw new AcessoNegadoException("Você não tem permissão para visualizar este paciente.");
        }

        return habitosRepository.findByPacienteIdOrderByDataRegistroAsc(pacienteId)
                .stream()
                .filter(h -> (inicio == null || !h.getDataRegistro().toLocalDate().isBefore(inicio)) &&
                        (fim == null || !h.getDataRegistro().toLocalDate().isAfter(fim)))
                .map(h -> new LinhaTempoHabitosDTO(
                        h.getDataRegistro(),
                        h.getHorasSono(),
                        h.getMinutosExercicio(),
                        h.getIndiceRepouso(),
                        h.getRepouso(),
                        h.getCanal(),
                        h.getDataReferencia()
                ))
                .collect(Collectors.toList());
    }

    public List<LinhaTempoSintomasDTO> consultarSintomas(Integer usuarioLogadoId,
                                                         Integer pacienteId,
                                                         LocalDate inicio,
                                                         LocalDate fim) {
        if (!permissaoService.podeVisualizarPaciente(usuarioLogadoId, pacienteId)) {
            throw new AcessoNegadoException("Você não tem permissão para visualizar este paciente.");
        }

        return diarioSintomasRepository.findByPacienteIdOrderByDataRegistroAsc(pacienteId)
                .stream()
                .filter(s -> (inicio == null || !s.getDataRegistro().toLocalDate().isBefore(inicio)) &&
                        (fim == null || !s.getDataRegistro().toLocalDate().isAfter(fim)))
                .map(s -> new LinhaTempoSintomasDTO(
                        s.getDataRegistro(),
                        s.getSintoma(),
                        s.getIntensidadeDor(),
                        s.getDataReferencia()
                ))
                .collect(Collectors.toList());
    }
}
