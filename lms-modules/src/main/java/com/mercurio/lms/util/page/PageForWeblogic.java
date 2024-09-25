package com.mercurio.lms.util.page;

import com.mercurio.adsm.framework.model.ResultSetPage;

import java.util.List;

/**
 * Contorna a limitação do weblogic de não converter org.springframework.data.domain.Page<br>
 * Contém apenas os campos necessários para criar um {@link ResultSetPage}.
 */
public class PageForWeblogic<T> {

    private long totalElements;
    private int number;
    private List<T> content;
    private boolean hasNext;
    private boolean hasPrevious;

    public ResultSetPage<T> toResultSetPage() {
        return new ResultSetPage<T>(
                number,
                hasPrevious,
                hasNext,
                content,
                totalElements);
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    public Boolean getHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(Boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }
}
