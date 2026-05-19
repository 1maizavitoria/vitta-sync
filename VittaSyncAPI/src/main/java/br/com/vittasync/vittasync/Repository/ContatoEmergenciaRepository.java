package br.com.vittasync.vittasync.Repository;


import br.com.vittasync.vittasync.Model.ContatoEmergencia;
import br.com.vittasync.vittasync.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ContatoEmergenciaRepository extends JpaRepository<ContatoEmergencia, Integer> {

    List<ContatoEmergencia> findByPaciente(Usuario paciente);

    int countByPaciente(Usuario paciente);
}
