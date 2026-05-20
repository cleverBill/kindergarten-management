import { useState, useEffect } from 'react';
import { apiService } from '../services/api';

export default function HomeworkManagement() {
  const [homework, setHomework] = useState([]);
  const [students, setStudents] = useState([]);
  const [formData, setFormData] = useState({
    studentId: '',
    name: '',
    score: 100,
    date: new Date().toISOString().split('T')[0]
  });

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [homeworkData, studentsData] = await Promise.all([
        apiService.getHomework(),
        apiService.getStudents()
      ]);
      setHomework(homeworkData);
      setStudents(studentsData);
      if (studentsData.length > 0) {
        setFormData(prev => ({ ...prev, studentId: studentsData[0].id.toString() }));
      }
    } catch (e) {
      console.error('加载数据失败:', e);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.name) {
      alert('请填写作业名称！');
      return;
    }
    try {
      await apiService.addHomework({
        studentId: parseInt(formData.studentId),
        name: formData.name,
        score: parseInt(formData.score),
        date: formData.date
      });
      setFormData({ ...formData, name: '' });
      loadData();
      alert('✅ 作业记录已保存！');
    } catch (e) {
      alert(e.message);
    }
  };

  const getHomeworkStats = () => {
    const stats = {};
    students.forEach(student => {
      const studentHomework = homework.filter(h => h.studentId === student.id);
      const total = studentHomework.length;
      const avg = total > 0
        ? (studentHomework.reduce((sum, h) => sum + h.score, 0) / total).toFixed(1)
        : 0;
      stats[student.id] = {
        name: student.name,
        total,
        avg
      };
    });
    return stats;
  };

  const stats = getHomeworkStats();

  return (
    <div className="homework-management">
      <div className="form-container">
        <h2>记录作业 📝</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>👧 选择学生</label>
            <select
              className="form-select"
              value={formData.studentId}
              onChange={(e) => setFormData({ ...formData, studentId: e.target.value })}
            >
              {students.map(s => (
                <option key={s.id} value={s.id}>👧 {s.name}</option>
              ))}
            </select>
          </div>
          <div className="form-group">
            <label>📚 作业名称</label>
            <input
              type="text"
              className="form-input"
              value={formData.name}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              placeholder="请输入作业名称"
            />
          </div>
          <div className="form-group">
            <label>⭐ 分数</label>
            <input
              type="number"
              className="form-input"
              value={formData.score}
              onChange={(e) => setFormData({ ...formData, score: e.target.value })}
              min="0"
              max="100"
            />
          </div>
          <div className="form-group">
            <label>📅 日期</label>
            <input
              type="date"
              className="form-input"
              value={formData.date}
              onChange={(e) => setFormData({ ...formData, date: e.target.value })}
            />
          </div>
          <button type="submit" className="form-btn">记录作业</button>
        </form>
      </div>

      <div className="list-container">
        <h2>作业统计 📚</h2>
        {students.length === 0 ? (
          <p className="empty-message">还没有学生~</p>
        ) : (
          <div className="homework-list">
            {students.map(student => {
              const stat = stats[student.id];
              if (stat.total === 0) {
                return (
                  <div key={student.id} className="list-item">
                    📝 <strong>{stat.name}</strong>: 还没有作业记录哦~
                  </div>
                );
              }
              return (
                <div key={student.id} className="list-item">
                  📝 <strong>{stat.name}</strong>: 平均 {stat.avg} 分 (共{stat.total}次作业)
                </div>
              );
            })}
          </div>
        )}
      </div>
    </div>
  );
}