package br.com.projeto.proposta.cartao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartaoRepositorio extends JpaRepository<Cartao, Long> {

    Optional<Cartao> findByNumero( final String numero );
}
