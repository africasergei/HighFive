import { useState } from 'react';
import Button from '../../../../components/button/Button';
import Input from '../../../../components/input/Input';
import ModalTitle from '../../../../components/title/ModalTitle';

interface TalentFilterModalProps {
    onApply : ()=> void;
}



const TalentFilterModal  = ({onApply} : TalentFilterModalProps) => {
    const [careerValue, setCareerValue] = useState('');
    const [educationValue, setEducationValue] = useState('');
    const [locationValue, setLocationValue] = useState('');
    const [jobValue, setJobValue] = useState('');


    return (
        <div
            className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-[1000]"
        >
            <div
                className="bg-white p-6 rounded-lg shadow-xl w-full max-w-md relative" 
            >
                <ModalTitle title="인재 필터" />

                <div className="flex space-x-5">
                    <Input
                        label="경력"
                        placeholder=""
                        size="s"
                        disabled={false}
                        type="text"
                        value={careerValue}
                        setValue={setCareerValue}

                    />
                    <Input
                        label="학력"
                        placeholder=""
                        size="s"
                        disabled={false}
                        type="text"
                        value={educationValue}
                        setValue={setEducationValue}

                    />
                </div>

                <div className="flex space-x-5"> 
                    <Input
                        label="지역"
                        placeholder=""
                        size="s"
                        disabled={false}
                        type="text"
                        value={locationValue}
                        setValue={setLocationValue}
            
                    />

                    <Input
                        label="직무"
                        placeholder=""
                        size="s"
                        disabled={false}
                        type="text"
                        value={jobValue}
                        setValue={setJobValue}
                    />
                </div>

                <div className="flex gap-2 mt-4 justify-end">
                    <Button color="theme" size="s" text="필터 적용" disabled={false} onClick={onApply} type="button" />
                </div>
            </div>
        </div>
    );
};

export default TalentFilterModal;