package com.cnsc.research.api;

import com.cnsc.research.domain.transaction.ResearchersDto;
import com.cnsc.research.service.ResearcherService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/researcher")
public class ResearcherController {
    private final Logger logger;
    private final ResearcherService service;

    @Autowired
    public ResearcherController(ResearcherService service, Logger logger) {
        this.logger = logger;
        this.service = service;
    }

    @GetMapping("/list")
    public List<ResearchersDto> getAllResearchers() {
        return service.getAllResearchers();
    }

    @PostMapping
    public ResponseEntity addResearcher(@RequestBody ResearchersDto researchersDto) {
        return service.saveResearcher(researchersDto);
    }

    @PutMapping
    public ResponseEntity editResearcher(@RequestBody ResearchersDto researchersDto) {
        return service.updateResearcher(researchersDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteResearcher(@PathVariable(name = "id") Integer researcherId) {
        return service.deleteResearcher(researcherId);
    }

    @GetMapping("/{id}")
    public ResponseEntity getResearcher(@PathVariable(name = "id") Integer researcherId) {
        return service.getResearcher(researcherId);
    }

    @GetMapping("/{id}/research/list")
    public ResponseEntity getResearches(@PathVariable(name = "id") Integer id) {
        return service.getResearches(id);
    }
}
