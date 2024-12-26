from flask import Flask, request, jsonify
import requests
import os
from dotenv import load_dotenv

load_dotenv()

vibezApi = Flask(__name__)

model = os.getenv("MODEL")
hfToken = os.getenv("HF_TOKEN")

headers = {
    "Authorization": f"Bearer {hfToken}"
}

def getAutoReplies(chatHistory):
    chatInput = "".join(chatHistory)
    payload = {
        "inputs": chatInput,
        "parameters": {
            "max_length": 100,
            "num_return_sequences": 1,
            "no_repeat_ngram_size": 2
        }
    }
    
    response = requests.post(model, headers=headers, json=payload)
    if response.status_code == 200:
        reply = response.json()[0]["generated_text"]
        return reply.strip()
    else:
        return f"Error: {response.json()}"

@vibezApi.route("/vibez/ai_reply", methods=["POST"])
def aiReplyApi():
    data = request.get_json()
    
    if "chatHistory" not in data:
        return jsonify({"error": "chatHistory not found"}), 400
    
    chatHistory = data["chatHistory"]
    
    if not isinstance(chatHistory, list) or not all(isinstance(chat, str) for chat in chatHistory):
        return jsonify({"error": "chatHistory must be a list of strings"}), 400
    
    reply = getAutoReplies(chatHistory)
    
    return jsonify({"reply": reply})

if __name__ == "__main__":
    vibezApi.run()
