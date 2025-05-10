package com.jobPrize.repository.member.custom;

import java.util.Optional;

import com.jobPrize.entity.Member;
import com.jobPrize.entity.QAppDocument;
import com.jobPrize.entity.QApplication;
import com.jobPrize.entity.QCareer;
import com.jobPrize.entity.QCareerDescription;
import com.jobPrize.entity.QCareerDescriptionContent;
import com.jobPrize.entity.QCertification;
import com.jobPrize.entity.QCoverLetter;
import com.jobPrize.entity.QCoverLetterContent;
import com.jobPrize.entity.QEducation;
import com.jobPrize.entity.QLanguageTest;
import com.jobPrize.entity.QMember;
import com.jobPrize.entity.QPayment;
import com.jobPrize.entity.QProposal;
import com.jobPrize.entity.QResume;
import com.jobPrize.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Member> findWithAllDocumentsById(Long id) {
        QMember member = QMember.member;
        QResume resume = QResume.resume;
        QEducation education = QEducation.education;
        QCareer career = QCareer.career;
        QCertification certification = QCertification.certification;
        QLanguageTest languageTest = QLanguageTest.languageTest;
        QCareerDescription careerDescription = QCareerDescription.careerDescription;
        QCareerDescriptionContent careerDescriptionContent= QCareerDescriptionContent.careerDescriptionContent;
        QCoverLetter coverLetter = QCoverLetter.coverLetter;
        QCoverLetterContent coverLetterContent = QCoverLetterContent.coverLetterContent;

        Member result = queryFactory
            .selectFrom(member) // 멤버 반환
            .leftJoin(member.resume, resume).fetchJoin() // 멤버에서 파생된 이력서
            .leftJoin(resume.educations, education).fetchJoin() // 이력서에서 파생된 학력들
            .leftJoin(resume.careers, career).fetchJoin() // 이력서에서 파생된 경력들
            .leftJoin(resume.certifications, certification).fetchJoin() // 이력서에서 파생된 자격증들
            .leftJoin(resume.languageTests, languageTest).fetchJoin() // 이력서에서 파생된 어학시험들
            .leftJoin(member.careerDescriptions, careerDescription).fetchJoin() // 멤버에서 파생된 경력기술서들
            .leftJoin(careerDescription.careerDescriptionContents, careerDescriptionContent).fetchJoin() //경력기술서에서 파생된 경력기술서 내용들
            .leftJoin(member.coverLetters, coverLetter).fetchJoin() // 멤버에서 파생된 자기소개서들
            .leftJoin(coverLetter.coverLetterContents, coverLetterContent).fetchJoin() //자기소개서에서 파생된 자기소개서 내용들
            .where(member.id.eq(id)) // 멤버id가 매개변수로 들어온 id와 같다는 조건
            .distinct() // 조인하여 출력된 결과는 복수이니 중복을 제거하여 각 객체가 존재하는 만큼만 출력
            .fetchOne(); // 멤버 객체 하나만 출력

        return Optional.ofNullable(result); // 메소드안의 result를 넣어 옵셔널로 처리
    }

	@Override
	public Optional<Member> findWithProposalsById(Long id) {
		QMember member = QMember.member;
		QProposal proposal = QProposal.proposal;
		
		Member result = queryFactory
	            .selectFrom(member)
	            .leftJoin(member.proposals, proposal)
	            .where(member.id.eq(id))
	            .distinct()
	            .fetchOne();
		
		
		return Optional.ofNullable(result);
	}

	@Override
	public Optional<Member> findWithApplicationsById(Long id) {
		QMember member = QMember.member;
		QApplication application = QApplication.application;
		QAppDocument appDocument = QAppDocument.appDocument;
		
		Member result = queryFactory
	            .selectFrom(member)
	            .leftJoin(member.applications, application)
	            .leftJoin(application.appDocument, appDocument).fetchJoin()
	            .where(member.id.eq(id))
	            .distinct()
	            .fetchOne();
		

		return Optional.ofNullable(result);
	}

	@Override
	public Optional<Member> findWithPaymentsById(Long id) {
		QMember member = QMember.member;
		QUser user = QUser.user;
		QPayment payment = QPayment.payment;
		
		Member result = queryFactory
	            .selectFrom(member)
	            .leftJoin(member.user, user).fetchJoin()
	            .leftJoin(user.payments, payment)
	            .where(member.id.eq(id))
	            .distinct()
	            .fetchOne();
	            
		return Optional.ofNullable(result);
	}
}