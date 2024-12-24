package com.chatlog.chatlog_server.models.DTOs;

import java.util.List;

public class PaginatedResponse {

    private List<ChatLogResponse> data;
    private int totalPages;
    private int totalItems;
    private int currentPage;


    public PaginatedResponse(List<ChatLogResponse> data, int totalPages, int totalItems, int currentPage) {
        this.data = data;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.currentPage = currentPage;
    }

    public List<ChatLogResponse> getData() {
        return data;
    }

    public void setData(List<ChatLogResponse> data) {
        this.data = data;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
