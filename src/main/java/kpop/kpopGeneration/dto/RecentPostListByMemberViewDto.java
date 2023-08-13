package kpop.kpopGeneration.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecentPostListByMemberViewDto {

    int size;
    List<RecentPostByMemberDto> recent;

}
