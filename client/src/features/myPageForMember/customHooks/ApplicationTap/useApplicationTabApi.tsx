import { printErrorInfo } from "../../../../common/utils/ErrorUtil";
import { createApplicationApi, readMyApplicationApi, readMyApplicationsApi, readMyProposalApi} from "../../apis/MyPageForMemberApi";
import type { ApplicationCreateDto, ApplicationResponseDto, ApplicationSummaryForMemberDto } from "../../props/myPageForMemberProps";
import { useApplicationTabController } from "./useApplicationTabController";


export const useApplicationTabApi = () => {
    const { 
        setApplicationSummaryForMemberDtos,
        setTotalElements,
        setApplicationResponseDto,
    } = useApplicationTabController();

    const readApplications = async (page: number, size: number) => {
        try {

            const response = await readMyApplicationsApi(page, size);
            console.log(response);
            const applicationSummaryForMemberDtos: ApplicationSummaryForMemberDto[] = response.data.content;
            setApplicationSummaryForMemberDtos(applicationSummaryForMemberDtos);
            setTotalElements(response.data.totalElements);
        } catch (err) {
            printErrorInfo(err);
        }

    }

    const readApplication = async (id: number) => {
        try {
            const applicationResponseDto: ApplicationResponseDto = (await readMyApplicationApi(id)).data;
            setApplicationResponseDto(applicationResponseDto);
        } catch (err) {
            printErrorInfo(err);
        }
    }

    const createApplication = async (applicationCreateDto : ApplicationCreateDto) =>{
        try {
            await createApplicationApi(applicationCreateDto)
        } catch (err) {
            printErrorInfo(err);
        }
    }

    return {
        createApplication,
        readApplications,
        readApplication,
    }


}