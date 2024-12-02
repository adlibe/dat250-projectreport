from flask import Flask, jsonify, request
from flask_cors import CORS

import uuid  # for generating a session ID
import hashlib  # for password hashing

app = Flask(__name__)

CORS(app)

# Items -----------------------------
items = []

def addItem(_t, _d):
    items.append({
        'title' : _t,
        'description' : _d
    })

addItem("Poll A", "Do you like sand?")
addItem("Poll B", "Are u sleepy?")


# Route to get all items
@app.route('/', methods=['GET'])
def get_items():
    return jsonify(items), 200

# Route to add a new item
@app.route('/', methods=['POST'])
def add_item():
    data = request.get_json()
    title = data.get('title')
    description = data.get('description')

    # Simple validation
    if not title or not description:
        return jsonify({'error': 'Title and description are required'}), 400

    # Create a new item
    item = {
        'title': title,
        'description': description
    }
    items.append(item)

    return jsonify(item), 201

# Polls ------------------------------

polls = []

iteratorState = 0

def idIterator():
    global iteratorState
    i = iteratorState
    iteratorState += 1
    return i

def addPoll(_t, _o):
    polls.append({
        'title' : _t,
        'options' : _o
    })

addPoll("Do you eat sand?", [{"question": "Yes", "votes": 1,                    "id": idIterator()}, 
                             {"question": "No", "votes": 2,                     "id": idIterator()}, 
                             {"question": "I'm Anakin", "votes": 10,            "id": idIterator()}, 
                             {"question": "What is this question?", "votes": 5, "id": idIterator()}])

addPoll("Is lasagna?", [{"question": "YES", "votes": 3,                         "id": idIterator()}, 
                        {"question": "???????", "votes": 99,                    "id": idIterator()}])


# Route to get all polls
@app.route('/polls', methods=['GET'])
def get_polls():
    return jsonify(polls), 200

# Route to add a new poll
@app.route('/polls', methods=['POST'])
def create_poll():
    try:
        # Parse request JSON
        data = request.get_json()

        # Validate request structure
        if not data or "title" not in data or "options" not in data:
            return jsonify({"error": "Invalid request format"}), 400

        title = data["title"]
        options = data["options"]

        if not isinstance(title, str) or not isinstance(options, list):
            return jsonify({"error": "Invalid data types"}), 400

        # Validate options format
        if not all(isinstance(option, str) for option in options):
            return jsonify({"error": "Options must be a list of strings"}), 400

        # Create poll options with unique IDs
        processed_options = [
            {"question": option, "votes": 0, "id": idIterator()} for option in options
        ]

        # Add poll to the global list
        polls.append({"title" : title, "options":processed_options})

        # Return success response
        return jsonify({"message": "Poll created successfully", "poll": [title, processed_options]}), 201

    except Exception as e:
        return jsonify({"error": str(e)}), 500

'''
def add_poll():
    data = request.get_json()
    
    # Extract title and options
    title = data.get('title')
    options = data.get('options')
    
    # Validation
    if not title or not isinstance(options, list):
        return jsonify({'error': 'Title and options (as a list) are required'}), 400
    
    # Ensure options are valid
    for option in options:
        if 'question' not in option or 'votes' not in option:
            return jsonify({'error': 'Each option must have a question and votes field'}), 400

    # Add the poll
    poll = {'title': title, 'options': options}
    polls.append(poll)

    return jsonify(poll), 201

'''


@app.route('/polls/vote', methods=['POST'])
def vote_on_poll():
    try:
        # Parse the JSON request data
        data = request.get_json()
        option_id = data.get('id')  # Only an `id` is expected

        # Validate the option ID
        if option_id is None:
            return jsonify({"error": "'id' is required"}), 400

        # Iterate through polls to find the matching option by ID
        for poll in polls:
            for option in poll['options']:
                if option['id'] == option_id:
                    # Increment the votes for the matched option
                    option['votes'] += 1
                    return jsonify(polls), 200

        # If no matching ID is found
        return jsonify({"error": f"No option with id {option_id} found"}), 404

    except Exception as e:
        # Handle unexpected errors
        return jsonify({"error": str(e)}), 500

'''
@app.route('/polls/vote', methods=['POST'])
def vote_on_poll():
    try:
        # Parse the JSON request data
        data = request.get_json()
        poll_index = data.get('poll')
        option_index = data.get('option')

        # Validate poll and option indices
        if poll_index is None or option_index is None:
            return jsonify({"error": "Both 'poll' and 'option' are required"}), 400

        if poll_index < 0 or poll_index >= len(polls):
            return jsonify({"error": "Poll index out of range"}), 400

        poll = polls[poll_index]
        if option_index < 0 or option_index >= len(poll['options']):
            return jsonify({"error": "Option index out of range"}), 400

        # Add one vote to the specified option
        polls[poll_index]['options'][option_index]['votes'] += 1

        # Return the updated list of polls
        return jsonify(polls), 200

    except Exception as e:
        # Handle unexpected errors
        return jsonify({"error": str(e)}), 500
'''



## USERS ---------------------

users = {
    "user1": "password",  # username: password
    "user2": "mrMime"
}

def hash_password(password):
    return hashlib.sha256(password.encode()).hexdigest()

@app.route('/login', methods=['POST'])
def login():
    # Get the data from the request
    data = request.get_json()

    # Check if both username and password are provided
    if not data or not data.get('username') or not data.get('password'):
        return make_response(jsonify({"error": "Missing username or password"}), 400)

    username = data['username']
    password = data['password']

    print(username)
    print(password)
    
    # Check if the username exists and if the password matches
    if username in users and users[username] == password:
        # Generate a session ID
        session_id = str(uuid.uuid4())

        #session_id = "sucsess :)"
        print("sucsess")
        
        # Return the session ID in the response
        return jsonify({"session_id": session_id}), 200
    else:
        # Invalid credentials
        return "Record not found", 400

    
@app.route('/register', methods=['POST'])
def add_user():
    """
    Handles POST requests to add a user to the `users` dictionary.
    The request must include `username` and `password` fields.
    The password must be at least 5 characters long.
    """
    try:
        # Parse request JSON
        data = request.get_json()

        # Validate request structure
        if not data or "username" not in data or "password" not in data:
            return jsonify({"error": "Invalid request format. Must include 'username' and 'password'."}), 400

        username = data["username"]
        password = data["password"]

        # Validate username and password
        if not isinstance(username, str) or not isinstance(password, str):
            return jsonify({"error": "Both 'username' and 'password' must be strings."}), 400

        if username in users:
            return jsonify({"error": f"User '{username}' already exists."}), 400

        if len(password) < 5:
            return jsonify({"error": "Password must be at least 5 characters long."}), 400

        # Add user to the dictionary
        users[username] = password

        return jsonify({"message": f"User '{username}' added successfully."}), 201

    except Exception as e:
        return jsonify({"error": str(e)}), 500


# Run the application
if __name__ == '__main__':
    app.run(debug=True)