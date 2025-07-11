import ModalTitle from "../../../common/components/title/ModalTitle";
import CommonModal from "../../../common/modals/CommonModal";
import Button from "../../../common/components/button/Button";
import TextArea from "../../../common/components/input/TextArea";
import { useEffect, useState } from "react";
import { createComment, readPostDetail, updatePost } from "../apis/PostApi";
import type { CommentCreateDto, PostUpdateDto } from "../props/PostProps";
import CommentList from "../components/PostCommentList";
import { useDispatch, useSelector } from "react-redux";
import type { RootState } from "../../../common/store/store";
import AuthUtil from "../../../common/utils/AuthUtil";
import { printErrorInfo } from "../../../common/utils/ErrorUtil";
import Input from "../../../common/components/input/Input";
import { startNewChat } from "../../chat/ChatControlSlice";
import { sendNotification } from "../../notification/NotificationControlSlice";

interface PostDetailModalProps {
    postId: number;
    authorId: number;
    nicknameOrName: string;
    onClose: () => void;
    onUpdateCommentCount?: (postId: number) => void;
    onUpdatePost? : (postId:number) => void;
}

interface CommentResponseDto {
    authorId: number;
    nicknameOrName: string;
    content: string;
    createdDate: string;
}
function PostDetailModal({ postId, onClose, onUpdateCommentCount, onUpdatePost}: PostDetailModalProps) {

    const dispatch = useDispatch();

    const accessToken = useSelector((state: RootState) => state.auth.accessToken);
    const currentUserId = AuthUtil.getIdFromToken(accessToken);



    const [title, setTitle] = useState<string>("");
    const [content, setContent] = useState<string>("");
    const [comment, setComment] = useState<string>("");
    const [comments, setComments] = useState<CommentResponseDto[]>([]);
    const [authorId, setAuthorId] = useState<number | null>(null);



    const fetchPostDetail = async () => {
        try {
            const res = await readPostDetail(postId);
            const { title, content, author_id, comments } = res.data;
            setTitle(title);
            setContent(content);
            setAuthorId(author_id);
            setComments(comments);
        } catch (err) {
            printErrorInfo(err);
        }
    };

    useEffect(() => {
        fetchPostDetail();
    }, [postId]);


   const handleCommentWrite = async () => {
    try {
        const dto: CommentCreateDto = {
            postId,
            content: comment,
        };
        await createComment(dto);       
        setComment("");                
        await fetchPostDetail();
        if(currentUserId != null && authorId != null){
            dispatch(sendNotification({id : currentUserId, receiverId: authorId, notificationType: "COMMENT"}));
        }
        

        if (onUpdateCommentCount) {        
            onUpdateCommentCount(postId);
        }
    } catch (err) {
        printErrorInfo(err);
    }
};


    const handlePostUpdate = async () => {
        try {
            const dto: PostUpdateDto = {
                id: postId,
                title: title,
                content: content
            };
            await updatePost(dto);
            
            if(onUpdatePost) {
                onUpdatePost(postId)
            }
        } catch (err) {
            printErrorInfo(err);
        }
    }

    const isAuthor = currentUserId !== null && currentUserId === authorId;

    // 댓글창에서 사용자명 클릭 시 채팅 버튼 모달 출력
    const handleCommentAuthorClick = (commentAuthorId: number, nicknameOrName: string): void => {
        if(commentAuthorId != currentUserId){
            dispatch(startNewChat({
                id: commentAuthorId,
                name: nicknameOrName,
                avatar: "/placeholder.svg?height=40&width=40",
                step: 1
            }))
        } else { return; }
    };

    return (
        <CommonModal size="l" onClose={onClose}>
            <div className="font-roboto">
                <ModalTitle title={"게시글"} />
                <div className="flex-1 space-y-2">
                    <Input label={"제목"} placeholder={""} size={"l"} disabled={!isAuthor} type={"text"} value={title} setValue={setTitle} />
                    <TextArea size={"l"} label={"내용"} placeholder={""} disabled={!isAuthor} value={content} setValue={setContent} />
                    <div className="border-b border-gray-200 w-[952px] mx-auto"></div>
                    <TextArea size={"l"} label={"댓글 작성"} placeholder={"댓글을 입력하세요"} disabled={false} value={comment} setValue={setComment} />
                </div>

                <div className="flex justify-end mb-10 mr-6">
                    <Button color={"theme"} text={"댓글 작성"} size={"s"} disabled={false} type="button" onClick={handleCommentWrite} />
                </div>

                <div className="mb-6">
                    <CommentList comments={comments} onAuthorClick={handleCommentAuthorClick} />
                </div>

                {isAuthor && (
                    <div className="flex justify-end mt-4 mr-6">
                        <Button color={"theme"} size={"s"} disabled={!isAuthor} text={"수정"} type="button" onClick={handlePostUpdate} />
                    </div>
                )}
            </div>
        </CommonModal>
    );
}

export default PostDetailModal;