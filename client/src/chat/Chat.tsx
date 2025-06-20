import { useState, useEffect, useCallback, useRef } from 'react';
import type { IMessage } from '@stomp/stompjs';
import { type RootState } from '../common/store/store';
import { useSelector } from 'react-redux';
import { MessageCircle, X, Send, User } from "lucide-react"
import { Avatar, AvatarImage, AvatarFallback } from '@radix-ui/react-avatar';
import { Dialog, DialogContent, DialogTitle } from '@radix-ui/react-dialog';
import { ScrollArea } from '@radix-ui/react-scroll-area';
import AuthUtil from '../common/utils/AuthUtil';
import { Button } from '@radix-ui/themes/dist/cjs/components/button';
import ChatInput from '../common/components/input/ChatInput';

interface ChatRoomList {
    chatRoomId: number;
    contentId?: number;
    senderId?: number;
    name: string;
    content?: string;
    createdAt?: string;
}

interface ChatMessage {
    chatRoomId: number;
    contentId: number;
    senderId: number;
    name: string;
    content: string;
    createdAt: string;
}

// interface MockUser {
//     id: number;
//     name: string;
//     userType: string;
//     avatar: string;
// }

// 임시 유저 리스트
/*
const mockUsers: MockUser[] = [
    {
        id: 1,
        name: "샘숭맨",
        userType: "기업회원",
        avatar: "/placeholder.svg?height=40&width=40",
    },
    {
        id: 2,
        name: "리짜이밍",
        userType: "일반회원",
        avatar: "/placeholder.svg?height=40&width=40",
    },
    {
        id: 3,
        name: "준스톤",
        userType: "일반회원",
        avatar: "/placeholder.svg?height=40&width=40",
    },
    {
        id: 4,
        name: "한화",
        userType: "기업회원",
        avatar: "/placeholder.svg?height=40&width=40",
    },
]
    */

