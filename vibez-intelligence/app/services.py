import os
from dotenv import load_dotenv
from openai import OpenAI
from database import db

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
        print(f"OpenAI API Error: {e}")
        return "Error: Unable to generate a reply. Please try again later."

def fetch_chat_messages(chat_id, user_id):
    try:
        direct_chat = db.directChats.find_one({"chatId": chat_id}, {"messageIds": 1})
        if not direct_chat or "messageIds" not in direct_chat:
            return []

        message_ids = direct_chat["messageIds"]
        messages = db.messages.find({"messageId": {"$in": message_ids}}, {"senderId": 1, "message": 1})
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
