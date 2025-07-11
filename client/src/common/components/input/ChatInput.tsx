interface ChatInput {
    label: string;
    placeholder: string;
    size: 's' | 'm' | 'l';
    disabled: boolean;
    type: 'text' | 'password' | 'email' | 'date';
    value: string;
    setValue: (value: string) => void
    handleKeyPress: (e: React.KeyboardEvent<HTMLInputElement>) => void
}

const sizeClass = {
    s: 'w-[220px] h-[42px]',
    m: 'w-[464px] h-[42px]',
    l: 'w-[952px] h-[42px]',
};

const defaultSetting = 'text-base font-roboto rounded-lg border border-gray-300 mb-6 ml-[24px] px-3';
const labelSetting = 'font-roboto text-base mb-2 inline-block ml-[24px]';

const ChatInput = ({label, placeholder, size, disabled, type, value, setValue, handleKeyPress}: ChatInput ) => {
    return (
            <div>
            <label className={labelSetting}>{label}</label>
            <br />
            <input className={`${sizeClass[size]} ${defaultSetting}`}
                placeholder={placeholder}
                disabled={disabled}
                type={type}
                value={value}
                onChange={(e: React.ChangeEvent<HTMLInputElement>) => setValue(e.target.value)}
                onKeyDown={handleKeyPress}
                />
        </div>
    );
}

export default ChatInput;