from flask import Flask
import app as vibez

vibezApi = Flask(__name__)

vibezApi.register_blueprint(vibez.vibez_blueprint)

if __name__ == "__main__":
    vibezApi.run(debug=True)
