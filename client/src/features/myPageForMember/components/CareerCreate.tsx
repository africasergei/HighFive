import { useState } from "react";
import { BigIntenalBox } from "../../../common/components/box/Box";
import Button from "../../../common/components/button/Button";
import Input from "../../../common/components/input/Input";
import type { CareerCreateDto } from "../props/myPageForMemberProps";
import { useDocumentTabApi } from "../customHooks/DocumentTab/useDocumentTabApi";
import Select from "../../../common/components/input/Select";
import { jobTypeEnum } from "../../../common/enum/Enum";

interface CareerCreateProps {
    setIsAddCareerMode : ( isAddCareerMode : boolean ) => void
}


const CareerCreate = ({setIsAddCareerMode }: CareerCreateProps) => {


    const [career, setCareer] = useState<CareerCreateDto>({
        companyName : "",
        job : "",
        department : "",
        position : "",
        startDate : "",
        endDate : "",
    });

    const {
        companyName,
        job,
        department,
        position,
        startDate,
        endDate,
    } = career;

    const setCareerField = (field: keyof CareerCreateDto) => (value: string) => {
        setCareer(prev => ({
            ...prev,
            [field]:value
        }));
    };

    const {
        createCareer
    } = useDocumentTabApi();


    const [isChangeButtonClicked, setIsChangeButtonClicked] = useState(true);
    const onClickChangeButton = () => {
        setIsChangeButtonClicked(true);
    }
    const onClickSaveButton = () => {
        createCareer(career);
        setIsChangeButtonClicked(false);
        setIsAddCareerMode(false);
    }

    return (
        <BigIntenalBox>
            <div className="flex">
                <Input label={"기업명"} placeholder={""} size={"bibm"} disabled={!isChangeButtonClicked} type={"text"} value={companyName} setValue={setCareerField("companyName")} />
                <Select label={"직무"} options={jobTypeEnum} size={"bibm"} disabled={!isChangeButtonClicked} value={job} setValue={setCareerField("job")} />
            </div>
            <div className="flex">
                <Input label={"부서"} placeholder={""} size={"bibm"} disabled={!isChangeButtonClicked} type={"text"} value={department} setValue={setCareerField("department")} />
                <Input label={"직급"} placeholder={""} size={"bibm"} disabled={!isChangeButtonClicked} type={"text"} value={String(position)} setValue={setCareerField("position")} />
            </div>
            <div className="flex">
                <Input label={"입사일"} placeholder={""} size={"bibs"} disabled={!isChangeButtonClicked} type={"date"} value={startDate} setValue={setCareerField("startDate")} />
                <Input label={"퇴사일"} placeholder={""} size={"bibs"} disabled={!isChangeButtonClicked} type={"date"} value={endDate || ""} setValue={setCareerField("endDate")} />
            </div>

            <div className="flex justify-end mr-6">
                {isChangeButtonClicked ?
                    <Button color={"theme"} size={"s"} disabled={false} text={"저장"} type={"button"} onClick={onClickSaveButton} />
                    :
                    <Button color={"theme"} size={"s"} disabled={false} text={"수정"} type={"button"} onClick={onClickChangeButton} />
                }
            </div>

        </BigIntenalBox>
    )
}

export default CareerCreate;