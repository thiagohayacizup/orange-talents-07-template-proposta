package br.com.projeto.proposta.aviso.viagem;

import br.com.projeto.proposta.cartao.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AvisoViagemRepositorio extends JpaRepository<AvisoViagem, Long> {

    Optional<AvisoViagem> findIdByCartao(final Cartao cartao );

    List<AvisoViagem> findFirst10ByStatus( final StatusAvisoViagem status );

}
