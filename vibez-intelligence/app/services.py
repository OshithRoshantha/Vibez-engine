import os
from dotenv import load_dotenv
from bson import ObjectId
from openai import OpenAI
from .database import db

load_dotenv()

client = OpenAI(
    base_url = os.getenv("BASE"),
    api_key = os.getenv("KEY"),
)

def get_auto_replies(messages):
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
            return completion.choices[0].message.content
        return "Error: No valid response from the model."
    except Exception as e:
        return "Hey, i'm a bit tired right now. Can we chat later? 🥲"

def fetch_chat_messages(receiver_id, user_id):
    try:
        direct_chat = db.directChats.find_one({"memberIds": {"$all": [receiver_id, user_id]}})
        if not direct_chat or "messageIds" not in direct_chat:
            return []
        message_ids = direct_chat["messageIds"]
        recent_messages = message_ids[-15:]
        recent_messages = [ObjectId(msg_id) for msg_id in recent_messages]
        messages = db.messages.find({"_id": {"$in": recent_messages}}, {"senderId": 1, "message": 1})
        formatted_messages = [
            {
                "role": "assistant" if message["senderId"] == user_id else "user",
                "content": message["message"]
            }
            for message in messages
        ]
        return formatted_messages
    except Exception as e:
        print(f"Database Error: {e}")
        return []
