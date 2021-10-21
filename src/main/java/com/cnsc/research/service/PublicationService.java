package com.cnsc.research.service;

import com.cnsc.research.domain.mapper.PublicationMapper;
import com.cnsc.research.domain.model.Publication;
import com.cnsc.research.domain.model.Researchers;
import com.cnsc.research.domain.repository.PublicationRepository;
import com.cnsc.research.domain.transaction.ExtendedPublicationDto;
import com.cnsc.research.domain.transaction.PublicationDto;
import com.cnsc.research.domain.transaction.PublicationQueryBuilder;
import com.cnsc.research.domain.transaction.PublicationSaveResponse;
import com.cnsc.research.misc.EntityBuilders;
import com.cnsc.research.misc.fields.PublicationFields;
import com.cnsc.research.misc.importer.CsvImport;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.OK;

@Service
public class PublicationService {
    private final PublicationRepository repository;
    private final Logger logger;
    private final PublicationMapper publicationMapper;
    private final EntityBuilders entityBuilders;

    @Autowired
    public PublicationService(PublicationRepository repository,
                              PublicationMapper publicationMapper,
                              Logger logger,
                              EntityBuilders entityBuilders
    ) {
        this.repository = repository;
        this.logger = logger;
        this.publicationMapper = publicationMapper;
        this.entityBuilders = entityBuilders;

    }

    public PublicationSaveResponse addPublication(ExtendedPublicationDto publicationDto) {
        try {
            if (repository.findByPublicationTitleIgnoreCaseAndDeleted(publicationDto.getPublicationTitle(), false).isPresent())
                return new PublicationSaveResponse(publicationDto.getPublicationTitle(), "Already Exist!");
            Publication publication = publicationMapper.toDomain(publicationDto);

            publicationDto.getResearchers().forEach(item -> logger.info("RESEARCHER ----" + item.getResearcherName()));

            //REMINDER -- this might cause a performance issue someday.
            // run profiling and change the implementation.
            List<Researchers> researchers = publicationDto.getResearchers()
                    .stream()
                    .map(item -> entityBuilders.buildResearcher(item.getResearcherName()))
                    .collect(Collectors.toList());

            publication.setResearchers(researchers);
            repository.save(publication);
            return new PublicationSaveResponse(publication.getPublicationTitle(), "Saved!");
        } catch (Exception e) {
            logger.error(format("Error on saving \"%s\"", publicationDto.getPublicationTitle()));
            logger.error(e.getMessage());
            return new PublicationSaveResponse(publicationDto.getPublicationTitle(), "Error!");
        }
    }

    public ExtendedPublicationDto getPublication(Long publicationId) {
        return publicationMapper.toExtendedTransaction(repository.getById(publicationId));
    }

    public String editPublication(PublicationDto publicationDto) {
        try {
            Publication publication = publicationMapper.toDomain(publicationDto);
            repository.save(publication);
            return "Publication saved";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String deletePublication(Long id) {
        String deleteMessage;
        Optional<Publication> publicationOptional = repository.findById(id);
        if (publicationOptional.isPresent()) {
            try {
                Publication publication = publicationOptional.get();
                publication.setDeleted(true);
                publication.setDateTimeDeleted(LocalDateTime.now());
                repository.save(publication);
                deleteMessage = "Publication has been deleted";
            } catch (Exception e) {
                deleteMessage = e.getMessage();
            }
        } else deleteMessage = "Publication ID:" + id + " didn't exist";

        return deleteMessage;
    }

    public List<ExtendedPublicationDto> getPublications() {
        try {
            List<Publication> publicationList = repository.findByDeletedIsFalse();
            return publicationMapper.toExtendedTransaction(publicationList);
        } catch (Exception e) {
            logger.error(format("Line 98 : %s", e.getMessage()));
            return List.of();
        }
    }

    public List<PublicationSaveResponse> savePublications(List<ExtendedPublicationDto> publicationDtos) {
        return publicationDtos
                .stream()
                .map(this::addPublication)
                .collect(Collectors.toList());
    }

    public ResponseEntity deletePublications(List<Long> idList) {
        AtomicInteger deleteCount = new AtomicInteger(0);
        idList.forEach((id) -> {
            //FIXME This is not safe validation. This might change someday
            if (this.deletePublication(id).equals("Publication has been deleted"))
                deleteCount.getAndIncrement();
        });
        return new ResponseEntity(format("%d items has been deleted", deleteCount.get()), OK);
    }

    public List<ExtendedPublicationDto> getPublicationFromFile(MultipartFile incomingFile) throws Exception {
        return new CsvImport<ExtendedPublicationDto>(incomingFile.getBytes(), new PublicationFields())
                .getMappedData(publicationMapper);
    }

    public List<ExtendedPublicationDto> getPublicationByTitle(String title) {
        return publicationMapper.toExtendedTransaction(repository.findByPublicationTitleIsContainingIgnoreCaseAndDeletedIsFalse(title));
    }

    public List<ExtendedPublicationDto> getRPublicationByAdvancedFilter(PublicationQueryBuilder queryBuilder) {
        return publicationMapper.toExtendedTransaction(repository.findAdvanced(queryBuilder.getResearchers()));
    }
}
