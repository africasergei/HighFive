import { useState, useEffect } from "react";
import Button from "../../../common/components/button/Button";
import EmptyState from "../../../common/components/emptyState/EmptyState";
import Pagination from "../../../common/components/pagination/Pagination";
import ModalTitle from "../../../common/components/title/ModalTitle";
import CommonPage from "../../../common/pages/CommonPage";

import RequestListItem from "../components/RequestListItem";
import RequestListTop from "../components/RequestListTop";
import CompletedRequestDetailModal from "../modals/CompletedRequestDetailModal";
import RequestDetailModal from "../modals/RequestDetailModal";
import RequestModal from "../modals/RequestModal";

import { printErrorInfo } from "../../../common/utils/ErrorUtil";

import type { RequestDetailDto, CompletedRequestDetailDto, RequestSummaryDto } from "../props/RequestProps";
import { useRequestController } from "../customHooks/useRequestController";

import { readRequestsApi, readRequestApi, readCompletedRequestApi, createConsultantRequestApi, createRequestApi } from "../apis/RequestApi";
import { readEducationsApi, readCareersApi, readCertificationsApi, readLanguageTestsApi, readCareerDescriptionsApi, readCoverLettersApi } from "../../myPageForMember/apis/MyPageForMemberApi";
import type { EducationResponseDto, CareerResponseDto, CertificationResponseDto, LanguageTestResponseDto } from "../../myPageForMember/props/myPageForMemberProps";
import { usePagination } from "../../../common/customHooks/usePagination";

const consultingType = '피드백'
const elementsPerPage = 10;
const pagesPerBlock = 10;





