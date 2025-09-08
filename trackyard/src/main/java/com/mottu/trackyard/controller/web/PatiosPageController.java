package com.mottu.trackyard.controller.web;

import com.mottu.trackyard.dto.PatiosDTO;
import com.mottu.trackyard.dto.PontosLeituraDTO;
import com.mottu.trackyard.service.PatioService;
import com.mottu.trackyard.service.PontosLeituraService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/patios")
public class PatiosPageController {

    private final PatioService patioService;
    private final PontosLeituraService pontoService;
    public PatiosPageController(PatioService patioService, PontosLeituraService pontoService) {
        this.patioService = patioService;
        this.pontoService = pontoService;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {
        Page<PatiosDTO> lista = patioService.getAllPatios(PageRequest.of(page, size));
        model.addAttribute("page", lista);
        return "patios/list";
    }
    
    @GetMapping("/{idPatio}/pontos")
    public String listarPontosDoPatio(@PathVariable Long idPatio,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      Model model) {
        var patio = patioService.getPatioById(idPatio); // para exibir nome no título
        Page<PontosLeituraDTO> lista = patioService.getPontosByPatio(idPatio, PageRequest.of(page, size));
        model.addAttribute("page", lista);
        model.addAttribute("patio", patio);
        model.addAttribute("idPatio", idPatio);
        return "pontos/list-by-patio";
    }
    
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("dto", new PatiosDTO(null, "", "", ""));
        model.addAttribute("isNew", true);
        return "patios/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("dto") PatiosDTO dto,
                         BindingResult br, Model model, RedirectAttributes ra) {
        if (br.hasErrors()) { model.addAttribute("isNew", true); return "patios/form"; }
        patioService.createPatio(dto); 
        ra.addFlashAttribute("msgSuccess", "Pátio criado com sucesso!");
        return "redirect:/patios";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("dto", patioService.getPatioById(id));
        model.addAttribute("isNew", false);
        return "patios/form";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("dto") PatiosDTO dto,
                         BindingResult br, Model model, RedirectAttributes ra) {
        if (br.hasErrors()) { model.addAttribute("isNew", false); return "patios/form"; }
        patioService.updatePatio(id, dto);
        ra.addFlashAttribute("msgSuccess", "Pátio atualizado com sucesso!");
        return "redirect:/patios";
    }

    @GetMapping("/{id}/excluir")
    public String confirmDelete(@PathVariable Long id, Model model) {
        model.addAttribute("dto", patioService.getPatioById(id));
        return "patios/confirm-delete";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        patioService.deletePatio(id);
        ra.addFlashAttribute("msgSuccess", "Pátio excluído com sucesso!");
        return "redirect:/patios";
    }
}
