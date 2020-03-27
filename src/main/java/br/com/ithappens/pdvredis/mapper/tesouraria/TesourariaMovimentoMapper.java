package br.com.ithappens.pdvredis.mapper.tesouraria;

import br.com.ithappens.pdvredis.model.tesouraria.FechamentoLoja;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TesourariaMovimentoMapper {

    List<FechamentoLoja> recuperarFechamentosParaFinalizacaoAutomatica();

    void atualizarFechamentoLojaParaFechado(@Param("fechamentoLoja") FechamentoLoja fechamentoLoja,
                                            @Param("status") Integer status);
}
