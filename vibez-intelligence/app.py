import re
from flask import Flask, request, jsonify
import os
from dotenv import load_dotenv
from flask_cors import CORS
from openai import OpenAI

load_dotenv()

vibezApi = Flask(__name__)
CORS(vibezApi, origins=["*"])

client = OpenAI(
    base_url=os.getenv("BASE"),
    api_key=os.getenv("KEY"),
)

SYSTEM_MESSAGE = os.getenv("SYSTEM_MESSAGE")

def getAutoReplies(messages):
    try:
        completion = client.chat.completions.create(
            extra_headers={
                "HTTP-Referer": os.getenv("SITEURL"),  
                "X-Title": os.getenv("SITENAME"),  
            },
            model=os.getenv("MODEL"),
            messages=messages,
        )
        
        if completion and completion.choices:
            reply = completion.choices[0].message.content
            return reply
        else:
            return "Error: No valid response from the model."

    except Exception as e:
        print(f"OpenAI API Error: {e}")
        return "Error: Unable to generate a reply. Please try again later."

@vibezApi.route("/vibez/ai_reply", methods=["POST"])
def aiReplyApi():
    data = request.get_json()
    if "messages" not in data:
        return jsonify({"error": "messages not found"}), 400
    messages = data["messages"]
    if not isinstance(messages, list) or not all(
        isinstance(msg, dict) and "role" in msg and "content" in msg for msg in messages
    ):
        return jsonify({"error": "Input not valid"}), 400

    system_message = {
        "role": "system",
        "content": SYSTEM_MESSAGE  
    }
    messages_with_system = [system_message] + messages
    reply = getAutoReplies(messages_with_system)

    return jsonify({"reply": reply})

if __name__ == "__main__":
    vibezApi.run()