from gpt_api.schemas.guide_schema import GuidePromptRequestDto
from gpt_api.schemas.similarity_schema import SimilarityRequestDto
from gpt_api.services.guide_service import GuideService
from gpt_api.services.consulting_service import ConsultingService
from gpt_api.services.similarity_service import SimilarityService

router = APIRouter()

@router.post("/guide/edit")
def set_edit_guide(req: GuidePromptRequestDto):
    GuideService.save_edit_guide(req)

@router.post("/guide/feedback")
def set_feedback_guide(req: GuidePromptRequestDto):
    GuideService.save_feedback_guide(req)

@router.post("/edit", response_model=GPTResponse)
def ask_edit(req: GPTRequest):
    return ConsultingService.call_edit_gpt(req)

@router.post("/feedback", response_model=GPTResponse)
def ask_feedback(req: GPTRequest):
    return ConsultingService.call_feedback_gpt(req)

@router.post("/similarity")
def get_similarity(req: SimilarityRequestDto):
    return SimilarityService.calc(req)