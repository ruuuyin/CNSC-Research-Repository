package com.cnsc.research.service;

import com.cnsc.research.domain.mapper.ResearcherMapper;
import com.cnsc.research.domain.repository.ResearchersRepository;
import com.cnsc.research.domain.transaction.ResearchersDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResearcherService {
    private final ResearchersRepository repository;
    private final ResearcherMapper mapper;
    private final Logger logger;

    @Autowired
    public ResearcherService(ResearchersRepository repository,ResearcherMapper mapper , Logger logger) {
        this.repository = repository;
        this.logger = logger;
        this.mapper = mapper;
    }

    public List<ResearchersDto> getAllResearchers() {
        return mapper.toResearchDto(repository.findAll());
    }
}