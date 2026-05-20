from flask import Flask, request, jsonify
from flask_cors import CORS
import sqlite3
import os

app = Flask(__name__)
CORS(app)

DB_NAME = 'student.db'

def init_db():
    conn = sqlite3.connect(DB_NAME)
    c = conn.cursor()
    
    c.execute('''
        CREATE TABLE IF NOT EXISTS students (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            grade TEXT NOT NULL,
            class_name TEXT NOT NULL,
            status TEXT DEFAULT '正常',
            created_at TEXT DEFAULT CURRENT_TIMESTAMP
        )
    ''')
    
    c.execute('''
        CREATE TABLE IF NOT EXISTS rewards (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            student_id INTEGER NOT NULL,
            type TEXT NOT NULL,
            reason TEXT,
            date TEXT NOT NULL,
            FOREIGN KEY (student_id) REFERENCES students(id)
        )
    ''')
    
    c.execute('''
        CREATE TABLE IF NOT EXISTS attendance (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            student_id INTEGER NOT NULL,
            date TEXT NOT NULL,
            status TEXT NOT NULL,
            FOREIGN KEY (student_id) REFERENCES students(id)
        )
    ''')
    
    c.execute('''
        CREATE TABLE IF NOT EXISTS homework (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            student_id INTEGER NOT NULL,
            name TEXT NOT NULL,
            score INTEGER NOT NULL,
            date TEXT NOT NULL,
            FOREIGN KEY (student_id) REFERENCES students(id)
        )
    ''')
    
    conn.commit()
    conn.close()

init_db()

@app.route('/api/students', methods=['GET'])
def get_students():
    conn = sqlite3.connect(DB_NAME)
    conn.row_factory = sqlite3.Row
    c = conn.cursor()
    c.execute('SELECT * FROM students')
    students = []
    for row in c.fetchall():
        student = dict(row)
        student['rewards'] = get_rewards_by_student(student['id'])
        student['attendance'] = get_attendance_by_student(student['id'])
        student['homework'] = get_homework_by_student(student['id'])
        students.append(student)
    conn.close()
    return jsonify(students)

@app.route('/api/students/<int:id>', methods=['GET'])
def get_student(id):
    conn = sqlite3.connect(DB_NAME)
    conn.row_factory = sqlite3.Row
    c = conn.cursor()
    c.execute('SELECT * FROM students WHERE id = ?', (id,))
    row = c.fetchone()
    if row:
        student = dict(row)
        student['rewards'] = get_rewards_by_student(id)
        student['attendance'] = get_attendance_by_student(id)
        student['homework'] = get_homework_by_student(id)
        conn.close()
        return jsonify(student)
    conn.close()
    return jsonify({'error': '学生不存在'}), 404

@app.route('/api/students', methods=['POST'])
def add_student():
    data = request.get_json()
    conn = sqlite3.connect(DB_NAME)
    c = conn.cursor()
    c.execute('''
        INSERT INTO students (name, grade, class_name, status)
        VALUES (?, ?, ?, ?)
    ''', (data['name'], data['grade'], data['className'], data.get('status', '正常')))
    conn.commit()
    student_id = c.lastrowid
    conn.close()
    return jsonify(get_student_data(student_id)), 201

@app.route('/api/students/<int:id>', methods=['PUT'])
def update_student(id):
    data = request.get_json()
    conn = sqlite3.connect(DB_NAME)
    c = conn.cursor()
    c.execute('''
        UPDATE students SET name=?, grade=?, class_name=?, status=? WHERE id=?
    ''', (data['name'], data['grade'], data['className'], data['status'], id))
    conn.commit()
    conn.close()
    return jsonify(get_student_data(id))

@app.route('/api/students/<int:id>', methods=['DELETE'])
def delete_student(id):
    conn = sqlite3.connect(DB_NAME)
    c = conn.cursor()
    c.execute('DELETE FROM rewards WHERE student_id = ?', (id,))
    c.execute('DELETE FROM attendance WHERE student_id = ?', (id,))
    c.execute('DELETE FROM homework WHERE student_id = ?', (id,))
    c.execute('DELETE FROM students WHERE id = ?', (id,))
    conn.commit()
    conn.close()
    return '', 204

