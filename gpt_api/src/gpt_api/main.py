# src/gpt_api/main.py

from fastapi import FastAPI
from gpt_api.routers import prompt_router
import uvicorn

app = FastAPI()

app.include_router(prompt_router.router)


def main():
    uvicorn.run("gpt_api.main:app", host="0.0.0.0", port=9000, reload=True)