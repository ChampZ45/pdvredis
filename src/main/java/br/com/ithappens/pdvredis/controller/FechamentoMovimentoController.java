package br.com.ithappens.pdvredis.controller;

import br.com.ithappens.pdvredis.mapper.tesouraria.RedisCache;
import br.com.ithappens.pdvredis.mapper.tesouraria.TesourariaMovimentoMapper;
import br.com.ithappens.pdvredis.model.tesouraria.FechamentoLoja;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Profile("dev")
@RestController
@Slf4j
@RequestMapping("/redis")
public class FechamentoMovimentoController {

    @Autowired
    private TesourariaMovimentoMapper tesourariaMovimentoMapper;

    @GetMapping(value = "/fechamentosFinalizacaoAutomatica")
    public ResponseEntity valorDinheiroTesourariaEncerrado(){
        RedisCache redisCache = new RedisCache("0");
        FechamentoLoja fechamentoLoja = null;
        List<FechamentoLoja> lista  = (List<FechamentoLoja>) redisCache.getObject("teste");
        if(lista == null || lista.isEmpty())
                lista = tesourariaMovimentoMapper.recuperarFechamentosParaFinalizacaoAutomatica();
        else {
            fechamentoLoja = lista.get(0);
            lista.remove(0);
        }
        redisCache.putObject("teste",lista);
        return ResponseEntity.ok(lista);
    }

    @PostMapping(value = "/atualizarFechamento")
    public ResponseEntity atualizarFechamento(@RequestBody FechamentoLoja fechamentoLoja){
        tesourariaMovimentoMapper.atualizarFechamentoLojaParaFechado(fechamentoLoja,fechamentoLoja.getStatus().getId());
        return ResponseEntity.ok("Atualizado com sucesso!");
    }
}
