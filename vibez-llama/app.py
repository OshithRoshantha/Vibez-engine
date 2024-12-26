from flask import Flask, request, jsonify
from transformers import LlamaForCausalLM, LlamaTokenizer
import torch
import os
from dotenv import load_dotenv

load_dotenv()
vibezLlama = Flask(__name__)

modelName = os.getenv("MODEL")
tokenizer = LlamaTokenizer.from_pretrained(modelName)
model = LlamaForCausalLM.from_pretrained(modelName)

model.eval()

def getAutoReplies(chatHistory):
    chatInput = "".join(chatHistory)
    inputs = tokenizer(chatInput, return_tensors="pt")
    
    with torch.no_grad():
        outputs = model.generate(inputs['inputIds'], max_length=100, num_return_sequences=5, no_repeat_ngram_size=2)
        reply = tokenizer.decode(outputs[0], skip_special_tokens=True)
        return reply.strip()
    
@vibezLlama.route("/ai_reply", methods=["POST"])
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
    vibezLlama.run()