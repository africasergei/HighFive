import { createSlice, type PayloadAction } from "@reduxjs/toolkit";
import type { RootState } from "../../common/store/store";

interface ChatNotificationState {
  // 읽지 않은 메시지가 있는 채팅방 ID 목록
  unreadChatRooms: number[];
}

const initialState: ChatNotificationState = {
  unreadChatRooms: [],
};

export const ChatNotificationSlice = createSlice({
  name: "chatNotification",
  initialState,
  reducers: {
    // 읽지 않은 채팅방 추가
    addUnreadChat: (state, action: PayloadAction<number>) => {
      // 중복 추가 방지
      if (!state.unreadChatRooms.includes(action.payload)) {
        state.unreadChatRooms.push(action.payload);
      }
    },
    // 읽은 채팅방 제거
    removeUnreadChat: (state, action: PayloadAction<number>) => {
      state.unreadChatRooms = state.unreadChatRooms.filter(
        (roomId) => roomId !== action.payload
      );
    },
    // 모든 채팅 알림 초기화
    clearAllChatNotifications: (state) => {
      state.unreadChatRooms = [];
    }
  },
});

export const { addUnreadChat, removeUnreadChat, clearAllChatNotifications } = ChatNotificationSlice.actions;

export const selectUnreadChatRooms = (state: RootState) => state.chatNotification.unreadChatRooms;

export default ChatNotificationSlice.reducer;