package com.example.EventPlanner.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PageResponse<T> {
    @SerializedName("content")
    @Expose
    private List<T> content;

    @SerializedName("pageable")
    @Expose
    private Pageable pageable;

    @SerializedName("last")
    @Expose
    private boolean last;

    @SerializedName("totalElements")
    @Expose
    private long totalElements;

    @SerializedName("totalPages")
    @Expose
    private int totalPages;

    @SerializedName("size")
    @Expose
    private int size;

    @SerializedName("number")
    @Expose
    private int number;

    @SerializedName("sort")
    @Expose
    private Sort sort;

    @SerializedName("first")
    @Expose
    private boolean first;

    @SerializedName("numberOfElements")
    @Expose
    private int numberOfElements;

    @SerializedName("empty")
    @Expose
    private boolean empty;

    // Nested Pageable class
    public static class Pageable {
        @SerializedName("pageNumber")
        @Expose
        private int pageNumber;

        @SerializedName("pageSize")
        @Expose
        private int pageSize;

        @SerializedName("sort")
        @Expose
        private Sort sort;

        @SerializedName("offset")
        @Expose
        private int offset;

        @SerializedName("paged")
        @Expose
        private boolean paged;

        @SerializedName("unpaged")
        @Expose
        private boolean unpaged;

        // Getters and setters
        public int getPageNumber() { return pageNumber; }
        public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber; }
        public int getPageSize() { return pageSize; }
        public void setPageSize(int pageSize) { this.pageSize = pageSize; }
        public Sort getSort() { return sort; }
        public void setSort(Sort sort) { this.sort = sort; }
        public int getOffset() { return offset; }
        public void setOffset(int offset) { this.offset = offset; }
        public boolean isPaged() { return paged; }
        public void setPaged(boolean paged) { this.paged = paged; }
        public boolean isUnpaged() { return unpaged; }
        public void setUnpaged(boolean unpaged) { this.unpaged = unpaged; }
    }

    // Nested Sort class
    public static class Sort {
        @SerializedName("empty")
        @Expose
        private boolean empty;

        @SerializedName("sorted")
        @Expose
        private boolean sorted;

        @SerializedName("unsorted")
        @Expose
        private boolean unsorted;

        // Getters and setters
        public boolean isEmpty() { return empty; }
        public void setEmpty(boolean empty) { this.empty = empty; }
        public boolean isSorted() { return sorted; }
        public void setSorted(boolean sorted) { this.sorted = sorted; }
        public boolean isUnsorted() { return unsorted; }
        public void setUnsorted(boolean unsorted) { this.unsorted = unsorted; }
    }

    // Getters and setters for main class
    public List<T> getContent() { return content; }
    public void setContent(List<T> content) { this.content = content; }
    public Pageable getPageable() { return pageable; }
    public void setPageable(Pageable pageable) { this.pageable = pageable; }
    public boolean isLast() { return last; }
    public void setLast(boolean last) { this.last = last; }
    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }
    public Sort getSort() { return sort; }
    public void setSort(Sort sort) { this.sort = sort; }
    public boolean isFirst() { return first; }
    public void setFirst(boolean first) { this.first = first; }
    public int getNumberOfElements() { return numberOfElements; }
    public void setNumberOfElements(int numberOfElements) { this.numberOfElements = numberOfElements; }
    public boolean isEmpty() { return empty; }
    public void setEmpty(boolean empty) { this.empty = empty; }
}

