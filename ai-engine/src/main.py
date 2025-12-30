import sys
import os
import logging
import io
from fastapi import FastAPI, UploadFile, File, HTTPException
import uvicorn
from pypdf import PdfReader
from docx import Document

# 1. Configuração de Path
current_dir = os.path.dirname(os.path.abspath(__file__))
if current_dir not in sys.path:
    sys.path.append(current_dir)

# 2. Imports Locais (Agora com o SDK novo configurado internamente)
from services.ai_service import analyze_resume_with_gemini
from embedding_service import generate_embeddings

# 3. Configuração de Logs
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = FastAPI(
    title="AI Job Hunter Engine",
    description="Microserviço Python para extração de texto, análise IA e Embeddings"
)

def extract_text_from_file(file_content: bytes, filename: str) -> str:
    text = ""
    try:
        if filename.lower().endswith('.pdf'):
            reader = PdfReader(io.BytesIO(file_content))
            for page in reader.pages:
                extracted = page.extract_text()
                if extracted:
                    text += extracted + "\n"
        elif filename.lower().endswith('.docx'):
            doc = Document(io.BytesIO(file_content))
            for para in doc.paragraphs:
                text += para.text + "\n"
        else:
            text = file_content.decode('utf-8')
    except Exception as e:
        logger.error(f"Erro na extração de texto de {filename}: {str(e)}")
        raise HTTPException(status_code=400, detail=f"Falha na extração de texto")
    
    return text.strip()

@app.get("/health")
async def health_check():
    return {"status": "online", "service": "ai-engine"}

@app.post("/api/v1/analyze-cv")
async def analyze_cv(file: UploadFile = File(...)):
    logger.info(f"Recebido arquivo para análise: {file.filename}")
    
    content = await file.read()
    if not content:
        raise HTTPException(status_code=400, detail="Arquivo vazio")

    # 1. Extração de texto bruta
    extracted_text = extract_text_from_file(content, file.filename)

    if not extracted_text:
        raise HTTPException(status_code=400, detail="Não foi possível extrair texto do documento")

    # 2. Análise IA (Gemini) com proteção contra erros de cota (429)
    try:
        ai_analysis = analyze_resume_with_gemini(extracted_text)
    except Exception as e:
        logger.warning(f"IA falhou ou cota excedida: {str(e)}")
        ai_analysis = {
            "skills": [],
            "seniority": "Analysis Pending",
            "summary": "AI Quota exceeded. Basic text extracted successfully."
        }

    # 3. Geração de Embeddings com proteção
    try:
        embedding_vector = generate_embeddings(extracted_text)
    except Exception:
        embedding_vector = [0.0] * 768

    logger.info(f"Processamento concluído para: {file.filename}")

    # 5. Retorno para o Java (Chaves em camelCase para bater com o DTO do Spring)
    return {
        "extractedText": extracted_text,
        "skills": ai_analysis.get("skills", []),
        "seniority": ai_analysis.get("seniority", "Unknown"),
        "summary": ai_analysis.get("summary", ""),
        "embedding": embedding_vector
    }

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)