import { useState, useEffect } from 'react';
import { apiService } from '../services/api';

export default function StudentManagement() {
  const [students, setStudents] = useState([]);
  const [formData, setFormData] = useState({
    name: '',
    grade: '',
    className: '',
    status: '正常'
  });
  const [showSuccess, setShowSuccess] = useState(false);

  useEffect(() => {
    loadStudents();
  }, []);

  const loadStudents = async () => {
    try {
      const data = await apiService.getStudents();
      setStudents(data);
    } catch (e) {
      console.error('加载学生列表失败:', e);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.name || !formData.grade || !formData.className) {
      alert('请填写完整信息！');
      return;
    }

    try {
      await apiService.addStudent({
        name: formData.name,
        grade: formData.grade,
        className: formData.className,
        status: formData.status
      });
      setFormData({ name: '', grade: '', className: '', status: '正常' });
      setShowSuccess(true);
      setTimeout(() => setShowSuccess(false), 3000);
      loadStudents();
    } catch (e) {
      alert(e.message);
    }
  };

  const handleDelete = async (id) => {
    if (!confirm('确定要删除这个学生吗？')) return;
    try {
      await apiService.deleteStudent(id);
      loadStudents();
    } catch (e) {
      alert(e.message);
    }
  };

  const getStatusClass = (status) => {
    switch (status) {
      case '正常': return 'status-normal';
      case '需关注': return 'status-warning';
      case '优秀': return 'status-excellent';
      default: return 'status-normal';
    }
  };

  return (
    <div className="student-management">
      <div className="form-container">
        <h2>添加新学生 🎉</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>👧 姓名</label>
            <input
              type="text"
              className="form-input"
              value={formData.name}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              placeholder="请输入学生姓名"
              required
            />
          </div>
          <div className="form-group">
            <label>📚 年级</label>
            <select
              className="form-select"
              value={formData.grade}
              onChange={(e) => setFormData({ ...formData, grade: e.target.value })}
              required
            >
              <option value="">请选择年级</option>
              <option value="小班">小班</option>
              <option value="中班">中班</option>
              <option value="大班">大班</option>
              <option value="一年级">一年级</option>
              <option value="二年级">二年级</option>
            </select>
          </div>
          <div className="form-group">
            <label>🏠 班级</label>
            <input
              type="text"
              className="form-input"
              value={formData.className}
              onChange={(e) => setFormData({ ...formData, className: e.target.value })}
              placeholder="请输入班级名称"
              required
            />
          </div>
          <div className="form-group">
            <label>💖 状态</label>
            <select
              className="form-select"
              value={formData.status}
              onChange={(e) => setFormData({ ...formData, status: e.target.value })}
            >
              <option value="正常">正常</option>
              <option value="优秀">优秀</option>
              <option value="需关注">需关注</option>
            </select>
          </div>
          <button type="submit" className="form-btn">添加学生</button>
          {showSuccess && <div className="success-message">✅ 添加成功！</div>}
        </form>
      </div>

      <div className="student-grid">
        {students.length === 0 ? (
          <p className="empty-message">还没有学生哦~ 快添加一些吧！🥰</p>
        ) : (
          students.map(student => (
            <div key={student.id} className="student-card">
              <h3>👧 {student.name}</h3>
              <p><strong>📚 年级:</strong> {student.grade}</p>
              <p><strong>🏠 班级:</strong> {student.className}</p>
              <p><strong>💖 状态:</strong> <span className={`status-tag ${getStatusClass(student.status)}`}>{student.status}</span></p>
              <button className="card-btn card-btn-danger" onClick={() => handleDelete(student.id)}>删除</button>
            </div>
          ))
        )}
      </div>
    </div>
  );
}