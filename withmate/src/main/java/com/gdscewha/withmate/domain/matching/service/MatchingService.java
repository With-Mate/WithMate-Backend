package com.gdscewha.withmate.domain.matching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

public class MatchingService {

}

/*

package com.gdscewha.withmate.domain.matching.service;

        import com.gdscewha.withmate.common.validation.ValidationService;
        import com.gdscewha.withmate.domain.memberrelation.entity.MemberRelation;
        import com.gdscewha.withmate.domain.memberrelation.repository.MemberRelationRepository;
        import lombok.RequiredArgsConstructor;
        import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class MatchingService {
}
/*
public class MemberRelationService {
    private final ValidationService validationService;
    private final MemberRelationRepository memberRelationRepository;

    //카테고리 선택 시 memberrelation의 category에 저장하기
    //목표와 카테고리를 DTO로 받을까?




    // 목표 입력 시 저장하기

    public MemberRelation updateMemberGoal(Long id, String newGoal) {

        //

        MemberRelation.setGoal(newGoal);

        return memberRelationRepository.save(memberRelation);
    }

}*/