@app.route('/api/rewards', methods=['GET'])
def get_rewards():
    conn = sqlite3.connect(DB_NAME)
    conn.row_factory = sqlite3.Row
    c = conn.cursor()
    c.execute('SELECT * FROM rewards ORDER BY date DESC')
    rewards = [dict(row) for row in c.fetchall()]
    conn.close()
    return jsonify(rewards)

@app.route('/api/rewards', methods=['POST'])
def add_reward():
    data = request.get_json()
    conn = sqlite3.connect(DB_NAME)
    c = conn.cursor()
    c.execute('''
        INSERT INTO rewards (student_id, type, reason, date)
        VALUES (?, ?, ?, ?)
    ''', (data['studentId'], data['type'], data['reason'], data['date']))
    conn.commit()
    reward_id = c.lastrowid
    conn.close()
    return jsonify(get_reward_data(reward_id)), 201

@app.route('/api/attendance', methods=['POST'])
def add_attendance():
    data = request.get_json()
    conn = sqlite3.connect(DB_NAME)
    c = conn.cursor()
    c.execute('''
        INSERT INTO attendance (student_id, date, status)
        VALUES (?, ?, ?)
    ''', (data['studentId'], data['date'], data['status']))
    conn.commit()
    attendance_id = c.lastrowid
    conn.close()
    return jsonify(get_attendance_data(attendance_id)), 201

@app.route('/api/homework', methods=['POST'])
def add_homework():
    data = request.get_json()
    conn = sqlite3.connect(DB_NAME)
    c = conn.cursor()
    c.execute('''
        INSERT INTO homework (student_id, name, score, date)
        VALUES (?, ?, ?, ?)
    ''', (data['studentId'], data['name'], data['score'], data['date']))
    conn.commit()
    homework_id = c.lastrowid
    conn.close()
    return jsonify(get_homework_data(homework_id)), 201

def get_rewards_by_student(student_id):
    conn = sqlite3.connect(DB_NAME)
    conn.row_factory = sqlite3.Row
    c = conn.cursor()
    c.execute('SELECT * FROM rewards WHERE student_id = ? ORDER BY date DESC', (student_id,))
    rewards = [dict(row) for row in c.fetchall()]
    conn.close()
    return rewards

def get_attendance_by_student(student_id):
    conn = sqlite3.connect(DB_NAME)
    conn.row_factory = sqlite3.Row
    c = conn.cursor()
    c.execute('SELECT * FROM attendance WHERE student_id = ? ORDER BY date DESC', (student_id,))
    attendance = [dict(row) for row in c.fetchall()]
    conn.close()
    return attendance

def get_homework_by_student(student_id):
    conn = sqlite3.connect(DB_NAME)
    conn.row_factory = sqlite3.Row
    c = conn.cursor()
    c.execute('SELECT * FROM homework WHERE student_id = ? ORDER BY date DESC', (student_id,))
    homework = [dict(row) for row in c.fetchall()]
    conn.close()
    return homework

def get_student_data(id):
    conn = sqlite3.connect(DB_NAME)
    conn.row_factory = sqlite3.Row
    c = conn.cursor()
    c.execute('SELECT * FROM students WHERE id = ?', (id,))
    row = c.fetchone()
    student = dict(row) if row else None
    conn.close()
    return student

def get_reward_data(id):
    conn = sqlite3.connect(DB_NAME)
    conn.row_factory = sqlite3.Row
    c = conn.cursor()
    c.execute('SELECT * FROM rewards WHERE id = ?', (id,))
    row = c.fetchone()
    reward = dict(row) if row else None
    conn.close()
    return reward

def get_attendance_data(id):
    conn = sqlite3.connect(DB_NAME)
    conn.row_factory = sqlite3.Row
    c = conn.cursor()
    c.execute('SELECT * FROM attendance WHERE id = ?', (id,))
    row = c.fetchone()
    attendance = dict(row) if row else None
    conn.close()
    return attendance

def get_homework_data(id):
    conn = sqlite3.connect(DB_NAME)
    conn.row_factory = sqlite3.Row
    c = conn.cursor()
    c.execute('SELECT * FROM homework WHERE id = ?', (id,))
    row = c.fetchone()
    homework = dict(row) if row else None
    conn.close()
    return homework

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080, debug=True)
