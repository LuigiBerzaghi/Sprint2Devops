package com.mottu.trackyard.controller.web;

import com.mottu.trackyard.dto.MotosDTO;
import com.mottu.trackyard.dto.PontosLeituraDTO;
import com.mottu.trackyard.dto.MovimentacoesDTO;
import com.mottu.trackyard.service.MotoService;
import com.mottu.trackyard.service.PontosLeituraService;
import com.mottu.trackyard.service.MovimentacaoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/movimentacoes")
public class MovimentacoesPageController {

    private final MovimentacaoService movService;
    private final MotoService motoService;
    private final PontosLeituraService pontoService;

    public MovimentacoesPageController(MovimentacaoService movService,
                                       MotoService motoService,
                                       PontosLeituraService pontoService) {
        this.movService = movService;
        this.motoService = motoService;
        this.pontoService = pontoService;
    }

    private void combos(Model model) {
        List<MotosDTO> motos = motoService.getAllMotos(PageRequest.of(0, 1000)).getContent();
        List<PontosLeituraDTO> pontos = pontoService.getAllPontosLeitura(PageRequest.of(0, 1000)).getContent();
        model.addAttribute("motos", motos);
        model.addAttribute("pontos", pontos);
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("dto", new MovimentacoesDTO(null, null, null, LocalDateTime.now()));
        model.addAttribute("isNew", true);
        combos(model);
        return "movimentacoes/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("dto") MovimentacoesDTO dto,
                         BindingResult br, Model model, RedirectAttributes ra) {
        if (br.hasErrors()) { model.addAttribute("isNew", true); combos(model); return "movimentacoes/form"; }
        movService.registrarMovimentacao(dto);
        ra.addFlashAttribute("msgSuccess", "Movimentação registrada com sucesso!");
        return "redirect:/movimentacoes";
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        model.addAttribute("dto", movService.getMovimentacaoById(id));
        return "movimentacoes/details";
    }

}
