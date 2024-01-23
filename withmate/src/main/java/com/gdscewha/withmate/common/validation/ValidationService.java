package com.gdscewha.withmate.common.validation;

import com.gdscewha.withmate.common.response.exception.*;

import com.gdscewha.withmate.domain.journey.repository.JourneyRepository;
import com.gdscewha.withmate.domain.journey.entity.Journey;
import com.gdscewha.withmate.domain.matching.entity.Matching;
import com.gdscewha.withmate.domain.matching.repository.MatchingRepository;
import com.gdscewha.withmate.domain.member.entity.Member;
import com.gdscewha.withmate.domain.member.repository.MemberRepository;
import com.gdscewha.withmate.domain.memberrelation.entity.MemberRelation;
import com.gdscewha.withmate.domain.memberrelation.repository.MemberRelationRepository;
import com.gdscewha.withmate.domain.relation.entity.Relation;
import com.gdscewha.withmate.domain.relation.repository.RelationRepository;
import com.gdscewha.withmate.domain.sticker.entity.Sticker;
import com.gdscewha.withmate.domain.sticker.repository.StickerRepository;
import com.gdscewha.withmate.domain.week.entity.Week;
import com.gdscewha.withmate.domain.week.repository.WeekRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ValidationService {
    private final JourneyRepository  journeyRepository;
    private final MatchingRepository matchingRepository;
    private final MemberRepository memberRepository;
    private final MemberRelationRepository memberRelationRepository;
    private final RelationRepository relationRepository;
    private final StickerRepository stickerRepository;
    private final WeekRepository weekRepository;

    public Journey valJourney(Long journeyId) {
        return journeyRepository.findById(journeyId)
                .orElseThrow(() -> new JourneyException(ErrorCode.JOURNEY_NOT_FOUND));
    }
    public Matching valMatching(Long matchingId) {
        return matchingRepository.findById(matchingId)
                .orElseThrow(() -> new MatchingException(ErrorCode.MATCHING_NOT_FOUND));
    }
    public Member valMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));
    }
    public Member valMember(String username) {
        return memberRepository.findByUserName(username)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public MemberRelation valMemberRelation(Long MRId) {
        return memberRelationRepository.findById(MRId)
                .orElseThrow(() -> new MemberRelationException(ErrorCode.MEMBERRELATION_NOT_FOUND));
    }
    public Relation valRelation(Long relationId) {
        return relationRepository.findById(relationId)
                .orElseThrow(() -> new MateRelationException(ErrorCode.RELATION_NOT_FOUND));
    }

    public Sticker valSticker(Long stickerId) {
        return stickerRepository.findById(stickerId)
                .orElseThrow(() -> new StickerException(ErrorCode.STICKER_NOT_FOUND));
    }
    public Week valWeek(Long weekId) {
        return weekRepository.findById(weekId)
                .orElseThrow(() -> new WeekException(ErrorCode.WEEK_NOT_FOUND));
    }
}
