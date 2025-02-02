from flask import Flask
from app import vibez_blueprint
from flask_cors import CORS

vibezApi = Flask(__name__)
CORS(vibezApi, origins=["*"])

vibezApi.register_blueprint(vibez_blueprint)

if __name__ == "__main__":
    vibezApi.run(host="0.0.0.0", port=5000, debug=True)
