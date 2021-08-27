package br.com.projeto.proposta.proposta.controlador;

import br.com.projeto.proposta.analise.financeira.AnaliseFinanceira;
import br.com.projeto.proposta.documento.validador.Documento;
import br.com.projeto.proposta.proposta.Proposta;
import br.com.projeto.proposta.proposta.PropostaRepositorio;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

class PropostaRequisicao {

    private final Proposta.Builder builder;

    PropostaRequisicao() {
        this.builder = Proposta.construtor();
    }

    @Documento
    @Size( min = 11, max = 18, message = "Documento com tamanho invalido.")
    private @NotBlank String documento;

    public void setDocumento(final String documento) {
        this.documento = documento;
    }

    public void setEmail(final String email) {
        builder.comEmail( email );
    }

    public void setNome(final String nome) {
        builder.comNome( nome );
    }

    public void setEndereco(final String endereco) {
        builder.comEndereco( endereco );
    }

    public void setSalario(final BigDecimal salario) {
        builder.comSalario( salario );
    }

    PropostaResposta criar(final PropostaRepositorio propostaRepositorio, final AnaliseFinanceira analiseFinanceira ){
        return new PropostaResposta(
                builder
                        .comDocumento( documento )
                        .construir()
                        .criar( propostaRepositorio, analiseFinanceira )
        );
    }

}
