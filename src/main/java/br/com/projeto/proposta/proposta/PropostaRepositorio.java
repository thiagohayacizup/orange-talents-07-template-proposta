package br.com.projeto.proposta.proposta;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PropostaRepositorio extends JpaRepository<Proposta, Long> {

    Optional<Proposta> findByDocumento( final String documento );

    List<Proposta> findFirst10ByStatusAndNumeroCartaoIsNull( final StatusProposta statusProposta );

}
