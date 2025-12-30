from google import genai
import os
import json
import logging

logger = logging.getLogger(__name__)

# 1. Configuração do Novo SDK (google-genai)
# Certifique-se de que a variável no docker-compose é GOOGLE_API_KEY
client = genai.Client(api_key=os.getenv("GOOGLE_API_KEY"))

def analyze_resume_with_gemini(resume_text):
    """
    Analisa o texto do currículo usando o novo SDK do Gemini.
    """
    # 2. Uso do modelo validado no terminal
    model_id = "gemini-2.0-flash"
    
    prompt = f"""
    You are an expert IT Recruiter. Analyze the following resume text and return ONLY a JSON object.
    
    Resume Text:
    {resume_text}
    
    Expected JSON Format:
    {{
        "skills": ["skill1", "skill2"],
        "seniority": "Junior/Middle/Senior/Expert",
        "summary": "Short professional summary"
    }}
    """
    
    try:
        # 3. Chamada correta usando o novo Client
        response = client.models.generate_content(
            model=model_id,
            contents=prompt
        )
        
        # 4. Limpeza da resposta para garantir JSON puro
        text_response = response.text
        json_text = text_response.replace("```json", "").replace("```", "").strip()
        
        return json.loads(json_text)
        
    except Exception as e:
        # Se der erro de Quota (429), ele retorna este fallback
        logger.error(f"Erro ao chamar o Gemini: {e}")
        return {
            "skills": [],
            "seniority": "Pending",
            "summary": "AI processing failed or quota exceeded."
        }