import { useState } from 'react';
import Button from '../../../../components/button/Button';
import Input from '../../../../components/input/Input';
import ModalTitle from '../../../../components/title/ModalTitle';
import TextArea from '../../../../components/input/TextArea';

export interface JobPostingFormData {
    title: string;
    companyType: string;
    workingTime: string;
    workLocation: string;
    job: string;
    career: string;
    education: string;
    salary: string;
    content: string;
    certification: string;
    jobPostingImages: File[];
}

interface JobPostingCreateModalProps {
    onSave: (data: JobPostingFormData) => void;
    modalTitle: string;
}

const JobPostingCreateModal = ({ onSave, modalTitle }: JobPostingCreateModalProps) => {
    const [title, setTitle] = useState<string>('');
    const [companyType, setCompanyType] = useState<string>('');
    const [workingTime, setWorkingTime] = useState<string>('');
    const [workLocation, setWorkLocation] = useState<string>('');
    const [job, setJob] = useState<string>('');
    const [career, setCareer] = useState<string>('');
    const [education, setEducation] = useState<string>('');
    const [content, setContent] = useState<string>('');
    const [salary, setSalary] = useState<string>('');
    const [certification, setCertification] = useState<string>('');
    const [jobPostingImages, setJobPostingImages] = useState<File[]>([]);

    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    const formattedDate = `${year}.${month}.${day}`;

    const getFormData = (): JobPostingFormData => {
        return {
            title,
            companyType,
            workingTime,
            workLocation,
            job,
            career,
            education,
            salary,
            content,
            certification,
            jobPostingImages,
        };
    };


    return (
        <div className="p-5">
            <div className="flex justify-between items-center mb-5">
                <ModalTitle title={modalTitle} />
                <div className="text-sm text-gray-600">
                    등록 날짜: {formattedDate}
                </div>
            </div>
            <div className='flex items-center space-x-20'>
                <Input

                    label="공고명"
                    placeholder=""
                    size="m"
                    disabled={false}
                    type="text"
                    value={title}
                    setValue={setTitle}
                />

                <Input
                    label="기업형태"
                    placeholder=""
                    size="m"
                    disabled={false}
                    type="text"
                    value={companyType}
                    setValue={setCompanyType}
                />
            </div>
            <div className='flex items-center space-x-20'>
                <Input
                    label="근무시간"
                    placeholder=""
                    size="m"
                    disabled={false}
                    type="text"
                    value={workingTime}
                    setValue={setWorkingTime}
                />
                <Input
                    label="근무지역"
                    placeholder=""
                    size="m"
                    disabled={false}
                    type="text"
                    value={workLocation}
                    setValue={setWorkLocation}
                />
            </div>
            <div className='flex items-center space-x-20'>
                <Input
                    label="모집부문"
                    placeholder=""
                    size="m"
                    disabled={false}
                    type="text"
                    value={job}
                    setValue={setJob}
                />
                <Input
                    label="경력"
                    placeholder=""
                    size="m"
                    disabled={false}
                    type="text"
                    value={career}
                    setValue={setCareer}
                />
            </div>
            <div className='flex items-center space-x-20'>
                <Input
                    label="학력"
                    placeholder=""
                    size="m"
                    disabled={false}
                    type="text"
                    value={education}
                    setValue={setEducation}
                />
                <Input
                    label="급여"
                    placeholder=""
                    size="m"
                    disabled={false}
                    type="text"
                    value={salary}
                    setValue={setSalary}
                />
            </div>

            <TextArea label={'내용'}
                placeholder={''}
                disabled={false}
                value={''}
                setValue={function (value: string): void {
                    throw new Error('Function not implemented.');
                }} />
            <TextArea label={'자격요건'}
                placeholder={''}
                disabled={false}
                value={''}
                setValue={function (value: string): void {
                    throw new Error('Function not implemented.');
                }} />
            <div className="mt-1">
                <TextArea
                    label="내용"
                    placeholder="내용"
                    disabled={false}
                    value={content}
                    setValue={setContent}
                />
            </div>

            <div className="mt-1">
                <TextArea
                    label="자격 요건"
                    placeholder="자격 요건"
                    disabled={false}
                    value={certification}
                    setValue={setCertification}
                />
            </div>

            <div className="image-upload-section mt-1">
                <p>채용공고 이미지</p>
                <div className="border border-dashed border-gray-300 p-5 text-center cursor-pointer min-h-[100px] flex items-center justify-center relative">
                    <input
                        type="file"
                        multiple
                        accept="image/*"
                        className="hidden"
                        id="jobPostingImageUpload"
                        onChange={(e) => {
                            if (e.target.files) {
                                setJobPostingImages(Array.from(e.target.files));
                            }
                        }}
                    />
                    <label htmlFor="jobPostingImageUpload" className="absolute inset-0 flex items-center justify-center cursor-pointer">
                        <img src="/path/to/your/image_icon.png" alt="Add Image" className="w-10 h-10" />
                        <span className="ml-2.5 text-gray-500">이미지 추가</span>
                    </label>
                    {jobPostingImages.length > 0 && (
                        <div className="mt-2.5 flex flex-wrap gap-2">
                            {jobPostingImages.map((file, index) => (
                                <span key={index} className="mr-2 bg-gray-200 px-2 py-1 rounded-md text-sm">
                                    {file.name}
                                </span>
                            ))}
                        </div>
                    )}
                </div>
                <p className="text-xs text-gray-500 mt-1">
                    * 최대 5개까지 업로드 가능합니다. (권장 크기: 1200 x 675px)
                </p>
            </div>
            <div className='text-right' mt-5>
                <Button color={'theme'} size={'s'} disabled={false} text={'작성'} onClick={function (): void {
                    throw new Error('Function not implemented.');
                }} type={'button'} />
            </div>
        </div >
    )
}

export default JobPostingCreateModal;