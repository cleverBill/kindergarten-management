import { useState, useEffect } from 'react';
import { apiService } from '../services/api';
import { storage } from '../utils/storage';

export default function UserManagement() {
  const [users, setUsers] = useState([]);
  const [selectedRole, setSelectedRole] = useState('ROLE_TEACHER');
  const currentUser = storage.getUser();

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    try {
      const data = await apiService.getUsers();
      setUsers(data);
    } catch (e) {
      console.error('加载用户列表失败:', e);
    }
  };

  const handleApprove = async (userId) => {
    try {
      await apiService.approveUser(userId, currentUser.username);
      loadUsers();
      alert('✅ 用户审核通过！');
    } catch (e) {
      alert(e.message);
    }
  };

  const handleReject = async (userId) => {
    if (!confirm('确定要拒绝该用户吗？')) return;
    try {
      await apiService.rejectUser(userId, currentUser.username);
      loadUsers();
      alert('✕ 用户已被拒绝');
    } catch (e) {
      alert(e.message);
    }
  };

  const handleDelete = async (userId, username) => {
    if (!confirm(`确定要删除用户 "${username}" 吗？此操作不可撤销！`)) return;
    try {
      await apiService.deleteUser(userId, currentUser.username);
      loadUsers();
      alert('🗑️ 用户已删除');
    } catch (e) {
      alert(e.message);
    }
  };

  const handleUpdateRole = async (userId, role) => {
    try {
      await apiService.updateUserRole(userId, role, currentUser.username);
      loadUsers();
      alert('✅ 角色更新成功！');
    } catch (e) {
      alert(e.message);
    }
  };

  const getRoleDisplayName = (role) => {
    const roleMap = {
      'ROLE_SUPER_ADMIN': '超级管理员',
      'ROLE_PRINCIPAL': '园长',
      'ROLE_TEACHER': '教师',
      'ROLE_FINANCE': '财务',
      'ROLE_CLEANER': '保洁',
      'ROLE_USER': '普通用户'
    };
    return roleMap[role] || role;
  };

  const getStatusStyle = (status) => {
    switch (status) {
      case 'approved': return { className: 'status-approved', label: '已通过' };
      case 'pending': return { className: 'status-pending', label: '待审核' };
      case 'rejected': return { className: 'status-rejected', label: '已拒绝' };
      default: return { className: 'status-pending', label: status };
    }
  };

  return (
    <div className="user-management">
      <h2>👥 用户管理</h2>
      
      <div className="users-table-container">
        <table className="users-table">
          <thead>
            <tr>
              <th>用户名</th>
              <th>角色</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            {users.map(user => {
              const statusInfo = getStatusStyle(user.status);
              return (
                <tr key={user.id}>
                  <td>{user.username}</td>
                  <td>
                    {user.role === 'ROLE_SUPER_ADMIN' ? (
                      getRoleDisplayName(user.role)
                    ) : (
                      <select
                        className="role-select"
                        value={user.role}
                        onChange={(e) => handleUpdateRole(user.id, e.target.value)}
                      >
                        <option value="ROLE_PRINCIPAL">园长</option>
                        <option value="ROLE_TEACHER">教师</option>
                        <option value="ROLE_FINANCE">财务</option>
                        <option value="ROLE_CLEANER">保洁</option>
                        <option value="ROLE_USER">普通用户</option>
                      </select>
                    )}
                  </td>
                  <td><span className={`status-tag ${statusInfo.className}`}>{statusInfo.label}</span></td>
                  <td>
                    <div className="action-buttons">
                      {user.status === 'pending' && (
                        <>
                          <button className="action-btn approve" onClick={() => handleApprove(user.id)}>✓ 通过</button>
                          <button className="action-btn reject" onClick={() => handleReject(user.id)}>✕ 拒绝</button>
                        </>
                      )}
                      {user.role !== 'ROLE_SUPER_ADMIN' && user.status !== 'pending' && (
                        <button className="action-btn delete" onClick={() => handleDelete(user.id, user.username)}>🗑️ 删除</button>
                      )}
                    </div>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
}