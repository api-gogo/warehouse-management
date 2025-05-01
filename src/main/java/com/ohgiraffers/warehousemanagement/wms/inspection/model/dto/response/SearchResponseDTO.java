package com.ohgiraffers.warehousemanagement.wms.inspection.model.dto.response;

public class SearchResponseDTO {
    private String inspectionType;
    private String searchType;
    private String search;

    protected SearchResponseDTO() {}

    public SearchResponseDTO(String inspectionType, String searchType, String search) {
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

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
