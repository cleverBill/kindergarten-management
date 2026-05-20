import { useState, useEffect } from 'react';
import { apiService } from '../services/api';

export default function AttendanceManagement() {
  const [attendance, setAttendance] = useState([]);
  const [students, setStudents] = useState([]);
  const [formData, setFormData] = useState({
    studentId: '',
    date: new Date().toISOString().split('T')[0],
    status: '正常'
  });

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [attendanceData, studentsData] = await Promise.all([
        apiService.getAttendance(),
        apiService.getStudents()
      ]);
      setAttendance(attendanceData);
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
    try {
      await apiService.addAttendance({
        studentId: parseInt(formData.studentId),
        date: formData.date,
        status: formData.status
      });
      loadData();
      alert('✅ 考勤记录已保存！');
    } catch (e) {
      alert(e.message);
    }
  };

  const getAttendanceStats = () => {
    const stats = {};
    students.forEach(student => {
      const studentAttendance = attendance.filter(a => a.studentId === student.id);
      const total = studentAttendance.length;
      const present = studentAttendance.filter(a => a.status === '正常').length;
      stats[student.id] = {
        name: student.name,
        total,
        present,
        rate: total > 0 ? ((present / total) * 100).toFixed(1) : 0
      };
    });
    return stats;
  };

  const stats = getAttendanceStats();

  return (
    <div className="attendance-management">
      <div className="form-container">
        <h2>记录考勤 📅</h2>
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
            <label>📅 日期</label>
            <input
              type="date"
              className="form-input"
              value={formData.date}
              onChange={(e) => setFormData({ ...formData, date: e.target.value })}
            />
          </div>
          <div className="form-group">
            <label>💼 状态</label>
            <select
              className="form-select"
              value={formData.status}
              onChange={(e) => setFormData({ ...formData, status: e.target.value })}
            >
              <option value="正常">✅ 正常</option>
              <option value="迟到">⏰ 迟到</option>
              <option value="缺勤">❌ 缺勤</option>
              <option value="请假">🏥 请假</option>
            </select>
          </div>
          <button type="submit" className="form-btn">记录考勤</button>
        </form>
      </div>

      <div className="list-container">
        <h2>考勤统计 📊</h2>
        {students.length === 0 ? (
          <p className="empty-message">还没有学生~</p>
        ) : (
          <div className="attendance-list">
            {students.map(student => {
              const stat = stats[student.id];
              return (
                <div key={student.id} className="list-item">
                  📊 <strong>{stat.name}</strong>: 出勤率 {stat.rate}% ({stat.present}/{stat.total}天)
                </div>
              );
            })}
          </div>
        )}
      </div>
    </div>
  );
}