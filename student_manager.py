import streamlit as st
import json
import os
from datetime import datetime

DATA_FILE = "students_data.json"

def load_data():
    if os.path.exists(DATA_FILE):
        with open(DATA_FILE, "r", encoding="utf-8") as f:
            return json.load(f)
    return {"students": [], "rewards": []}

def save_data(data):
    with open(DATA_FILE, "w", encoding="utf-8") as f:
        json.dump(data, f, ensure_ascii=False, indent=2)

def init_session_state():
    if "data" not in st.session_state:
        st.session_state.data = load_data()
    if "selected_student" not in st.session_state:
        st.session_state.selected_student = None

def add_student(name, grade, class_name, status="正常"):
    student = {
        "id": len(st.session_state.data["students"]) + 1,
        "name": name,
        "grade": grade,
        "class_name": class_name,
        "status": status,
        "attendance": [],
        "homework": [],
        "rewards": [],
        "created_at": datetime.now().strftime("%Y-%m-%d %H:%M")
    }
    st.session_state.data["students"].append(student)
    save_data(st.session_state.data)

def add_reward(student_id, reward_type, reason):
    reward = {
        "id": len(st.session_state.data["rewards"]) + 1,
        "student_id": student_id,
        "type": reward_type,
        "reason": reason,
        "date": datetime.now().strftime("%Y-%m-%d")
    }
    st.session_state.data["rewards"].append(reward)
    for student in st.session_state.data["students"]:
        if student["id"] == student_id:
            student["rewards"].append(reward)
            break
    save_data(st.session_state.data)

def add_attendance(student_id, date, status):
    attendance = {
        "date": date,
        "status": status
    }
    for student in st.session_state.data["students"]:
        if student["id"] == student_id:
            student["attendance"].append(attendance)
            break
    save_data(st.session_state.data)

def add_homework(student_id, homework_name, score, date):
    homework = {
        "name": homework_name,
        "score": score,
        "date": date
    }
    for student in st.session_state.data["students"]:
        if student["id"] == student_id:
            student["homework"].append(homework)
            break
    save_data(st.session_state.data)

def update_student_status(student_id, new_status):
    for student in st.session_state.data["students"]:
        if student["id"] == student_id:
            student["status"] = new_status
            break
    save_data(st.session_state.data)

def delete_student(student_id):
    st.session_state.data["students"] = [
        s for s in st.session_state.data["students"] if s["id"] != student_id
    ]
    st.session_state.data["rewards"] = [
        r for r in st.session_state.data["rewards"] if r["student_id"] != student_id
    ]
    save_data(st.session_state.data)

def get_student_by_id(student_id):
    for student in st.session_state.data["students"]:
        if student["id"] == student_id:
            return student
    return None
