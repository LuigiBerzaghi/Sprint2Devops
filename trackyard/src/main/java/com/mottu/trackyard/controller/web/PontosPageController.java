package com.mottu.trackyard.controller.web;

import com.mottu.trackyard.dto.PontosLeituraDTO;
import com.mottu.trackyard.dto.PatiosDTO;
import com.mottu.trackyard.service.PontosLeituraService;
import com.mottu.trackyard.service.PatioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/pontos")
public class PontosPageController {

    private final PontosLeituraService pontoService;
    private final PatioService patioService;

    public PontosPageController(PontosLeituraService pontoService, PatioService patioService) {
        this.pontoService = pontoService;
        this.patioService = patioService;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {
        Page<PontosLeituraDTO> lista = pontoService.getAllPontosLeitura(PageRequest.of(page, size));
        model.addAttribute("page", lista);
        return "pontos/list";
    }

    private void carregarPatios(Model model) {
        List<PatiosDTO> patios = patioService.getAllPatios(PageRequest.of(0, 1000)).getContent();
        model.addAttribute("patios", patios);
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("dto", new PontosLeituraDTO(null, null, "", ""));
        model.addAttribute("isNew", true);
        carregarPatios(model);
        return "pontos/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("dto") PontosLeituraDTO dto,
                         BindingResult br, Model model, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("isNew", true);
            carregarPatios(model);
            return "pontos/form";
        }
        pontoService.createPontoLeitura(dto);
        ra.addFlashAttribute("msgSuccess", "Ponto de leitura criado com sucesso!");
        return "redirect:/pontos";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("dto", pontoService.getPontoLeituraById(id));
        model.addAttribute("isNew", false);
        carregarPatios(model);
        return "pontos/form";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("dto") PontosLeituraDTO dto,
                         BindingResult br, Model model, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("isNew", false);
            carregarPatios(model);
            return "pontos/form";
        }
        pontoService.updatePontoLeitura(id, dto); // <<< usa o id do PATH
        ra.addFlashAttribute("msgSuccess", "Ponto de leitura atualizado com sucesso!");
        return "redirect:/pontos";
    }

    @GetMapping("/{id}/excluir")
    public String confirmDelete(@PathVariable Long id, Model model) {
        model.addAttribute("dto", pontoService.getPontoLeituraById(id));
        return "pontos/confirm-delete";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        pontoService.deletePontoLeitura(id);
        ra.addFlashAttribute("msgSuccess", "Ponto de leitura excluído com sucesso!");
        return "redirect:/pontos";
    }
}
