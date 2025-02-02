import os
from dotenv import load_dotenv
from flask import Blueprint, request, jsonify
from .services import get_auto_replies, fetch_chat_messages

load_dotenv()

vibez_blueprint = Blueprint("vibez", __name__)

@vibez_blueprint.route("/vibez/ai_reply", methods=["POST"])
def ai_reply_api():
    
    data = request.get_json()
    if not data or "receiverId" not in data or "userId" not in data:
        return jsonify({"error": "receiverId and userId are required"}), 400

    receiver_id = data["receiverId"]
    user_id = data["userId"]

    messages = fetch_chat_messages(receiver_id, user_id)

    if not isinstance(messages, list) or not all(
        isinstance(msg, dict) and "role" in msg and "content" in msg for msg in messages
    ):
        return jsonify({"error": "Invalid input format"}), 400

    system_message = {
        "role": "system",
        "content": os.getenv("SYSTEM_MESSAGE") 
    }
    messages.insert(0, system_message)

    reply = get_auto_replies(messages)
    
    return jsonify({"reply": reply})



