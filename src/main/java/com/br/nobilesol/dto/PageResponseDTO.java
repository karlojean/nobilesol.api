package com.br.nobilesol.dto;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponseDTO<T> {
    private List<T> content;
    private long totalElements;
    private int pageNumber;
    private int totalPages;
    private int pageSize;

    public static <T> PageResponseDTO<T> from(Page<T> page) {
        return PageResponseDTO.<T>builder()
                .content(page.getContent())
                .totalElements(page.getTotalElements())
                .pageNumber(page.getNumber())
                .totalPages(page.getTotalPages())
                .pageSize(page.getSize())
                .build();
    }
}