// SockJS와 Stomp를 이용해서 웹 소켓 서버로 연결하고 메시지를 주고 받는 기능 구현
const Chat = () => {
    const [chatRoomList, setChatRoomList] = useState<ChatRoomList[]>([]);
    const [chatRoomId, setChatRoomId] = useState<number | null>(null);
    const [chatRoomName, setChatRoomName] = useState<string | null>('');
    const [senderId, setSenderId] = useState<number | null>(null);
    const [targetId, setTargetId] = useState<number | null>(null);
    const [content, setContent] = useState<string>('');
    const [chatHistory, setChatHistory] = useState<ChatMessage[]>([]);
    const [showChatList, setShowChatList] = useState(false)
    const [selectedChat, setSelectedChat] = useState<boolean>(false)

    const scrollViewportRef = useRef<HTMLDivElement | null>(null);

    const stompClient = useSelector((state: RootState) => state.websocket.client); // 웹소켓 객체 획득
    const token = useSelector((state: RootState) => state.auth.accessToken); // 토큰 획득

    // 토큰에서 ID 추출하여 발신자 ID로 세팅 후, 채팅 상대의 ID를 가져옴.
    const getEachId = useCallback(() => {
        const id = AuthUtil.getIdFromToken(token); // ID 추출
        if (id != null) {
            setSenderId(id);
            setTargetId(1); // 채팅 대상 회원 ID 추출 (테스트를 위해 임의 작성함)
        } else {
            alert("해당 기능은 로그인이 필요합니다.")
            // 로그인 페이지로 이동
        }
    }, [token])

    // 채팅방 생성 또는 기존 채팅방 참여
    const createChatRoom = () => {
        getEachId();

        if (senderId != null && targetId != null) {
            fetch("https://localhost:9000/chats", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify({
                    id: targetId
                })
            })
                .then((response) => response.json()) // JSON -> Javascript 객체로 변환
                .then((data) => setChatRoomId(data.chatRoomId))
                .catch((error) => console.error("chatRoomId를 가져오지 못했습니다.: ", error));

            // 채팅방 구독 후, 채팅 대상 또한 동일한 채팅방을 구독하도록 서버에 요청
            if (stompClient != null) {
                stompClient.subscribe(`/topic/${chatRoomId}`, receiveMessage);
                stompClient.publish({
                    destination: "app/chat/subscribe",
                    body: JSON.stringify({ targetId, chatRoomId })
                })
            }
        } else {
            alert("해당 기능은 로그인이 필요합니다.")
            // 로그인 페이지로 이동
        }
    }

        // "전송" 버튼 클릭 시, 메시지 전송
    const handleSendMessage = () => {
        if (content.trim() && selectedChat && stompClient) {
            const message: ChatMessage = {
                chatRoomId: chatRoomId!,
                contentId: 9999, // 프론트 내 임시적인 데이터 처리이므로 임의의 숫자 할당
                senderId: senderId!,
                name: "me", // 상기와 마찬가지로 임의의 데이터 할당당
                content: content,
                createdAt: String(new Date())
            }
            setChatHistory((prev) => [...prev, message])
            setContent("")

            stompClient.publish({
                destination: '/app/chat/send',
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify({ senderId, targetId, chatRoomId, content })
            });
        }
    }

    // Enter키를 통한 메시지 전송
    const handleKeyPress = (e: React.KeyboardEvent) => {
        if (e.key === "Enter") {
            handleSendMessage()
        }
    }

    // 메시지 수신
    const receiveMessage = (payload: IMessage) => {
        const receivedMessage: ChatMessage = JSON.parse(payload.body);
        setChatHistory(prevHistory => [...prevHistory, receivedMessage]);
    };

    // 채팅방 리스트 불러오기
    const getChatRoomList = async () => {
        try {
            const response = await fetch('http://localhost:8090/chats', {
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                }
            });
            const tempChatRoomList: ChatRoomList[] = await response.json();
            setChatRoomList(tempChatRoomList);
        } catch(error){
            console.log("채팅방 리스트 불러오기 실패: ", error);
        }
    }

    // 채팅 내역 불러오기
    const getHistory = async () => { // IMessage : STOMP 라이브러리에서 지원하는 메시지 인터페이스
        try {
            const response = await fetch(`http://localhost:8090/chats/detail`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,
                },
                body: JSON.stringify({ chatRoomId })
            });
            const chatContents: ChatMessage[] = await response.json();

            if (chatContents) {
                setChatHistory(() => [...chatContents]); // 대화 내용 업데이트
            }
        } catch (error) {
            console.error("채팅 내역 불러오기 실패: ", error);
        }
    }

    // 메시지 수발신 시 채팅방 리스트 자동 렌더링
    useEffect(() => {
        getChatRoomList();
    }, [chatHistory])

    // 채팅 시 화면 자동 스크롤
    useEffect(() => {
        if (scrollViewportRef.current) {
            scrollViewportRef.current.scroll({
                top:  scrollViewportRef.current.scrollHeight, 
                behavior: 'smooth'
            });
        }
    }, [chatHistory]);

    // 채팅 아이콘 클릭 시, 채팅 리스트 출력
    const handleChatIconClick = () => {
        setShowChatList(!showChatList)
    }

    // 채팅방 클릭 시, 채팅 메시지 출력
    const handleChatSelect = (chatRoom: ChatRoomList) => {
        createChatRoom();
        setChatRoomId(chatRoom.chatRoomId)
        getHistory();
        setShowChatList(false)
        setSelectedChat(true);
        setChatRoomName(chatRoom.name);
    }

    return (
        <div className="fixed bottom-6 right-6 z-50">
            {/* 채팅 아이콘 */}
            {token && ( // 로그인 상태일 때만 채팅 아이콘 활성화
                <Button
                    onClick={handleChatIconClick}
                    className="rounded-full w-14 h-14 bg-[#EE57CD] hover:bg-[#EE57CD]/90 shadow-lg"
                    size="icon"
                >
                    <MessageCircle className="h-6 w-6 text-white" />
                </Button>
            )}

            {/* 채팅 리스트 */}
            {showChatList && (
                <div className="absolute bottom-16 right-0 w-80 bg-white rounded-lg shadow-xl border border-gray-200 max-h-96 overflow-hidden">
                    <div className="p-4 border-b border-gray-200">
                        <div className="flex items-center justify-between">
                            <h3 className="font-semibold text-gray-900">채팅</h3>
                            <Button variant="ghost" size="icon" onClick={() => setShowChatList(false)} className="h-6 w-6">
                                <X className="h-4 w-4" />
                            </Button>
                        </div>
                    </div>
                    <ScrollArea className="max-h-80">
                        <div className="p-2">
                            {chatRoomList.map((chatRoom) => (
                                <div
                                    key={chatRoom.chatRoomId}
                                    onClick={() => handleChatSelect(chatRoom)}
                                    className="flex items-center p-3 hover:bg-gray-50 rounded-lg cursor-pointer transition-colors"
                                >
                                    <Avatar className="h-10 w-10 mr-3">
                                        <AvatarImage src={/*mockUsers[chatRoom.chatRoomId].avatar || */"/placeholder.svg"} alt={chatRoom.name} />
                                        <AvatarFallback>
                                            <User className="h-5 w-5" />
                                        </AvatarFallback>
                                    </Avatar>
                                    <div className="flex-1 min-w-0">
                                        <p className="font-medium text-gray-900 truncate">{chatRoom.name}</p>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </ScrollArea>
                </div>
            )}

            {/* 채팅 모달 */}
            <Dialog open={selectedChat} onOpenChange={() => setSelectedChat(false)}>
                <DialogContent className="sm:max-w-md bg-white">
                        <DialogTitle className="flex items-center space-x-3">
                            <Avatar className="h-8 w-8">
                                <AvatarImage src={/* selectedChat?.avatar || */"/placeholder.svg"} alt={chatRoomName!} />
                                <AvatarFallback>
                                    <User className="h-4 w-4" />
                                </AvatarFallback>
                            </Avatar>
                            <span>{chatRoomName}</span>
                        </DialogTitle>
                        <Button variant="ghost" size="icon" onClick={() => setSelectedChat(false)} className="h-6 w-6">
                            <X className="h-4 w-4" />
                        </Button>

                    {/* 메시지 영역 */}
                    <ScrollArea className="h-80 w-full pr-4">
                        <div className="space-y-3">
                            {chatHistory.map((message) => (
                                <div key={message.contentId} className={`flex ${message.senderId === senderId ? "justify-end" : "justify-start"}`}>
                                    <div
                                        className={`max-w-xs px-4 py-2 rounded-lg ${message.senderId === senderId
                                            ? "bg-[#EE57CD] text-white"
                                            : "bg-white border border-[#EE57CD] text-[#EE57CD]"
                                            }`}
                                    >
                                        <p className="text-sm">{message.content}</p>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </ScrollArea>

                    {/* 메시지 입력 영역 */}
                    <div className="flex items-center space-x-2 pt-4 border-t">
                        <ChatInput
                            label=""
                            placeholder="메시지를 입력하세요..."
                            size="m"
                            disabled={false}
                            type="text"
                            value={content}
                            setValue = {setContent}
                            handleKeyPress={handleKeyPress}
                            // className="flex-1"
                        />
                        <Button onClick={handleSendMessage} className="bg-[#EE57CD] hover:bg-[#EE57CD]/90 text-white" size="icon">
                            <Send className="h-4 w-4" />
                        </Button>
                    </div>
                </DialogContent>
            </Dialog>
        </div>
    )
}

export default Chat;