const FeedbackRequestPage = () => {

    const [totalElements, setTotalElements] = useState(0);


    // 페이지 내 전역 상태 선언부
    const {
        targetJob,
        targetCompanyName,
        clickedCareerDescriptionId,
        clickedCoverLetterId,
        showModalNumber,
        requestSummaryDtos,
        showRequestModal,
        showRequestDetailModal,
        requestDetailDto,
        completedRequestDetailDto,
        isCompleted,
        setTargetJob,
        setTargetCompanyName,
        setClickedCareerDescriptionId,
        setClickedCoverLetterId,
        setShowModalNumber,
        setResume,
        setCareerDescriptionSummaryDtos,
        setCoverLetterSummaryDtos,
        setRequestSummaryDtos,
        setShowRequestModal,
        setShowRequestDetailModal,
        setRequestDetailDto,
        setCompletedRequestDetailDto,
        setIsCompleted
    } = useRequestController();


    const {
        clickedPage,
        pageBlockIndex,
        lastPage,
        lastPageBlockIndex,
        onClickFirst,
        onClickPrev,
        onClickNext,
        onClickLast,
        setClickedPage,
    } = usePagination({
        totalElements: totalElements,
        elementsPerPage: elementsPerPage,
        pagesPerBlock: pagesPerBlock,
    });

    // 페이지 내 상태 관리부
    const onClickRequestModal = () => {
        setShowModalNumber(0)
        setShowRequestModal(true);
    }


    // 페이지 내 API 호출부
    useEffect(() => {
        const fetchData = async () => {
            try {
                const res = await readRequestsApi(clickedPage - 1, elementsPerPage, consultingType);
                const totalElements = res.data.totalElements as number
                setTotalElements(totalElements);
                const requestSummaryDtos = res.data.content as RequestSummaryDto[];
                setRequestSummaryDtos(requestSummaryDtos);

            }
            catch (err) {
                printErrorInfo(err)
            }
        }



        fetchData();



    }, [clickedPage]);




    const onClickRequestDetailModal = (id: number) => {
        const fetchData = async () => {
            try {
                const res = await readRequestApi(id);
                const requestDetailDto = res.data.content as RequestDetailDto;
                setRequestDetailDto(requestDetailDto);
            } catch (err) {
                printErrorInfo(err)
            }
        };
        const fetchCompletedData = async () => {
            try {
                const res = await readCompletedRequestApi(id);
                const completedRequestDetailDto = res.data.content as CompletedRequestDetailDto;
                setCompletedRequestDetailDto(completedRequestDetailDto);
            } catch (err) {
                printErrorInfo(err)
            }
        };



        if (requestSummaryDtos.find(item => item.id === id)?.requestStatus === "완료") {
            setIsCompleted(true);
            fetchCompletedData();
        } else {
            setIsCompleted(false);
            fetchData();
        }
        setShowRequestDetailModal(true);
    }

    const onClickRequestToConsultant = async (id: number) => {
        try {
            await createConsultantRequestApi(id);
            setShowRequestDetailModal(false);

        } catch (err) {
            printErrorInfo(err)
        }
    }

    useEffect(() => {

        if (showModalNumber === 1) {
            const fetchData = async () => {

                try {
                    const educationResponseDtos = (await readEducationsApi()).data.content as EducationResponseDto[];
                    const careerResponseDtos = (await readCareersApi()).data.content as CareerResponseDto[];
                    const certificationResponseDtos = (await readCertificationsApi()).data.content as CertificationResponseDto[];
                    const languageTestResponseDtos = (await readLanguageTestsApi()).data.content as LanguageTestResponseDto[];
                    setResume({ educationResponseDtos, careerResponseDtos, certificationResponseDtos, languageTestResponseDtos })
                } catch (err) {
                    printErrorInfo(err);

                }

            }


            fetchData();



        } else if (showModalNumber === 2) {
            const fetchData = async () => {

                try {
                    const careerDescriptionSummaryDtos = (await readCareerDescriptionsApi()).data.content;
                    setCareerDescriptionSummaryDtos(careerDescriptionSummaryDtos);
                } catch (err) {
                    printErrorInfo(err);
                }
            }

            fetchData();

        } else if (showModalNumber === 3) {
            const fetchData = async () => {

                try {
                    const coverLetterSummaryDtos = (await readCoverLettersApi()).data.content;
                    setCoverLetterSummaryDtos(coverLetterSummaryDtos);
                } catch (err) {
                    printErrorInfo(err);
                }
            }

            fetchData();


        } else if (showModalNumber === 4) {

            const post = async () => {
                try {
                    await createRequestApi(targetJob, targetCompanyName, consultingType, clickedCoverLetterId, clickedCareerDescriptionId);

                }
                catch (err) {
                    printErrorInfo(err);
                }
            }
            post();
            setTargetJob('');
            setTargetCompanyName('');
            setClickedCareerDescriptionId(-1);
            setClickedCoverLetterId(-1);

        }
    }, [showModalNumber]);


    return (
        <>


            <CommonPage>
                <ModalTitle title={`AI + 컨설턴트 ${consultingType}`} />
                <div className="flex justify-end">
                    <Button color={"theme"} size={"m"} disabled={false} text={`새 ${consultingType} 요청하기`} type={"button"} onClick={onClickRequestModal} />
                </div>
                <RequestListTop />
                <div className="mb-3">
                    {requestSummaryDtos.length > 0 ? requestSummaryDtos.map(requestSummaryDto =>
                        <RequestListItem requestSummaryDto={requestSummaryDto} key={requestSummaryDto.id} onClick={() => onClickRequestDetailModal(requestSummaryDto.id)} />
                    ) : <EmptyState title={"요청 내역이 없습니다."} text={"요청을 등록해주세요."} />}
                </div>

                <div className="flex justify-center">
                    <Pagination
                        currentPageBlockIndex={pageBlockIndex}
                        lastPageBlockIndex={lastPageBlockIndex}
                        pagesPerBlock={pagesPerBlock}
                        lastPage={lastPage}
                        clickedPage={clickedPage}
                        onClickFirst={onClickFirst}
                        onClickPrev={onClickPrev}
                        onClickNext={onClickNext}
                        onClickLast={onClickLast}
                        onClickPage={setClickedPage}
                    />
                </div>

            </CommonPage>

            {showRequestModal && <RequestModal consultingType={consultingType} />}

            {showRequestDetailModal && isCompleted && completedRequestDetailDto && (
                <CompletedRequestDetailModal />
            )}

            {showRequestDetailModal && !isCompleted && requestDetailDto && (
                <RequestDetailModal onClick={() => onClickRequestToConsultant(requestDetailDto.requestResponseDto.id)} />
            )}

        </>
    )
};

export default FeedbackRequestPage;
