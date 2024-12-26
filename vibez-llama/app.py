from flask import Flask, request, jsonify
from transformers import LlamaForCausalLM, LlamaTokenizer
from huggingface_hub import hf_hub_download
import torch
import os
from dotenv import load_dotenv

load_dotenv()
vibezLlama = Flask(__name__)

modelName = os.getenv("MODEL")
hf_token = os.getenv("HF_TOKEN")

modelPath = hf_hub_download(repo_id=modelName, filename="pytorch_model.bin", token=hf_token)
configPath = hf_hub_download(repo_id=modelName, filename="config.json", token=hf_token)
tokenizerPath = hf_hub_download(repo_id=modelName, filename="tokenizer.json", token=hf_token)

tokenizer = LlamaTokenizer.from_pretrained(tokenizerPath)
model = LlamaForCausalLM.from_pretrained(modelPath, config=configPath)

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