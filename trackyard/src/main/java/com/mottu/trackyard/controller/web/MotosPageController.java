package com.mottu.trackyard.controller.web;

import com.mottu.trackyard.dto.MotosDTO;
import com.mottu.trackyard.dto.MovimentacoesDTO;
import com.mottu.trackyard.dto.PontosLeituraDTO;
import com.mottu.trackyard.service.MotoService;
import com.mottu.trackyard.service.MovimentacaoService;
import com.mottu.trackyard.service.PontosLeituraService;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/motos")
public class MotosPageController {

    private final MotoService motoService;
    private final MovimentacaoService movService;
    private final PontosLeituraService pontoService;

    public MotosPageController(MotoService motoService,
            MovimentacaoService movService,
            PontosLeituraService pontoService) {
				this.motoService = motoService;
				this.movService = movService;
				this.pontoService = pontoService;
				}

    
    private void carregarPontos(Model model) {
        List<PontosLeituraDTO> pontos = pontoService.getAllPontosLeitura(PageRequest.of(0, 1000)).getContent();
        model.addAttribute("pontos", pontos);
    }

    
    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {
        Page<MotosDTO> lista = motoService.getAllMotos(PageRequest.of(page, size));
        model.addAttribute("page", lista);
        return "motos/list";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("dto", new MotosDTO(null, "", ""));
        model.addAttribute("isNew", true);
        return "motos/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("dto") MotosDTO dto,
                         BindingResult br, Model model, RedirectAttributes ra) {
      if (br.hasErrors()) { model.addAttribute("isNew", true); return "motos/form"; }
      motoService.createMoto(dto); 
      ra.addFlashAttribute("msgSuccess", "Moto criada com sucesso!");
      return "redirect:/motos";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable("id") String id, Model model) {
        MotosDTO dto = motoService.getMotoById(id);
        model.addAttribute("dto", dto);
        model.addAttribute("isNew", false);
        return "motos/form";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable String id,
                         @Valid @ModelAttribute("dto") MotosDTO dto,
                         BindingResult br, Model model, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("isNew", false);
            return "motos/form";
        }
        motoService.updateMoto(id, dto);
        ra.addFlashAttribute("msgSuccess", "Moto atualizada com sucesso!");
        return "redirect:/motos";
    }


    @GetMapping("/{id}/excluir")
    public String confirmDelete(@PathVariable("id") String id, Model model) {
        model.addAttribute("dto", motoService.getMotoById(id));
        model.addAttribute("isNew", false);
        return "motos/confirm-delete";
    }

    @GetMapping("/{idMoto}/movimentacoes")
    public String listarMovimentacoes(@PathVariable String idMoto,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      Model model) {
        Page<MovimentacoesDTO> lista = movService.getHistoricoMoto(idMoto, PageRequest.of(page, size));
        model.addAttribute("page", lista);
        model.addAttribute("idMoto", idMoto);
        return "movimentacoes/list-by-moto";
    }
    
    @GetMapping("/{idMoto}/movimentacoes/novo")
    public String novoMovimento(@PathVariable String idMoto, Model model) {
        model.addAttribute("dto", new MovimentacoesDTO(null, idMoto, null, LocalDateTime.now()));
        model.addAttribute("isNew", true);
        model.addAttribute("idMoto", idMoto);
        carregarPontos(model);
        return "movimentacoes/form-by-moto";
    }

    @PostMapping("/{idMoto}/movimentacoes")
    public String criarMovimento(@PathVariable String idMoto,
                                 @Valid @ModelAttribute("dto") MovimentacoesDTO dto,
                                 BindingResult br, Model model, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("isNew", true);
            model.addAttribute("idMoto", idMoto);
            carregarPontos(model);
            return "movimentacoes/form-by-moto";
        }
        // blindagem: força o idMoto do path
        MovimentacoesDTO seguro = new MovimentacoesDTO(null, idMoto, dto.idPonto(), dto.dataHora());
        movService.registrarMovimentacao(seguro); 
        ra.addFlashAttribute("msgSuccess", "Movimentação registrada com sucesso!");
        return "redirect:/motos/" + idMoto + "/movimentacoes";
    }

    
    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        motoService.deleteMoto(id);
        ra.addFlashAttribute("msgSuccess", "Moto excluída com sucesso!");
        return "redirect:/motos";
    }
}
