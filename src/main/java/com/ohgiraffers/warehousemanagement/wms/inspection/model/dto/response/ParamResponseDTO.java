package com.ohgiraffers.warehousemanagement.wms.inspection.model.dto.response;

public class ParamResponseDTO {
    private String inspectionType;
    private String searchType;
    private Long search;

    protected ParamResponseDTO() {}

    public ParamResponseDTO(String inspectionType, String searchType, Long search) {
        this.inspectionType = inspectionType;
        this.searchType = searchType;
        this.search = search;
    }

    public String getInspectionType() {
        return inspectionType;
    }

    public void setInspectionType(String inspectionType) {
        this.inspectionType = inspectionType;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public Long getSearch() {
        return search;
    }

    public void setSearch(Long search) {
        this.search = search;
    }
}
