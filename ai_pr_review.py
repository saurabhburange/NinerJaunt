import os
import sys
import json
import vertexai
from vertexai.generative_models import GenerativeModel
import textwrap
import re

def clean_text(text):
    text = text.replace('#', '')
    text = re.sub(r'\s*\n\s*', ' ', text)
    text = re.sub(r'\*', '-', text)
    return text.strip()

# Set Google Cloud authentication
keyfile_path = os.path.join(os.getenv("GITHUB_WORKSPACE", ""), "gcp-key.json")
os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = keyfile_path

# Google Cloud Config
PROJECT_ID = "carbide-acre-451700-g8"
LOCATION = "us-central1"
MODEL_NAME = "gemini-pro"

def call_vertex_ai(pr_data, commit_message=None):
    vertexai.init(project=PROJECT_ID, location=LOCATION)
    model = GenerativeModel(MODEL_NAME)


    print(pr_data, 'log 1')

    prompt = f"""
    You are an AI code reviewer, Give me a small consise summary and any suggestions. Make it short. PR data - {pr_data}.
    """

    response = model.generate_content(prompt)

    return response.text


if __name__ == "__main__":
    pr_data_file = sys.argv[1] if len(sys.argv) > 1 else None
    commit_message_file = sys.argv[2] if len(sys.argv) > 2 else None

    if not pr_data_file or not os.path.exists(pr_data_file):
        print("Error: PR data file is missing.")
        sys.exit(1)

    with open(pr_data_file, "r", encoding="utf-8") as file:
        pr_data = file.read()

    commit_message = None
    if commit_message_file and os.path.exists(commit_message_file):
        with open(commit_message_file, "r", encoding="utf-8") as file:
            commit_message = file.read().strip()

    feedback = call_vertex_ai(pr_data, commit_message)

    feedback1 = feedback.lstrip("#")


    with open("ai_feedback.md", "w", encoding="utf-8") as outfile:
        outfile.write(feedback1)

