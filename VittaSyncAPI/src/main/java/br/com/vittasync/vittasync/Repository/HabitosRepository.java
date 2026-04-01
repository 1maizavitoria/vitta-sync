package br.com.vittasync.vittasync.Repository;


import br.com.vittasync.vittasync.Model.Habitos;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface HabitosRepository extends JpaRepository<Habitos, Integer> {
    List<Habitos> findByPacienteCpf(String cpf);
}
