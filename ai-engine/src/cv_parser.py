from typing import NamedTuple, List
from io import BytesIO
from pypdf import PdfReader
from docx import Document

# DTO for parsing results - Keeping English First
class ParsedData(NamedTuple):
    extracted_text: str
    skills: List[str]
    seniority: str

def parse_pdf(content: bytes) -> str:
    """Extracts text from a PDF file using pypdf."""
    text = ""
    try:
        reader = PdfReader(BytesIO(content))
        for page in reader.pages:
            page_text = page.extract_text()
            if page_text:
                text += page_text + "\n"
    except Exception as e:
        print(f"Error parsing PDF: {e}")
        text = "Error: PDF content could not be extracted."
    return text

def parse_docx(content: bytes) -> str:
    """Extracts text from a DOCX file."""
    text = ""
    try:
        document = Document(BytesIO(content))
        for paragraph in document.paragraphs:
            text += paragraph.text + "\n"
    except Exception as e:
        print(f"Error parsing DOCX: {e}")
        text = "Error: DOCX content could not be extracted."
    return text

def parse_document_content(content: bytes, filename: str) -> ParsedData:
    """Main function to route parsing based on file extension."""
    filename = filename.lower()
    
    if filename.endswith(".pdf"):
        raw_text = parse_pdf(content)
    elif filename.endswith((".docx", ".doc")):
        raw_text = parse_docx(content)
    elif filename.endswith(".txt"):
        try:
            raw_text = content.decode('utf-8')
        except UnicodeDecodeError:
            raw_text = content.decode('latin-1')
    else:
        raw_text = "Unsupported file type."

    # For now, we return mock data for skills and seniority. 
    # These will be populated by the AI model in the next step.
    return ParsedData(
        extracted_text=raw_text.strip(),
        skills=["Processing...", "AI-Analysis-Pending"],
        seniority="Under Analysis"
    )