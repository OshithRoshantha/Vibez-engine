import os
from pymongo import MongoClient

MONGO_URI = os.getenv("MONGO_URI")  
DB_NAME = os.getenv("DB_NAME")  

client = MongoClient(MONGO_URI)
db = client["VibezAppDB"] 
