from flask import Flask
from app import vibez_blueprint

vibezApi = Flask(__name__)

vibezApi.register_blueprint(vibez_blueprint)

if __name__ == "__main__":
    vibezApi.run(debug=True)
