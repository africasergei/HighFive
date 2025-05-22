from fastapi import FastAPI
from gpt_api.routes.gpt import router as api_router
import uvicorn

app = FastAPI()

app.include_router(api_router, prefix="/gpt_api")

def main():
    uvicorn.run("gpt_api.main:app", host="127.0.0.1", port=9000, reload=True)
