const API_URL = 'http://localhost:8080/api';

export const apiService = {
  login: async (username, password) => {
    const response = await fetch(`${API_URL}/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password })
    });
    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || '登录失败');
    }
    return response.json();
  },

  register: async (username, password, kindergarten) => {
    const response = await fetch(`${API_URL}/auth/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password, kindergarten })
    });
    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || '注册失败');
    }
    return response.text();
  },

  getStudents: async () => {
    const response = await fetch(`${API_URL}/students`);
    if (!response.ok) throw new Error('获取学生列表失败');
    return response.json();
  },

  addStudent: async (studentData) => {
    const response = await fetch(`${API_URL}/students`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(studentData)
    });
    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || '添加学生失败');
    }
    return response.json();
  },

  updateStudent: async (id, studentData) => {
    const response = await fetch(`${API_URL}/students/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(studentData)
    });
    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || '更新学生失败');
    }
    return response.json();
  },

  deleteStudent: async (id) => {
    const response = await fetch(`${API_URL}/students/${id}`, {
      method: 'DELETE'
    });
    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || '删除学生失败');
    }
    return response.text();
  },

  getRewards: async () => {
    const response = await fetch(`${API_URL}/students/rewards`);
    if (!response.ok) throw new Error('获取奖励列表失败');
    return response.json();
  },

  addReward: async (rewardData) => {
    const response = await fetch(`${API_URL}/students/rewards`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(rewardData)
    });
    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || '添加奖励失败');
    }
    return response.json();
  },

  getAttendance: async () => {
    const response = await fetch(`${API_URL}/students/attendance`);
    if (!response.ok) throw new Error('获取考勤列表失败');
    return response.json();
  },

  addAttendance: async (attendanceData) => {
    const response = await fetch(`${API_URL}/students/attendance`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(attendanceData)
    });
    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || '添加考勤失败');
    }
    return response.json();
  },

  getHomework: async () => {
    const response = await fetch(`${API_URL}/students/homework`);
    if (!response.ok) throw new Error('获取作业列表失败');
    return response.json();
  },

  addHomework: async (homeworkData) => {
    const response = await fetch(`${API_URL}/students/homework`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(homeworkData)
    });
    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || '添加作业失败');
    }
    return response.json();
  },

  getUsers: async () => {
    const response = await fetch(`${API_URL}/auth/users`);
    if (!response.ok) throw new Error('获取用户列表失败');
    return response.json();
  },

  approveUser: async (userId, currentUser) => {
    const response = await fetch(`${API_URL}/auth/users/${userId}/approve`, {
      method: 'PUT',
      headers: { 
        'Content-Type': 'application/json',
        'X-Current-User': currentUser
      }
    });
    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || '审核失败');
    }
    return response.json();
  },

  rejectUser: async (userId, currentUser) => {
    const response = await fetch(`${API_URL}/auth/users/${userId}/reject`, {
      method: 'PUT',
      headers: { 
        'Content-Type': 'application/json',
        'X-Current-User': currentUser
      }
    });
    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || '拒绝失败');
    }
    return response.json();
  },

  deleteUser: async (userId, currentUser) => {
    const response = await fetch(`${API_URL}/auth/users/${userId}`, {
      method: 'DELETE',
      headers: { 
        'Content-Type': 'application/json',
        'X-Current-User': currentUser
      }
    });
    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || '删除用户失败');
    }
    return response.text();
  },

  updateUserRole: async (userId, role, currentUser) => {
    const response = await fetch(`${API_URL}/auth/users/${userId}/role`, {
      method: 'PUT',
      headers: { 
        'Content-Type': 'application/json',
        'X-Current-User': currentUser
      },
      body: JSON.stringify(role)
    });
    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || '更新角色失败');
    }
    return response.json();
  }
};