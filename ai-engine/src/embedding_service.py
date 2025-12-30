from google import genai
import os
import logging

logger = logging.getLogger(__name__)

# Configuração do novo cliente usando a chave do ambiente
client = genai.Client(api_key=os.getenv("GOOGLE_API_KEY"))

def generate_embeddings(text: str):
    """
    Gera embeddings usando o novo SDK google-genai.
    """
    try:
        # Limita o texto para evitar estouro de tokens no tier gratuito
        truncated_text = text[:3000] 
        
        # Modelo atualizado para geração de embeddings
        response = client.models.embed_content(
            model="text-embedding-004",
            contents=truncated_text
        )
        
        # Retorna a lista de floats (vetor)
        return response.embeddings[0].values
        
    except Exception as e:
        logger.error(f"Erro ao gerar embeddings: {e}")
        # Retorna vetor zerado (768 dimensões) para não travar o banco de dados
        return [0.0] * 768