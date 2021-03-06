package com.cnsc.research.domain.transaction;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResearchDto implements Mappable {

    private Integer id;
    private List<FundingAgency> fundingAgency;
    private Double budget;
    private LocalDate startDate;
    private LocalDate endDate;
    private String researchStatus;
    private DeliveryUnit deliveryUnit;
    private String remarks;
    private List<Researchers> researchers;
    private ResearchFile researchFile;
    private Boolean isPublic;
    private List<ResearchAgenda> researchAgenda;
    private Long view;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ResearchFile {
        private Integer fileId = null;
        private String title;
        private String fileName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Researchers {
        private Integer researcherId = null;
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DeliveryUnit {
        private Integer unitId = null;
        private String unitName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FundingAgency {
        private Integer agencyId = null;
        private String agencyName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ResearchAgenda {
        private Integer agendaId = null;
        private String agendaName;
    }

}
